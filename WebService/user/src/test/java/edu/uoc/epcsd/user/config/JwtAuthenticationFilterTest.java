package edu.uoc.epcsd.user.config;

import edu.uoc.epcsd.user.services.TokenService;
import edu.uoc.epcsd.user.services.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtAuthenticationFilterTest {

    @Mock
    private TokenService jwtService;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDoFilterInternal() throws ServletException, IOException {
        // Mocking
        String validToken = "validToken";
        String userEmail = "user@example.com";
        UserDetails userDetails = mock(UserDetails.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer " + validToken);
        when(jwtService.extractUserName(validToken)).thenReturn(userEmail);
        when(userDetailsService.loadUserByUsername(userEmail)).thenReturn(userDetails);
        when(jwtService.isTokenValid(validToken, userDetails)).thenReturn(true);

        // Execution
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Verification
        verify(jwtService).extractUserName(validToken);
        verify(userDetailsService).loadUserByUsername(userEmail);
        verify(jwtService).isTokenValid(validToken, userDetails);
        verify(filterChain).doFilter(request, response);
        verify(userDetails).getAuthorities();
    }

    @Test
    void testDoFilterInternalNoAuthorizationHeader() throws ServletException, IOException {
        // Mocking
        when(request.getHeader("Authorization")).thenReturn(null);

        // Execution
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Verification
        verifyNoMoreInteractions(jwtService, userDetailsService);
        verify(filterChain).doFilter(request, response);
    }

}