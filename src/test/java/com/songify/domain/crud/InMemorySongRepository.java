package com.songify.domain.crud;

import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemorySongRepository implements SongRepository {

    Map<Long, Song> db = new HashMap<>();
    AtomicInteger index = new AtomicInteger(0);

    @Override
    public List<Song> findAll(final Pageable pageable) {
        return db.values().stream().toList();
    }

    @Override
    public Optional<Song> findById(final Long id) {
        return Optional.ofNullable(db.get(id));
    }

    @Override
    public void deleteById(final Long id) {
        db.remove(id);
    }

    @Override
    public void updateById(final Long id, final Song newSong) {
        db.put(id, newSong);
    }

    @Override
    public Song save(final Song song) {
        long index = this.index.getAndIncrement();
        song.setId(index);
        song.setGenre(new Genre(1L, "default"));
        db.put(index, song);
        return song;
    }

    @Override
    public boolean existsById(final Long id) {
        return db.get(id) != null;
    }

    @Override
    public int deleteByIdIn(final Collection<Long> ids) {
        ids.forEach(id -> db.remove(id));
        return 0;
    }
}
