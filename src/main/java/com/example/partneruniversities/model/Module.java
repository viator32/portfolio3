package com.example.partneruniversities.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.hateoas.RepresentationModel;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Module extends RepresentationModel<Module> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int semester;
    private int creditPoints;

    @NotNull(message = "University is mandatory")
    @ManyToOne
    @JoinColumn(name = "university_id")
    @JsonBackReference
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

    @JsonProperty("university")
    public void setUniversity(University university) {
        this.university = university;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Module module = (Module) o;
        return Objects.equals(id, module.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            ObjectNode moduleNode = mapper.createObjectNode();
            moduleNode.put("name", name);
            moduleNode.put("semester", semester);
            moduleNode.put("creditPoints", creditPoints);

            if (university != null && university.getId() != null) {
                ObjectNode universityNode = mapper.createObjectNode();
                universityNode.put("id", university.getId());
                moduleNode.set("university", universityNode);
            }

            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(moduleNode);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
