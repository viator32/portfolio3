package com.example.partneruniversities;

import com.example.partneruniversities.client.PartnerUniversitiesClient;
import com.example.partneruniversities.model.Module;
import com.example.partneruniversities.model.University;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpClientErrorException;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
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

        // Log the university before sending
        System.out.println("Creating university with data: " + university);

        University createdUniversity = client.createUniversity(university);
        assertNotNull(createdUniversity);
        assertNotNull(createdUniversity.getId());

        Module module = new Module();
        module.setName("Test Module");
        module.setSemester(1);
        module.setCreditPoints(5);
        module.setUniversity(createdUniversity);

        // Log the module before sending
        System.out.println("Creating module with data: " + module);

        Module createdModule = null;
        try {
            createdModule = client.createModule(module);
        } catch (HttpClientErrorException e) {
            System.err.println("Error creating module: " + e.getMessage());
            System.err.println("Response body: " + e.getResponseBodyAsString());
            throw e;
        }

        assertNotNull(createdModule);
        assertNotNull(createdModule.getId());

        // Additional assertions to verify the module data
        assertEquals("Test Module", createdModule.getName());
        assertEquals(1, createdModule.getSemester());
        assertEquals(5, createdModule.getCreditPoints());
        assertNotNull(createdModule.getUniversity());
        assertEquals(createdUniversity.getId(), createdModule.getUniversity().getId());

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
        assertNotNull(updatedModule); // Ensure update was successful
        assertEquals("Updated Test Module", updatedModule.getName());

        client.deleteModule(createdModule.getId());
        client.deleteUniversity(createdUniversity.getId());
    }

    @Test
    void testCreateModuleWithoutUniversity() {
        Module module = new Module();
        module.setName("Test Module");
        module.setSemester(1);
        module.setCreditPoints(5);
        // Do not set the university to simulate the error

        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> client.createModule(module));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
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
