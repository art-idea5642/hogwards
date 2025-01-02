package com.art.hogwards_students;

import com.art.hogwards_students.controllers.FacultyController;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyTestRestTemplate {

    @LocalServerPort
    private int port;

    @Autowired
    private FacultyController facultyController;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    private static final ObjectMapper mapper = new ObjectMapper();
    List<Faculty> faculties;
    Faculty faculty;
    Faculty faculty2;

    @BeforeEach
    void setUp () {
        faculty = new Faculty("Гриффиндор", "Оранжевый" );
        faculty2 = new Faculty("Пуффендуй", "Зелёный");
        facultyRepository.save(faculty);
        facultyRepository.save(faculty2);
        faculties = facultyRepository.findAll();

    }
    @AfterEach
    void cleanUp () {
        facultyRepository.deleteAll();
    }


    @Test
    public void contextLoads() throws Exception {
        Assertions.assertThat(facultyController).isNotNull();
    }

    @Test
    public void testCreateFaculty() throws Exception {
        Faculty faculty = new Faculty("Гриффиндор", "Оранжевый" );

        ResponseEntity<Faculty> response = restTemplate.postForEntity("http://localhost:" + port + "/faculty", faculty, Faculty.class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertNotNull(response.getBody());
        assertEquals(faculty.getName(), response.getBody().getName());
        assertEquals(response.getBody(), faculty);
    }

    @Test
    public void testFindFacultyById () throws JSONException, JsonProcessingException {
        long id = faculty.getId();

        ResponseEntity<Faculty> actualFaculty = this.restTemplate.getForEntity(
                "http://localhost:" + port + "/faculty/" + id, Faculty.class);

        assertEquals(HttpStatus.OK, actualFaculty.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, actualFaculty.getHeaders().getContentType());
        assertEquals(actualFaculty.getBody(), faculty);
    }

    @Test
    public void testFindFacultiesByColour () throws JSONException, JsonProcessingException {
        List<Faculty> expectedFaculties = facultyRepository.findByColour("Оранжевый");
        String facultyColour = "Оранжевый";

        ResponseEntity<List<Faculty>> actualFaculties = this.restTemplate.exchange("http://localhost:" + port + "/faculty/colour/" +
                "Оранжевый", HttpMethod.GET, null, new ParameterizedTypeReference<>() {
        });

        assertEquals(HttpStatus.OK, actualFaculties.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, actualFaculties.getHeaders().getContentType());
        assertEquals(actualFaculties.getBody(), expectedFaculties);
    }

    @Test
    public void testFindFacultiesByColourOrName () throws JSONException, JsonProcessingException {
        List<Faculty> expectedFaculties = facultyRepository.findByColour("Оранжевый");
        String facultyName = faculty.getName();

        ResponseEntity<List<Faculty>> actualFaculties = this.restTemplate.exchange("http://localhost:" + port + "/faculty/search/" +
                facultyName, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
        });

        assertEquals(HttpStatus.OK, actualFaculties.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, actualFaculties.getHeaders().getContentType());
        assertEquals(actualFaculties.getBody(), expectedFaculties);
    }

    @Test
    public void testEditFaculty() throws JSONException, JsonProcessingException {
        Faculty expectedFaculty = new Faculty("Изменённое имя", "Изменённый цвет");
        HttpEntity<Faculty> entity = new HttpEntity<>(expectedFaculty);

        ResponseEntity<Faculty> actualFaculty = this.restTemplate.exchange("http://localhost:" + port + "/faculty",
                HttpMethod.PUT, entity, Faculty.class);

        assertEquals(HttpStatus.OK, actualFaculty.getStatusCode());
        assertEquals(expectedFaculty, actualFaculty.getBody());
    }


    @Test
    public void testDeleteFaculty() throws JSONException, JsonProcessingException {

        HttpEntity<Faculty> entity = new HttpEntity<>(null, new HttpHeaders());

        ResponseEntity<Faculty> actualFaculty = this.restTemplate.exchange("http://localhost:" + port + "/faculty/" + faculties.get(0).getId(),
                HttpMethod.DELETE, null, Faculty.class);

        assertEquals(HttpStatus.OK, actualFaculty.getStatusCode());
        assertNull(actualFaculty.getBody());

    }

    @Test
    public void testGetAllFaculties () throws JSONException, JsonProcessingException {
        Collection<Faculty> expectedFaculties = faculties;

        ResponseEntity <Collection<Faculty>> actualFaculties = this.restTemplate.exchange("http://localhost:" + port +
                "/faculty", HttpMethod.GET, null, new ParameterizedTypeReference<>() {        });

        assertEquals(HttpStatus.OK, actualFaculties.getStatusCode());
        assertEquals(expectedFaculties, actualFaculties.getBody());
    }

}
