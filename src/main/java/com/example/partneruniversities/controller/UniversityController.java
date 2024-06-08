package com.example.partneruniversities.controller;

import com.example.partneruniversities.assembler.UniversityModelAssembler;
import com.example.partneruniversities.model.Module;
import com.example.partneruniversities.model.University;
import com.example.partneruniversities.service.ModuleService;
import com.example.partneruniversities.service.UniversityService;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpHeaders;
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
    private final ModuleService moduleService;
    private final UniversityModelAssembler assembler;

    public UniversityController(UniversityService universityService, ModuleService moduleService, UniversityModelAssembler assembler) {
        this.universityService = universityService;
        this.moduleService = moduleService;
        this.assembler = assembler;
    }

    @GetMapping
    public ResponseEntity<?> getAllUniversities() {
        List<EntityModel<University>> universities = universityService.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        HttpHeaders headers = new HttpHeaders();
        headers.add("self", linkTo(methodOn(UniversityController.class).getAllUniversities()).withSelfRel().getHref());

        return ResponseEntity.ok().headers(headers).body(universities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUniversityById(@PathVariable Long id) {
        University university = universityService.findById(id)
                .orElseThrow(() -> new RuntimeException("University not found"));

        EntityModel<University> universityModel = assembler.toModel(university);

        HttpHeaders headers = new HttpHeaders();
        headers.add("self", universityModel.getLink("self").orElseThrow().getHref());
        headers.add("update", linkTo(methodOn(UniversityController.class).updateUniversity(id, university)).withRel("update").getHref());
        headers.add("delete", linkTo(methodOn(UniversityController.class).deleteUniversity(id)).withRel("delete").getHref());

        return ResponseEntity.ok().headers(headers).body(universityModel);
    }

    @GetMapping("/{universityId}/modules")
    public ResponseEntity<?> getModulesByUniversityId(@PathVariable Long universityId) {
        List<Module> modules = moduleService.getModulesByUniversityId(universityId);

        HttpHeaders headers = new HttpHeaders();
        headers.add("self", linkTo(methodOn(UniversityController.class).getModulesByUniversityId(universityId)).withSelfRel().getHref());

        return ResponseEntity.ok().headers(headers).body(modules);
    }

    @PostMapping
    public ResponseEntity<?> createUniversity(@RequestBody University university) {
        University savedUniversity = universityService.save(university);
        EntityModel<University> entityModel = assembler.toModel(savedUniversity);

        HttpHeaders headers = new HttpHeaders();
        headers.add("self", entityModel.getLink("self").orElseThrow().getHref());
        headers.add("update", linkTo(methodOn(UniversityController.class).updateUniversity(savedUniversity.getId(), savedUniversity)).withRel("update").getHref());
        headers.add("delete", linkTo(methodOn(UniversityController.class).deleteUniversity(savedUniversity.getId())).withRel("delete").getHref());

        return ResponseEntity.created(entityModel.getRequiredLink("self").toUri())
                .headers(headers)
                .body(entityModel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUniversity(@PathVariable Long id, @RequestBody University universityDetails) {
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

        HttpHeaders headers = new HttpHeaders();
        headers.add("self", entityModel.getLink("self").orElseThrow().getHref());
        headers.add("update", linkTo(methodOn(UniversityController.class).updateUniversity(id, updatedUniversity)).withRel("update").getHref());
        headers.add("delete", linkTo(methodOn(UniversityController.class).deleteUniversity(id)).withRel("delete").getHref());

        return ResponseEntity.ok().headers(headers).body(entityModel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUniversity(@PathVariable Long id) {
        universityService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
