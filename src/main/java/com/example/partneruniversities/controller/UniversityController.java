package com.example.partneruniversities.controller;

import com.example.partneruniversities.assembler.UniversityModelAssembler;
import com.example.partneruniversities.model.University;
import com.example.partneruniversities.service.UniversityService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
     * GET /universities : Get all universities with pagination.
     *
     * @param pageable        pagination information
     * @param pagedAssembler  assembler for pagination
     * @return PagedModel of universities
     */
    @GetMapping
    public PagedModel<EntityModel<University>> getAllUniversities(Pageable pageable, PagedResourcesAssembler<University> pagedAssembler) {
        Page<University> universities = universityService.findAll(pageable);
        return pagedAssembler.toModel(universities, assembler);
    }

    /**
     * GET /universities/{id} : Get a university by ID.
     *
     * @param id the ID of the university to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the university, or with status 404 (Not Found)
     */
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<University>> getUniversityById(@PathVariable Long id) {
        University university = universityService.findById(id);
        return university != null
                ? ResponseEntity.ok(assembler.toModel(university))
                : ResponseEntity.notFound().build();
    }

    /**
     * POST /universities : Create a new university.
     *
     * @param university the university to create
     * @return the ResponseEntity with status 201 (Created) and with body the new university, or with status 400 (Bad Request) if the university has already an ID
     */
    @PostMapping
    public ResponseEntity<EntityModel<University>> createUniversity(@RequestBody University university) {
        University savedUniversity = universityService.save(university);
        return ResponseEntity.created(linkTo(methodOn(UniversityController.class).getUniversityById(savedUniversity.getId())).toUri())
                .body(assembler.toModel(savedUniversity));
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
        University updatedUniversity = universityService.update(id, universityDetails);
        return updatedUniversity != null
                ? ResponseEntity.ok(assembler.toModel(updatedUniversity))
                : ResponseEntity.notFound().build();
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
