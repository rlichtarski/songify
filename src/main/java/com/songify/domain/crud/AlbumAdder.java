package com.songify.domain.crud;

import com.songify.domain.crud.dto.AlbumDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Log4j2
@Service
@AllArgsConstructor(access = lombok.AccessLevel.PACKAGE)
class AlbumAdder {

    private final AlbumRepository albumRepository;
    private final SongRetriever songRetriever;

    public AlbumDto addAlbum(Long songId, String title, Instant releaseDate) {
        final Song songById = songRetriever.findSongById(songId);
        Album album = new Album();
        album.setTitle(title);
        album.setReleaseDate(releaseDate);
        album.addSong(songById);
        final Album savedAlbum = albumRepository.save(album);
        return new AlbumDto(savedAlbum.getId(), savedAlbum.getTitle());
    }

}
