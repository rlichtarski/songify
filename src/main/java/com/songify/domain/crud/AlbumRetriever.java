package com.songify.domain.crud;

import com.songify.domain.crud.dto.AlbumInfo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
class AlbumRetriever {
    private final AlbumRepository albumRepository;

    AlbumInfo findAlbumByIdWithArtistsAndSongs(final Long id) {
        return albumRepository.findAlbumByIdWithArtistsAndSongs(id)
                .orElseThrow(() -> new AlbumNotFoundException("Album with id: " + id + " not found"));
    }
}
