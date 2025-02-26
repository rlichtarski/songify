package com.songify.domain.crud;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryGenreRepository implements GenreRepository {
    Map<Long, Genre> db = new HashMap<>();
    AtomicInteger index = new AtomicInteger(1);

    InMemoryGenreRepository() {
        save(new Genre(1L, "default"));
    }

    @Override
    public int deleteById(final Long id) {
        db.remove(id);
        return id.intValue();
    }

    @Override
    public Genre save(final Genre genre) {
        long index = this.index.getAndIncrement();
        genre.setId(index);
        db.put(index, genre);
        return genre;
    }

    @Override
    public Optional<Genre> findGenreById(final Long id) {
        return Optional.ofNullable(db.get(id));
    }

    @Override
    public Set<Genre> findAll() {
        return new HashSet<>(db.values());
    }
}
