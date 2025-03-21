package com.songify.domain.crud;

import com.songify.domain.crud.dto.ArtistDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Log4j2
class ArtistRetriever {

    private final ArtistRepository artistRepository;

    Set<ArtistDto> findAllArtist(final Pageable pageable) {
        return artistRepository.findAll(pageable)
                .stream()
                .map(artist -> new ArtistDto(
                        artist.getId(),
                        artist.getName()
                ))
                .collect(Collectors.toSet());

    }

    Artist findById(final Long artistId) {
        return artistRepository.findById(artistId)
            .orElseThrow(() -> new ArtistNotFoundException("Artist with id " + artistId + " not found"));
    }
}
