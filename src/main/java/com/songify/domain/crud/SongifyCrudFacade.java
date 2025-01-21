package com.songify.domain.crud;

import com.songify.domain.crud.dto.AlbumDto;
import com.songify.domain.crud.dto.AlbumRequestDto;
import com.songify.domain.crud.dto.ArtistDto;
import com.songify.domain.crud.dto.ArtistRequestDto;
import com.songify.domain.crud.dto.GenreDto;
import com.songify.domain.crud.dto.GenreRequestDto;
import com.songify.domain.crud.dto.SongDto;
import com.songify.domain.crud.dto.SongRequestDto;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(access = lombok.AccessLevel.PACKAGE)
@Transactional
public class SongifyCrudFacade {

    private final SongAdder songAdder;
    private final SongRetriever songRetriever;
    private final SongDeleter songDeleter;
    private final SongUpdater songUpdater;
    private final ArtistAdder artistAdder;
    private final GenreAdder genreAdder;
    private final AlbumAdder albumAdder;
    private final ArtistRetriever artistRetriever;

    public ArtistDto addArtist(ArtistRequestDto dto) {
        return artistAdder.addArtist(dto.name());
    }

    public GenreDto addGenre(GenreRequestDto dto) {
        return genreAdder.addGenre(dto.name());
    }

    public AlbumDto addAlbumWithSong(AlbumRequestDto dto) {
        return albumAdder.addAlbum(dto.songId(), dto.title(), dto.releaseDate());
    }

    public SongDto addSong(final SongRequestDto songDto) {
        return songAdder.addSong(songDto);
    }

    public Set<ArtistDto> findAllArtists() {
        return artistRetriever.findAllArtist();
    }

    public List<SongDto> findAllSongs(final Pageable pageable) {
        return songRetriever.findAll(pageable);
    }

    public SongDto findSongDtoById(final Long id) {
        return songRetriever.findSongDtoById(id);
    }

    public void deleteSongById(final Long id) {
        songRetriever.existsById(id);
        songDeleter.deleteById(id);
    }

    public void updateSongById(final Long id, final SongDto newSongDto) {
        songRetriever.existsById(id);
        songUpdater.updateById(
                id,
                Song.builder()
                        .id(newSongDto.id())
                        .name(newSongDto.name())
                        .build()
        );

    }

    public SongDto updateSongPartiallyById(final Long id, final SongDto songToUpdate) {
        songRetriever.existsById(id);
        Song songFromDb = songRetriever.findSongById(id);
        Song toSave = new Song();
        if (songToUpdate.name() != null) {
            toSave.setName(songToUpdate.name());
        } else {
            toSave.setName(songFromDb.getName());
        }
        songUpdater.updateById(id, toSave);
        return SongDto.builder()
                .id(toSave.getId())
                .name(toSave.getName())
                .build();
    }
}
