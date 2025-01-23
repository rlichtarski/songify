package com.songify.domain.crud;

import com.songify.domain.crud.dto.AlbumInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

interface AlbumRepository extends Repository<Album, Long> {
    Album save(Album album);

    @Query("""
            select a from Album a
            inner join fetch a.songs songs
            inner join fetch a.artists artists
            where a.id = :id""")
    Optional<AlbumInfo> findAlbumByIdWithArtistsAndSongs(@Param("id") Long id);

    /*@Query("select a from Album a where a.id = :id")
    Optional<AlbumInfo> findAlbumByIdWithArtistsAndSongs(@Param("id") Long id);*/
}
