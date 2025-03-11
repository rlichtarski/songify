package com.songify.infrastructure.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
class MySuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication) throws IOException, ServletException {
        final OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
        setResponseCookie(response, oidcUser.getIdToken().getTokenValue());
        this.setAlwaysUseDefaultTargetUrl(true);
//        this.setDefaultTargetUrl("/token"); //THIS IS FOR STATEFUL OAUTH2
        this.setDefaultTargetUrl("https://localhost:3000"); //THIS IS FOR FRONTEND AFTER SUCCESSFUL LOGIN
        super.onAuthenticationSuccess(request, response, authentication);
    }

    private void setResponseCookie(HttpServletResponse response, String tokenValue) {
        Cookie cookie = new Cookie("accessToken", tokenValue);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60); // 1 hour
        response.addCookie(cookie);
    }

}
