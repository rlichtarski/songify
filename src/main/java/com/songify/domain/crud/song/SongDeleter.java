package com.songify.domain.crud.song;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@Transactional
@AllArgsConstructor(access = lombok.AccessLevel.PACKAGE)
class SongDeleter {

    private final SongRepository songRepository;
    private final SongRetriever songRetriever;

    void deleteById(Long id) {
        songRetriever.existsById(id);
        log.info("deleting a song with id " + id);
        songRepository.deleteById(id);
    }

}

