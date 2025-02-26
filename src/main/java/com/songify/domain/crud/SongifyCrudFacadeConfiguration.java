package com.songify.domain.crud;

class SongifyCrudFacadeConfiguration {

    public static SongifyCrudFacade createSongifyCrud(
            final SongRepository songRepository,
            final GenreRepository genreRepository,
            final ArtistRepository artistRepository,
            final AlbumRepository albumRepository) {

        SongRetriever songRetriever = new SongRetriever(songRepository);
        SongUpdater songUpdater = new SongUpdater(songRepository, songRetriever);
        AlbumAdder albumAdder = new AlbumAdder(albumRepository, songRetriever);
        ArtistRetriever artistRetriever = new ArtistRetriever(artistRepository);
        AlbumRetriever albumRetriever = new AlbumRetriever(albumRepository);
        SongAssigner songAssigner = new SongAssigner(songRetriever, albumRetriever);
        GenreDeleter genreDeleter = new GenreDeleter(genreRepository);
        GenreRetriever genreRetriever = new GenreRetriever(genreRepository);
        GenreAssigner genreAssigner = new GenreAssigner(songRetriever, genreRetriever);
        SongDeleter songDeleter = new SongDeleter(songRepository, songRetriever, genreDeleter);
        SongAdder songAdder = new SongAdder(songRepository, genreAssigner);
        ArtistAdder artistAdder = new ArtistAdder(artistRepository, albumAdder, songAdder);
        GenreAdder genreAdder = new GenreAdder(genreRepository);
        AlbumDeleter albumDeleter = new AlbumDeleter(albumRepository);
        ArtistDeleter artistDeleter = new ArtistDeleter(
                artistRepository, artistRetriever, albumRetriever, albumDeleter, songDeleter);
        ArtistAssigner artistAssigner = new ArtistAssigner(artistRetriever, albumRetriever);
        ArtistUpdater artistUpdater = new ArtistUpdater(artistRetriever);

        return new SongifyCrudFacade(
                songAdder,
                songRetriever,
                songDeleter,
                songUpdater,
                songAssigner,
                artistAdder,
                genreAdder,
                genreAssigner,
                albumAdder,
                artistRetriever,
                albumRetriever,
                artistDeleter,
                artistAssigner,
                artistUpdater,
                genreRetriever
        );
    }

}
