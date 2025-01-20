package com.songify.infrastructure.crud.song.controller;

import com.songify.domain.crud.SongifyCrudFacade;
import com.songify.domain.crud.dto.SongDto;
import com.songify.domain.crud.dto.SongRequestDto;
import com.songify.infrastructure.crud.song.controller.dto.request.CreateSongRequestDto;
import com.songify.infrastructure.crud.song.controller.dto.request.PartiallyUpdateSongRequestDto;
import com.songify.infrastructure.crud.song.controller.dto.request.UpdateSongRequestDto;
import com.songify.infrastructure.crud.song.controller.dto.response.CreateSongResponseDto;
import com.songify.infrastructure.crud.song.controller.dto.response.DeleteSongResponseDto;
import com.songify.infrastructure.crud.song.controller.dto.response.GetAllSongsResponseDto;
import com.songify.infrastructure.crud.song.controller.dto.response.GetSongResponseDto;
import com.songify.infrastructure.crud.song.controller.dto.response.PartiallyUpdateSongResponseDto;
import com.songify.infrastructure.crud.song.controller.dto.response.UpdateSongResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.songify.infrastructure.crud.song.controller.SongMapper.mapFromCreateSongRequestDtoToSongDto;
import static com.songify.infrastructure.crud.song.controller.SongMapper.mapFromPartiallyUpdateSongRequestDtoToSong;
import static com.songify.infrastructure.crud.song.controller.SongMapper.mapFromSongDtoToPartiallyUpdateSongResponseDto;
import static com.songify.infrastructure.crud.song.controller.SongMapper.mapFromSongToCreateSongResponseDto;
import static com.songify.infrastructure.crud.song.controller.SongMapper.mapFromSongToDeleteSongResponseDto;
import static com.songify.infrastructure.crud.song.controller.SongMapper.mapFromSongToGetAllSongsResponseDto;
import static com.songify.infrastructure.crud.song.controller.SongMapper.mapFromSongToGetSongResponseDto;
import static com.songify.infrastructure.crud.song.controller.SongMapper.mapFromSongToUpdateSongResponseDto;
import static com.songify.infrastructure.crud.song.controller.SongMapper.mapFromUpdateSongRequestDtoToSongDto;

@RestController
@Log4j2
@RequestMapping("/songs")
@AllArgsConstructor(access = lombok.AccessLevel.PACKAGE)
public class SongRestController {

    private final SongifyCrudFacade songFacade;

    //request param crashes the whole pagination
    @GetMapping
    public ResponseEntity<GetAllSongsResponseDto> getAllSongs(
            @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        List<SongDto> allSongs = songFacade.findAll(pageable);
        GetAllSongsResponseDto songResponseDto = mapFromSongToGetAllSongsResponseDto(allSongs);
        return ResponseEntity.ok(songResponseDto);
    }

    @GetMapping("/{id}")
    ResponseEntity<GetSongResponseDto> getSongById(@PathVariable Long id, @RequestHeader(required = false) String requestId) {
        log.info(requestId);
        SongDto song = songFacade.findSongDtoById(id);
        GetSongResponseDto response = mapFromSongToGetSongResponseDto(song);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    ResponseEntity<CreateSongResponseDto> postSong(@RequestBody @Valid SongRequestDto request) {
        SongDto savedSong = songFacade.addSong(request);
        CreateSongResponseDto body = mapFromSongToCreateSongResponseDto(savedSong);
        return ResponseEntity.ok(body);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<DeleteSongResponseDto> deleteSongByIdUsingPathVariable(@PathVariable Long id) {
        songFacade.deleteById(id);
        DeleteSongResponseDto body = mapFromSongToDeleteSongResponseDto(id);
        return ResponseEntity.ok(body);
    }

    @PutMapping("/{id}")
    ResponseEntity<UpdateSongResponseDto> update(@PathVariable Long id,
                                                 @RequestBody @Valid UpdateSongRequestDto request) {
        SongDto newSongDto = mapFromUpdateSongRequestDtoToSongDto(request);
        songFacade.updateById(id, newSongDto);
        UpdateSongResponseDto body = mapFromSongToUpdateSongResponseDto(newSongDto);
        return ResponseEntity.ok(body);
    }

    @Operation(summary = "PATCH doesn't work so this is an alternative for PATCH")
    @PutMapping("patch/{id}")
    public ResponseEntity<PartiallyUpdateSongResponseDto> partiallyUpdateSong(
            @PathVariable Long id,
            @RequestBody PartiallyUpdateSongRequestDto request
    ) {
        SongDto updatedSong = mapFromPartiallyUpdateSongRequestDtoToSong(request);
        SongDto savedSong = songFacade.updatePartiallyById(id, updatedSong);
        PartiallyUpdateSongResponseDto body = mapFromSongDtoToPartiallyUpdateSongResponseDto(savedSong);
        return ResponseEntity.ok(body);
    }


}

