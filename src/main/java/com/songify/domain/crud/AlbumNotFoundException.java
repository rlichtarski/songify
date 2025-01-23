package com.songify.domain.crud;

class AlbumNotFoundException extends RuntimeException {

    public AlbumNotFoundException(final String message) {
        super(message);
    }

}
