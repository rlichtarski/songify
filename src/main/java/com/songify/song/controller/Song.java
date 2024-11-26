package com.songify.song.controller;

import lombok.Builder;

@Builder
public record Song(String artistName, String songName) {
}
