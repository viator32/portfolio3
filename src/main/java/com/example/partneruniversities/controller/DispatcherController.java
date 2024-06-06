package com.example.partneruniversities.controller;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class DispatcherController {

    @GetMapping
    public RepresentationModel<?> getDispatcherLinks() {
        RepresentationModel<?> model = new RepresentationModel<>();

        model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UniversityController.class).getAllUniversities()).withRel("universities"));
        model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ModuleController.class).getAllModules()).withRel("modules"));

        return model;
    }
}
