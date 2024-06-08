package com.example.partneruniversities;

import com.example.partneruniversities.client.PartnerUniversitiesClient;
import com.example.partneruniversities.model.Module;
import com.example.partneruniversities.model.University;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
class PartnerUniversitiesClientIntegrationTest {

    private PartnerUniversitiesClient client;

    @BeforeEach
    void setUp() {
        client = new PartnerUniversitiesClient();
    }

    @Test
    void testCreateAndDeleteUniversity() {
        University university = new University();
        university.setName("Test University");
        university.setCountry("Test Country");
        university.setDepartmentName("Test Department");
        university.setDepartmentUrl("http://test.department.url");
        university.setContactPerson("Test Person");
        university.setMaxIncomingStudents(100);
        university.setMaxOutgoingStudents(50);
        university.setNextSpringSemesterStart("2025-01-01");
        university.setNextAutumnSemesterStart("2025-09-01");

        University createdUniversity = client.createUniversity(university);
        assertNotNull(createdUniversity);
        assertNotNull(createdUniversity.getId());

        client.deleteUniversity(createdUniversity.getId());
    }

    @Test
    void testCreateAndDeleteModule() {
        University university = new University();
        university.setName("Test University");
        university.setCountry("Test Country");
        university.setDepartmentName("Test Department");
        university.setDepartmentUrl("http://test.department.url");
        university.setContactPerson("Test Person");
        university.setMaxIncomingStudents(100);
        university.setMaxOutgoingStudents(50);
        university.setNextSpringSemesterStart("2025-01-01");
        university.setNextAutumnSemesterStart("2025-09-01");

        University createdUniversity = client.createUniversity(university);

        Module module = new Module();
        module.setName("Test Module");
        module.setSemester(1);
        module.setCreditPoints(5);
        module.setUniversity(createdUniversity);

        Module createdModule = client.createModule(module);
        assertNotNull(createdModule);
        assertNotNull(createdModule.getId());

        client.deleteModule(createdModule.getId());
        client.deleteUniversity(createdUniversity.getId());
    }

    @Test
    void testUpdateUniversity() {
        University university = new University();
        university.setName("Test University");
        university.setCountry("Test Country");
        university.setDepartmentName("Test Department");
        university.setDepartmentUrl("http://test.department.url");
        university.setContactPerson("Test Person");
        university.setMaxIncomingStudents(100);
        university.setMaxOutgoingStudents(50);
        university.setNextSpringSemesterStart("2025-01-01");
        university.setNextAutumnSemesterStart("2025-09-01");

        University createdUniversity = client.createUniversity(university);
        createdUniversity.setName("Updated Test University");

        University updatedUniversity = client.updateUniversity(createdUniversity.getId(), createdUniversity);
        assertEquals("Updated Test University", updatedUniversity.getName());

        client.deleteUniversity(createdUniversity.getId());
    }

    @Test
    void testUpdateModule() {
        University university = new University();
        university.setName("Test University");
        university.setCountry("Test Country");
        university.setDepartmentName("Test Department");
        university.setDepartmentUrl("http://test.department.url");
        university.setContactPerson("Test Person");
        university.setMaxIncomingStudents(100);
        university.setMaxOutgoingStudents(50);
        university.setNextSpringSemesterStart("2025-01-01");
        university.setNextAutumnSemesterStart("2025-09-01");

        University createdUniversity = client.createUniversity(university);

        Module module = new Module();
        module.setName("Test Module");
        module.setSemester(1);
        module.setCreditPoints(5);
        module.setUniversity(createdUniversity);

        Module createdModule = client.createModule(module);
        createdModule.setName("Updated Test Module");

        Module updatedModule = client.updateModule(createdModule.getId(), createdModule);
        assertEquals("Updated Test Module", updatedModule.getName());

        client.deleteModule(createdModule.getId());
        client.deleteUniversity(createdUniversity.getId());
    }

    @Test
    void testGetUniversityById() {
        University university = new University();
        university.setName("Test University");
        university.setCountry("Test Country");
        university.setDepartmentName("Test Department");
        university.setDepartmentUrl("http://test.department.url");
        university.setContactPerson("Test Person");
        university.setMaxIncomingStudents(100);
        university.setMaxOutgoingStudents(50);
        university.setNextSpringSemesterStart("2025-01-01");
        university.setNextAutumnSemesterStart("2025-09-01");

        University createdUniversity = client.createUniversity(university);

        University fetchedUniversity = client.getUniversityById(createdUniversity.getId());
        assertNotNull(fetchedUniversity);
        assertEquals("Test University", fetchedUniversity.getName());

        client.deleteUniversity(createdUniversity.getId());
    }

    @Test
    void testGetModuleById() {
        University university = new University();
        university.setName("Test University");
        university.setCountry("Test Country");
        university.setDepartmentName("Test Department");
        university.setDepartmentUrl("http://test.department.url");
        university.setContactPerson("Test Person");
        university.setMaxIncomingStudents(100);
        university.setMaxOutgoingStudents(50);
        university.setNextSpringSemesterStart("2025-01-01");
        university.setNextAutumnSemesterStart("2025-09-01");

        University createdUniversity = client.createUniversity(university);

        Module module = new Module();
        module.setName("Test Module");
        module.setSemester(1);
        module.setCreditPoints(5);
        module.setUniversity(createdUniversity);

        Module createdModule = client.createModule(module);

        Module fetchedModule = client.getModuleById(createdModule.getId());
        assertNotNull(fetchedModule);
        assertEquals("Test Module", fetchedModule.getName());

        client.deleteModule(createdModule.getId());
        client.deleteUniversity(createdUniversity.getId());
    }
}
