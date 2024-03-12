package edu.uoc.epcsd.user.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import edu.uoc.epcsd.user.entities.User;
import edu.uoc.epcsd.user.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceTest {

	@InjectMocks
	private UserDetailsServiceImpl service;
	
	@Mock
	private UserRepository userRepository;
	
	@Test
	void loadUserByUsername() {
		String mail = "test@gmail.com";
		User user = User.builder().email(mail).build();
		when(userRepository.findByEmail(mail)).thenReturn(Optional.of(user));
		UserDetails result = service.loadUserByUsername(mail);
		assertEquals(result.getUsername(), mail);
	}
	
	@Test
	void loadUserByUsernameNotFound() {
		String mail = "usuarioNoExistente@gmail.com";
        when(userRepository.findByEmail(mail)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            service.loadUserByUsername(mail);
        });
	}
	
}
