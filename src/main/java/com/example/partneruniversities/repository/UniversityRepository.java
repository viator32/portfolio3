package com.example.partneruniversities.repository;

import com.example.partneruniversities.model.University;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UniversityRepository extends JpaRepository<University, Long> {
    Page<University> findByNameContainingAndCountryContainingAndDepartmentNameContaining(String name, String country, String departmentName, Pageable pageable);
}
