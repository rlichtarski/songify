package com.songify.domain.crud;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Log4j2
class ArtistDeleter {

    private final ArtistRepository artistRepository;
    private final ArtistRetriever artistRetriever;
    private final AlbumRetriever albumRetriever;
    private final AlbumDeleter albumDeleter;
    private final SongDeleter songDeleter;

    void deleteArtistByIdWithAlbumsAndSongs(final Long artistId) {
        final Artist artist = artistRetriever.findById(artistId);
        final Set<Album> artistAlbums = albumRetriever.findAlbumsByArtistId(artist.getId());
        if (artistAlbums.isEmpty()) {
            log.info("Artist with id " + artistId + " does not have albums");
            artistRepository.deleteById(artistId);
            return;
        }

        artistAlbums.stream()
                .filter(album -> album.getArtists().size() >= 2)
                .forEach(album -> album.removeArtist(artist));

        final Set<Album> albumsWithOnlyOneArtist = artistAlbums.stream()
                .filter(album -> album.getArtists().size() == 1)
                .collect(Collectors.toSet());

        final Set<Long> allSongsIdsFromAllAlbumsWhereOnlyOneArtist = albumsWithOnlyOneArtist.stream()
                .flatMap(album -> album.getSongs().stream())
                .map(Song::getId)
                .collect(Collectors.toSet());
        songDeleter.deleteAllSongsById(allSongsIdsFromAllAlbumsWhereOnlyOneArtist);

        final Set<Long> albumsIds = albumsWithOnlyOneArtist.stream()
                .map(Album::getId)
                .collect(Collectors.toSet());
        albumDeleter.deleteAllAlbumsByIds(albumsIds);

        artistRepository.deleteById(artistId);
    }

}
