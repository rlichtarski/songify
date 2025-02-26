package com.songify.domain.crud;

import com.songify.domain.crud.dto.AlbumDto;
import com.songify.domain.crud.dto.AlbumInfo;
import com.songify.domain.crud.dto.AlbumRequestDto;
import com.songify.domain.crud.dto.ArtistDto;
import com.songify.domain.crud.dto.ArtistRequestDto;
import com.songify.domain.crud.dto.GenreDto;
import com.songify.domain.crud.dto.SongDto;
import com.songify.domain.crud.dto.SongLanguageDto;
import com.songify.domain.crud.dto.SongRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

import static com.songify.domain.crud.SongifyCrudFacadeConfiguration.createSongifyCrud;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SongifyCrudFacadeTest {

    SongifyCrudFacade songifyCrudFacade = createSongifyCrud(
            new InMemorySongRepository(),
            new InMemoryGenreRepository(),
            new InMemoryArtistRepository(),
            new InMemoryAlbumRepository()
    );

    @Mock
    SongifyCrudFacade mockedSongifyCrudFacade;

    @Test
    @DisplayName("Should add 'Artist1' with id:0 When name was sent")
    public void should_add_artist_one_with_id_zero_when_name_was_sent() {
        // given
        final ArtistRequestDto artist = ArtistRequestDto.builder()
                .name("Artist1")
                .build();
        final Set<ArtistDto> allArtists = songifyCrudFacade.findAllArtists(Pageable.unpaged());
        assertTrue(allArtists.isEmpty());

        // when
        final ArtistDto response = songifyCrudFacade.addArtist(artist);

        // then
        assertThat(response.id()).isEqualTo(0L);
        assertThat(response.name()).isEqualTo("Artist1");
        final int size = songifyCrudFacade.findAllArtists(Pageable.unpaged()).size();
        assertThat(size).isEqualTo(1);
    }

    @Test
    @DisplayName("Should retrieve song by id with genre")
    public void should_retrieve_song_by_id() {
        // given
        when(mockedSongifyCrudFacade.findSongDtoById(1L))
                .thenReturn(
                        new SongDto(1L, "TestName", new GenreDto(1L, "default")));

        // when
        final SongDto songDtoById = mockedSongifyCrudFacade.findSongDtoById(1L);

        // then
        assertThat(songDtoById.id()).isEqualTo(1L);
        assertThat(songDtoById.name()).isEqualTo("TestName");
        verify(mockedSongifyCrudFacade, times(1)).findSongDtoById(1L);
    }

    @Test
    @DisplayName("Should throw exception 'ArtistNotFound' When deleting artist with id:0")
    public void should_throw_exception_artist_not_found_when_deleting_artist_with_id_zero() {
        // given
        assertThat(songifyCrudFacade.findAllArtists(Pageable.unpaged())).isEmpty();
        // when
        final Throwable throwable = catchThrowable(() -> songifyCrudFacade.deleteArtistByIdWithAlbumsAndSongs(0L));
        // then
        assertThat(throwable).isInstanceOf(ArtistNotFoundException.class);
        assertThat(throwable.getMessage()).isEqualTo("Artist with id 0 not found");
    }

    @Test
    @DisplayName("Should delete artist by id when they have no albums")
    public void should_delete_artist_by_id_when_they_have_no_albums() {
        // given
        final ArtistRequestDto artist = ArtistRequestDto.builder()
                .name("Artist1")
                .build();
        final Long artistId = songifyCrudFacade.addArtist(artist).id();
        assertThat(songifyCrudFacade.findAlbumsByArtistId(artistId)).isEmpty();
        // when
        songifyCrudFacade.deleteArtistByIdWithAlbumsAndSongs(artistId);
        // then
        assertThat(songifyCrudFacade.findAllArtists(Pageable.unpaged())).isEmpty();
    }

    @Test
    @DisplayName("Should delete artist with album and songs when artist has one album and he was the only artist in album")
    public void should_delete_artist_with_album_and_songs_when_artist_has_one_album_and_he_was_the_only_artist_in_album() {
        // given
        final ArtistRequestDto artist = ArtistRequestDto.builder()
                .name("Artist1")
                .build();
        final Long artistId = songifyCrudFacade.addArtist(artist).id();
        final SongRequestDto song1 = SongRequestDto.builder()
                .name("song1")
                .language(SongLanguageDto.ENGLISH)
                .build();
        final SongDto songDto = songifyCrudFacade.addSong(song1);
        final Long songId = songDto.id();
        final AlbumDto albumDto = songifyCrudFacade.addAlbumWithSong(
                AlbumRequestDto
                        .builder()
                        .songsId(Set.of(songId))
                        .title("album title 1")
                        .build()
        );
        final Long albumId = albumDto.id();
        songifyCrudFacade.addArtistToAlbum(artistId, albumId);
        assertThat(songifyCrudFacade.findAlbumsByArtistId(artistId).size()).isEqualTo(1);
        assertThat(songifyCrudFacade.countArtistsByAlbumId(albumId)).isEqualTo(1);
        // when
        songifyCrudFacade.deleteArtistByIdWithAlbumsAndSongs(artistId);
        // then
        assertThat(songifyCrudFacade.findAllArtists(Pageable.unpaged())).isEmpty();
        final Throwable throwable = catchThrowable(() -> songifyCrudFacade.findSongDtoById(songId));
        assertThat(throwable).isInstanceOf(SongNotFoundException.class);
        assertThat(throwable.getMessage()).isEqualTo("Song with id 0 not found");
        final Throwable albumThrowable = catchThrowable(() -> songifyCrudFacade.findAlbumById(albumId));
        assertThat(albumThrowable).isInstanceOf(AlbumNotFoundException.class);
        assertThat(albumThrowable.getMessage()).isEqualTo("Album with id: 0 not found");
    }

    @Test
    @DisplayName("Should add album with song")
    public void should_add_album_with_song() {
        // given
        final SongRequestDto songRequestDto = SongRequestDto.builder()
                .name("song1")
                .language(SongLanguageDto.ENGLISH)
                .build();
        final SongDto songDto = songifyCrudFacade.addSong(songRequestDto);
        final AlbumRequestDto album = AlbumRequestDto
                .builder()
                .songsId(Set.of(songDto.id()))
                .title("album title 1")
                .build();
        assertThat(songifyCrudFacade.findAllAlbums()).isEmpty();
        // when
        final AlbumDto albumDto = songifyCrudFacade.addAlbumWithSong(album);
        // then
        assertThat(songifyCrudFacade.findAllAlbums()).isNotEmpty();
        final AlbumInfo albumByIdWithSongs = songifyCrudFacade.findAlbumByIdWithArtistsAndSongs(albumDto.id());
        final Set<AlbumInfo.SongInfo> songs = albumByIdWithSongs.getSongs();
        assertTrue(songs.stream().anyMatch(song -> song.getId().equals(songDto.id())));
    }

    @Test
    public void should_add_song() {
        // given
        final SongRequestDto song1 = SongRequestDto.builder()
                .name("song1")
                .language(SongLanguageDto.ENGLISH)
                .build();
        // when
        assertThat(songifyCrudFacade.findAllSongs(Pageable.unpaged())).isEmpty();
        final SongDto songDto = songifyCrudFacade.addSong(song1);
        // then
        assertThat(songDto.id()).isNotNull();
        assertThat(songDto.name()).isEqualTo(song1.name());
        final List<SongDto> allSongs = songifyCrudFacade.findAllSongs(Pageable.unpaged());
        assertThat(allSongs)
                .extracting(SongDto::id)
                .containsExactly(0L);
    }

    @Test
    @DisplayName("Should add artist to album")
    public void should_add_artist_to_album() {
        // given
        final ArtistRequestDto artist = ArtistRequestDto.builder()
                .name("Artist1")
                .build();
        final Long artistId = songifyCrudFacade.addArtist(artist).id();
        final SongRequestDto song1 = SongRequestDto.builder()
                .name("song1")
                .language(SongLanguageDto.ENGLISH)
                .build();
        final SongDto songDto = songifyCrudFacade.addSong(song1);
        final Long songId = songDto.id();
        final AlbumDto albumDto = songifyCrudFacade.addAlbumWithSong(
                AlbumRequestDto
                        .builder()
                        .songsId(Set.of(songId))
                        .title("album title 1")
                        .build()
        );
        final Long albumId = albumDto.id();
        assertThat(songifyCrudFacade.findAlbumsByArtistId(artistId)).isEmpty();
        // when
        songifyCrudFacade.addArtistToAlbum(artistId, albumId);
        // then
        final Set<AlbumDto> albumsByArtistId = songifyCrudFacade.findAlbumsByArtistId(artistId);
        /*assertTrue(albumsByArtistId.stream()
                .anyMatch(album -> album.artists()
                        .stream().anyMatch(artistFromAlbum -> artistFromAlbum.id().equals(artistId)))
        );*/
        assertThat(albumsByArtistId)
                .extracting(AlbumDto::id)
                .containsExactly(albumId);
    }

    @Test
    @DisplayName("Should throw exception when album not found by id")
    public void should_throw_exception_when_album_not_found_by_id() {
        // given
        assertThat(songifyCrudFacade.findAllAlbums()).isEmpty();
        // when
        final Throwable albumThrowable = catchThrowable(() -> songifyCrudFacade.findAlbumById(0L));
        // then
        assertThat(albumThrowable).isInstanceOf(AlbumNotFoundException.class);
        assertThat(albumThrowable.getMessage()).isEqualTo("Album with id: 0 not found");
    }

    @Test
    @DisplayName("Should throw exception when song not found by id")
    public void should_throw_exception_when_song_not_found_by_id() {
        // given
        assertThat(songifyCrudFacade.findAllSongs(Pageable.unpaged())).isEmpty();
        // when
        final Throwable throwable = catchThrowable(() -> songifyCrudFacade.findSongDtoById(0L));
        // then
        assertThat(throwable).isInstanceOf(SongNotFoundException.class);
        assertThat(throwable.getMessage()).isEqualTo("Song with id 0 not found");
    }

    @Test
    @DisplayName("Should delete the only artist from album by id When there were more than one artists in album")
    public void should_delete_the_only_artist_from_album_by_id_when_there_were_more_than_one_artists_in_album() {
        // given
        final ArtistRequestDto artist1 = ArtistRequestDto.builder()
                .name("Artist1")
                .build();
        final Long artist1Id = songifyCrudFacade.addArtist(artist1).id();
        final ArtistRequestDto artist2 = ArtistRequestDto.builder()
                .name("Artist1")
                .build();
        final Long artist2Id = songifyCrudFacade.addArtist(artist2).id();
        final SongRequestDto song1 = SongRequestDto.builder()
                .name("song1")
                .language(SongLanguageDto.ENGLISH)
                .build();
        final SongDto songDto = songifyCrudFacade.addSong(song1);
        final Long songId = songDto.id();
        final AlbumDto albumDto = songifyCrudFacade.addAlbumWithSong(
                AlbumRequestDto
                        .builder()
                        .songsId(Set.of(songId))
                        .title("album title 1")
                        .build()
        );
        final Long albumId = albumDto.id();
        songifyCrudFacade.addArtistToAlbum(artist1Id, albumId);
        songifyCrudFacade.addArtistToAlbum(artist2Id, albumId);
        assertThat(songifyCrudFacade.findAlbumsByArtistId(artist1Id)).hasSize(1);
        assertThat(songifyCrudFacade.findAlbumsByArtistId(artist2Id)).hasSize(1);
        assertThat(songifyCrudFacade.countArtistsByAlbumId(albumId)).isEqualTo(2);
        // when
        songifyCrudFacade.deleteArtistByIdWithAlbumsAndSongs(artist1Id);
        // then
        final AlbumInfo albumByIdWithArtistsAndSongs = songifyCrudFacade.findAlbumByIdWithArtistsAndSongs(albumId);
        final Set<AlbumInfo.ArtistInfo> artists = albumByIdWithArtistsAndSongs.getArtists();
        assertThat(artists).hasSize(1);
        assertThat(artists)
                .extracting(AlbumInfo.ArtistInfo::getId)
                .containsExactly(artist2Id);
    }

    @Test
    @DisplayName("Should delete artist with albums and songs when artist has two albums and he was the only artist in album")
    public void should_delete_artist_with_albums_and_songs_when_artist_has_two_albums_and_he_was_the_only_artist_in_album() {
        // given
        final ArtistRequestDto artistRequestDto = ArtistRequestDto.builder()
                .name("Artist1")
                .build();
        final Long artistId = songifyCrudFacade.addArtist(artistRequestDto).id();
        final SongRequestDto song1 = SongRequestDto.builder()
                .name("song1")
                .language(SongLanguageDto.ENGLISH)
                .build();
        final SongRequestDto song2 = SongRequestDto.builder()
                .name("song2")
                .language(SongLanguageDto.ENGLISH)
                .build();
        final SongRequestDto song3 = SongRequestDto.builder()
                .name("song3")
                .language(SongLanguageDto.ENGLISH)
                .build();
        final SongDto songDto = songifyCrudFacade.addSong(song1);
        final SongDto songDto2 = songifyCrudFacade.addSong(song2);
        final SongDto songDto3 = songifyCrudFacade.addSong(song3);
        final Long songId = songDto.id();
        final Long songId2 = songDto2.id();
        final Long songId3 = songDto3.id();
        final AlbumDto albumDto = songifyCrudFacade.addAlbumWithSong(
                AlbumRequestDto
                        .builder()
                        .songsId(Set.of(songId, songId2))
                        .title("album title 1")
                        .build()
        );
        final AlbumDto albumDto2 = songifyCrudFacade.addAlbumWithSong(
                AlbumRequestDto
                        .builder()
                        .songsId(Set.of(songId3))
                        .title("album title 2")
                        .build()
        );
        final Long albumId = albumDto.id();
        final Long albumId2 = albumDto2.id();
        songifyCrudFacade.addArtistToAlbum(artistId, albumId);
        songifyCrudFacade.addArtistToAlbum(artistId, albumId2);
        assertThat(songifyCrudFacade.countArtistsByAlbumId(albumId)).isEqualTo(1);
        assertThat(songifyCrudFacade.countArtistsByAlbumId(albumId2)).isEqualTo(1);
        assertThat(songifyCrudFacade.findAllArtists(Pageable.unpaged())).hasSize(1);
        assertThat(songifyCrudFacade.findAllAlbums()).hasSize(2);
        assertThat(songifyCrudFacade.findAllSongs(Pageable.unpaged())).hasSize(3);
        // when
        songifyCrudFacade.deleteArtistByIdWithAlbumsAndSongs(artistId);
        // then
        assertThat(songifyCrudFacade.findAllArtists(Pageable.unpaged())).isEmpty();
        assertThat(songifyCrudFacade.findAllAlbums()).isEmpty();
        assertThat(songifyCrudFacade.findAllSongs(Pageable.unpaged())).isEmpty();
    }

}