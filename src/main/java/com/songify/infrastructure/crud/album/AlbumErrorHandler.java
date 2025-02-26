package com.songify.infrastructure.crud.album;

import com.songify.domain.crud.AlbumNotFoundException;
import com.songify.domain.crud.SongNotFoundException;
import com.songify.infrastructure.crud.album.dto.response.ErrorAlbumResponseDto;
import com.songify.infrastructure.crud.song.controller.error.ErrorSongResponseDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Log4j2
class AlbumErrorHandler {

    @ExceptionHandler(AlbumNotFoundException.class)
    public ResponseEntity<ErrorAlbumResponseDto> handleException(AlbumNotFoundException exception) {
        log.warn("AlbumNotFoundException while accessing songName");
        ErrorAlbumResponseDto errorSongResponseDto = new ErrorAlbumResponseDto(exception.getMessage(), HttpStatus.NOT_FOUND);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorSongResponseDto);
    }
}
