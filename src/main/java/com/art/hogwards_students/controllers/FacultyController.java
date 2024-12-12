package com.art.hogwards_students.controllers;

import com.art.hogwards_students.model.Faculty;
import com.art.hogwards_students.services.FacultyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/faculty")
public class FacultyController {


    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }


    @PostMapping
    public ResponseEntity<Faculty> createFaculty(@RequestBody Faculty faculty) {
        Faculty createdFaculty = facultyService.createFaculty(faculty);
        return ResponseEntity.ok(createdFaculty);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Faculty> findFacultyByName(@RequestParam long id) {
        Faculty faculty = facultyService.findFacultyByID(id);
        if (faculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculty);
    }


    @GetMapping("/colour/{colour}")
    public ResponseEntity<List<Faculty>> findFacultiesByColour(@RequestParam String colour) {
        List<Faculty> faculties = facultyService.findFacultiesByColour(colour);
        if (faculties.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculties);
    }

    @GetMapping("/search/{query}")
    public ResponseEntity<List<Faculty>> findFacultiesByColourOrName(@RequestParam String query) {
        List<Faculty> faculties = facultyService.findFacultiesByColourOrName(query);
        if (faculties.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculties);
    }


    @PutMapping
    public ResponseEntity<Faculty> editFaculty(@RequestBody Faculty faculty) {
        Faculty updatedFaculty = facultyService.editFaculty(faculty);
        return ResponseEntity.ok(updatedFaculty);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Faculty> deleteFacultyByName(@RequestParam long id) {
        facultyService.deleteFacultyByID(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Collection<Faculty>> getAllFaculties() {
        Collection<Faculty> faculties = facultyService.getAllFaculties();
        if (faculties.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(faculties);
    }


}
