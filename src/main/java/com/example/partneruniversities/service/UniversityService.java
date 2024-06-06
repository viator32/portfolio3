package com.example.partneruniversities.service;

import com.example.partneruniversities.model.Module;
import com.example.partneruniversities.model.University;
import com.example.partneruniversities.repository.UniversityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UniversityService {

    private final UniversityRepository universityRepository;
    private final ModuleService moduleService;

    public UniversityService(UniversityRepository universityRepository, ModuleService moduleService) {
        this.universityRepository = universityRepository;
        this.moduleService = moduleService;
    }

    public List<University> findAll() {
        return universityRepository.findAll();
    }

    public Optional<University> findById(Long id) {
        return universityRepository.findById(id);
    }

    @Transactional
    public University save(University university) {
        // Ensure all modules are properly linked to the university
        for (Module module : university.getModules()) {
            module.setUniversity(university);
        }
        return universityRepository.save(university);
    }

    @Transactional
    public void deleteById(Long id) {
        // Delete associated modules first to avoid constraint violations
        moduleService.deleteModulesByUniversityId(id);
        universityRepository.deleteById(id);
    }

    public List<Module> getModulesByUniversityId(Long universityId) {
        return moduleService.findByUniversityId(universityId);
    }
}
