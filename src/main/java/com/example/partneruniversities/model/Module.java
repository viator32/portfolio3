package com.example.partneruniversities.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import org.springframework.hateoas.RepresentationModel;

/**
 * Entity representing a Module.
 * This class includes information about a module offered at a partner university.
 */
@Entity
public class Module extends RepresentationModel<Module> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int semester;
    private int creditPoints;

    @ManyToOne
    private University university;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public int getCreditPoints() {
        return creditPoints;
    }

    public void setCreditPoints(int creditPoints) {
        this.creditPoints = creditPoints;
    }

    public University getUniversity() {
        return university;
    }

    public void setUniversity(University university) {
        this.university = university;
    }
}
