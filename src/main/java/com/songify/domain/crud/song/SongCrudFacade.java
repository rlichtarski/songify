package com.songify.domain.crud.song;

import com.songify.domain.crud.song.dto.SongDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(access = lombok.AccessLevel.PACKAGE)
public class SongCrudFacade {

    private final SongAdder songAdder;
    private final SongRetriever songRetriever;
    private final SongDeleter songDeleter;
    private final SongUpdater songUpdater;

    public List<SongDto> findAll(final Pageable pageable) {
        return songRetriever.findAll(pageable)
                .stream()
                .map(song -> SongDto.builder()
                        .id(song.getId())
                        .name(song.getName())
                        .build())
                .collect(Collectors.toList());
    }

    public SongDto findSongDtoById(final Long id) {
        final Song songById = songRetriever.findSongById(id);
        return SongDto.builder()
                .id(songById.getId())
                .name(songById.getName())
                .build();
    }

    public SongDto addSong(final SongDto songDto) {
        Song song = Song.builder()
                .id(songDto.id())
                .name(songDto.name())
                .build();
        final Song addedSong = songAdder.addSong(song);
        return SongDto.builder()
                .id(addedSong.getId())
                .name(addedSong.getName())
                .build();
    }

    public void deleteById(final Long id) {
        songRetriever.existsById(id);
        songDeleter.deleteById(id);
    }

    public void updateById(final Long id, final SongDto newSongDto) {
        songRetriever.existsById(id);
        songUpdater.updateById(
                id,
                Song.builder()
                        .id(newSongDto.id())
                        .name(newSongDto.name())
                        .build()
        );

    }

    public SongDto updatePartiallyById(final Long id, final SongDto songToUpdate) {
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
