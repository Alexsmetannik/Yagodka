package org.yagodka.models;

public class Token {
    private String token;
    private Long userId;

    public Token(String token, Long userId) {
        this.token = token;
        this.userId = userId;
    }

    // Getters
    public String getToken() { return token; }
    public Long getUserId() { return userId; }
}
