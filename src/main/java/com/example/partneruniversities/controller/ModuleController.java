package com.example.partneruniversities.controller;

import com.example.partneruniversities.model.Module;
import com.example.partneruniversities.repository.ModuleRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

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
    public ResponseEntity<CollectionModel<EntityModel<Module>>> getAllModules() {
        List<EntityModel<Module>> modules = moduleRepository.findAll().stream()
                .map(module -> EntityModel.of(module,
                        linkTo(methodOn(ModuleController.class).getModuleById(module.getId())).withSelfRel(),
                        linkTo(methodOn(ModuleController.class).getAllModules()).withRel("modules")))
                .collect(Collectors.toList());

        HttpHeaders headers = new HttpHeaders();
        headers.add("self", linkTo(methodOn(ModuleController.class).getAllModules()).withSelfRel().getHref());

        CollectionModel<EntityModel<Module>> collectionModel = CollectionModel.of(modules, linkTo(methodOn(ModuleController.class).getAllModules()).withSelfRel());
        return ResponseEntity.ok().headers(headers).body(collectionModel);
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

        EntityModel<Module> entityModel = EntityModel.of(module,
                linkTo(methodOn(ModuleController.class).getModuleById(id)).withSelfRel(),
                linkTo(methodOn(ModuleController.class).getAllModules()).withRel("modules"));

        HttpHeaders headers = new HttpHeaders();
        headers.add("self", entityModel.getLink("self").orElseThrow().getHref());
        headers.add("update", linkTo(methodOn(ModuleController.class).updateModule(id, module)).withRel("update").getHref());
        headers.add("delete", linkTo(methodOn(ModuleController.class).deleteModule(id)).withRel("delete").getHref());

        return ResponseEntity.ok().headers(headers).body(entityModel);
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
                linkTo(methodOn(ModuleController.class).getModuleById(savedModule.getId())).withSelfRel(),
                linkTo(methodOn(ModuleController.class).getAllModules()).withRel("modules"));

        HttpHeaders headers = new HttpHeaders();
        headers.add("self", entityModel.getLink("self").orElseThrow().getHref());
        headers.add("update", linkTo(methodOn(ModuleController.class).updateModule(savedModule.getId(), savedModule)).withRel("update").getHref());
        headers.add("delete", linkTo(methodOn(ModuleController.class).deleteModule(savedModule.getId())).withRel("delete").getHref());

        return ResponseEntity.created(entityModel.getRequiredLink("self").toUri())
                .headers(headers)
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
                linkTo(methodOn(ModuleController.class).getModuleById(updatedModule.getId())).withSelfRel(),
                linkTo(methodOn(ModuleController.class).getAllModules()).withRel("modules"));

        HttpHeaders headers = new HttpHeaders();
        headers.add("self", entityModel.getLink("self").orElseThrow().getHref());
        headers.add("update", linkTo(methodOn(ModuleController.class).updateModule(id, updatedModule)).withRel("update").getHref());
        headers.add("delete", linkTo(methodOn(ModuleController.class).deleteModule(id)).withRel("delete").getHref());

        return ResponseEntity.ok().headers(headers).body(entityModel);
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
