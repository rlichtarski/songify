package com.songify.infrastructure.security.jwt;

import com.auth0.jwt.JWT;
import com.songify.infrastructure.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Component
@RequiredArgsConstructor
class JwtTokenGenerator {

    private final AuthenticationManager authenticationManager;
    private final Clock clock;
    private final JwtConfigurationProperties properties;

    String authenticateAndGenerateToken(final String username, final String password) {
        final UsernamePasswordAuthenticationToken authenticate = new UsernamePasswordAuthenticationToken(username, password);
        final Authentication authentication = authenticationManager.authenticate(authenticate);
        SecurityUser user = (SecurityUser) authentication.getPrincipal();
        final Instant issuedAt = LocalDateTime.now(clock).toInstant(ZoneOffset.UTC);
        final Instant expiresAt = issuedAt.plus(Duration.ofMinutes(properties.expirationMinutes()));
        return JWT.create()
                .withSubject(user.getUsername())
                .withIssuedAt(issuedAt)
                .withExpiresAt(expiresAt)
                .withIssuer(properties.issuer())
                .withClaim("roles", user.getAuthoritiesAsString())
                .sign(null);
    }

}
