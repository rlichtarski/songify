package com.songify.song.controller;

import com.songify.song.dto.request.PartiallyUpdateSongRequestDto;
import com.songify.song.dto.request.CreateSongRequestDto;
import com.songify.song.dto.request.UpdateSongRequestDto;
import com.songify.song.dto.response.*;
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
@RequestMapping("/songs")
public class SongRestController {

    Map<Integer, Song> database = new HashMap<>(Map.of(
            1, new Song("Billie Eilish", "Bad Guy"),
            2, new Song("Taco Hemingway", "Leszcz na betonie"),
            3, new Song("Bon Jovi", "Runaway"),
            4, new Song("AC/DC", "Highway to hell")
    ));

    @GetMapping
    public ResponseEntity<GetAllSongsResponseDto> getAllSongs(@RequestParam(required = false) Integer limit) {
        if (limit != null) {
            Map<Integer, Song> limitedSongs = database.entrySet()
                    .stream()
                    .limit(limit)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            GetAllSongsResponseDto songResponseDto = new GetAllSongsResponseDto(limitedSongs);
            return ResponseEntity.ok(songResponseDto);
        }
        GetAllSongsResponseDto songResponseDto = new GetAllSongsResponseDto(database);
        return ResponseEntity.ok(songResponseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetSongResponseDto> getSongById(
            @PathVariable Integer id,
            @RequestHeader(required = false) String requestId
    ) {
        log.info("requestId: " + requestId);
        if (!database.containsKey(id)) {
            throw new SongNotFoundException("Song with id " + id + " not found");
        }
        Song song = database.get(id);
        GetSongResponseDto getSongResponseDto = new GetSongResponseDto(song);
        return ResponseEntity.ok(getSongResponseDto);
    }

    @PostMapping
    public ResponseEntity<CreateSongResponseDto> postSong(@RequestBody @Valid CreateSongRequestDto request) {
        Song song = new Song(request.artistName(), request.songName());
        log.info("New songName: " + song);
        database.put(database.size() + 1, song);
        return ResponseEntity.ok(new CreateSongResponseDto(song));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteSongResponseDto> deleteSongByIdUsingPathVariable(@PathVariable Integer id) {
        if (!database.containsKey(id)) {
            throw new SongNotFoundException("Song with id " + id + " not found");
        }
        log.info("Deleting songName: " + database.get(id));
        database.remove(id);
        DeleteSongResponseDto body = new DeleteSongResponseDto(
                "Deleted songName with id " + id,
                HttpStatus.OK
        );
        return ResponseEntity.ok(body);
    }

    @PutMapping("/{id}")
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
        log.info("Updated songName: " + oldSong.songName() + " to " + newSongNameToUpdate + ", old artist: " + oldSong.artistName() + " to " + newArtistNameToUpdate);
        return ResponseEntity.ok(new UpdateSongResponseDto(newSongNameToUpdate, newArtistNameToUpdate));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PartiallyUpdateSongResponseDto> partiallyUpdateSong(
            @PathVariable Integer id,
            @RequestBody PartiallyUpdateSongRequestDto request
    ) {
        if (!database.containsKey(id)) {
            throw new SongNotFoundException("Song with id " + id + " not found.");
        }
        Song songName = database.get(id);
        Song.SongBuilder songBuilder = Song.builder();

        if (request.songName() != null) {
            songBuilder.songName(request.songName());
            log.info("Partially updated songName name.");
        } else {
            songBuilder.songName(songName.songName());
        }

        if (request.artistName() != null) {
            songBuilder.artistName(request.artistName());
            log.info("Partially updated artist name.");
        } else {
            songBuilder.artistName(songName.artistName());
        }

        Song updatedSong = songBuilder.build();
        database.put(id, updatedSong);
        log.info("Response: {}", new PartiallyUpdateSongResponseDto(updatedSong));
        PartiallyUpdateSongResponseDto body = new PartiallyUpdateSongResponseDto(updatedSong);
        return ResponseEntity.ok(body);
    }



}
