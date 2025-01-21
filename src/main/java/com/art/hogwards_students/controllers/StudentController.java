package com.art.hogwards_students.controllers;

import com.art.hogwards_students.model.Faculty;
import com.art.hogwards_students.model.Student;
import com.art.hogwards_students.services.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    public StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentInfo(@PathVariable long id) {
        Student student = studentService.findStudent(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @GetMapping("/age/{age}")
    public ResponseEntity<List<Student>> getStudentsByAge(@PathVariable int age) {
        List<Student> students = studentService.findStudentByAge(age);
        if (students.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(students);
    }

    @GetMapping("/age")
    public ResponseEntity<List<Student>> getStudentsByAgeRange(
            @RequestParam(required = false) Integer age1,
            @RequestParam(required = false) Integer age2) {
        if (age1 == null && age2 == null) {
            return ResponseEntity.badRequest().build();
        }
        List<Student> students = studentService.findStudentByAgeBetween(age1, age2);
        if (students.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(students);
    }


    @GetMapping
    public ResponseEntity<Collection<Student>> getStudents(@RequestParam(required = false) String facultyName) {
        if (facultyName != null && !facultyName.isBlank()){
            return ResponseEntity.ok(studentService.getAllByFaculty(facultyName));
        }
        return ResponseEntity.ok(studentService.getAll());
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @PutMapping
    public ResponseEntity<Student> editStudent(@RequestBody Student student) {
        Student editableStudent = studentService.editStudent(student);
        if (editableStudent == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(editableStudent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Student> deleteStudent(@PathVariable long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/faculty/{studentName}")
    public ResponseEntity<Faculty> getStudentsFaculty(@PathVariable String studentName) {
        Faculty faculty = studentService.getStudentsFaculty(studentName);
        if (faculty == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(faculty);
    }

    @GetMapping("/counter")
    public long countAllStudent () {
        return studentService.countAllStudents();
    }

    @GetMapping("/average_age")
    public float getAverageAge () {
        return studentService.getAverageAge();
    }

    @GetMapping("/last_5_students")
    public ResponseEntity<List<Student>> findLastFiveStudents () {
        List<Student> students = studentService.findLastFiveStudents();
        if (students.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(students);
    }

    @GetMapping ("/students-with-a")
    public ResponseEntity <List <String>> getStudentsByNameStartsWithA () {
        List <String> students = studentService.getStudentsByNameStartsWithA();
        if (students.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(students);
    }

    @GetMapping("/average_age_streams")
    public double getAverageAgeWithStreams () {
        return studentService.getAverageAgeWithStreams();
    }

}
