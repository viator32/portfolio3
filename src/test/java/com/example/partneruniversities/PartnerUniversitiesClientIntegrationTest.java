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

import java.util.List;

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

        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> {
            client.createModule(module);
        });

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

    @Test
    void testSearchUniversities() {
        // Creating some sample universities
        University university1 = new University();
        university1.setName("Alpha University");
        university1.setCountry("Country A");
        university1.setDepartmentName("Department A");
        university1.setDepartmentUrl("http://alpha.university.url");
        university1.setContactPerson("Alpha Person");
        university1.setMaxIncomingStudents(100);
        university1.setMaxOutgoingStudents(50);
        university1.setNextSpringSemesterStart("2025-01-01");
        university1.setNextAutumnSemesterStart("2025-09-01");

        University university2 = new University();
        university2.setName("Beta University");
        university2.setCountry("Country B");
        university2.setDepartmentName("Department B");
        university2.setDepartmentUrl("http://beta.university.url");
        university2.setContactPerson("Beta Person");
        university2.setMaxIncomingStudents(150);
        university2.setMaxOutgoingStudents(60);
        university2.setNextSpringSemesterStart("2025-02-01");
        university2.setNextAutumnSemesterStart("2025-10-01");

        University createdUniversity1 = client.createUniversity(university1);
        University createdUniversity2 = client.createUniversity(university2);

        // Performing the search
        List<University> universities = client.searchUniversities("University", "", "", 0, 10, "name", "asc");
        assertNotNull(universities);
        assertFalse(universities.isEmpty());
        assertTrue(universities.stream().anyMatch(u -> u.getName().equals("Alpha University")));
        assertTrue(universities.stream().anyMatch(u -> u.getName().equals("Beta University")));

        client.deleteUniversity(createdUniversity1.getId());
        client.deleteUniversity(createdUniversity2.getId());
    }
}
