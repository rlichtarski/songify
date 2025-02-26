package com.songify.domain.crud;

import com.songify.domain.crud.dto.AlbumDto;
import com.songify.domain.crud.dto.ArtistDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
class SongAssigner {

    private final SongRetriever songRetriever;
    private final AlbumRetriever albumRetriever;

    AlbumDto assignSongToAlbum(final Long albumId, final Long songId) {
        final Album album = albumRetriever.findById(albumId);
        final Song song = songRetriever.findSongById(songId);
        album.addSong(song);
        final Set<ArtistDto> artistsDtos = album.getArtists()
                .stream()
                .map(artist -> new ArtistDto(
                        artist.getId(),
                        artist.getName()
                )).collect(Collectors.toSet());
        return new AlbumDto(
                album.getId(),
                album.getTitle(),
                artistsDtos,
                album.getSongsIds()
        );
    }
}
