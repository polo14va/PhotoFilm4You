package edu.uoc.epcsd.user.controllers;

import edu.uoc.epcsd.user.config.ClientUtils;
import edu.uoc.epcsd.user.controllers.dtos.CreateUserRequest;
import edu.uoc.epcsd.user.controllers.dtos.GetUserResponse;
import edu.uoc.epcsd.user.entities.User;
import edu.uoc.epcsd.user.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Log4j2
@RestController
@RequestMapping("/users")
public class UserController {

    private static final String ERROR_PROCES = "Error al procesar la solicitud, porfavor contacta con el administrador";
    private static final String MSG_EMAIL_RESET="Email enviado \nPor favor, Revisa tu correo electronico para cambiar la contraseña";
    
    private static final String ERROR_EXCEPTION = "An unexpected error occurred: ";
    private static final String ERROR_EXCEPTION_MSG = "Error: ";
    private final UserService userService;
    private final ClientUtils clientUtils;
    @Autowired
    public UserController(UserService userService, ClientUtils clientUtils) {
        this.userService = userService;
        this.clientUtils = clientUtils;
    }

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAllUsers() {
        log.trace("getAllUsers");

        return userService.findAll();
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<GetUserResponse> getUserById(@PathVariable @NotNull Long userId) {
        log.trace("getUserById");

        return userService.findById(userId).map(user -> ResponseEntity.ok().body(GetUserResponse.fromDomain(user)))
                .orElse(ResponseEntity.notFound().build());
    }
    @PutMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable @NotNull Long userId, @RequestBody CreateUserRequest updateUserRequest) {
        log.trace("updateUser");

        try {
            User updatedUser = userService.updateUser(
                    userId,
                    updateUserRequest.getEmail(),
                    updateUserRequest.getFullName(),
                    updateUserRequest.getPhoneNumber()
            );

            return ResponseEntity.ok().body(updatedUser);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body("Update Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocurrió un error inesperado");
        }
    }

    @GetMapping("/toAlert")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<GetUserResponse[]> getUsersToAlert(@RequestParam @NotNull Long productId, @RequestParam @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)  LocalDate availableOnDate) {
        log.trace("getUsersToAlert");

        return ResponseEntity.ok().body(userService.getUsersToAlert(productId, availableOnDate).stream().map(GetUserResponse::fromDomain).toArray(GetUserResponse[]::new));
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createUser(@RequestBody CreateUserRequest createUserRequest) {
        log.trace("createUser");
        try {
            log.trace("Creating user " + createUserRequest);

            User createdUser = userService.createUser(
                    createUserRequest.getEmail(),
                    createUserRequest.getPassword(),
                    createUserRequest.getFullName(),
                     createUserRequest.getPhoneNumber()
            );



            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(createdUser.getId());
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body(ERROR_EXCEPTION_MSG + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    @PutMapping("/setAdmin")
    public ResponseEntity<Object> setAdmin(@RequestParam String email) {
        log.trace("setAdmin");
        try {
            User updatedUser = userService.setAdmin(
                    email
            );
            return ResponseEntity.ok().body(updatedUser);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body(ERROR_EXCEPTION_MSG + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocurrió un error inesperado");
        }
    }

    @PostMapping("/send-change-password-email")
    public ResponseEntity<Object> sendChangePasswordEmail(@RequestBody Map<String, String> requestBody) {
        log.trace("sendChangePasswordEmail");

        try {
            String jwtToken = requestBody.get("token");
            if(!userService.sendEmailPassword(jwtToken)) {
                return ResponseEntity.badRequest().body(ERROR_PROCES);
            }

            return ResponseEntity.ok().body(MSG_EMAIL_RESET);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ERROR_EXCEPTION + e.getMessage());
        }
    }

    @GetMapping("/send-change-password-unsafe-email")
    public ResponseEntity<Object> sendChangePasswordEmail(@RequestParam(name = "email") @NotNull String email , HttpServletRequest request) {
        log.trace("sendChangePasswordEmail");

        try {
            String clientIpAddress = clientUtils.getClientIpAddress(request);
            if(!userService.sendPasswordResetEmailUnsafe(email,clientIpAddress)) {
                return ResponseEntity.ok().body(MSG_EMAIL_RESET);
            }

            return ResponseEntity.ok().body(MSG_EMAIL_RESET);
        } catch (Exception e) {
            return ResponseEntity.ok().body(MSG_EMAIL_RESET);
        }
    }



    @PostMapping("/change-password")
    public ResponseEntity<Object> changePassword(@RequestBody Map<String, String> requestBody) {
        log.trace("changePassword");

        try {
            String jwtToken = requestBody.get("token");
            String tempCode = requestBody.get("tempCode");
            String newPassword = requestBody.get("newPassword");
            if (newPassword == null) {
                return ResponseEntity.badRequest().body(ERROR_PROCES);
            }

            String email = requestBody.get("email");
            String uuid = requestBody.get("safecode");

            if(newPassword.length() >= 8) {
                if(jwtToken != null && tempCode != null )
                {
                    if (userService.updatePasswordByToken(jwtToken, newPassword)) {
                        return ResponseEntity.ok().body("Password cambiado correctamente");
                    }
                }else{

                    if(uuid != null && email!= null && userService.updatePasswordUnsafe(uuid,email,newPassword)){
                        return ResponseEntity.ok().body("Password modificado");
                    }
                }
            }
            return ResponseEntity.badRequest().body(ERROR_PROCES);

        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ERROR_EXCEPTION + e.getMessage());
        }
    }


}
