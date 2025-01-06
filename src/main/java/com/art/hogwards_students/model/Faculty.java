package com.art.hogwards_students.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Collection;
import java.util.Objects;

@Entity
public class Faculty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String colour;

    @JsonIgnore
    @OneToMany(mappedBy = "faculty", cascade = CascadeType.ALL)
    private Collection<Student> students;

    public Faculty(String name, String colour) {
        this.name = name;
        this.colour = colour;
    }

    public Faculty(long id, String name, String colour) {
        this.id = id;
        this.name = name;
        this.colour = colour;
    }

    public Faculty() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Faculty faculty = (Faculty) o;
        return  Objects.equals(name, faculty.name) &&
                Objects.equals(colour, faculty.colour);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, colour);
    }

    @Override
    public String toString() {
        return "Faculty{" +
                "ID=" + id +
                ", name='" + name + '\'' +
                ", colour='" + colour + '\'' +
                '}';
    }
}

