package com.songify.domain.crud;

import com.songify.domain.crud.dto.GenreDto;
import com.songify.domain.crud.dto.SongDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@AllArgsConstructor(access = lombok.AccessLevel.PACKAGE)
class SongRetriever {

    private final SongRepository songRepository;

    private final List<Song> songs = new ArrayList<>();

    List<SongDto> findAll(Pageable pageable) {
        log.info("Retrieving all songs...");
        return songRepository.findAll(pageable).stream()
                .map(song -> SongDto.builder()
                        .id(song.getId())
                        .name(song.getName())
                        .genre(new GenreDto(
                                song.getGenre().getId(),
                                song.getGenre().getName()
                        ))
                        .build())
                .collect(Collectors.toList());
    }

    SongDto findSongDtoById(Long id) {
        final Song songById = songRepository.findById(id)
                .orElseThrow(() -> new SongNotFoundException("Song with id " + id + " not found"));
        return SongDto.builder()
                .id(songById.getId())
                .name(songById.getName())
                .genre(new GenreDto(
                        songById.getGenre().getId(),
                        songById.getGenre().getName()
                ))
                .build();
    }

    Song findSongById(Long id) {
        return songRepository.findById(id)
                .orElseThrow(() -> new SongNotFoundException("Song with id " + id + " not found"));
    }

    void existsById(Long id) {
        if (!songRepository.existsById(id)) {
            throw new SongNotFoundException("Song with id " + id + " not found");
        }
    }

}
