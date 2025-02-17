package com.songify.domain.crud;

import com.songify.domain.crud.dto.AlbumDto;
import com.songify.domain.crud.dto.AlbumInfo;
import com.songify.domain.crud.dto.ArtistDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
class AlbumRetriever {
    private final AlbumRepository albumRepository;

    AlbumInfo findAlbumByIdWithArtistsAndSongs(final Long id) {
        return albumRepository.findAlbumByIdWithArtistsAndSongs(id)
                .orElseThrow(() -> new AlbumNotFoundException("Album with id: " + id + " not found"));
    }

    long countArtistsByAlbumId(final Long id) {
        return findById(id).getArtists()
                .size();
    }

    Set<Album> findAlbumsByArtistId(final Long artistId) {
        return albumRepository.findAllAlbumsByArtistId(artistId);
    }

    Set<AlbumDto> findAlbumsDtosByArtistId(final Long artistId) {
        return findAlbumsByArtistId(artistId).stream()
                .map(album -> new AlbumDto(
                        album.getId(),
                        album.getTitle(),
                        album.getArtists()
                                .stream().map(artist -> new ArtistDto(artist.getId(), artist.getName()))
                                .collect(Collectors.toSet())
                )).collect(Collectors.toSet());
    }

    Album findById(final Long albumId) {
        return albumRepository.findById(albumId)
                .orElseThrow(() -> new AlbumNotFoundException("Album with id: " + albumId + " not found"));
    }

    AlbumDto findDtoById(final Long albumId) {
        final Album album = findById(albumId);
        return new AlbumDto(
                album.getId(),
                album.getTitle(),
                album.getArtists()
                        .stream().map(artist -> new ArtistDto(artist.getId(), artist.getName()))
                        .collect(Collectors.toSet())
        );
    }

    Set<AlbumDto> findAll() {
        return albumRepository.findAll()
                .stream()
                .map(album -> new AlbumDto(
                        album.getId(),
                        album.getTitle(),
                        album.getArtists()
                                .stream().map(artist -> new ArtistDto(artist.getId(), artist.getName()))
                                .collect(Collectors.toSet())
                )).collect(Collectors.toSet());
    }
}
