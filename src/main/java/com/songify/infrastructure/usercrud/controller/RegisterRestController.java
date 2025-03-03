package com.songify.infrastructure.usercrud.controller;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@Log4j2
@AllArgsConstructor
class RegisterRestController {

    private final UserDetailsManager userDetailsManager;

    @PostMapping("/register")
    public ResponseEntity<RegisterUserResponseDto> register(@RequestBody RegisterUserRequestDto dto) {
        final String userName = dto.userName();
        final String password = dto.password();
        userDetailsManager.createUser(
                User.builder()
                        .username(userName)
                        .password(password)
                        .build()

        );
        return ResponseEntity.ok(new RegisterUserResponseDto("Created user"));
    }

}
