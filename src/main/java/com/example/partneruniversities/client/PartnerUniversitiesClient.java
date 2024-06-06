package com.example.partneruniversities.client;

import com.example.partneruniversities.model.Module;
import com.example.partneruniversities.model.University;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.client.Traverson;
import org.springframework.hateoas.server.core.TypeReferences;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Map;

@Component
public class PartnerUniversitiesClient {

    private Traverson traverson;
    private RestTemplate restTemplate;
    private ObjectMapper objectMapper;

    public void setPort(int port) {
        String baseUrl = "http://localhost:" + port + "/";
        this.traverson = new Traverson(URI.create(baseUrl), MediaTypes.HAL_JSON);
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public CollectionModel<EntityModel<University>> getAllUniversities() {
        try {
            return traverson.follow("universities")
                    .toObject(new TypeReferences.CollectionModelType<EntityModel<University>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to get all universities: " + e.getMessage(), e);
        }
    }

    public EntityModel<University> getUniversityById(Long id) {
        try {
            Link link = traverson.follow("universities").asLink();
            Map<String, Object> response = restTemplate.getForObject(link.expand().getHref() + "/" + id, Map.class);
            University university = objectMapper.convertValue(response, University.class);
            String selfLink = ((Map<String, String>) ((Map<String, Object>) response.get("_links")).get("self")).get("href");
            return EntityModel.of(university, Link.of(selfLink, "self"));
        } catch (Exception e) {
            throw new RuntimeException("Failed to get university by ID " + id + ": " + e.getMessage(), e);
        }
    }

    public EntityModel<University> createUniversity(University university) {
        try {
            Link link = traverson.follow("universities").asLink();
            Map<String, Object> response = restTemplate.postForObject(link.expand().getHref(), university, Map.class);
            University createdUniversity = objectMapper.convertValue(response, University.class);
            String selfLink = ((Map<String, String>) ((Map<String, Object>) response.get("_links")).get("self")).get("href");
            return EntityModel.of(createdUniversity, Link.of(selfLink, "self"));
        } catch (Exception e) {
            throw new RuntimeException("Failed to create university: " + e.getMessage(), e);
        }
    }

    public void updateUniversity(Long id, University university) {
        try {
            EntityModel<University> entityModel = getUniversityById(id);
            Link link = entityModel.getLink("self").orElseThrow(() -> new RuntimeException("University self link not found"));
            restTemplate.put(link.expand().getHref(), university);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update university with ID " + id + ": " + e.getMessage(), e);
        }
    }

    public void deleteUniversity(Long id) {
        try {
            EntityModel<University> entityModel = getUniversityById(id);
            Link link = entityModel.getLink("self").orElseThrow(() -> new RuntimeException("University self link not found"));
            restTemplate.delete(link.expand().getHref());
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete university with ID " + id + ": " + e.getMessage(), e);
        }
    }

    public CollectionModel<EntityModel<Module>> getAllModules() {
        try {
            return traverson.follow("modules")
                    .toObject(new TypeReferences.CollectionModelType<EntityModel<Module>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to get all modules: " + e.getMessage(), e);
        }
    }

    public EntityModel<Module> getModuleById(Long id) {
        try {
            Link link = traverson.follow("modules").asLink();
            Map<String, Object> response = restTemplate.getForObject(link.expand().getHref() + "/" + id, Map.class);
            Module module = objectMapper.convertValue(response, Module.class);
            String selfLink = ((Map<String, String>) ((Map<String, Object>) response.get("_links")).get("self")).get("href");
            return EntityModel.of(module, Link.of(selfLink, "self"));
        } catch (Exception e) {
            throw new RuntimeException("Failed to get module by ID " + id + ": " + e.getMessage(), e);
        }
    }

    public EntityModel<Module> createModule(Module module) {
        try {
            Link link = traverson.follow("modules").asLink();
            Map<String, Object> response = restTemplate.postForObject(link.expand().getHref(), module, Map.class);
            Module createdModule = objectMapper.convertValue(response, Module.class);
            String selfLink = ((Map<String, String>) ((Map<String, Object>) response.get("_links")).get("self")).get("href");
            return EntityModel.of(createdModule, Link.of(selfLink, "self"));
        } catch (Exception e) {
            throw new RuntimeException("Failed to create module: " + e.getMessage(), e);
        }
    }

    public void updateModule(Long id, Module module) {
        try {
            EntityModel<Module> entityModel = getModuleById(id);
            Link link = entityModel.getLink("self").orElseThrow(() -> new RuntimeException("Module self link not found"));
            restTemplate.put(link.expand().getHref(), module);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update module with ID " + id + ": " + e.getMessage(), e);
        }
    }

    public void deleteModule(Long id) {
        try {
            EntityModel<Module> entityModel = getModuleById(id);
            Link link = entityModel.getLink("self").orElseThrow(() -> new RuntimeException("Module self link not found"));
            restTemplate.delete(link.expand().getHref());
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete module with ID " + id + ": " + e.getMessage(), e);
        }
    }
}
