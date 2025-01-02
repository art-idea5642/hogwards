package com.art.hogwards_students;

import com.art.hogwards_students.controllers.FacultyController;
import com.art.hogwards_students.model.Faculty;
import com.art.hogwards_students.repositories.FacultyRepository;
import com.art.hogwards_students.services.FacultyService;
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


import java.util.List;
import java.util.Optional;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FacultyController.class)
public class FacultyWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacultyRepository facultyRepository;

    @SpyBean
    private FacultyService facultyService;

    Faculty faculty;
    String name;
    String colour;
    long id;
    JSONObject facultyObject = new JSONObject();

    @BeforeEach
    void setUp() throws Exception {
        name = "Факультет";
        colour = "Цвет";
        id = 1;
        faculty = new Faculty(id, name, colour);
        facultyObject.put("id", id);
        facultyObject.put("name", name);
        facultyObject.put("colour", colour);
    }

    @Test
    public void createFacultyTest() throws Exception {

        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.colour").value(colour));

        verify(facultyRepository, times(1)).save(faculty);

    }

    @Test
    public void testFindFacultyById() throws Exception {


        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/1")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.colour").value(colour));

        verify(facultyRepository, times(1)).findById(1L);
    }


    @Test
    public void testFindFacultiesByColour() throws Exception {
        when(facultyRepository.findByColour(any(String.class))).thenReturn(List.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/colour/Цвет")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(faculty.getId()))
                .andExpect(jsonPath("$[0].name").value(faculty.getName()))
                .andExpect(jsonPath("$[0].colour").value(faculty.getColour()));

        verify(facultyRepository, times(1)).findByColour("Цвет");
    }

    @Test
    public void testFindFacultiesByColourOrName() throws Exception {
        when(facultyRepository.findByColourIgnoreCaseOrNameIgnoreCase(any(String.class), any(String.class))).thenReturn(List.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/search/Факультет")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(faculty.getId()))
                .andExpect(jsonPath("$[0].name").value(faculty.getName()))
                .andExpect(jsonPath("$[0].colour").value(faculty.getColour()));

        verify(facultyRepository, times(1)).
                findByColourIgnoreCaseOrNameIgnoreCase("Факультет", "Факультет");

    }

    @Test
    public void testEditFaculty() throws Exception {

        String newName = "Факультет";
        String newColour = "Цвет";
        long newId = 2;

        faculty.setId(newId);
        faculty.setName(newName);
        faculty.setColour(newColour);

        facultyObject.put("id", newId);
        facultyObject.put("name", newName);
        facultyObject.put("colour", newColour);

        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(faculty.getId()))
                .andExpect(jsonPath("$.name").value(faculty.getName()))
                .andExpect(jsonPath("$.colour").value(faculty.getColour()));

        verify(facultyRepository, times(1)).save(argThat(f ->
                f.getId() == newId &&
                        f.getName().equals(newName) &&
                        f.getColour().equals(newColour)
        ));

    }


    @Test
    public void testDeleteFaculty() throws Exception {
        doNothing().when(facultyRepository).deleteById(id);


        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculty/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());


        verify(facultyRepository, times(1)).deleteById(id);
    }

    @Test
    public void testGetAllFaculties() throws Exception {
        List<Faculty> faculties = List.of(faculty);
        when(facultyRepository.findAll()).thenReturn(faculties);


        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(faculty.getId()))
                .andExpect(jsonPath("$.[0].name").value(faculty.getName()))
                .andExpect(jsonPath("$.[0].colour").value(faculty.getColour()));


        verify(facultyRepository, times(1)).findAll();
    }


}
