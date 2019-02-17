package com.example.ppmtool.security;

import com.example.ppmtool.domain.User;
import io.jsonwebtoken.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.ppmtool.security.SecurityConstants.EXPIRATION_TIME;
import static com.example.ppmtool.security.SecurityConstants.SECRET;
import static io.jsonwebtoken.SignatureAlgorithm.HS512;

@Component
public class JwtTokenProvider {

    public String generateToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Date now = new Date(System.currentTimeMillis());

        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        String userId = Long.toString(user.getId());

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", Long.toString(user.getId()));
        claims.put("username", user.getUsername());
        claims.put("fullName", user.getFullName());
        return Jwts.builder()
                .setSubject(userId)
                .addClaims(claims)
                .setExpiration(expiryDate)
                .signWith(HS512, SECRET)
                .compact();

    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            System.out.println("Invalid token = [" + token + "]" + e.getMessage() );
        } catch (MalformedJwtException e) {
            System.out.println("Malformed token = [" + token + "]" + e.getMessage());
        } catch (ExpiredJwtException e) {
            System.out.println("Expired token = [" + token + "]" + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Illegal argument  = [" + token + "]" + e.getMessage());
        }
        return false;
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        String idStr = claims.get("id").toString();

        return Long.parseLong(idStr);

    }
}
