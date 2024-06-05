package com.example.partneruniversities.controller;

import com.example.partneruniversities.assembler.ModuleModelAssembler;
import com.example.partneruniversities.model.Module;
import com.example.partneruniversities.service.ModuleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

/**
 * REST controller for managing Module entities.
 * Provides endpoints for CRUD operations and HATEOAS links.
 */
@RestController
@RequestMapping("/modules")
public class ModuleController {

    private final ModuleService moduleService;
    private final ModuleModelAssembler assembler;

    public ModuleController(ModuleService moduleService, ModuleModelAssembler assembler) {
        this.moduleService = moduleService;
        this.assembler = assembler;
    }

    /**
     * GET /modules : Get all modules with pagination.
     *
     * @param pageable        pagination information
     * @param pagedAssembler  assembler for pagination
     * @return PagedModel of modules
     */
    @GetMapping
    public PagedModel<EntityModel<Module>> getAllModules(Pageable pageable, PagedResourcesAssembler<Module> pagedAssembler) {
        Page<Module> modules = moduleService.findAll(pageable);
        return pagedAssembler.toModel(modules, assembler);
    }

    /**
     * GET /modules/{id} : Get a module by ID.
     *
     * @param id the ID of the module to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the module, or with status 404 (Not Found)
     */
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Module>> getModuleById(@PathVariable Long id) {
        Module module = moduleService.findById(id);
        return module != null
                ? ResponseEntity.ok(assembler.toModel(module))
                : ResponseEntity.notFound().build();
    }

    /**
     * POST /modules : Create a new module.
     *
     * @param module the module to create
     * @return the ResponseEntity with status 201 (Created) and with body the new module, or with status 400 (Bad Request) if the module has already an ID
     */
    @PostMapping
    public ResponseEntity<EntityModel<Module>> createModule(@RequestBody Module module) {
        Module savedModule = moduleService.save(module);
        return ResponseEntity.created(linkTo(methodOn(ModuleController.class).getModuleById(savedModule.getId())).toUri())
                .body(assembler.toModel(savedModule));
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
        Module updatedModule = moduleService.update(id, moduleDetails);
        return updatedModule != null
                ? ResponseEntity.ok(assembler.toModel(updatedModule))
                : ResponseEntity.notFound().build();
    }

    /**
     * DELETE /modules/{id} : Delete a module by ID.
     *
     * @param id the ID of the module to delete
     * @return the ResponseEntity with status 204 (NO_CONTENT)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteModule(@PathVariable Long id) {
        moduleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
