package com.example.partneruniversities;

import com.example.partneruniversities.client.PartnerUniversitiesClient;
import com.example.partneruniversities.model.Module;
import com.example.partneruniversities.model.University;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.EntityModel;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PartnerUniversitiesClientIntegrationTest {

    @Autowired
    private PartnerUniversitiesClient client;

    private Long createdUniversityId;
    private Long createdModuleId;
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Test
    @Order(1)
    public void testCreateUniversity() throws Exception {
        University university = new University();
        university.setName("New University");
        university.setCountry("Country");
        university.setDepartmentName("Department");
        university.setDepartmentUrl("http://example.com");
        university.setContactPerson("Contact Person");
        university.setMaxOutgoingStudents(10);
        university.setMaxIncomingStudents(10);
        university.setNextSpringSemesterStart("2023-01-15");
        university.setNextAutumnSemesterStart("2023-09-15");

        String universityJson = objectMapper.writeValueAsString(university);
        EntityModel<University> createdUniversity = client.createUniversity(universityJson);
        assertThat(createdUniversity).isNotNull();
        assertThat(Objects.requireNonNull(createdUniversity.getContent()).getId()).isNotNull();
        assertThat(createdUniversity.getContent().getId()).isEqualTo(1L);
        createdUniversityId = createdUniversity.getContent().getId();
    }

    @Test
    @Order(2)
    public void testUpdateUniversity() throws Exception {
        University university = new University();
        university.setName("Updated University");
        university.setCountry("Updated Country");
       university.setDepartmentName("Updated Department");
       university.setDepartmentUrl("http://updated.example.com");
       university.setContactPerson("Updated Contact Person");
       university.setMaxOutgoingStudents(15);
        university.setMaxIncomingStudents(15);
        university.setNextSpringSemesterStart("2023-02-01");
        university.setNextAutumnSemesterStart("2023-10-01");

        University updateUniversity = new University();
        updateUniversity.setName("Updated University");
        updateUniversity.setCountry("Updated Country");
        updateUniversity.setDepartmentName("Updated Department");
        updateUniversity.setDepartmentUrl("http://updated.example.com");
        updateUniversity.setContactPerson("Updated Contact Person");
        updateUniversity.setMaxOutgoingStudents(15);
        updateUniversity.setMaxIncomingStudents(15);
        updateUniversity.setNextSpringSemesterStart("2023-02-01");
        updateUniversity.setNextAutumnSemesterStart("2023-10-01");

        String universityJson = objectMapper.writeValueAsString(university);
        String updatedUniversityJson = objectMapper.writeValueAsString(updateUniversity);
        EntityModel<University> createdUniversity = client.createUniversity(universityJson);
        EntityModel<University> updatedUniversity = client.updateUniversity(Objects.requireNonNull(createdUniversity.getContent()).getId(), updatedUniversityJson);
        assertThat(updatedUniversity).isNotNull();
        assertThat(Objects.requireNonNull(updatedUniversity.getContent()).getName()).isEqualTo("Updated University");
    }

    @Test
    @Order(3)
    public void testGetAllUniversities() {
        List<EntityModel<University>> universities = client.getAllUniversities();
        assertThat(universities).isNotNull();
    }

    @Test
    @Order(4)
    public void testGetUniversityById() {
        EntityModel<University> university = client.getUniversityById(createdUniversityId);
        assertThat(university).isNotNull();
        assertThat(Objects.requireNonNull(university.getContent()).getId()).isEqualTo(createdUniversityId);
    }

    @Test
    @Order(5)
    public void testSearchUniversities() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "New University");
        List<EntityModel<University>> universities = client.searchUniversities(params);
        assertThat(universities).isNotNull();
    }

    @Test
    @Order(6)
    public void testCreateModule() {
        Module module = new Module();
        module.setName("New Module");
        module.setSemester(4);
        module.setCreditPoints(6);
        University refUniversity = new University();
        refUniversity.setId(createdUniversityId);
        module.setUniversity(refUniversity);

        String moduleJson = module.toString();
        EntityModel<Module> createdModule = client.createModule(moduleJson);
        assertThat(createdModule).isNotNull();
        assertThat(Objects.requireNonNull(createdModule.getContent()).getId()).isNotNull();
        createdModuleId = createdModule.getContent().getId();

    }

    @Test
    @Order(7)
    public void testUpdateModule() {
        Module module = new Module();
        module.setName("New Module Update test");
        module.setSemester(4);
        module.setCreditPoints(6);
        University refUniversity = new University();
        refUniversity.setId(createdUniversityId);
        module.setUniversity(refUniversity);

        String moduleJson = module.toString();
        EntityModel<Module> createdModule = client.createModule(moduleJson);

        Module updateModule = new Module();
        updateModule.setName("Updated Module");
        updateModule.setSemester(6);
        updateModule.setCreditPoints(8);
        refUniversity.setId(createdUniversityId);
        updateModule.setUniversity(refUniversity);

        String updateModuleJson = updateModule.toString();
        EntityModel<Module> updatedModule = client.updateModule(Objects.requireNonNull(createdModule.getContent()).getId(), updateModuleJson);
        assertThat(updatedModule).isNotNull();
        assertThat(Objects.requireNonNull(updatedModule.getContent()).getName()).isEqualTo("Updated Module");
    }

    @Test
    @Order(8)
    public void testGetAllModules() {
        List<EntityModel<Module>> modules = client.getAllModules();
        assertThat(modules).isNotNull();
    }

    @Test
    @Order(9)
    public void testGetModuleById() {
        EntityModel<Module> module = client.getModuleById(createdModuleId);
        assertThat(module).isNotNull();
        assertThat(Objects.requireNonNull(module.getContent()).getId()).isEqualTo(createdModuleId);
    }

    @Test
    @Order(10)
    public void testDeleteUniversity() throws Exception {
        University university = new University();
        university.setName("University to Delete");
        university.setCountry("Country");
        university.setDepartmentName("Department");
        university.setDepartmentUrl("http://example.com");
        university.setContactPerson("Contact Person");
        university.setMaxOutgoingStudents(10);
        university.setMaxIncomingStudents(10);
        university.setNextSpringSemesterStart("2023-01-15");
        university.setNextAutumnSemesterStart("2023-09-15");

        String universityJson = objectMapper.writeValueAsString(university);
        EntityModel<University> createdUniversity = client.createUniversity(universityJson);
        Long idToDelete = Objects.requireNonNull(createdUniversity.getContent()).getId();
        client.deleteUniversity(idToDelete);

        // Verify deletion
        try {
            client.getUniversityById(idToDelete);
            Assertions.fail("Expected a RuntimeException to be thrown");
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).contains("404");
        }
    }

    @Test
    @Order(11)
    public void testDeleteModule() {
        Module module = new Module();
        module.setName("Module to Delete");
        module.setSemester(4);
        module.setCreditPoints(6);
        University refUniversity = new University();
        refUniversity.setId(createdUniversityId);
        module.setUniversity(refUniversity);

        String moduleJson = module.toString();
        EntityModel<Module> createdModule = client.createModule(moduleJson);
        Long idToDelete = Objects.requireNonNull(createdModule.getContent()).getId();
        client.deleteModule(idToDelete);

        // Verify deletion
        try {
            client.getModuleById(idToDelete);
            Assertions.fail("Expected a RuntimeException to be thrown");
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).contains("404");
        }
    }

    @Test
    @Order(12)
    public void testGetModulesByUniversityId() {
        List<EntityModel<Module>> modules = client.getModulesByUniversityId(createdUniversityId);
        assertThat(modules).isNotNull();
    }
}
