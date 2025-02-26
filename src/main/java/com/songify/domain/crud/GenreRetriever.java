
package com.songify.domain.crud;

import com.songify.domain.crud.dto.GenreDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Log4j2
@Service
@AllArgsConstructor(access = lombok.AccessLevel.PACKAGE)
class GenreRetriever {

    private final GenreRepository genreRepository;

    Genre findGenreById(final Long id) {
        return genreRepository.findGenreById(id)
                .orElseThrow(() -> new GenreNotFoundException("Genre not found"));
    }

    Set<GenreDto> findAll() {
        return genreRepository.findAll()
                .stream()
                .map(genre -> new GenreDto(genre.getId(), genre.getName()))
                .collect(Collectors.toSet());
    }
}
