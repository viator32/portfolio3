package com.example.partneruniversities.assembler;

import com.example.partneruniversities.controller.ModuleController;
import com.example.partneruniversities.model.Module;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

/**
 * Assembler class to convert Module entities into HATEOAS-compliant models.
 */
@Component
public class ModuleModelAssembler implements RepresentationModelAssembler<Module, EntityModel<Module>> {


    @Override
    public EntityModel<Module> toModel(Module module) {
        return EntityModel.of(module,
                linkTo(methodOn(ModuleController.class).getModuleById(module.getId())).withSelfRel(),
                linkTo(methodOn(ModuleController.class).getAllModules()).withRel("modules"));
    }

    @Override
    public CollectionModel<EntityModel<Module>> toCollectionModel(Iterable<? extends Module> entities) {
        // Convert each Module to an EntityModel and add to the collection model
        CollectionModel<EntityModel<Module>> moduleModels = RepresentationModelAssembler.super.toCollectionModel(entities);

        // Add a self link to the collection model
        moduleModels.add(linkTo(methodOn(ModuleController.class).getAllModules()).withSelfRel());

        return moduleModels;
    }
}
