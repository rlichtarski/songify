package com.songify.domain.crud;

import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InMemoryArtistRepository implements ArtistRepository {
    
    Map<Long, Artist> db = new HashMap<>();
    AtomicInteger index = new AtomicInteger(0);
    
    @Override
    public Artist save(final Artist artist) {
        long index = this.index.getAndIncrement();
        artist.setId(index);
        db.put(index, artist);
        return artist;
    }

    @Override
    public Set<Artist> findAll(final Pageable pageable) {
        return new HashSet<>(db.values());
    }

    @Override
    public Optional<Artist> findById(final Long artistId) {
        return Optional.ofNullable(db.get(artistId));
    }

    @Override
    public int deleteById(final Long id) {
        db.remove(id);
        return id.intValue();
    }
}
