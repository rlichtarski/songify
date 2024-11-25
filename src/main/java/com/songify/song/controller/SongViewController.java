package com.songify.song.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
public class SongViewController {

    private Map<Integer, String> database = new HashMap<>();

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/view/songs")
    public String viewSongs(Model model) {
        database.put(1, "Billie Eilish");
        database.put(2, "Taco Hemingway");
        database.put(3, "Metallica");
        database.put(4, "AC/DC");
        model.addAttribute("songMap", database);
        return "songs";
    }

}
