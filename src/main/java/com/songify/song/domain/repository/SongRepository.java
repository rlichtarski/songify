package com.songify.song.domain.repository;

import com.songify.song.domain.model.Song;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class SongRepository {

    Map<Integer, Song> database = new HashMap<>(Map.of(
            1, new Song("Billie Eilish", "Bad Guy"),
            2, new Song("Taco Hemingway", "Leszcz na betonie"),
            3, new Song("Bon Jovi", "Runaway"),
            4, new Song("AC/DC", "Highway to hell")
    ));

    public Song saveSongToDatabase(Song song) {
        database.put(database.size() + 1, song);
        return song;
    }

    public Map<Integer, Song> findAll() {
        return database;
    }

}
