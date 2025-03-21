package com.songify.infrastructure.crud.artist;

import com.songify.domain.crud.dto.ArtistDto;

import java.util.Set;

public record AllArtistsResponseDto(
        Set<ArtistDto> artists
) {
}
