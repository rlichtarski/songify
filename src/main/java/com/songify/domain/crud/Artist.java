package com.songify.domain.crud;

import com.songify.domain.crud.util.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter(AccessLevel.PACKAGE)
@Setter(AccessLevel.PACKAGE)
class Artist extends BaseEntity {

    @Id
    @GeneratedValue(generator = "artist_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(
            name = "artist_id_seq",
            sequenceName = "artist_id_seq",
            allocationSize = 1
    )
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToMany
    private Set<Album> albums = new HashSet<>();

    public Artist(final String name) {
        this.name = name;
    }

    Artist(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    void removeAlbum(Album album) {
        albums.remove(album);
    }

    void addAlbum(final Album album) {
        //czyli to co tutaj jest to jest dirty checking? Że zapisujemy sobie po prostu do Seta, nic więcej?
        albums.add(album);
        album.addArtist(this);
    }
}
