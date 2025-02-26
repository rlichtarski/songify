package com.songify.domain.crud;

class GenreNotFoundException extends RuntimeException {
    public GenreNotFoundException(final String message) {
        super(message);
    }
}
