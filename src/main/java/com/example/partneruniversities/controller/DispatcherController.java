package com.example.partneruniversities.controller;

import com.example.partneruniversities.assembler.DispatcherAssembler;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class DispatcherController {

    private final DispatcherAssembler dispatcherAssembler;

    public DispatcherController(DispatcherAssembler dispatcherAssembler) {
        this.dispatcherAssembler = dispatcherAssembler;
    }

    @GetMapping
    public RepresentationModel<?> getDispatcherLinks() {
        return dispatcherAssembler.createDispatcherLinks();
    }
}
