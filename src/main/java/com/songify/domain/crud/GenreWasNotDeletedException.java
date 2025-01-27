package com.songify.domain.crud;

class GenreWasNotDeletedException extends RuntimeException {
    public GenreWasNotDeletedException(final String message) {
        super(message);
    }
}
