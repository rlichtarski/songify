package com.songify.domain.crud;

import com.songify.domain.crud.dto.ArtistDto;
import com.songify.domain.crud.dto.ArtistRequestDto;
import com.songify.domain.crud.dto.SongLanguageDto;
import com.songify.domain.crud.dto.SongRequestDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Set;
import java.util.UUID;

@Log4j2
@Service
@AllArgsConstructor(access = lombok.AccessLevel.PACKAGE)
class ArtistAdder {

    private final ArtistRepository artistRepository;
    private final AlbumAdder albumAdder;
    private final SongAdder songAdder;

    ArtistDto addArtist(final String name) {
        final Artist artist = new Artist(name);
        final Artist savedArtist = artistRepository.save(artist);
        return new ArtistDto(savedArtist.getId(), savedArtist.getName());
    }

    ArtistDto addArtistWithDefaultAlbumAndSong(final ArtistRequestDto artistRequestDto) {
        final String name = artistRequestDto.name();
        Artist save = saveArtistWithDefaultAlbumAndSong(name);
        return new ArtistDto(save.getId(), save.getName());
    }

    private Artist saveArtistWithDefaultAlbumAndSong(final String name) {
        final Album album = albumAdder.addAlbum(
                "default-album: " + UUID.randomUUID(),
                LocalDateTime.now().toInstant(ZoneOffset.UTC)
        );
//        final Album album = new Album();
//        album.setTitle("default album: " + UUID.randomUUID());
//        album.setReleaseDate(LocalDateTime.now().toInstant(ZoneOffset.UTC));
        final Song song = songAdder.addSongGetEntity(new SongRequestDto(
                "default-song-name: " + UUID.randomUUID(),
                LocalDateTime.now().toInstant(ZoneOffset.UTC),
                0L,
                SongLanguageDto.OTHER
        ));
        Artist artist = new Artist(name);
        album.addSong(song);
        artist.addAlbum(album);
        return artistRepository.save(artist);
    }

}
