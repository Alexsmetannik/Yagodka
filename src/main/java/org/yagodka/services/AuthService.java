package org.yagodka.services;

import org.yagodka.models.Token;
import org.yagodka.models.User;
import org.yagodka.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Value("${token.secret}")
    private String secret;

    @Value("${token.expiration}")
    private long expiration;

    private final Map<String, Long> activeTokens = new HashMap<>();
    private final SecretKey key;

    public AuthService(@Value("${token.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public Token register(User user) {
        userRepository.save(user);
        User registeredUser = userRepository.findByUsername(user.getUsername());
        return generateToken(registeredUser);
    }

    public Token login(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return generateToken(user);
        }
        return null;
    }

    public boolean logout(String token) {
        return activeTokens.remove(token) != null;
    }

    public boolean validateToken(String token) {
        return activeTokens.containsKey(token);
    }

    public Long getUserIdFromToken(String token) {
        return activeTokens.get(token);
    }

    private Token generateToken(User user) {
        String token = Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key, Jwts.SIG.HS512)
                .compact();

        activeTokens.put(token, user.getId());
        return new Token(token, user.getId());
    }
}
