package edu.uoc.epcsd.user.config;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ClientUtilsTest {

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private ClientUtils clientUtils;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void getClientIpAddress_WithXForwardedForHeader_ShouldReturnIpAddress() {
        // Arrange
        String ipAddress = "192.168.1.1";
        when(request.getHeader("X-Forwarded-For")).thenReturn(ipAddress);

        // Act
        String result = clientUtils.getClientIpAddress(request);

        // Assert
        assertEquals(ipAddress, result);
    }

    @Test
    void getClientIpAddress_WithoutXForwardedForHeader_ShouldReturnRemoteAddr() {
        // Arrange
        String remoteAddr = "192.168.1.2";
        when(request.getHeader("X-Forwarded-For")).thenReturn(null);
        when(request.getRemoteAddr()).thenReturn(remoteAddr);

        // Act
        String result = clientUtils.getClientIpAddress(request);

        // Assert
        assertEquals(remoteAddr, result);
    }
}