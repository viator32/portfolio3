package com.example.partneruniversities.repository;

import com.example.partneruniversities.model.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ModuleRepository extends JpaRepository<Module, Long> {

    List<Module> findByUniversityId(Long universityId);

    @Modifying
    @Query("DELETE FROM Module m WHERE m.university.id = :universityId")
    void deleteByUniversityId(Long universityId);
}
