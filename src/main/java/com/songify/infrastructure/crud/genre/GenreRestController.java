package com.songify.infrastructure.crud.genre;

import com.songify.domain.crud.SongifyCrudFacade;
import com.songify.domain.crud.dto.GenreDto;
import com.songify.domain.crud.dto.GenreRequestDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@Log4j2
@RequestMapping("/genres")
@AllArgsConstructor
class GenreRestController {

    private final SongifyCrudFacade songifyCrudFacade;

    @PostMapping
    ResponseEntity<GenreDto> addGenre(@RequestBody GenreRequestDto genreRequestDto) {
        final GenreDto genreDto = songifyCrudFacade.addGenre(genreRequestDto);
        return ResponseEntity.ok(genreDto);
    }

    @GetMapping
    ResponseEntity<GetAllGenresResponseDto> getGenres() {
        final Set<GenreDto> genreDto = songifyCrudFacade.retrieveGenres();
        return ResponseEntity.ok(new GetAllGenresResponseDto(genreDto));
    }


}
