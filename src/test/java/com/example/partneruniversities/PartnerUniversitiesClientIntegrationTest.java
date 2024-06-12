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

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PartnerUniversitiesClientIntegrationTest {

    @Autowired
    private PartnerUniversitiesClient client;

    private Long createdUniversityId;
    private Long createdModuleId;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    public void setUp() throws Exception {
        // Create a test university
        University university = new University();
        university.setName("Test University");
        university.setCountry("Test Country");
        university.setDepartmentName("Test Department");
        university.setDepartmentUrl("http://test.example.com");
        university.setContactPerson("Test Contact Person");
        university.setMaxOutgoingStudents(10);
        university.setMaxIncomingStudents(10);
        university.setNextSpringSemesterStart("2023-01-15");
        university.setNextAutumnSemesterStart("2023-09-15");

        String universityJson = objectMapper.writeValueAsString(university);
        EntityModel<University> createdUniversity = client.createUniversity(universityJson);
        this.createdUniversityId = createdUniversity.getContent().getId();

        // Create a test module
        Module module = new Module();
        module.setName("Test Module");
        module.setSemester(5);
        module.setCreditPoints(5);
        University refUniversity = new University();
        refUniversity.setId(createdUniversityId);
        module.setUniversity(refUniversity);

        String moduleJson = module.toString();
        EntityModel<Module> createdModule = client.createModule(moduleJson);
        this.createdModuleId = createdModule.getContent().getId();
    }

    @AfterAll
    public void tearDown() {
        // Clean up test module
        if (createdModuleId != null) {
            client.deleteModule(createdModuleId);
        }

        // Clean up test university
        if (createdUniversityId != null) {
            client.deleteUniversity(createdUniversityId);
        }
    }

    @Test
    public void testGetAllUniversities() {
        List<EntityModel<University>> universities = client.getAllUniversities();
        assertThat(universities).isNotNull();
        assertThat(universities.size()).isGreaterThan(0);
    }

    @Test
    public void testGetUniversityById() {
        EntityModel<University> university = client.getUniversityById(createdUniversityId);
        assertThat(university).isNotNull();
        assertThat(university.getContent().getId()).isEqualTo(createdUniversityId);
    }

    @Test
    public void testSearchUniversities() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "Test University");
        List<EntityModel<University>> universities = client.searchUniversities(params);
        assertThat(universities).isNotNull();
        assertThat(universities.size()).isGreaterThan(0);
    }

    @Test
    public void testGetAllModules() {
        List<EntityModel<Module>> modules = client.getAllModules();
        assertThat(modules).isNotNull();
        assertThat(modules.size()).isGreaterThan(0);
    }

    @Test
    public void testGetModuleById() {
        EntityModel<Module> module = client.getModuleById(createdModuleId);
        assertThat(module).isNotNull();
        assertThat(module.getContent().getId()).isEqualTo(createdModuleId);
    }

    @Test
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
        assertThat(createdUniversity.getContent().getId()).isNotNull();

        // Clean up created university
        client.deleteUniversity(createdUniversity.getContent().getId());
    }

    @Test
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

        String universityJson = objectMapper.writeValueAsString(university);
        EntityModel<University> updatedUniversity = client.updateUniversity(createdUniversityId, universityJson);
        assertThat(updatedUniversity).isNotNull();
        assertThat(updatedUniversity.getContent().getName()).isEqualTo("Updated University");
    }

    @Test
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
        Long idToDelete = createdUniversity.getContent().getId();
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
    public void testCreateModule() throws Exception {
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
        assertThat(createdModule.getContent().getId()).isNotNull();

        // Clean up created module
        client.deleteModule(createdModule.getContent().getId());
    }

    @Test
    public void testDeleteModule() throws Exception {
        Module module = new Module();
        module.setName("Module to Delete");
        module.setSemester(4);
        module.setCreditPoints(6);
        University refUniversity = new University();
        refUniversity.setId(createdUniversityId);
        module.setUniversity(refUniversity);

        String moduleJson = module.toString();
        EntityModel<Module> createdModule = client.createModule(moduleJson);
        Long idToDelete = createdModule.getContent().getId();
        client.deleteModule(idToDelete);

        // Verify deletion
        try {
            client.getModuleById(idToDelete);
            Assertions.fail("Expected a RuntimeException to be thrown");
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).contains("Module not found");
        }
    }

    @Test
    public void testGetModulesByUniversityId() {
        List<EntityModel<Module>> modules = client.getModulesByUniversityId(createdUniversityId);
        assertThat(modules).isNotNull();
        assertThat(modules.size()).isGreaterThan(0);
    }
}
