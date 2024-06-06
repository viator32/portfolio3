package com.example.partneruniversities.controller;

import com.example.partneruniversities.assembler.UniversityModelAssembler;
import com.example.partneruniversities.model.University;
import com.example.partneruniversities.service.UniversityService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

/**
 * REST controller for managing University entities.
 * Provides endpoints for CRUD operations and HATEOAS links.
 */
@RestController
@RequestMapping("/universities")
public class UniversityController {

    private final UniversityService universityService;
    private final UniversityModelAssembler assembler;

    public UniversityController(UniversityService universityService, UniversityModelAssembler assembler) {
        this.universityService = universityService;
        this.assembler = assembler;
    }

    /**
     * GET /universities : Get all universities.
     *
     * @return the CollectionModel of universities
     */
    @GetMapping
    public CollectionModel<EntityModel<University>> getAllUniversities() {
        List<EntityModel<University>> universities = universityService.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(universities, linkTo(methodOn(UniversityController.class).getAllUniversities()).withSelfRel());
    }

    /**
     * GET /universities/{id} : Get a university by ID.
     *
     * @param id the ID of the university to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the university, or with status 404 (Not Found)
     */
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<University>> getUniversityById(@PathVariable Long id) {
        University university = universityService.findById(id)
                .orElseThrow(() -> new RuntimeException("University not found"));
        return ResponseEntity.ok(assembler.toModel(university));
    }

    /**
     * POST /universities : Create a new university.
     *
     * @param university the university to create
     * @return the ResponseEntity with status 201 (Created) and with body the new university
     */
    @PostMapping
    public ResponseEntity<EntityModel<University>> createUniversity(@RequestBody University university) {
        University savedUniversity = universityService.save(university);
        EntityModel<University> entityModel = assembler.toModel(savedUniversity);
        return ResponseEntity.created(entityModel.getRequiredLink("self").toUri())
                .body(entityModel);
    }

    /**
     * PUT /universities/{id} : Update an existing university.
     *
     * @param id the ID of the university to update
     * @param universityDetails the university to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated university, or with status 404 (Not Found)
     */
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<University>> updateUniversity(@PathVariable Long id, @RequestBody University universityDetails) {
        University updatedUniversity = universityService.findById(id)
                .map(university -> {
                    university.setName(universityDetails.getName());
                    university.setCountry(universityDetails.getCountry());
                    university.setDepartmentName(universityDetails.getDepartmentName());
                    university.setDepartmentUrl(universityDetails.getDepartmentUrl());
                    university.setContactPerson(universityDetails.getContactPerson());
                    university.setMaxOutgoingStudents(universityDetails.getMaxOutgoingStudents());
                    university.setMaxIncomingStudents(universityDetails.getMaxIncomingStudents());
                    university.setNextSpringSemesterStart(universityDetails.getNextSpringSemesterStart());
                    university.setNextAutumnSemesterStart(universityDetails.getNextAutumnSemesterStart());
                    university.setModules(universityDetails.getModules());
                    return universityService.save(university);
                })
                .orElseGet(() -> {
                    universityDetails.setId(id);
                    return universityService.save(universityDetails);
                });

        EntityModel<University> entityModel = assembler.toModel(updatedUniversity);
        return ResponseEntity.ok(entityModel);
    }

    /**
     * DELETE /universities/{id} : Delete a university by ID.
     *
     * @param id the ID of the university to delete
     * @return the ResponseEntity with status 204 (NO_CONTENT)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUniversity(@PathVariable Long id) {
        universityService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
