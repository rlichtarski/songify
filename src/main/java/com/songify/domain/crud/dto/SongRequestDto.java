package com.songify.domain.crud.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.Instant;

@Builder
public record SongRequestDto(
        @NotNull(message = "song name must not be null")
        @NotEmpty(message = "song name must not be empty")
        String name,
        Instant releaseDate,
        Long duration,
        SongLanguageDto language
) {
}
