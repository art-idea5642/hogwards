package com.art.hogwards_students.repositories;

import com.art.hogwards_students.model.Faculty;
import com.art.hogwards_students.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {

    List<Faculty> findByColour(String colour);

    List<Faculty> findByColourIgnoreCaseOrNameIgnoreCase(String colour, String name);

}
