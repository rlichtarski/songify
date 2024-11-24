package com.songify;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class SongsController {

    Map<Integer, String> database = new HashMap<>();

    @GetMapping("/songs")
    public ResponseEntity<SongResponseDto> getAllSongs() {
        database.put(1, "Billie Eilish");
        database.put(2, "Taco Hemingway");
        SongResponseDto songResponseDto = new SongResponseDto(database);
        return ResponseEntity.ok(songResponseDto);
    }

    @GetMapping("/songs/{id}")
    public ResponseEntity<SingleSongResponseDto> getSongById(@PathVariable Integer id) {
        String song = database.get(id);
        if (song == null) {
            return ResponseEntity.notFound().build();
        }
        SingleSongResponseDto songResponseDto = new SingleSongResponseDto(song);
        return ResponseEntity.ok(songResponseDto);
    }



}
