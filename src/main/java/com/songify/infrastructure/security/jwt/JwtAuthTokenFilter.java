// IT IS COMMENTED OU BECAUSE OF OAUTH2 FOR GOOGLE


//package com.songify.infrastructure.security.jwt;
//
//import com.auth0.jwt.JWT;
//import com.auth0.jwt.JWTVerifier;
//import com.auth0.jwt.algorithms.Algorithm;
//import com.auth0.jwt.interfaces.DecodedJWT;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.security.KeyPair;
//import java.security.interfaces.RSAPublicKey;
//import java.util.List;
//
//@Component
//@Log4j2
//@RequiredArgsConstructor
//public class JwtAuthTokenFilter extends OncePerRequestFilter {
//
//    private final KeyPair keyPair;
//
//    @Override
//    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
//        log.info(request.toString());
//        String token = getTokenFromRequest(request);
//        if (token == null) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//        final JWTVerifier jwtVerifier = JWT.require(Algorithm.RSA256((RSAPublicKey) keyPair.getPublic(), null))
//                .build();
//
//        final DecodedJWT decodedToken = jwtVerifier.verify(token);
//        final String login = decodedToken.getSubject();
//        final List<SimpleGrantedAuthority> roles = decodedToken.getClaim("roles")
//                .asList(String.class)
//                .stream()
//                .map(SimpleGrantedAuthority::new)
//                .toList();
//
//        final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(login, null, roles);
//        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
//
//        filterChain.doFilter(request, response);
//    }
//
//    private String getTokenFromRequest(HttpServletRequest request) {
//        String authorizationHeader = request.getHeader("Authorization");
//        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer")) {
//            return authorizationHeader.substring(7);
//        }
//        if (request.getCookies() != null) {
//            for (Cookie cookie : request.getCookies()) {
//                if (cookie.getName().equals("accessToken")) {
//                    return cookie.getValue();
//                }
//            }
//        }
//        return null;
//    }
//
//
//}
