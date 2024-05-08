package com.car.rental.utils;

import com.car.rental.model.User;
import org.jboss.logmanager.Logger;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.NumericDate;

import javax.enterprise.context.RequestScoped;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@RequestScoped
public class TokenService {

    public final static Logger LOGGER = Logger.getLogger(TokenService.class.getSimpleName());

    // Access token expiration
    private static final int ACCESS_TOKEN_EXPIRATION_MINUTES = 120;

    // Refresh token expiration

    public String generateUserToken(User user) {
        return generateToken(user);
    }

    private String generateToken(User user) {
        try {
            JwtClaims jwtClaims = new JwtClaims();
            jwtClaims.setIssuer("car_rental_backend");
            jwtClaims.setJwtId(UUID.randomUUID().toString());
            jwtClaims.setSubject("user");
            jwtClaims.setClaim("id", user.getId());
            jwtClaims.setClaim("name", user.getName());
            jwtClaims.setClaim("lastName", user.getLastName());
            jwtClaims.setClaim("email", user.getEmail());
            jwtClaims.setClaim("phone", user.getPhone());
            jwtClaims.setClaim("username", user.getUsername());
            jwtClaims.setClaim("role", user.getRole().name());
            jwtClaims.setAudience("using-jwt");

            // Calculate expiration time
            LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(TokenService.ACCESS_TOKEN_EXPIRATION_MINUTES);
            jwtClaims.setExpirationTime(NumericDate.fromMilliseconds(expirationTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()));

            String token = TokenUtils.generateTokenString(jwtClaims);
            LOGGER.info("TOKEN generated: " + token);
            return token;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
