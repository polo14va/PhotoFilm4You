package edu.uoc.epcsd.user.controllers;

import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.uoc.epcsd.user.config.ErrorResponse;
import edu.uoc.epcsd.user.controllers.dtos.LoginRequest;
import edu.uoc.epcsd.user.controllers.dtos.UserResponse;
import edu.uoc.epcsd.user.entities.User;
import edu.uoc.epcsd.user.services.TokenService;
import edu.uoc.epcsd.user.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final TokenService tokenService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;



    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest) {
        try {
            String email = loginRequest.getEmail();
            String password = loginRequest.getPassword();

            Optional<User> userOptional = userService.findByEmail(email);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                if (user.getActive() == 1) {
                    // Check if the user is activated
                    if (passwordEncoder.matches(password, user.getPassword())) {
                    	authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(user.getEmail(), password));
                    	
                        String token = tokenService.generateToken(user);

                        return ResponseEntity.ok(token);
                    } else {
                        log.error("Invalid password for user: " + email);
                        ErrorResponse errorResponse = new ErrorResponse("Invalid login");
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
                    }
                } else {
                    log.error("User not activated: " + email);
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse("Account not activated. Please activate your account."));
                }
            } else {
                log.error("User not found with email: " + email);
            }

        } catch (Exception e) {
            log.error("An unexpected error occurred: " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Unexpected error occurred"));
        }
        return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body(new ErrorResponse("Invalid login broken"));
    }

    @PostMapping("/loginUser")
    public ResponseEntity<Object> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            String email = loginRequest.getEmail();
            String password = loginRequest.getPassword();

            Optional<User> userOptional = userService.findByEmail(email);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                if (user.getPassword().equals(password)) {
                    log.info("User logged in: " + user.getEmail());

                    UserResponse userResponse = new UserResponse();
                    userResponse.setId(
                        user.getId()
                        );
                    userResponse.setFullName(
                        user.getFullName()
                        );
                    userResponse.setEmail(
                        user.getEmail()
                        );
                    userResponse.setPhoneNumber(
                        user.getPhoneNumber()
                        );
                    userResponse.setRole(
                        user.getRole()
                        );

                    return ResponseEntity.ok(userResponse);
                }
            } else {
                log.error("User not found with email: " + email);
            }

        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Invalid credentials");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Invalid credentials"));
    }

    @GetMapping("/admins")
    public ResponseEntity<List<Integer>> getAllAdminUsers() {
        List<Integer> adminUsers = userService.findAllAdminUsers();

        if (adminUsers.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(adminUsers);
        }
    }

    @GetMapping("/confirm")
    public ResponseEntity<String> confirmUser(@RequestParam @NotNull Long id) {
        boolean isActivated = userService.activateUser(id);
        if (isActivated) {
            return ResponseEntity.ok("Usuario activado con éxito.");
        } else {
            return ResponseEntity.badRequest().body("No se pudo activar el usuario.");
        }
    }

    @PostMapping("/resend-confirmation")
    public ResponseEntity<Object> resendEmailConfirmation(@RequestBody String email) {
        try {
            userService.resendEmailConfirmation(email);
            return ResponseEntity.ok("Email confirmation resent successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Email not sent");
        }
    }






}
