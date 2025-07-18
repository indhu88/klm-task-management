
/**
 * This package contains classes responsible for JWT token generation,
 * validation
 * It integrates with Spring Security to handle user authentication
 * and authorization using JWT tokens.
 */
package com.klm.taskmanagement.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

/**
 * Component responsible for generating and validating JWT tokens.
 * Uses the HMAC256 algorithm with a secret key loaded from application properties.
 */
@Component
public class JwtTokenGenerator {
    // Secret key for signing JWT tokens (injected from application properties)
    @Value("${jwt.secret}")
    private String jwtSecret;
    // JWT expiration time in milliseconds (injected from application properties)
    @Value("${jwt.expiration}")
    private long jwtExpirationInMs;
    /**
     * Returns the signing key algorithm for JWT creation and validation.
     */
    public Algorithm getSigningKey() {
        return Algorithm.HMAC256(jwtSecret);
    }
    /**
     * Generates a JWT token with the username as subject and roles as claims.
     *
     * @param userName the username to embed in the token subject
     * @param roles the roles to embed as claims in the token
     * @return a signed JWT token string
     */
    public String generateToken(String userName, Set<String> roles) {
        return JWT.create()
                .withSubject(userName)
                .withClaim("roles", new ArrayList<>(roles))
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .sign(getSigningKey());
    }
    /**
     * Extracts the username (subject) from the JWT token.
     *
     * @param token the JWT token string
     * @return the username embedded in the token
     */
    public String getUsernameFromToken(String token) {
        return verifyToken(token).getSubject();
    }
    /**
     * Extracts the roles claim from the JWT token as a String.
     * (Note: This currently returns the raw claim toString(),
     * consider modifying for proper role extraction.)
     *
     * @param token the JWT token string
     * @return the roles claim as a String representation
     */
    public String getRolesFromToken(String token) {
        return verifyToken(token).getClaim("roles").asList(String.class).toString();
    }
    /**
     * Verifies the token using the configured signing key.
     * Throws an exception if verification fails.
     *
     * @param token the JWT token string
     * @return the decoded JWT token
     */
    public DecodedJWT verifyToken(String token) {
        JWTVerifier verifier = JWT.require(getSigningKey()).build();
        return verifier.verify(token);
    }
    /**
     * Validates the JWT token.
     *
     * @param token the JWT token string
     * @return true if the token is valid, false otherwise
     */
    public boolean validateToken(String token) {
        try{
            verifyToken(token);
            return true;
        } catch (Exception e) {
            System.err.println("Invalid Token"+ e.getMessage());
            return false;
        }
    }
}
