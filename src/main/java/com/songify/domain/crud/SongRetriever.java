package com.songify.domain.crud;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
@AllArgsConstructor(access = lombok.AccessLevel.PACKAGE)
class SongRetriever {

    private final SongRepository songRepository;

    private final List<Song> songs = new ArrayList<>();

    List<Song> findAll(Pageable pageable) {
        log.info("Retrieving all songs...");
        return songRepository.findAll(pageable);
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

    Song compareSongs() {
        Song song1 = songRepository.findById(3L)
                .orElseThrow(() -> new SongNotFoundException("Song with id " + 3L + " not found."));

        log.info(song1);
        songs.add(song1);

//        for (Song song : songs) {
//            log.info(song);
//        }

        log.info(songs.get(0).equals(songs.get(1)));
        return song1;
    }

}
