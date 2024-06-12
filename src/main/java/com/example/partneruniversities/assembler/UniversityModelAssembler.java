package com.example.partneruniversities.assembler;

import com.example.partneruniversities.controller.UniversityController;
import com.example.partneruniversities.model.University;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class UniversityModelAssembler implements RepresentationModelAssembler<University, EntityModel<University>> {

    @Override
    public @NotNull EntityModel<University> toModel(@NotNull University university) {
        // Create the self link for the university
        EntityModel<University> universityModel = EntityModel.of(university,
                linkTo(methodOn(UniversityController.class).getUniversityById(university.getId())).withSelfRel());

        // Add the department URL as a link
        universityModel.add(Link.of(university.getDepartmentUrl(), "departmentUrl"));

        // Add link to the modules related to this university
        universityModel.add(linkTo(methodOn(UniversityController.class).getModulesByUniversityId(university.getId())).withRel("modules"));

        return universityModel;
    }

    @Override
    public @NotNull CollectionModel<EntityModel<University>> toCollectionModel(@NotNull Iterable<? extends University> entities) {
        CollectionModel<EntityModel<University>> universityModels = RepresentationModelAssembler.super.toCollectionModel(entities);

        universityModels.add(linkTo(methodOn(UniversityController.class).getAllUniversities()).withSelfRel());
        universityModels.add(linkTo(methodOn(UniversityController.class).searchUniversities("", "", "", 0, 10, "name", "asc")).withRel("search"));

        return universityModels;
    }
}
