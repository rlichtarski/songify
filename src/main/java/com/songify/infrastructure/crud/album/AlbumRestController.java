package com.songify.infrastructure.crud.album;

import com.songify.domain.crud.SongifyCrudFacade;
import com.songify.domain.crud.dto.AlbumDto;
import com.songify.domain.crud.dto.AlbumInfo;
import com.songify.domain.crud.dto.AlbumRequestDto;
import com.songify.infrastructure.crud.album.dto.response.GetAllAlbumsResponseDto;
import com.songify.infrastructure.crud.song.controller.dto.response.PartiallyUpdateSongResponseDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

import static com.songify.infrastructure.crud.album.AlbumMapper.mapFromAllAlbumsToGetAllAlbumsResponseDto;

@RestController
@Log4j2
@RequestMapping("/albums")
@AllArgsConstructor
class AlbumRestController {

    private final SongifyCrudFacade songifyCrudFacade;

    @PostMapping
    ResponseEntity<AlbumDto> postAlbum(@RequestBody AlbumRequestDto albumRequestDto) {
        final AlbumDto albumDto = songifyCrudFacade.addAlbumWithSong(albumRequestDto);
        return ResponseEntity.ok(albumDto);
    }

    @GetMapping("/{albumId}")
    ResponseEntity<AlbumInfo> findAlbumByIdWithArtistsAndSongs(@PathVariable Long albumId) {
        final AlbumInfo albumDto = songifyCrudFacade.findAlbumByIdWithArtistsAndSongs(albumId);
        return ResponseEntity.ok(albumDto);
    }

    @PutMapping("/{albumId}/songs/{songId}")
    ResponseEntity<AlbumDto> assignSongToAlbum(@PathVariable Long albumId, @PathVariable Long songId) {
        AlbumDto albumDto = songifyCrudFacade.addSongToAlbum(albumId, songId);
        return ResponseEntity.ok(albumDto);
    }

    @GetMapping()
    ResponseEntity<GetAllAlbumsResponseDto> getAllAlbums() {
        final Set<AlbumDto> allAlbums = songifyCrudFacade.findAllAlbums();
        final GetAllAlbumsResponseDto getAllAlbumsResponseDto = mapFromAllAlbumsToGetAllAlbumsResponseDto(allAlbums);
        return ResponseEntity.ok(getAllAlbumsResponseDto);
    }

}
