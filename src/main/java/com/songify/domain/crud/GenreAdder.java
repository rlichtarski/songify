package com.songify.domain.crud;

import com.songify.domain.crud.dto.GenreDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@AllArgsConstructor(access = lombok.AccessLevel.PACKAGE)
class GenreAdder {

    private final GenreRepository genreRepository;

    public GenreDto addGenre(String name) {
        Genre genre = new Genre(name);
        final Genre toSave = genreRepository.save(genre);
        return new GenreDto(toSave.getId(), toSave.getName());
    }

}
