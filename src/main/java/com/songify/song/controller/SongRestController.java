package com.songify.song.controller;

import com.songify.song.dto.request.SongRequestDto;
import com.songify.song.dto.request.UpdateSongRequestDto;
import com.songify.song.dto.response.DeleteSongResponseDto;
import com.songify.song.dto.response.SingleSongResponseDto;
import com.songify.song.dto.response.SongResponseDto;
import com.songify.song.dto.response.UpdateSongResponseDto;
import com.songify.song.error.SongNotFoundException;
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

    Map<Integer, Song> database = new HashMap<>(Map.of(
            1, new Song("Billie Eilish", "Bad Guy"),
            2, new Song("Taco Hemingway", "Leszcz na betonie"),
            3, new Song("Bon Jovi", "Runaway"),
            4, new Song("AC/DC", "Highway to hell")
    ));

    @GetMapping("/songs")
    public ResponseEntity<SongResponseDto> getAllSongs(@RequestParam(required = false) Integer limit) {
        if (limit != null) {
            Map<Integer, Song> limitedSongs = database.entrySet()
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
        log.info("requestId: " + requestId);
        if (!database.containsKey(id)) {
            throw new SongNotFoundException("Song with id " + id + " not found");
        }
        Song song = database.get(id);
        SingleSongResponseDto songResponseDto = new SingleSongResponseDto(song);
        return ResponseEntity.ok(songResponseDto);
    }

    @PostMapping("/songs")
    public ResponseEntity<SingleSongResponseDto> postSong(@RequestBody @Valid SongRequestDto request) {
        Song song = new Song(request.artistName(), request.songName());
        log.info("New song: " + song);
        database.put(database.size() + 1, song);
        return ResponseEntity.ok(new SingleSongResponseDto(song));
    }

    @DeleteMapping("/song/{id}")
    public ResponseEntity<DeleteSongResponseDto> deleteSongByIdUsingPathVariable(@PathVariable Integer id) {
        if (!database.containsKey(id)) {
            throw new SongNotFoundException("Song with id " + id + " not found");
        }
        log.info("Deleting song: " + database.get(id));
        database.remove(id);
        DeleteSongResponseDto body = new DeleteSongResponseDto(
                "Deleted song with id " + id,
                HttpStatus.OK
        );
        return ResponseEntity.ok(body);
    }

    @PutMapping("/songs/{id}")
    public ResponseEntity<UpdateSongResponseDto> updateSong(
            @PathVariable Integer id,
            @RequestBody @Valid UpdateSongRequestDto request
    ) {
        if (!database.containsKey(id)) {
            throw new SongNotFoundException("Song with id " + id + " not found");
        }
        String newSongNameToUpdate = request.songName();
        String newArtistNameToUpdate = request.artistName();
        Song song = new Song(newArtistNameToUpdate, newSongNameToUpdate);
        Song oldSong = database.put(id, song);
        log.info("Updated song: " + oldSong.song() + " to " + newSongNameToUpdate + ", old artist: " + oldSong.artistName() + " to " + newArtistNameToUpdate);
        return ResponseEntity.ok(new UpdateSongResponseDto(newSongNameToUpdate, newArtistNameToUpdate));
    }




}
