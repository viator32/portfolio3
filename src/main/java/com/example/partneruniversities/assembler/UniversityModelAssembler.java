package com.example.partneruniversities.assembler;

import com.example.partneruniversities.controller.UniversityController;
import com.example.partneruniversities.model.University;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

/**
 * Assembler class to convert University entities into HATEOAS-compliant models.
 */
@Component
public class UniversityModelAssembler implements RepresentationModelAssembler<University, EntityModel<University>> {

    @Override
    public EntityModel<University> toModel(University university) {
        // Create the self link for the university
        EntityModel<University> universityModel = EntityModel.of(university,
                linkTo(methodOn(UniversityController.class).getUniversityById(university.getId())).withSelfRel(),
                linkTo(methodOn(UniversityController.class).getAllUniversities(null, null)).withRel("universities"));

        // Add the department URL as a link
        universityModel.add(Link.of(university.getDepartmentUrl(), "departmentUrl"));

        return universityModel;
    }

    @Override
    public CollectionModel<EntityModel<University>> toCollectionModel(Iterable<? extends University> entities) {
        // Convert each University to an EntityModel and add to the collection model
        CollectionModel<EntityModel<University>> universityModels = RepresentationModelAssembler.super.toCollectionModel(entities);

        // Add a self link to the collection model
        universityModels.add(linkTo(methodOn(UniversityController.class).getAllUniversities(null, null)).withSelfRel());

        return universityModels;
    }
}
