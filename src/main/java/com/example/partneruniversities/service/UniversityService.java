package com.example.partneruniversities.service;

import com.example.partneruniversities.model.Module;
import com.example.partneruniversities.model.University;
import com.example.partneruniversities.repository.UniversityRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class UniversityService {

    private final UniversityRepository universityRepository;
    private final ModuleService moduleService;

    public UniversityService(UniversityRepository universityRepository, ModuleService moduleService) {
        this.universityRepository = universityRepository;
        this.moduleService = moduleService;
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

    public Page<University> searchUniversities(String name, String country, String departmentName, int page, int size, String sortBy, String direction) {
        Pageable pageable = PageRequest.of(page, size, direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        return universityRepository.findByNameContainingAndCountryContainingAndDepartmentNameContaining(name, country, departmentName, pageable);
    }
}
