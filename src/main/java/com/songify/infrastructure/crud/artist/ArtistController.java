package com.songify.infrastructure.crud.artist;

import com.songify.domain.crud.SongifyCrudFacade;
import com.songify.domain.crud.dto.ArtistDto;
import com.songify.domain.crud.dto.ArtistRequestDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@Log4j2
@RequestMapping("/artists")
@AllArgsConstructor
class ArtistController {

    private final SongifyCrudFacade songifyCrudFacade;

    @PostMapping()
    ResponseEntity<ArtistDto> postArtist(@RequestBody ArtistRequestDto artistRequestDto) {
        final ArtistDto artistDto = songifyCrudFacade.addArtist(artistRequestDto);
        return ResponseEntity.ok(artistDto);
    }

    @GetMapping
    ResponseEntity<AllArtistsResponseDto> findAllArtist(@PageableDefault(page = 0, size = 10) Pageable pageable) {
        final Set<ArtistDto> artistSet = songifyCrudFacade.findAllArtists(pageable);
        return ResponseEntity.ok(new AllArtistsResponseDto(artistSet));
    }

}
