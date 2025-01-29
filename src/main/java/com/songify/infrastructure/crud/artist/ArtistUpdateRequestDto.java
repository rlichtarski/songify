package com.songify.infrastructure.crud.artist;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ArtistUpdateRequestDto(
        @NotNull(message = "artist name must not be null!")
        @NotEmpty(message = "artist name must not be null!")
        String artistName) {
}
