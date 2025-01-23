package com.art.hogwards_students.services;

import com.art.hogwards_students.model.Faculty;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.stream.Stream;

@Service
public class InfoService {
    Long sum;

    public Long sum (){
        sum = Stream.
                iterate(1L, a -> a +1)
                .limit(1_000_000)
                .reduce(0L, Long::sum);
        return sum;
    }
}
