package com.art.hogwards_students;


import com.art.hogwards_students.controllers.StudentController;
import com.art.hogwards_students.model.Faculty;
import com.art.hogwards_students.model.Student;
import com.art.hogwards_students.repositories.FacultyRepository;
import com.art.hogwards_students.repositories.StudentRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.json.JSONException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;


import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentsTestRestTemplate {


    List<Student> saveStudents;
    Student student1;
    Student student2;
    Faculty faculty;

    @LocalServerPort
    private int port;

    @Autowired
    private StudentController studentController;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    private static final ObjectMapper mapper = new ObjectMapper();


    @BeforeEach
    void setUp() {
        student1 = new Student();
        student1.setName("Артём");
        student1.setAge(27);
        studentRepository.save(student1);
        student2 = new Student();
        student2.setName("Гарри");
        student2.setAge(15);
        studentRepository.save(student2);
        saveStudents = studentRepository.findAll();
    }

    @AfterEach
    public void cleanUp() {
        studentRepository.deleteAll();
        facultyRepository.deleteAll();
    }

    @Test
    public void contextLoads() throws JSONException, JsonProcessingException {
        Assertions.assertThat(studentController).isNotNull();
    }


    @Test
    public void testCreateStudent() throws JSONException, JsonProcessingException {
        Faculty faculty = new Faculty();
        Student student = new Student();

        student.setAge(35);
        student.setName("Тестовое имя");
        student.setFaculty(faculty);
        facultyRepository.save(faculty);

        ResponseEntity<Student> response = restTemplate.postForEntity("http://localhost:" + port + "/student", student, Student.class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertNotNull(response.getBody());
        assertEquals("Тестовое имя", response.getBody().getName());
        assertEquals(response.getBody(), student);
    }


    @Test
    public void testGetStudentInfo() throws JSONException, JsonProcessingException {
        String expectedStudent = mapper.writeValueAsString(student1);
        long studentId = student1.getID();

        ResponseEntity<String> actualStudent = this.restTemplate.getForEntity("http://localhost:" + port + "/student/" + studentId, String.class);

        assertEquals(HttpStatus.OK, actualStudent.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, actualStudent.getHeaders().getContentType());
        JSONAssert.assertEquals(expectedStudent, actualStudent.getBody(), false);
    }

    @Test
    public void testGetStudentInfo2() throws JSONException, JsonProcessingException {
        Student expectedStudent = student1;
        long id = student1.getID();

        ResponseEntity<Student> actualStudent = this.restTemplate.getForEntity(
                "http://localhost:" + port + "/student/" + id, Student.class
        );

        assertEquals(HttpStatus.OK, actualStudent.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, actualStudent.getHeaders().getContentType());
        assertEquals(actualStudent.getBody(), expectedStudent);
    }

    @Test
    public void testGetStudentByAge() throws JSONException, JsonProcessingException {

        List<Student> expectedStudents = studentRepository.findByAge(15);


        ResponseEntity<List<Student>> actualStudents = this.restTemplate.exchange("http://localhost:" + port + "/student/age/" +
                15, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
        });


        if (expectedStudents.isEmpty()) {
            assertEquals(HttpStatus.NOT_FOUND, actualStudents.getStatusCode());
        } else {

            assertEquals(HttpStatus.OK, actualStudents.getStatusCode());
            assertNotNull(actualStudents.getBody());
            assertEquals(expectedStudents.size(), actualStudents.getBody().size());

            assertEquals(expectedStudents, actualStudents.getBody());
        }
    }

    @Test
    public void testGetStudentByAgeBetween() throws JSONException, JsonProcessingException {

        List<Student> expectedStudents = studentRepository.findByAgeBetween(15, 28);


        ResponseEntity<List<Student>> actualStudents = this.restTemplate.exchange("http://localhost:" + port + "/student/age?age1=15&age2=28"
                , HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                });


        if (expectedStudents.isEmpty()) {
            assertEquals(HttpStatus.NOT_FOUND, actualStudents.getStatusCode());
        } else {

            assertEquals(HttpStatus.OK, actualStudents.getStatusCode());
            assertNotNull(actualStudents.getBody());
            assertEquals(expectedStudents.size(), actualStudents.getBody().size());

            assertEquals(expectedStudents, actualStudents.getBody());
        }
    }

    @Test
    public void testGetStudents() throws JSONException, JsonProcessingException {
        List<Student> expectedStudents = saveStudents;

        ResponseEntity<List<Student>> actualStudents = this.restTemplate.exchange("http://localhost:" + port + "/student"
                , HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                });


        if (expectedStudents.isEmpty()) {
            assertEquals(HttpStatus.NOT_FOUND, actualStudents.getStatusCode());
        } else {

            assertEquals(HttpStatus.OK, actualStudents.getStatusCode());
            assertNotNull(actualStudents.getBody());
            assertEquals(expectedStudents.size(), actualStudents.getBody().size());

            assertEquals(expectedStudents, actualStudents.getBody());
        }

    }

    @Test
    public void testEditStudent() throws JSONException, JsonProcessingException {
        Student expectedStudent = new Student("Изменённое имя", 30);
        HttpEntity<Student> entity = new HttpEntity<>(expectedStudent);

        ResponseEntity<Student> actualStudent = this.restTemplate.exchange("http://localhost:" + port + "/student",
                HttpMethod.PUT, entity, Student.class);

        assertEquals(HttpStatus.OK, actualStudent.getStatusCode());
        assertEquals(expectedStudent, actualStudent.getBody());

    }

    @Test
    public void testDeleteStudent() throws JSONException, JsonProcessingException {

        HttpEntity<Student> entity = new HttpEntity<>(null, new HttpHeaders());

        ResponseEntity<Student> actualStudent = this.restTemplate.exchange("http://localhost:" + port + "/student/" + saveStudents.get(0).getID(),
                HttpMethod.DELETE, null, Student.class);

        assertEquals(HttpStatus.OK, actualStudent.getStatusCode());
        assertNull(actualStudent.getBody());

    }

    @Test
    public void testGetStudentFaculty() throws JSONException, JsonProcessingException {
        faculty = new Faculty();
        faculty.setName("Тест");
        faculty.setColour("Тест");
        facultyRepository.save(faculty);

        student1.setFaculty(faculty);
        studentRepository.save(student1);

        System.out.println("Testing URL: " + "http://localhost:" + port + "/student/faculty/" + student1.getName());
        System.out.println("Student name: " + student1.getName());


        ResponseEntity<Faculty> actualFaculty = this.restTemplate.getForEntity("http://localhost:" + port + "/student/faculty/" + student1.getName(),
                Faculty.class);

        assertEquals(HttpStatus.OK, actualFaculty.getStatusCode());
        assertEquals(faculty, actualFaculty.getBody());
    }


}
