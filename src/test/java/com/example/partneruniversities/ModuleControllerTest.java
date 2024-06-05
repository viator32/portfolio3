package com.example.partneruniversities;

import com.example.partneruniversities.client.PartnerUniversitiesClient;
import com.example.partneruniversities.model.Module;
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
public class ModuleControllerTest {

    @Autowired
    private PartnerUniversitiesClient client;

    @BeforeEach
    public void setUp() {
        client.setPort(8080);  // Ensure it points to the Docker container port
    }

    @Test
    public void testGetAllModules() {
        CollectionModel<EntityModel<Module>> response = client.getAllModules();
        assertThat(response).isNotNull();
        assertThat(response.getContent()).isNotEmpty();
    }

    @Test
    public void testCreateModule() {
        Module module = new Module();
        module.setName("Test Module");
        module.setSemester(1);
        module.setCreditPoints(5);

        EntityModel<Module> response = client.createModule(module);
        assertThat(response).isNotNull();
        assertThat(response.getContent().getName()).isEqualTo("Test Module");
    }

    @Test
    public void testUpdateModule() {
        Module module = client.getModuleById(1L).getContent();
        assertThat(module).isNotNull();

        module.setName("Updated Module");
        client.updateModule(1L, module);

        Module updatedModule = client.getModuleById(1L).getContent();
        assertThat(updatedModule.getName()).isEqualTo("Updated Module");
    }

    @Test
    public void testDeleteModule() {
        client.deleteModule(1L);
        EntityModel<Module> response = client.getModuleById(1L);
        assertThat(response).isNull();
    }

    @Test
    public void testGetModuleById() {
        EntityModel<Module> response = client.getModuleById(2L);
        assertThat(response).isNotNull();
        assertThat(response.getContent().getName()).isEqualTo("Advanced Algorithms");
    }
}
