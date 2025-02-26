package com.songify.domain.crud;

public class AlbumNotFoundException extends RuntimeException {

    public AlbumNotFoundException(final String message) {
        super(message);
    }

}
