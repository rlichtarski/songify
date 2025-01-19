package com.songify.domain.crud;

import com.songify.domain.crud.dto.ArtistDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@AllArgsConstructor(access = lombok.AccessLevel.PACKAGE)
class ArtistAdder {

    private final ArtistRepository artistRepository;

    ArtistDto addArtist(final String name) {
        final Artist artist = new Artist(name);
        final Artist savedArtist = artistRepository.save(artist);
        return new ArtistDto(savedArtist.getId(), savedArtist.getName());
    }
}
