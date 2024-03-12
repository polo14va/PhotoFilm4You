package edu.uoc.epcsd.user.config;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.client.RestTemplate;

class SecurityConfigTest {

	
    @Test
    void testPasswordEncoderBean() {
        // Arrange
        SecurityConfig securityConfig = new SecurityConfig(null, null);

        // Act
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();

        // Assert
        assertNotNull(passwordEncoder);
        assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
    }
    
    @Test
    void testRestTemplateBean() {
        // Arrange
        SecurityConfig securityConfig = new SecurityConfig(null, null);

        // Act
        RestTemplate restTemplate = securityConfig.restTemplate();

        // Assert
        assertNotNull(restTemplate);
    }

    @Test
    void testAuthenticationManagerBean() throws Exception {
        // Arrange
        SecurityConfig securityConfig = new SecurityConfig(null, null);

        // Mock AuthenticationConfiguration
        AuthenticationConfiguration authenticationConfiguration = mock(AuthenticationConfiguration.class);
        AuthenticationManager mockAuthenticationManager = mock(AuthenticationManager.class);

        // Mock behavior for getAuthenticationManager
        when(authenticationConfiguration.getAuthenticationManager()).thenReturn(mockAuthenticationManager);

        // Act
        AuthenticationManager authenticationManager = securityConfig.authenticationManager(authenticationConfiguration);

        // Assert
        assertNotNull(authenticationManager);
    }

    @Test
    void testAuthenticationProviderBean() {
        // Arrange
        SecurityConfig securityConfig = new SecurityConfig(null, null);

        // Act
        AuthenticationProvider authenticationProvider = securityConfig.authenticationProvider();

        // Assert
        assertNotNull(authenticationProvider);
    }






}
