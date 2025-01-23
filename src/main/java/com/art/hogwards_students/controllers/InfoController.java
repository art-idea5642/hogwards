package com.art.hogwards_students.controllers;

import com.art.hogwards_students.services.InfoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Stream;

@RestController
@RequestMapping("/port")
public class InfoController {

    private final InfoService infoService;

    public InfoController(InfoService infoService) {
        this.infoService = infoService;
    }

    @Value("${server.port}")
    private int port;

    @GetMapping
    public int getPort() {
        return port;
    }

    @GetMapping("/sum")
    public ResponseEntity <Long> sum () {
        return ResponseEntity.ok(infoService.sum());
    }
}
