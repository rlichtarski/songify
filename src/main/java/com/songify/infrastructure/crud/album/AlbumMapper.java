package com.songify.infrastructure.crud.album;


import com.songify.domain.crud.dto.AlbumDto;
import com.songify.infrastructure.crud.album.dto.response.GetAllAlbumsResponseDto;

import java.util.List;
import java.util.Set;

class AlbumMapper {

    static GetAllAlbumsResponseDto mapFromAllAlbumsToGetAllAlbumsResponseDto(Set<AlbumDto> albums) {
        return new GetAllAlbumsResponseDto(albums);
    }
}