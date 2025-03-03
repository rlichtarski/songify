package com.songify.infrastructure.usercrud.controller;

import jakarta.validation.constraints.NotNull;

record RegisterUserRequestDto(
        @NotNull(message = "User name cannot be null")
        String userName,
        @NotNull(message = "Password cannot be null")
        String password
) {
}
