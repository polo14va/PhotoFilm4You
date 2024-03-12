package edu.uoc.epcsd.user.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class ClientUtils {
    public String getClientIpAddress(HttpServletRequest request) {
        // Obtener la dirección IP del cliente
        String ipAddress = request
            .getHeader("X-Forwarded-For");
        if (
                ipAddress == null
            ) 
            {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }
}
