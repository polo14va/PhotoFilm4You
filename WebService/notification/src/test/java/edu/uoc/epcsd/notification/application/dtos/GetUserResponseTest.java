package edu.uoc.epcsd.notification.application.dtos;

import edu.uoc.epcsd.notification.application.rest.dtos.GetUserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class GetUserResponseTest {

    private GetUserResponse getUserResponse;

    @BeforeEach
    void setUp() {
        getUserResponse = new GetUserResponse();
    }

    @Test
    void settingIdReflectsInGetter() {
        Long expectedId = 123L;
        getUserResponse.setId(expectedId);
        assertEquals(expectedId, getUserResponse.getId());
    }

    @Test
    void settingDifferentIdReflectsInGetter() {
        Long unexpectedId = 123L;
        Long expectedId = 456L;
        getUserResponse.setId(expectedId);
        assertNotEquals(unexpectedId, getUserResponse.getId());
    }

    @Test
    void settingFullNameReflectsInGetter() {
        String expectedFullName = "John Doe";
        getUserResponse.setFullName(expectedFullName);
        assertEquals(expectedFullName, getUserResponse.getFullName());
    }

    @Test
    void settingDifferentFullNameReflectsInGetter() {
        String unexpectedFullName = "Jane Doe";
        String expectedFullName = "John Doe";
        getUserResponse.setFullName(expectedFullName);
        assertNotEquals(unexpectedFullName, getUserResponse.getFullName());
    }

    @Test
    void settingEmailReflectsInGetter() {
        String expectedEmail = "john.doe@example.com";
        getUserResponse.setEmail(expectedEmail);
        assertEquals(expectedEmail, getUserResponse.getEmail());
    }

    @Test
    void settingDifferentEmailReflectsInGetter() {
        String unexpectedEmail = "jane.doe@example.com";
        String expectedEmail = "john.doe@example.com";
        getUserResponse.setEmail(expectedEmail);
        assertNotEquals(unexpectedEmail, getUserResponse.getEmail());
    }

    @Test
    void settingPhoneNumberReflectsInGetter() {
        String expectedPhoneNumber = "1234567890";
        getUserResponse.setPhoneNumber(expectedPhoneNumber);
        assertEquals(expectedPhoneNumber, getUserResponse.getPhoneNumber());
    }

    @Test
    void settingDifferentPhoneNumberReflectsInGetter() {
        String unexpectedPhoneNumber = "0987654321";
        String expectedPhoneNumber = "1234567890";
        getUserResponse.setPhoneNumber(expectedPhoneNumber);
        assertNotEquals(unexpectedPhoneNumber, getUserResponse.getPhoneNumber());
    }

    @Test
    void testGetUserResponseToString() {
        GetUserResponse userResponse = GetUserResponse.builder()
                .id(1L)
                .fullName("John Doe")
                .email("john@example.com")
                .phoneNumber("123456789")
                .build();

        String expectedToString = "GetUserResponse(id=1, fullName=John Doe, email=john@example.com, phoneNumber=123456789)";
        assertEquals(expectedToString, userResponse.toString());
    }
}