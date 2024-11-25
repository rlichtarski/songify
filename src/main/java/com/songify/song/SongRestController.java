package com.songify.song;

import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Log4j2
public class SongRestController {

    Map<Integer, String> database = new HashMap<>(Map.of(
            1, "Billie Eilish",
            2, "Taco Hemingway",
            3, "Metallica",
            4, "AC/DC"
    ));

    @GetMapping("/songs")
    public ResponseEntity<SongResponseDto> getAllSongs(@RequestParam(required = false) Integer limit) {
        if (limit != null) {
            Map<Integer, String> limitedSongs = database.entrySet()
                    .stream()
                    .limit(limit)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            SongResponseDto songResponseDto = new SongResponseDto(limitedSongs);
            return ResponseEntity.ok(songResponseDto);
        }
        SongResponseDto songResponseDto = new SongResponseDto(database);
        return ResponseEntity.ok(songResponseDto);
    }

    @GetMapping("/songs/{id}")
    public ResponseEntity<SingleSongResponseDto> getSongById(
            @PathVariable Integer id,
            @RequestHeader(required = false) String requestId
    ) {
        if (requestId != null) {
            log.info("RequestID was: " + requestId);
        }
        String song = database.get(id);
        if (song == null) {
            return ResponseEntity.notFound().build();
        }
        SingleSongResponseDto songResponseDto = new SingleSongResponseDto(song);
        return ResponseEntity.ok(songResponseDto);
    }

    @PostMapping("/songs")
    public ResponseEntity<SingleSongResponseDto> postSong(@RequestBody @Valid SongRequestDto request) {
        String songName = request.songName();
        log.info("New song: " + songName);
        database.put(database.size() + 1, songName);
        return ResponseEntity.ok(new SingleSongResponseDto(songName));
    }

    @DeleteMapping("/song/{id}")
    public ResponseEntity<DeleteSongResponseDto> deleteSongByIdUsingPathVariable(@PathVariable Integer id) {
        if (!database.containsKey(id)) {
            DeleteSongResponseDto body = new DeleteSongResponseDto(
                    "Song with id " + id + " not found",
                    HttpStatus.NOT_FOUND
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(body);
        }
        log.info("Deleting song: " + database.get(id));
        database.remove(id);
        DeleteSongResponseDto body = new DeleteSongResponseDto(
                "Deleted song with id " + id,
                HttpStatus.OK
        );
        return ResponseEntity.ok(body);
    }

    /*@DeleteMapping("/song")
    public ResponseEntity<String> deleteSongByIdUsingRequestParam(@RequestParam Integer id) {
        log.info("Deleting song: " + database.get(id));
        database.remove(id);
        return ResponseEntity.ok("deleted a song!");
    }*/




}
