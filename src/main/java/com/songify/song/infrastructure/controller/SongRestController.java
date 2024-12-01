package com.songify.song.infrastructure.controller;

import com.songify.song.domain.service.SongAdder;
import com.songify.song.domain.service.SongRetriever;
import com.songify.song.infrastructure.controller.dto.request.PartiallyUpdateSongRequestDto;
import com.songify.song.infrastructure.controller.dto.request.CreateSongRequestDto;
import com.songify.song.infrastructure.controller.dto.request.UpdateSongRequestDto;
import com.songify.song.infrastructure.controller.dto.response.*;
import com.songify.song.domain.model.SongNotFoundException;
import com.songify.song.domain.model.Song;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.songify.song.infrastructure.controller.SongMapper.*;

@RestController
@Log4j2
@RequestMapping("/songs")
public class SongRestController {

    private final SongAdder songAdder;
    private final SongRetriever songRetriever;

    public SongRestController(SongAdder songAdder, SongRetriever songRetriever) {
        this.songAdder = songAdder;
        this.songRetriever = songRetriever;
    }

    @GetMapping
    public ResponseEntity<GetAllSongsResponseDto> getAllSongs(@RequestParam(required = false) Integer limit) {
        Map<Integer, Song> allSongs = songRetriever.findAll();
        if (limit != null) {
            Map<Integer, Song> limitedSongs = songRetriever.findAllLimitedBy(limit);
            GetAllSongsResponseDto songResponseDto = mapFromSongToGetAllSongsResponseDto(limitedSongs);
            return ResponseEntity.ok(songResponseDto);
        }
        GetAllSongsResponseDto songResponseDto = mapFromSongToGetAllSongsResponseDto(allSongs);
        return ResponseEntity.ok(songResponseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetSongResponseDto> getSongById(
            @PathVariable Integer id,
            @RequestHeader(required = false) String requestId
    ) {
        log.info("requestId: " + requestId);
        Map<Integer, Song> allSongs = songRetriever.findAll();
        if (!allSongs.containsKey(id)) {
            throw new SongNotFoundException("Song with id " + id + " not found");
        }
        Song song = allSongs.get(id);
        GetSongResponseDto getSongResponseDto = mapFromSongToGetSongResponseDto(song);
        return ResponseEntity.ok(getSongResponseDto);
    }

    @PostMapping
    public ResponseEntity<CreateSongResponseDto> postSong(@RequestBody @Valid CreateSongRequestDto request) {
        Song song = mapFromCreateSongRequestDtoToSong(request);
        songAdder.addSong(song);
        CreateSongResponseDto body = mapSongToCreateSongDto(song);
        return ResponseEntity.ok(body);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteSongResponseDto> deleteSongByIdUsingPathVariable(@PathVariable Integer id) {
        Map<Integer, Song> allSongs = songRetriever.findAll();
        if (!allSongs.containsKey(id)) {
            throw new SongNotFoundException("Song with id " + id + " not found");
        }
        log.info("Deleting songName: " + allSongs.get(id));
        allSongs.remove(id);
        DeleteSongResponseDto body = mapFromSongToDeleteSongResponseDto(id);
        return ResponseEntity.ok(body);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateSongResponseDto> updateSong(
            @PathVariable Integer id,
            @RequestBody @Valid UpdateSongRequestDto request
    ) {
        Map<Integer, Song> allSongs = songRetriever.findAll();
        if (!allSongs.containsKey(id)) {
            throw new SongNotFoundException("Song with id " + id + " not found");
        }
        Song newSongNameToUpdate = mapFromUpdateSongRequestDtoToSong(request);
        Song oldSong = allSongs.put(id, newSongNameToUpdate);
        log.info("Updated songName: " + oldSong.songName() + " to " + newSongNameToUpdate + ", old artist: " + oldSong.artistName() + " to " + newSongNameToUpdate.artistName());
        UpdateSongResponseDto body = mapFromSongToUpdateSongResponseDto(newSongNameToUpdate);
        return ResponseEntity.ok(body);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PartiallyUpdateSongResponseDto> partiallyUpdateSong(
            @PathVariable Integer id,
            @RequestBody PartiallyUpdateSongRequestDto request
    ) {
        Map<Integer, Song> allSongs = songRetriever.findAll();
        if (!allSongs.containsKey(id)) {
            throw new SongNotFoundException("Song with id " + id + " not found.");
        }
        Song songFromDb = allSongs.get(id);
        Song updatedSong = mapFromPartiallyUpdateSongRequestDtoToSong(request);
        Song.SongBuilder songBuilder = Song.builder();

        if (updatedSong.songName() != null) {
            songBuilder.songName(updatedSong.songName());
            log.info("Partially updated songName name.");
        } else {
            songBuilder.songName(songFromDb.songName());
        }

        if (updatedSong.artistName() != null) {
            songBuilder.artistName(updatedSong.artistName());
            log.info("Partially updated artist name.");
        } else {
            songBuilder.artistName(songFromDb.artistName());
        }

        allSongs.put(id, updatedSong);
        PartiallyUpdateSongResponseDto body = mapFromSongToPartiallyUpdateSongResponseDto(updatedSong);
        return ResponseEntity.ok(body);
    }



}

