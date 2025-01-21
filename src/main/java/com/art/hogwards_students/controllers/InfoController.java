package com.art.hogwards_students.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Stream;

@RestController
@RequestMapping("/port")
public class InfoController {

    @Value("${server.port}")
    private int port;

    @GetMapping
    public int getPort() {
        return port;
    }

    @GetMapping("/sum")
    public ResponseEntity <Long> sum () {
        long sum = Stream.
                iterate(1L, a -> a +1)
                .limit(1_000_000)
                .reduce(0L, Long::sum);
        return ResponseEntity.ok(sum);
    }
}
