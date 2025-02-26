package com.songify.infrastructure.crud.album.dto.response;

import com.songify.domain.crud.dto.AlbumDto;

import java.util.List;
import java.util.Set;

public record GetAllAlbumsResponseDto(Set<AlbumDto> albums) {
}
