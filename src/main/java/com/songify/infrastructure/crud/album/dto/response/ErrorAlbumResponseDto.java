package com.songify.infrastructure.crud.album.dto.response;

import org.springframework.http.HttpStatus;

public record ErrorAlbumResponseDto(String message, HttpStatus httpStatus) { }
