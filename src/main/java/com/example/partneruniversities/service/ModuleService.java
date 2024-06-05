package com.example.partneruniversities.service;

import com.example.partneruniversities.model.Module;
import com.example.partneruniversities.repository.ModuleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service class for managing Module entities.
 * Provides methods for CRUD operations and business logic.
 */
@Service
public class ModuleService {

    private final ModuleRepository moduleRepository;

    public ModuleService(ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }

    public Page<Module> findAll(Pageable pageable) {
        return moduleRepository.findAll(pageable);
    }

    public Module findById(Long id) {
        return moduleRepository.findById(id).orElse(null);
    }

    public Module save(Module module) {
        return moduleRepository.save(module);
    }

    public Module update(Long id, Module moduleDetails) {
        Module module = moduleRepository.findById(id).orElse(null);
        if (module != null) {
            module.setName(moduleDetails.getName());
            module.setSemester(moduleDetails.getSemester());
            module.setCreditPoints(moduleDetails.getCreditPoints());
            return moduleRepository.save(module);
        } else {
            return null;
        }
    }

    public void deleteById(Long id) {
        moduleRepository.deleteById(id);
    }
}
