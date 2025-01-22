package com.art.hogwards_students.services;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
public class InfoService {

    @Value("${server.port}")
    private int port;

    public int getPort() {
        return port;
    }

    Long sum;

    public Long sum (){
        sum = Stream.
                iterate(1L, a -> a +1)
                .limit(1_000_000)
                .reduce(0L, Long::sum);
        return sum;
    }
}
