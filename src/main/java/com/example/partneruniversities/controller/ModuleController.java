package com.example.partneruniversities.controller;

import com.example.partneruniversities.model.Module;
import com.example.partneruniversities.repository.ModuleRepository;
import com.example.partneruniversities.repository.UniversityRepository;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/modules")
public class ModuleController {

    private final ModuleRepository moduleRepository;
    private final UniversityRepository universityRepository;

    public ModuleController(ModuleRepository moduleRepository, UniversityRepository universityRepository) {
        this.moduleRepository = moduleRepository;
        this.universityRepository = universityRepository;
    }

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

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Module>> getModuleById(@PathVariable Long id) {
        Module module = moduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Module not found"));

        EntityModel<Module> entityModel = EntityModel.of(module,
                linkTo(methodOn(ModuleController.class).getModuleById(id)).withSelfRel(),
                linkTo(methodOn(ModuleController.class).getAllModules()).withRel("modules"));

        return ResponseEntity.ok().body(entityModel);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EntityModel<Module>> createModule(@Valid @RequestBody Module module) {
        if (module.getUniversity() == null || !universityRepository.existsById(module.getUniversity().getId())) {
            return ResponseEntity.badRequest().body(EntityModel.of(module,
                    linkTo(methodOn(ModuleController.class).getAllModules()).withRel("modules")));
        }
        Module savedModule = moduleRepository.save(module);
        EntityModel<Module> entityModel = EntityModel.of(savedModule,
                linkTo(methodOn(ModuleController.class).getModuleById(savedModule.getId())).withSelfRel(),
                linkTo(methodOn(ModuleController.class).getAllModules()).withRel("modules"));

        return ResponseEntity.created(entityModel.getRequiredLink("self").toUri()).body(entityModel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Module>> updateModule(@PathVariable Long id, @Valid @RequestBody Module moduleDetails) {
        if (moduleDetails.getUniversity() == null || !universityRepository.existsById(moduleDetails.getUniversity().getId())) {
            return ResponseEntity.badRequest().body(EntityModel.of(moduleDetails,
                    linkTo(methodOn(ModuleController.class).getAllModules()).withRel("modules")));
        }

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

        return ResponseEntity.ok().body(entityModel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteModule(@PathVariable Long id) {
        moduleRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
