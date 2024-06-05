package com.example.partneruniversities;

import com.example.partneruniversities.client.PartnerUniversitiesClient;
import com.example.partneruniversities.model.University;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UniversityControllerTest {

    @Autowired
    private PartnerUniversitiesClient client;

    @BeforeEach
    public void setUp() {
        client.setPort(8080);  // Ensure it points to the Docker container port
    }

    @Test
    public void testGetAllUniversities() {
        CollectionModel<EntityModel<University>> response = client.getAllUniversities();
        assertThat(response).isNotNull();
        assertThat(response.getContent()).isNotEmpty();
    }

    @Test
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
    public void testUpdateUniversity() {
        University university = client.getUniversityById(1L).getContent();
        assertThat(university).isNotNull();

        university.setName("Updated University");
        client.updateUniversity(1L, university);

        University updatedUniversity = client.getUniversityById(1L).getContent();
        assertThat(updatedUniversity.getName()).isEqualTo("Updated University");
    }

    @Test
    public void testDeleteUniversity() {
        client.deleteUniversity(1L);
        EntityModel<University> response = client.getUniversityById(1L);
        assertThat(response).isNull();
    }

    @Test
    public void testGetUniversityById() {
        EntityModel<University> response = client.getUniversityById(2L);
        assertThat(response).isNotNull();
        assertThat(response.getContent().getName()).isEqualTo("Stanford University");
    }
}
