package edu.uoc.epcsd.user.services;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import edu.uoc.epcsd.user.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;


@Service
public class TokenService {

	@Value("${jwt.secret}")
    private String jwtSigningKey;


    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token); return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token); return claimsResolvers.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
      Map<String, Object> claims = new HashMap<>(); claims.put("role", ((User)(userDetails)).getRole()); claims.put("id", ((User)(userDetails)).getId()); return Jwts.builder().setClaims(claims).setSubject(userDetails.getUsername()).setIssuedAt(new Date(System.currentTimeMillis())).setExpiration(new Date(System.currentTimeMillis() + 3600000)).signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey); return Keys.hmacShaKeyFor(keyBytes);
    }

	
    public Map<String, Object> readToken(String jwtToken) {
    	return extractAllClaims(jwtToken);
    }

    public String generateUUID() {
        UUID uuid = UUID.randomUUID();

        long timestamp = System.currentTimeMillis();
        String tokenData = uuid.toString() + timestamp;

        return Base64.getEncoder().encodeToString(tokenData.getBytes());

    }

    public boolean isUUIDValid(String token) {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(token);
            String decodedToken = new String(decodedBytes);

            // Extraer el timestamp de la cadena decodificada
            String timestampStr = decodedToken.substring(36);
            long timestamp = Long.parseLong(timestampStr);

            long currentTime = System.currentTimeMillis();
            long timeDifference = currentTime - timestamp;

            // Verificar si la diferencia es menor a 10 minutos (600,000 milisegundos)
            return timeDifference < 600000;
        } catch (Exception e) {
            return false;
        }
    }
}
