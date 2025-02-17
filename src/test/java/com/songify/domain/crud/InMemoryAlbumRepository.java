package com.songify.domain.crud;

import com.songify.domain.crud.dto.AlbumInfo;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InMemoryAlbumRepository implements AlbumRepository {

    Map<Long, Album> db = new HashMap<>();
    AtomicInteger index = new AtomicInteger(0);

    @Override
    public Album save(final Album album) {
        long index = this.index.getAndIncrement();
        album.setId(index);
        db.put(index, album);
        return album;
    }

    @Override
    public Optional<AlbumInfo> findAlbumByIdWithArtistsAndSongs(final Long id) {
        final Album album = db.get(id);
        final AlbumInfoTestImpl albumInfoTest = new AlbumInfoTestImpl(album);
        return Optional.of(albumInfoTest);
    }

    @Override
    public Set<Album> findAllAlbumsByArtistId(final Long id) {
        return db.values()
                .stream()
                .filter(album -> album.getArtists().stream()
                        .anyMatch(artist -> artist.getId().equals(id)))
                .collect(Collectors.toSet());
    }

    @Override
    public int deleteByIdIn(final Collection<Long> ids) {
        ids.forEach(id -> db.remove(id));
        return 0;
    }

    @Override
    public Optional<Album> findById(final Long id) {
        return Optional.ofNullable(db.get(id));
    }

    @Override
    public Set<Album> findAll() {
        return new HashSet<>(db.values());
    }
}
