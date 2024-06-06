package com.example.partneruniversities.service;

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

    public University save(University university) {
        return universityRepository.save(university);
    }

    @Transactional
    public void deleteById(Long id) {
        moduleService.deleteByUniversityId(id);
        universityRepository.deleteById(id);
    }
}
