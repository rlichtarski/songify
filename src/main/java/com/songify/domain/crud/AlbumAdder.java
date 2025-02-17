package com.songify.domain.crud;

import com.songify.domain.crud.dto.AlbumDto;
import com.songify.domain.crud.dto.ArtistDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

@Log4j2
@Service
@AllArgsConstructor(access = lombok.AccessLevel.PACKAGE)
class AlbumAdder {

    private final AlbumRepository albumRepository;
    private final SongRetriever songRetriever;

    AlbumDto addAlbum(Set<Long> songIds, String title, Instant releaseDate) {
        final Set<Song> songsToAdd = songIds.stream()
                .map(songRetriever::findSongById)
                .collect(Collectors.toSet());
//        final Song songById = songRetriever.findSongById(songId);
        Album album = new Album();
        album.setTitle(title);
        album.setReleaseDate(releaseDate);
        album.addSongs(songsToAdd);
        final Album savedAlbum = albumRepository.save(album);
        return new AlbumDto(
                savedAlbum.getId(),
                savedAlbum.getTitle(),
                savedAlbum.getArtists()
                        .stream().map(artist -> new ArtistDto(artist.getId(), artist.getName()))
                        .collect(Collectors.toSet())
        );
    }

    Album addAlbum(String title, Instant releaseDate) {
        Album album = new Album();
        album.setTitle(title);
        album.setReleaseDate(releaseDate);
        return albumRepository.save(album);
    }

}
