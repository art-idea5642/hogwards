package com.art.hogwards_students;

import com.art.hogwards_students.controllers.StudentController;
import com.art.hogwards_students.model.Faculty;
import com.art.hogwards_students.model.Student;
import com.art.hogwards_students.repositories.FacultyRepository;
import com.art.hogwards_students.repositories.StudentRepository;
import com.art.hogwards_students.services.StudentService;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.util.UriComponentsBuilder;


import java.util.List;
import java.util.Optional;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
public class StudentWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private FacultyRepository facultyRepository;

    @SpyBean
    private StudentService studentService;

    Faculty faculty;
    Student student;
    String name;
    int age;
    long id;
    JSONObject studentObject = new JSONObject();
    JSONObject facultyObject = new JSONObject();

    @BeforeEach
    void setUp() throws Exception {
        faculty = new Faculty("Имя факультета", "Цвет факультета");
        name = "Имя";
        age = 27;
        id = 1;
        student = new Student(id, name, age, faculty);
        facultyObject.put("id", faculty.getId());
        facultyObject.put("name", faculty.getName());
        facultyObject.put("colour", faculty.getColour());
        studentObject.put("id", id);
        studentObject.put("name", name);
        studentObject.put("age", age);
        studentObject.put("faculty", facultyObject);
    }

    @Test
    public void createStudentTest() throws Exception {

        when(studentRepository.save(any(Student.class))).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/student")
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age))
                .andExpect(jsonPath("$.faculty").value(faculty));

        verify(studentRepository, times(1)).save(student);

    }

    @Test
    public void testGetStudentInfo() throws Exception {
        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age))
                .andExpect(jsonPath("$.faculty").value(faculty));

        verify(studentRepository, times(1)).findById(id);
    }

    @Test
    public void testGetStudentByAge() throws Exception {

        when(studentRepository.findByAge(any(Integer.class))).thenReturn(List.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/age/" + age)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(id))
                .andExpect(jsonPath("$[0].name").value(name))
                .andExpect(jsonPath("$[0].age").value(age))
                .andExpect(jsonPath("$[0].faculty").value(faculty));

        verify(studentRepository, times(1)).findByAge(age);

    }

    @Test
    public void testGetStudentByAgeBetween() throws Exception {
        int age1 = 10;
        int age2 = 30;

        when(studentRepository.findByAgeBetween(any(Integer.class),
                any(Integer.class))).thenReturn(List.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .get(UriComponentsBuilder.fromPath("/student/age")
                                .queryParam("age1", age1)
                                .queryParam("age2", age2)
                                .toUriString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(id))
                .andExpect(jsonPath("$[0].name").value(name))
                .andExpect(jsonPath("$[0].age").value(age))
                .andExpect(jsonPath("$[0].faculty").value(faculty));

        verify(studentRepository, times(1)).findByAgeBetween(age1, age2);

    }


    @Test
    public void testGetStudents() throws Exception {
        List<Student> students = List.of(student);
        when(studentRepository.findAll()).thenReturn(students);


        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(student.getID()))
                .andExpect(jsonPath("$.[0].name").value(student.getName()))
                .andExpect(jsonPath("$.[0].age").value(student.getAge()))
                .andExpect(jsonPath("$.[0].faculty").value(student.getFaculty()));


        verify(studentRepository, times(1)).findAll();
    }

    @Test
    public void testEditStudent() throws Exception {
        String newName = "Новое имя";
        int newAge = 30;


        student.setName(newName);
        student.setAge(newAge);

        studentObject.put("name", newName);
        studentObject.put("age", newAge);


        when(studentRepository.save(any(Student.class))).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student")
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(student.getID()))
                .andExpect(jsonPath("$.name").value(student.getName()));

        verify(studentRepository, times(1)).save(argThat(s ->
                s.getName().equals(newName) &&
                        s.getAge() == newAge
        ));
    }

    @Test
    public void testDeleteStudent() throws Exception {

        doNothing().when(studentRepository).deleteById(id);
        when(studentRepository.existsById(id)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/student/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(studentRepository, times(1)).deleteById(id);

    }

    @Test
    public void testGetStudentFaculty() throws Exception {
        when(studentRepository.findByName(any(String.class))).thenReturn(student);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/faculty/" + student.getName())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(faculty.getId()))
                .andExpect(jsonPath("$.name").value(faculty.getName()))
                .andExpect(jsonPath("$.colour").value(faculty.getColour()));
        verify(studentRepository, times(1)).findByName(student.getName());
    }
}

