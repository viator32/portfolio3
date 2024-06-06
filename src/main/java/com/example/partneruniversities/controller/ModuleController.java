package com.example.partneruniversities.controller;

import com.example.partneruniversities.model.Module;
import com.example.partneruniversities.repository.ModuleRepository;
import com.example.partneruniversities.service.ModuleService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * REST controller for managing Module entities.
 * Provides endpoints for CRUD operations and HATEOAS links.
 */
@RestController
@RequestMapping("/modules")
public class ModuleController {

    private final ModuleRepository moduleRepository;

    public ModuleController(ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }

    /**
     * GET /modules : Get all modules.
     *
     * @return the CollectionModel of modules
     */
    @GetMapping
    public CollectionModel<EntityModel<Module>> getAllModules() {
        List<EntityModel<Module>> modules = moduleRepository.findAll().stream()
                .map(module -> EntityModel.of(module,
                        WebMvcLinkBuilder.linkTo(methodOn(ModuleController.class).getModuleById(module.getId())).withSelfRel(),
                        WebMvcLinkBuilder.linkTo(methodOn(ModuleController.class).getAllModules()).withRel("modules")))
                .collect(Collectors.toList());

        return CollectionModel.of(modules, WebMvcLinkBuilder.linkTo(methodOn(ModuleController.class).getAllModules()).withSelfRel());
    }

    /**
     * GET /modules/{id} : Get a module by ID.
     *
     * @param id the ID of the module to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the module, or with status 404 (Not Found)
     */
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Module>> getModuleById(@PathVariable Long id) {
        Module module = moduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Module not found"));

        return ResponseEntity.ok(EntityModel.of(module,
                WebMvcLinkBuilder.linkTo(methodOn(ModuleController.class).getModuleById(id)).withSelfRel(),
                WebMvcLinkBuilder.linkTo(methodOn(ModuleController.class).getAllModules()).withRel("modules")));
    }

    /**
     * POST /modules : Create a new module.
     *
     * @param module the module to create
     * @return the ResponseEntity with status 201 (Created) and with body the new module
     */
    @PostMapping
    public ResponseEntity<EntityModel<Module>> createModule(@RequestBody Module module) {
        Module savedModule = moduleRepository.save(module);
        EntityModel<Module> entityModel = EntityModel.of(savedModule,
                WebMvcLinkBuilder.linkTo(methodOn(ModuleController.class).getModuleById(savedModule.getId())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(methodOn(ModuleController.class).getAllModules()).withRel("modules"));

        return ResponseEntity.created(entityModel.getRequiredLink("self").toUri())
                .body(entityModel);
    }

    /**
     * PUT /modules/{id} : Update an existing module.
     *
     * @param id the ID of the module to update
     * @param moduleDetails the module to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated module, or with status 404 (Not Found)
     */
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Module>> updateModule(@PathVariable Long id, @RequestBody Module moduleDetails) {
        Module updatedModule = moduleRepository.findById(id)
                .map(module -> {
                    module.setName(moduleDetails.getName());
                    module.setSemester(moduleDetails.getSemester());
                    module.setCreditPoints(moduleDetails.getCreditPoints());
                    module.setUniversity(moduleDetails.getUniversity());
                    return moduleRepository.save(module);
                })
                .orElseGet(() -> {
                    moduleDetails.setId(id);
                    return moduleRepository.save(moduleDetails);
                });

        EntityModel<Module> entityModel = EntityModel.of(updatedModule,
                WebMvcLinkBuilder.linkTo(methodOn(ModuleController.class).getModuleById(updatedModule.getId())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(methodOn(ModuleController.class).getAllModules()).withRel("modules"));

        return ResponseEntity.ok(entityModel);
    }

    /**
     * DELETE /modules/{id} : Delete a module by ID.
     *
     * @param id the ID of the module to delete
     * @return the ResponseEntity with status 204 (NO_CONTENT)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteModule(@PathVariable Long id) {
        moduleRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
