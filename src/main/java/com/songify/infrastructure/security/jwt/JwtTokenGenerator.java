package com.songify.infrastructure.security.jwt;

import org.springframework.stereotype.Component;

@Component
class JwtTokenGenerator {
    String authenticateAndGenerateToken(final String username, final String password) {
        return "token123";
    }

}
