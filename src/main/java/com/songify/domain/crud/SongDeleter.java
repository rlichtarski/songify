package com.songify.domain.crud;

import com.songify.domain.crud.dto.SongDto;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Set;

@Log4j2
@Service
@Transactional
@AllArgsConstructor(access = lombok.AccessLevel.PACKAGE)
class SongDeleter {

    private final SongRepository songRepository;
    private final SongRetriever songRetriever;
    private final GenreDeleter genreDeleter;

    void deleteById(Long id) {
        songRetriever.existsById(id);
        log.info("deleting a song with id " + id);
        songRepository.deleteById(id);
    }

    void deleteSongAndGenreById(final Long songId) {
        final SongDto songDtoById = songRetriever.findSongDtoById(songId);
        final Long genreId = songDtoById.genre().id();

        deleteById(songId);
        genreDeleter.deleteById(genreId);
    }

    void deleteAllSongsById(final Set<Long> songsIds) {
        songRepository.deleteByIdIn(songsIds);
    }
}

