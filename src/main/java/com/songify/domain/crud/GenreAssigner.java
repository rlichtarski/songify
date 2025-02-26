
package com.songify.domain.crud;

import com.songify.domain.crud.dto.GenreDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@AllArgsConstructor(access = lombok.AccessLevel.PACKAGE)
class GenreAssigner {

    private final SongRetriever songRetriever;
    private final GenreRetriever genreRetriever;

    void assignDefaultGenreToSong(final Long id) {
        final Song songById = songRetriever.findSongById(id);
        Genre genre = genreRetriever.findGenreById(1L);
        songById.setGenre(genre);
    }

    void assignGenreToSong(final Long songId, final Long genreId) {
        final Song songById = songRetriever.findSongById(songId);
        Genre genre = genreRetriever.findGenreById(genreId);
        songById.setGenre(genre);
    }
}
