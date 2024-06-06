package com.example.partneruniversities;

import com.example.partneruniversities.client.PartnerUniversitiesClient;
import com.example.partneruniversities.model.University;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(OrderAnnotation.class)
public class UniversityControllerTest {

    @Autowired
    private PartnerUniversitiesClient client;

    @BeforeEach
    public void setUp() {
        client.setPort(8080);  // Ensure it points to the Docker container port
    }

    @Test
    @Order(1)
    public void testCreateUniversity() {
        University university = new University();
        university.setName("Test University");
        university.setCountry("Test Country");
        university.setDepartmentName("Test Department");
        university.setDepartmentUrl("http://test.com");
        university.setContactPerson("Dr. Test");
        university.setMaxOutgoingStudents(5);
        university.setMaxIncomingStudents(3);
        university.setNextSpringSemesterStart("2025-01-01");
        university.setNextAutumnSemesterStart("2024-09-01");

        EntityModel<University> response = client.createUniversity(university);
        assertThat(response).isNotNull();
        assertThat(response.getContent().getName()).isEqualTo("Test University");
    }

    @Test
    @Order(2)
    public void testCreateUniversity2() {
        University university = new University();
        university.setName("Test University 2");
        university.setCountry("Test Country 2");
        university.setDepartmentName("Test Department 2");
        university.setDepartmentUrl("http://test2.com");
        university.setContactPerson("Dr. Test 2");
        university.setMaxOutgoingStudents(10);
        university.setMaxIncomingStudents(8);
        university.setNextSpringSemesterStart("2025-02-01");
        university.setNextAutumnSemesterStart("2024-10-01");

        EntityModel<University> response = client.createUniversity(university);
        assertThat(response).isNotNull();
        assertThat(response.getContent().getName()).isEqualTo("Test University 2");
    }

    @Test
    @Order(3)
    public void testGetAllUniversities() {
        CollectionModel<EntityModel<University>> response = client.getAllUniversities();
        assertThat(response).isNotNull();
        assertThat(response.getContent()).isNotEmpty();
    }

    @Test
    @Order(4)
    public void testGetUniversityById() {
        EntityModel<University> response = client.getUniversityById(1L);
        assertThat(response).isNotNull();
        assertThat(response.getContent().getName()).isEqualTo("Stanford University");
    }

    @Test
    @Order(5)
    public void testUpdateUniversity() {
        University university = client.getUniversityById(1L).getContent();
        assertThat(university).isNotNull();

        university.setName("Updated University");
        client.updateUniversity(1L, university);

        University updatedUniversity = client.getUniversityById(1L).getContent();
        assertThat(updatedUniversity.getName()).isEqualTo("Updated University");
    }

    @Test
    @Order(6)
    public void testDeleteUniversity() {
        // First, ensure the university exists
        EntityModel<University> university = client.getUniversityById(1L);
        assertThat(university).isNotNull();

        // Perform delete operation
        client.deleteUniversity(1L);

        // Verify the university has been deleted by checking for an exception or null response
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            client.getUniversityById(1L);
        });

        assertThat(exception.getMessage()).contains("Failed to get university by ID 1");
    }
}
