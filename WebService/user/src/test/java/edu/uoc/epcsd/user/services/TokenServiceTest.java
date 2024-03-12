package edu.uoc.epcsd.user.services;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import edu.uoc.epcsd.user.entities.User;

class TokenServiceTest {

    @InjectMocks
    private TokenService tokenService;

    private User user;
    private Map<String, Object> claims;
    private String jwtToken;
    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);
        user = User.builder().id(1L).email("john@example.com").role("USER").build();
        jwtToken = "validToken";
        claims = new HashMap<>();
        claims.put("role", "USER");
        claims.put("id", 1L);
    }



//    @Test
//    void testGenerateUserToken_Success() {
//        // Arrange
//        when(jwtUtil.generateToken("john@example.com", claims)).thenReturn("generatedToken");
//
//        // Act
//        String generatedToken = tokenService.generateUserToken(user);
//
//        // Assert
//        assertEquals("generatedToken", generatedToken);
//    }
//
//    @Test
//    void testReadToken_Success() {
//        // Arrange
//
//        when(jwtUtil.extractClaims("validToken")).thenReturn(claims);
//
//        // Act
//        Map<String, Object> actualClaims = tokenService.readToken(jwtToken);
//
//        // Assert
//        assertEquals(claims, actualClaims);
//    }

    @Test
    void testGenerateUUID_Success() {

        String generatedUUID = tokenService.generateUUID();

        assertNotNull(generatedUUID);

        assertTrue(isBase64Encoded(generatedUUID));
    }

    private boolean isBase64Encoded(String str) {
        try {
            Base64.getDecoder().decode(str);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Test
    void testIsUUIDValid_ValidToken() {
        // Arrange

        String validToken = generateValidToken();

        // Act
        boolean isValid = tokenService.isUUIDValid(validToken);

        // Assert
        assertTrue(isValid);
    }

    @Test
    void testIsUUIDValid_InvalidToken() {
        // Arrange
        String invalidToken = "invalidToken";
        // Act
        boolean isValid = tokenService.isUUIDValid(invalidToken);

        // Assert
        assertFalse(isValid);
    }
    @Test
    void testIsUUIDValid_ExpiredToken() {
        // Arrange
        String expiredToken = generateExpiredToken();
        // Act
        boolean isValid = tokenService.isUUIDValid(expiredToken);
        // Assert
        assertFalse(isValid);
    }

    private String generateValidToken() {
        long validTimestamp = System.currentTimeMillis() - (5 * 60 * 1000); // 5 minutes in milliseconds

        String uuid = java.util.UUID.randomUUID().toString();
        String tokenData = uuid + validTimestamp;
        return Base64.getEncoder().encodeToString(tokenData.getBytes());
    }

    private String generateExpiredToken() {
        return "expiredToken";
    }
}