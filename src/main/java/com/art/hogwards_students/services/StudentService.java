package com.art.hogwards_students.services;

import com.art.hogwards_students.model.Faculty;
import com.art.hogwards_students.model.Student;
import com.art.hogwards_students.repositories.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@Service
public class StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(Student student) {
        logger.info("Was invoked method for create student");
        logger.debug("Creating student with details: {}", student);
        return studentRepository.save(student);
    }

    public Student findStudent(long id) {
        logger.info("Was invoked method for find student");
        logger.debug("Finding student with id: {}", id);
        return studentRepository.findById(id).orElseThrow(() -> {
            logger.warn("There is no student with id = {}", id);
            return new IllegalArgumentException("Студент с id = " + id + " не найден.");
        });
    }

    public List<Student> findStudentByAge(int age) {
        logger.info("Was invoked method for find student by age");
        logger.debug("Finding students with age: {}", age);
        return studentRepository.findByAge(age);
    }

    public List<Student> findStudentByAgeBetween(Integer age1, Integer age2) {
        logger.info("Was invoked method for find students by age range");
        logger.debug("Finding students with age between {} and {}", age1, age2);
        return studentRepository.findByAgeBetween(age1, age2);
    }

    public Student editStudent(Student student) {
        logger.info("Was invoked method for edit student");
        logger.debug("Editing student with details: {}", student);
        return studentRepository.save(student);
    }

    public void deleteStudent(long id) {
        logger.info("Was invoked method for delete student");
        logger.debug("Deleting student with id: {}", id);
        if (!studentRepository.existsById(id)) {
            logger.warn("Attempted to delete non-existent student with id: {}", id);
            throw new IllegalArgumentException("Студент с id = " + id + " не найден.");
        }
        studentRepository.deleteById(id);
    }

    public Collection<Student> getAll() {
        logger.info("Was invoked method for get all students");
        return Collections.unmodifiableCollection(studentRepository.findAll());
    }

    public Collection<Student> getAllByFaculty(String facultyName) {
        logger.info("Was invoked method for get all students by faculty");
        logger.debug("Finding students from faculty: {}", facultyName);
        return studentRepository.findByFacultyName(facultyName);
    }

    public Faculty getStudentsFaculty(String studentName) {
        logger.info("Was invoked method for get student's faculty");
        logger.debug("Finding faculty for student with name: {}", studentName);
        Student student = studentRepository.findByName(studentName);
        if (student == null) {
            logger.error("Student with name {} not found", studentName);
            throw new IllegalArgumentException("Студент с именем " + studentName + " не найден.");
        }
        return student.getFaculty();
    }

    public long countAllStudents() {
        logger.info("Was invoked method for count all students");
        long count = studentRepository.countAllStudents();
        logger.debug("Total number of students: {}", count);
        return count;
    }

    public float getAverageAge() {
        logger.info("Was invoked method for get average age of students");
        float averageAge = studentRepository.getAverageAge();
        logger.debug("Calculated average age: {}", averageAge);
        return averageAge;
    }

    public List<Student> findLastFiveStudents() {
        logger.info("Was invoked method for find last five students");
        List<Student> students = studentRepository.findLastFiveStudents();
        logger.debug("Last five students: {}", students);
        return students;
    }

    public List<String> getStudentsByNameStartsWithA() {
        List<Student> students = studentRepository.findAll();

        List<String> studentsWithA = students.stream()
                .map(Student::getName)
                .filter(name -> name.startsWith("A"))
                .map(String::toUpperCase)
                .sorted()
                .toList();

        if (studentsWithA.isEmpty()) {
            logger.warn("No students found with names starting with 'A'.");
        } else {
            logger.debug("Students with names starting with 'A': {}", studentsWithA);
        }

        return studentsWithA;
    }

    public double getAverageAgeWithStreams() {
        List<Student> students = studentRepository.findAll();

        if (students.isEmpty()) {
            logger.warn("No students found to calculate the average age.");
            throw new IllegalArgumentException("Список студентов пуст. Невозможно рассчитать средний возраст.");
        }

        double averageAge = students.stream()
                .mapToInt(Student::getAge)
                .average()
                .orElse(0);

        logger.debug("Calculated average age: {}", averageAge);
        return averageAge;
    }


}
