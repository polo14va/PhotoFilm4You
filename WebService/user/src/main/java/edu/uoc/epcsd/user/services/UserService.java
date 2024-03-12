package edu.uoc.epcsd.user.services;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.uoc.epcsd.user.entities.Alert;
import edu.uoc.epcsd.user.entities.EmailMessage;
import edu.uoc.epcsd.user.entities.User;
import edu.uoc.epcsd.user.repositories.AlertRepository;
import edu.uoc.epcsd.user.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;


import org.apache.commons.validator.routines.EmailValidator;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserService {

    private static final String EMAIL_TOPIC = "email-topic";
    private static final String EMAIL_TEMPLATE = "correo.html";
    private final UserRepository userRepository;
    private final AlertRepository alertRepository;
    private final PasswordEncoder passwordEncoder;

    private final KafkaTemplate<String, EmailMessage> kafkaTemplate;
    private final TokenService tokenService;

    private static final long MAX_RESET_REQUESTS_PER_MINUTE = 2;
    private final Map<String, Long> ipRequestCount = new HashMap<>();
    private final Map<String, Long> ipLastRequestTime = new HashMap<>();

    @Value("${userService.changePassword.url}")
    private String passwordUrl;

    @Value("${userService.confirmIdURL.url.mail}")
    private String confirmIdURL;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public List<Integer> findAllAdminUsers() {
        return userRepository.findAllAdminUsers();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User createUser(String email, String password, String fullName, String phoneNumber) {

        User user = User.builder()
                .active(0)
                .email(email)
                .fullName(fullName)
                .password(passwordEncoder.encode(password))
                .phoneNumber(phoneNumber)
                .role("USER")
                .build();

        userRepository.save(user);

        String confirmURL = String.format(confirmIdURL,user.getId().toString());

        EmailMessage emailMessage = new EmailMessage(
                user.getEmail(),
                "Bienvenido a Nuestra Plataforma",
                EMAIL_TEMPLATE,
                "Bienvenido a nuestra plataforma, por favor confirma tu cuenta haciendo click en el siguiente enlace:",
                confirmURL,
                "Confirmar Cuenta"
        );

        kafkaTemplate.send(EMAIL_TOPIC,emailMessage );

        return user;
    }

    public Set<User> getUsersToAlert(Long productId, LocalDate availableOnDate) {

        return alertRepository.findAlertsByProductIdAndInterval(productId, availableOnDate).stream().map(Alert::getUser).collect(Collectors.toSet());
    }


    public boolean activateUser(Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setActive(1);
            userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }


    public void resendEmailConfirmation(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isPresent()) {
            User user = userOptional.get();
            String confirmURL = String.format(confirmIdURL, user.getId().toString());

            if (user.getActive() == 0) {
                EmailMessage emailMessage = new EmailMessage(
                        user.getEmail(),
                        "Bienvenido a Nuestra Plataforma",
                        EMAIL_TEMPLATE,
                        "Bienvenido a nuestra plataforma, por favor confirma tu cuenta haciendo click en el siguiente enlace:",
                        confirmURL,
                        "Confirmar Cuenta"
                );

                kafkaTemplate.send(EMAIL_TOPIC, emailMessage);
            }
        } else {
            log.error("Usuario no encontrado con el email: " + email);
        }
    }


    public boolean sendEmailPassword(String token) {
        Map<String, Object> claims = tokenService.readToken(token);

        if (claims.containsKey("sub")) {
            String email = (String) claims.get("sub");

            Optional<User> userOptional = userRepository.findByEmail(email);

            if (userOptional.isPresent()) {
                User user = userOptional.get();
                sendPasswordResetEmail(user, token);
                return true;
            }
        }
        return false;
    }
    private void sendPasswordResetEmail(User user, String token) {
        String resetPasswordLink = String.format(passwordUrl, token);

        EmailMessage emailMessage = new EmailMessage(
                user.getEmail(),
                "Restablecer Contraseña",
                EMAIL_TEMPLATE,
                "Por favor, restablece tu contraseña usando el siguiente enlace:",
                resetPasswordLink,
                "Ir a restablecer Contraseña"
        );

        kafkaTemplate.send(EMAIL_TOPIC, emailMessage);
    }

    private boolean isValidEmail(String email) {
        return email != null && EmailValidator.getInstance().isValid(email);
    }

    public boolean isRateLimited(String ipAddress) {
        long currentTime = System.currentTimeMillis();
        Long lastRequestTime = ipLastRequestTime.get(ipAddress);
        Long requestCount = ipRequestCount.getOrDefault(ipAddress, 0L);

        if (lastRequestTime == null || currentTime - lastRequestTime >= 60000) {
            ipRequestCount.put(ipAddress, 1L);
            ipLastRequestTime.put(ipAddress, currentTime);
        } else {
            ipRequestCount.put(ipAddress, requestCount + 1);
        }
        return ipRequestCount.get(ipAddress) > MAX_RESET_REQUESTS_PER_MINUTE;
    }

    public boolean sendPasswordResetEmailUnsafe(String email, String clientIpAddress) {
        if (!isValidEmail(email)) {
            return false;
        }
        if (isRateLimited(clientIpAddress)) {
            EmailMessage emailMessage = new EmailMessage(
                    "pds.g5.uoc@gmail.com",
                    "ABUSO DE RESET CONTRASEÑA ALCANZADO",
                    EMAIL_TEMPLATE,
                    "el email: "+email+" ha intentado abusar del sistema de reseteo de contraseña desde la IP:"+clientIpAddress + ", por favor, revisa el sistema y toma las medidas necesarias",
                    "http://localhost/admin",
                    "IR APANEL DE CONTROL"
            );

            kafkaTemplate.send(EMAIL_TOPIC, emailMessage);
            return false;
        }

        Optional<User> userOptional = userRepository.findByEmail(email);

        String resetPasswordLink = String.format(passwordUrl, tokenService.generateUUID())
                .replace("TEMPCODE", "SAFECODE")
                .concat("&EMAIL=").concat(email);

        if (userOptional.isPresent()) {
            EmailMessage emailMessage = new EmailMessage(
                    email,
                    "Restablecer Contraseña",
                    EMAIL_TEMPLATE,
                    "Por favor, restablece tu contraseña usando el siguiente enlace:",
                    resetPasswordLink,
                    "Ir a restablecer Contraseña"
            );

            kafkaTemplate.send(EMAIL_TOPIC, emailMessage);
            return true;
        }
        return false;
    }

    private void sendPasswordResetConfirmEmail(User user, String token) {
        if (token == null) {
            token = String.format(passwordUrl, tokenService.generateUUID())
                    .replace("TEMPCODE", "SAFECODE")
                    .concat("&EMAIL=").concat(user.getEmail());
        }
        EmailMessage emailMessage = new EmailMessage(
                user.getEmail(),
                "Contraseña restablecida",
                EMAIL_TEMPLATE,
                "Se informa de que su contraseña ha sido restablecida, en caso de no haber sido UD. por favor redirijase al siguente enlace para restablecerla:",
                token,
                "Reatablecer contraseña de forma segura"
        );

        kafkaTemplate.send(EMAIL_TOPIC, emailMessage);
    }

    public boolean updatePasswordByToken(String token, String password) {
        Map<String, Object> claims = tokenService.readToken(token);

        if (claims.containsKey("sub")) {
            String email = (String) claims.get("sub");

            Optional<User> userOptional = userRepository.findByEmail(email);

            if (userOptional.isPresent()) {
                User user = userOptional.get();

                user.setPassword(passwordEncoder.encode(password)); // Codifica la nueva contraseña
                userRepository.save(user);
                sendPasswordResetConfirmEmail(user, token);
                return true;
            }
        }
        return false;
    }

    public boolean updatePasswordUnsafe(String uuid, String email, String password) {
        if (tokenService.isUUIDValid(uuid) && password != null && isValidEmail(email)) {
            Optional<User> userOptional = userRepository.findByEmail(email);

            if (userOptional.isPresent()) {
                User user = userOptional.get();

                user.setPassword(passwordEncoder.encode(password));
                userRepository.save(user);
                sendPasswordResetConfirmEmail(user, null);
                return true;
            }
        }
        return false;
    }

    @Transactional
    public User updateUser(Long userId, String email, String fullName, String phoneNumber) {
        return userRepository.findById(userId).map(user -> {
            user.setEmail(email);
            user.setFullName(fullName);
            user.setPhoneNumber(phoneNumber);
            return userRepository.save(user);
        }).orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + userId));
    }

    @Transactional
    public User setAdmin(String email){
        return userRepository.findByEmail(email).map(user -> {
            user.setRole("ADMIN");

            return userRepository.save(user);
        }).orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con email: " + email));

    }

    public void setPasswordUrl(String passwordUrl) {
        this.passwordUrl = passwordUrl;
    }

    public void setConfirmIdURL(String confirmIdURL) {
        this.confirmIdURL = confirmIdURL;
    }


}
