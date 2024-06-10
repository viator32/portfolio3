package com.example.partneruniversities.assembler;

import com.example.partneruniversities.controller.ModuleController;
import com.example.partneruniversities.controller.UniversityController;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class DispatcherAssembler {

    public RepresentationModel<?> createDispatcherLinks() {
        RepresentationModel<?> dispatcher = new RepresentationModel<>();
        dispatcher.add(linkTo(methodOn(UniversityController.class).getAllUniversities()).withRel("universities"));
        dispatcher.add(linkTo(methodOn(ModuleController.class).getAllModules()).withRel("modules"));
        dispatcher.add(linkTo(methodOn(UniversityController.class).searchUniversities("", "", "", 0, 10, "name", "asc")).withRel("search"));
        return dispatcher;
    }

}
