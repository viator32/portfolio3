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

    public List<Module> findByUniversityId(Long universityId) {
        return moduleRepository.findByUniversityId(universityId);
    }

    @Transactional
    public void deleteModulesByUniversityId(Long universityId) {
        moduleRepository.deleteByUniversityId(universityId);
    }
}
