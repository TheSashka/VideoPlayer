package com.wotn.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class PageController {

    @Value("${video.location}")
    private String videoLocation;

    @GetMapping("/")
    public String index(Model model) throws IOException {
        List<String> videos = Files.list(Paths.get(videoLocation)).map(e -> e.getFileName().toString()).collect(Collectors.toList());
        model.addAttribute("videos", videos);
        return "index";
    }

    @GetMapping("/{videoName}")
    public String video(@PathVariable String videoName, Model model) {
        model.addAttribute("videoName", videoName);
        return "video";
    }
}
