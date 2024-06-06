package com.example.partneruniversities;

import com.example.partneruniversities.client.PartnerUniversitiesClient;
import com.example.partneruniversities.model.Module;
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
    @Order(1)
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
    @Order(2)
    public void testCreateModule2() {
        Module module = new Module();
        module.setName("Test Module2");
        module.setSemester(1);
        module.setCreditPoints(5);

        EntityModel<Module> response = client.createModule(module);
        assertThat(response).isNotNull();
        assertThat(response.getContent().getName()).isEqualTo("Test Module2");
    }

    @Test
    @Order(3)
    public void testGetModuleById() {
        EntityModel<Module> response = client.getModuleById(1L);
        assertThat(response).isNotNull();
        assertThat(response.getContent().getName()).isEqualTo("Test Module");
    }

    @Test
    @Order(4)
    public void testUpdateModule() {
        Module module = client.getModuleById(1L).getContent();
        assertThat(module).isNotNull();

        module.setName("Updated Module");
        client.updateModule(1L, module);

        Module updatedModule = client.getModuleById(1L).getContent();
        assertThat(updatedModule.getName()).isEqualTo("Updated Module");
    }

    @Test
    @Order(5)
    public void testDeleteModule() {
        // First, ensure the module exists
        EntityModel<Module> module = client.getModuleById(2L);
        assertThat(module).isNotNull();

        // Perform delete operation
        client.deleteModule(2L);

        // Verify the module has been deleted by checking for an exception or null response
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            client.getModuleById(2L);
        });

        assertThat(exception.getMessage()).contains("Failed to get module by ID 2");
    }

}
