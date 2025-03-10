// IT IS COMMENTED OU BECAUSE OF OAUTH2 FOR GOOGLE

//package com.songify.infrastructure.security.jwt;
//
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.AllArgsConstructor;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.security.NoSuchAlgorithmException;
//
//@RestController
//@RequiredArgsConstructor
//@Log4j2
//class JwtTokenController {
//
//    private final JwtTokenGenerator tokenGenerator;
//
//    @PostMapping("/token")
//    public ResponseEntity<JwtResponseDto> authenticateAndGenerateToken(
//            @RequestBody TokenRequestDto dto,
//            HttpServletResponse response
//    ) throws NoSuchAlgorithmException {
//        String token = tokenGenerator.authenticateAndGenerateToken(dto.username(), dto.password());
//        Cookie cookie = new Cookie("accessToken", token);
//        cookie.setHttpOnly(true);
//        cookie.setSecure(true); //ensure the cookie is sent only via HTTPS
//        cookie.setPath("/");
//        cookie.setMaxAge(60 * 60); //1h
//        response.addCookie(cookie);
//        return ResponseEntity.ok(
//                JwtResponseDto
//                        .builder()
//                        .token(token)
//                        .build()
//        );
//    }
//
//}
