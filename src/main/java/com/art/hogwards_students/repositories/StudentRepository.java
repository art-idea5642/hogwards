package com.art.hogwards_students.repositories;


import com.art.hogwards_students.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByAge(int age);

    List<Student> findByAgeBetween(int age1, int age2);

    List<Student> findByFacultyName (String facultyName);

    Student findByName (String studentName);

    @Query (value = " SELECT COUNT(*) FROM student ", nativeQuery = true)
    long countAllStudents();

    @Query (value = "SELECT AVG(age) FROM student", nativeQuery = true)
    float getAverageAge();

    @Query (value = "SELECT * FROM student ORDER BY id DESC LIMIT 5", nativeQuery = true)
    List <Student> findLastFiveStudents();

}
