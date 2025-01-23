package com.art.hogwards_students.services;

import com.art.hogwards_students.model.Faculty;
import com.art.hogwards_students.repositories.FacultyRepository;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FacultyService {

    private static final Logger logger = LoggerFactory.getLogger(FacultyService.class);

    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty createFaculty(Faculty faculty) {
        logger.info("Was invoked method for creating a faculty");
        logger.debug("Creating faculty: {}", faculty);
        return facultyRepository.save(faculty);
    }

    public Faculty findFacultyByID(long id) {
        logger.info("Was invoked method for finding a faculty by ID");
        logger.debug("Searching for faculty with ID: {}", id);
        Optional<Faculty> faculty = facultyRepository.findById(id);
        if (faculty.isPresent()) {
            return faculty.get();
        } else {
            logger.error("Faculty with ID {} not found", id);
            throw new IllegalArgumentException("Faculty with ID " + id + " not found");
        }
    }

    public List<Faculty> findFacultiesByColour(String colour) {
        logger.info("Was invoked method for finding faculties by colour");
        logger.debug("Searching for faculties with colour: {}", colour);
        return facultyRepository.findByColour(colour);
    }

    public List<Faculty> findFacultiesByColourOrName(String query) {
        logger.info("Was invoked method for finding faculties by colour or name");
        logger.debug("Searching for faculties with query: {}", query);
        return facultyRepository.findByColourIgnoreCaseOrNameIgnoreCase(query, query);
    }

    public Faculty editFaculty(Faculty faculty) {
        logger.info("Was invoked method for editing a faculty");
        logger.debug("Editing faculty: {}", faculty);
        if (!facultyRepository.existsById(faculty.getId())) {
            logger.error("Attempted to edit a faculty that does not exist with id: {}", faculty.getId());
            throw new IllegalArgumentException("Faculty with ID " + faculty.getId() + " does not exist");
        }
        return facultyRepository.save(faculty);
    }

    public void deleteFacultyByID(long id) {
        logger.info("Was invoked method for deleting a faculty by ID");
        logger.debug("Deleting faculty with ID: {}", id);
        if (!facultyRepository.existsById(id)) {
            logger.error("Attempted to delete a faculty that does not exist with id: {}", id);
            throw new IllegalArgumentException("Faculty with ID " + id + " does not exist");
        }
        facultyRepository.deleteById(id);
    }

    public Collection<Faculty> getAllFaculties() {
        logger.info("Was invoked method for retrieving all faculties");
        return facultyRepository.findAll();
    }

    public String getTheLongestFacultyName() {
        return facultyRepository.findAll().stream()
                .map(Faculty::getName)
                .max(Comparator.comparing(String::length))
                .orElseThrow(() -> new RuntimeException("Факультеты не найдены"));
    }


}

