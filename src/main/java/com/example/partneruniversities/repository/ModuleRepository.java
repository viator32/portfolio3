package com.example.partneruniversities.repository;

import com.example.partneruniversities.model.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {
    void deleteByUniversityId(Long universityId);
}
