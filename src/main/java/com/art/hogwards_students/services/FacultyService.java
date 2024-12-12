package com.art.hogwards_students.services;

import com.art.hogwards_students.model.Faculty;
import com.art.hogwards_students.repositories.FacultyRepository;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty createFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Faculty findFacultyByID(long id) {
        return facultyRepository.findById(id).get();
    }

    public List<Faculty> findFacultiesByColour(String colour) {
        return facultyRepository.findByColour(colour);
    }

    public List<Faculty> findFacultiesByColourOrName (String query) {
        return facultyRepository.findByColourIgnoreCaseOrNameIgnoreCase(query, query);
    }

    public Faculty editFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public void deleteFacultyByID(long id) {
        facultyRepository.deleteById(id);
    }

    public Collection<Faculty> getAllFaculties() {
        return facultyRepository.findAll();
    }

}

