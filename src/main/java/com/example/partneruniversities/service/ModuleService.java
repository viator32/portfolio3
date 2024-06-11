package com.example.partneruniversities.service;

import com.example.partneruniversities.model.Module;
import com.example.partneruniversities.repository.ModuleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ModuleService {

    private final ModuleRepository moduleRepository;

    public ModuleService(ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }

    public List<Module> findAll() {
        return moduleRepository.findAll();
    }

    public List<Module> getModulesByUniversityId(Long universityId) {
        return moduleRepository.findByUniversityId(universityId);
    }

    @Transactional
    public void deleteModulesByUniversityId(Long universityId) {
        moduleRepository.deleteByUniversityId(universityId);
    }

    @Transactional
    public Module save(Module module) {
        return moduleRepository.save(module);
    }

    public Module findById(Long id) {
        return moduleRepository.findById(id).orElseThrow(() -> new RuntimeException("Module not found"));
    }

    @Transactional
    public void deleteById(Long id) {
        moduleRepository.deleteById(id);
    }
}
