package com.example.partneruniversities.repository;

import com.example.partneruniversities.model.University;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for University entities.
 * Extends JpaRepository to provide CRUD operations and pagination support.
 */
public interface UniversityRepository extends JpaRepository<University, Long> {
}
