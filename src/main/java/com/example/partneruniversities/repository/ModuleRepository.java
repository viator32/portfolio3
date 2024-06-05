package com.example.partneruniversities.repository;

import com.example.partneruniversities.model.Module;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for Module entities.
 * Extends JpaRepository to provide CRUD operations and pagination support.
 */
public interface ModuleRepository extends JpaRepository<Module, Long> {
}
