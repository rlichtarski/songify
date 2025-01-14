package com.songify.song.domain.service;

import com.songify.song.domain.model.Song;
import com.songify.song.domain.repository.SongRepository;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@Transactional
public class SongUpdater {

    private final SongRepository songRepository;
    private final SongRetriever songRetriever;

    public SongUpdater(SongRepository songRepository, SongRetriever songRetriever) {
        this.songRepository = songRepository;
        this.songRetriever = songRetriever;
    }

    public void updateById(Long id, Song newSong) {
        songRetriever.existsById(id);
        songRepository.updateById(id, newSong);
    }

    public Song updatePartiallyById(Long id, Song songFromRequest) {
        Song songFromDatabase = songRetriever.findSongById(id);
        Song.SongBuilder songBuilder = Song.builder();

        if (songFromRequest.getName() != null) {
            songBuilder.name(songFromRequest.getName());
        } else {
            songBuilder.name(songFromDatabase.getName());
        }

        if (songFromRequest.getArtist() != null) {
            songBuilder.artist(songFromRequest.getArtist());
        } else {
            songBuilder.artist(songFromDatabase.getArtist());
        }

        Song toSave = songBuilder.build();
        updateById(id, toSave);
        return toSave;
    }

    //Dirty checking version
    /*public void updateById(Long id, Song newSong) {
        Song songById = songRetriever.findSongById(id);
        songById.setName(newSong.getName());
        songById.setArtist(newSong.getArtist());
    }

    public Song updatePartiallyById(Long id, Song songFromRequest) {
        Song songFromDatabase = songRetriever.findSongById(id);

        if (songFromRequest.getName() != null) {
            songFromDatabase.setName(songFromRequest.getName());
        }
        if (songFromRequest.getArtist() != null) {
            songFromDatabase.setArtist(songFromRequest.getArtist());
        }

        return songFromDatabase;
    }*/

}
