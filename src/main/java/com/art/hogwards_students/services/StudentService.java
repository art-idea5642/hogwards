package com.art.hogwards_students.services;

import com.art.hogwards_students.model.Faculty;
import com.art.hogwards_students.model.Student;
import com.art.hogwards_students.repositories.StudentRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student findStudent(long id) {
        return studentRepository.findById(id).get();
    }

    public List<Student> findStudentByAge(int age) {
        return studentRepository.findByAge(age);
    }


    public List<Student> findStudentByAgeBetween(Integer age1, Integer age2) {
        return studentRepository.findByAgeBetween(age1, age2);
    }

    public Student editStudent(Student student) {
        return studentRepository.save(student);
    }

    public void deleteStudent(long id) {
        studentRepository.deleteById(id);
    }

    public Collection<Student> getAll() {
        return studentRepository.findAll();
    }

    public Collection<Student> getAllByFaculty(String facultyName) {
        return studentRepository.findByFacultyName(facultyName);
    }

    public Faculty getStudentsFaculty(String studentName) {
        return studentRepository.findByName(studentName).getFaculty();
    }

}
