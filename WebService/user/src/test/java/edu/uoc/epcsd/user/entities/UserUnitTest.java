package edu.uoc.epcsd.user.entities;

import edu.uoc.epcsd.user.repositories.UserRepository;
import edu.uoc.epcsd.user.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class UserUnitTest {

    private final Long USER_ID = 1L;
    private final String EMAIL = "ejemplo@gmail.com";
    private final String FULL_NAME = "John Doe";
    private final String PASSWORD = "password";
    private final String PHONE_NUMBER = "123456789";
    private final String ROLE = "USER";
    private final int ACTIVE = 1;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void whenValidUserId_thenUserShouldBeFound() {
        User user = User.builder()
                .id(USER_ID)
                .email(EMAIL)
                .fullName(FULL_NAME)
                .password(PASSWORD)
                .phoneNumber(PHONE_NUMBER)
                .role(ROLE)
                .active(ACTIVE)
                .build();

        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.findById(USER_ID);
        assertThat(foundUser.isPresent()).isTrue();
        assertEquals(user, foundUser.get());
    }

    @Test
    void whenInvalidUserId_thenExceptionShouldBeThrown() {
        Long WRONG_USER_ID = 10L;
        when(userRepository.findById(WRONG_USER_ID)).thenReturn(Optional.empty());

        Optional<User> user = userService.findById(WRONG_USER_ID);
        assertThat(user.isEmpty()).isTrue();
    }

    @Test
    void whenFindByEmail_thenUserShouldBeFound() {
        User user = User.builder()
                .id(USER_ID)
                .email(EMAIL)
                .fullName(FULL_NAME)
                .password(PASSWORD)
                .phoneNumber(PHONE_NUMBER)
                .role(ROLE)
                .active(ACTIVE)
                .build();

        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.findByEmail(EMAIL);
        assertThat(foundUser.isPresent()).isTrue();
        assertEquals(user, foundUser.get());
    }

    @Test
    void whenFindByEmail_thenUserShouldNotBeFound() {
        String WRONG_EMAIL = "wrong@example.com";
        when(userRepository.findByEmail(WRONG_EMAIL)).thenReturn(Optional.empty());

        Optional<User> user = userService.findByEmail(WRONG_EMAIL);
        assertThat(user.isEmpty()).isTrue();
    }

    @Test
    void whenFindAllUsers_thenShouldReturnAll() {
        User user1 = User.builder().id(1L).email("user1@example.com").build();
        User user2 = User.builder().id(2L).email("user2@example.com").build();
        User user3 = User.builder().id(3L).email("user3@example.com").build();
        List<User> users = Arrays.asList(user1, user2, user3);

        when(userRepository.findAll()).thenReturn(users);

        List<User> userList = userService.findAll();
        assertThat(userList.size()).isEqualTo(users.size());
    }

    @Test
    void testEqualsAndHashCode() {
        User user1 = User.builder().id(1L).email("user1@example.com").build();
        User user2 = User.builder().id(1L).email("user1@example.com").build();
        User user3 = User.builder().id(2L).email("user2@example.com").build();

        // Test Equals
        assertThat(user1.equals(user2)).isTrue();
        assertThat(user1.equals(user3)).isFalse();

        // Test HashCode
        assertThat(user1.hashCode()).isEqualTo(user2.hashCode());
        assertThat(user1.hashCode()).isNotEqualTo(user3.hashCode());
    }


    @Test
    public void testGetAuthoritiesWithRole() {
        // Create an instance of YourClass
        User user = new User();

        // Set the role (replace "ROLE_USER" with your actual role)
        user.setRole("ROLE_USER");

        // Call the getAuthorities method
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        // Assert that the authorities contain the expected SimpleGrantedAuthority
        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Test
    public void testGetAuthoritiesWithoutRole() {
        // Create an instance of YourClass
        User user = new User();

        // Call the getAuthorities method when role is null
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        // Assert that the authorities are empty
        assertTrue(authorities.isEmpty());
    }


}
