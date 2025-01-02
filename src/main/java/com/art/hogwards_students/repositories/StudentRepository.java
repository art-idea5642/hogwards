package com.art.hogwards_students.repositories;


import com.art.hogwards_students.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByAge(int age);

    List<Student> findByAgeBetween(int age1, int age2);

    List<Student> findByFacultyName (String facultyName);

    Student findByName (String studentName);

}
