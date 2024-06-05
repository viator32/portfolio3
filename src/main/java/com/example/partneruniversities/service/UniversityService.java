package com.example.partneruniversities.service;

import com.example.partneruniversities.model.University;
import com.example.partneruniversities.repository.UniversityRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service class for managing University entities.
 * Provides methods for CRUD operations and business logic.
 */
@Service
public class UniversityService {

    private final UniversityRepository universityRepository;

    public UniversityService(UniversityRepository universityRepository) {
        this.universityRepository = universityRepository;
    }

    public Page<University> findAll(Pageable pageable) {
        return universityRepository.findAll(pageable);
    }

    public University findById(Long id) {
        return universityRepository.findById(id).orElse(null);
    }

    public University save(University university) {
        return universityRepository.save(university);
    }

    public University update(Long id, University universityDetails) {
        University university = universityRepository.findById(id).orElse(null);
        if (university != null) {
            university.setName(universityDetails.getName());
            university.setCountry(universityDetails.getCountry());
            university.setDepartmentName(universityDetails.getDepartmentName());
            university.setDepartmentUrl(universityDetails.getDepartmentUrl());
            university.setContactPerson(universityDetails.getContactPerson());
            university.setMaxOutgoingStudents(universityDetails.getMaxOutgoingStudents());
            university.setMaxIncomingStudents(universityDetails.getMaxIncomingStudents());
            university.setNextSpringSemesterStart(universityDetails.getNextSpringSemesterStart());
            university.setNextAutumnSemesterStart(universityDetails.getNextAutumnSemesterStart());
            return universityRepository.save(university);
        } else {
            return null;
        }
    }

    public void deleteById(Long id) {
        universityRepository.deleteById(id);
    }
}
