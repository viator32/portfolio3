package com.example.partneruniversities.service;

import com.example.partneruniversities.repository.ModuleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ModuleService {

    private final ModuleRepository moduleRepository;

    public ModuleService(ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }

    @Transactional
    public void deleteByUniversityId(Long universityId) {
        moduleRepository.deleteByUniversityId(universityId);
    }
}
