package com.ortim.security.jwt;

import com.ortim.component.MessageService;
import com.ortim.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtTokenProvider {

    @Value("${jwt.secret.key}")
    private String secretKey;
    @Getter
    @Value("${jwt.expiration}")
    private long jwtExpiration;
    @Getter
    @Value("${jwt.refresh_token.expiration}")
    private long refreshExpiration;

    private final JwtTokenBlackList jwtTokenBlackList;

    private final MessageService messageService;

    @Autowired
    public JwtTokenProvider(JwtTokenBlackList jwtTokenBlackList, MessageService messageService) {
        this.jwtTokenBlackList = jwtTokenBlackList;
        this.messageService = messageService;
    }

    public String extractUserEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(User user) {
        if (user == null) {
            throw new IllegalArgumentException(
                    messageService.getMessage("user.not.found.message", null)
            );
        }

        return buildToken(new HashMap<>(), user.getEmail(), jwtExpiration);
    }

    public String generateToken(User user, long expiration) {
        if (user == null) {
            throw new IllegalArgumentException(
                    messageService.getMessage("user.not.found.message", null)
            );
        }
        return buildToken(new HashMap<>(), user.getEmail(), expiration);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails.getUsername(), jwtExpiration);
    }

    public String generateRefreshToken(User user) {
        return buildToken(new HashMap<>(), user.getEmail(), refreshExpiration);
    }

    private String buildToken(Map<String, Object> extraClaims, String subject, long expiration) {
        try {
            return Jwts.builder()
                    .setClaims(extraClaims)
                    .setSubject(subject)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + expiration))
                    .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                    .compact();
        } catch (JwtException e) {
            throw new RuntimeException(
                    messageService.getMessage("token.create.false.message", null)
            );
        }
    }

    public boolean validateToken(String token, User user) {
        if (jwtTokenBlackList.isContainsBlackListToken(token)) {
            return false;
        }
        final String username = extractUserEmail(token);
        return username.equals(user.getEmail()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        Date expiration = extractExpiration(token);
        return expiration != null && expiration.before(new Date());
    }

    private Date extractExpiration(String token) {
        try {
            return extractClaim(token, Claims::getExpiration);
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException | IllegalArgumentException e) {
            return Jwts.claims();
        }
    }

    private Key getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}