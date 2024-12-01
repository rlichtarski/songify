package com.songify.song.domain.model;

import lombok.Builder;

@Builder
public record Song(String songName, String artistName) {
}
