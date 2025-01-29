package com.songify.domain.crud;

import com.songify.domain.crud.util.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter(AccessLevel.PACKAGE)
@Setter(AccessLevel.PACKAGE)
class Album extends BaseEntity {

    @Id
    @GeneratedValue(generator = "album_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(
            name = "album_id_seq",
            sequenceName = "album_id_seq",
            allocationSize = 1
    )
    private Long id;

    private String title;

    private Instant releaseDate;

    @OneToMany
    @JoinColumn(name = "album_id")
    private Set<Song> songs = new HashSet<>();

    @ManyToMany(mappedBy = "albums")
    private Set<Artist> artists = new HashSet<>();

    void addSong(final Song songById) {
        songs.add(songById);
    }

    void removeArtist(Artist artist) {
        artists.remove(artist);
        artist.removeAlbum(this);
    }

    void addArtist(final Artist artist) {
        //tutaj tak samo, to jest ten dirty checking?
        artists.add(artist);
    }
}
