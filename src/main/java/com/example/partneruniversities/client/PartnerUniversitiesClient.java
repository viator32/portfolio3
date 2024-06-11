package com.example.partneruniversities.client;

import com.example.partneruniversities.model.Module;
import com.example.partneruniversities.model.University;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.client.Traverson;
import org.springframework.hateoas.mediatype.hal.Jackson2HalModule;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class PartnerUniversitiesClient {

    private static final String DISPATCHER_URL = "http://localhost:8080/";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final Traverson traverson;
    private final Map<String, URI> linkCache;

    public PartnerUniversitiesClient() {
        this.restTemplate = new RestTemplate();
        this.traverson = new Traverson(URI.create(DISPATCHER_URL), MediaTypes.HAL_JSON);
        this.traverson.setRestOperations(restTemplate);
        this.objectMapper = new ObjectMapper().registerModule(new Jackson2HalModule());
        this.linkCache = new HashMap<>();
        initializeLinks();
    }

    private void initializeLinks() {
        try {
            linkCache.put("universities", URI.create(traverson.follow("universities").asLink().getHref()));
            linkCache.put("modules", URI.create(traverson.follow("modules").asLink().getHref()));
            linkCache.put("search", URI.create(traverson.follow("search").asLink().getHref()));
        } catch (Exception e) {
            System.err.println("Error initializing links: " + e.getMessage());
        }
    }

    public University createUniversity(University university) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON); // Ensure this is set

            HttpEntity<University> request = new HttpEntity<>(university, headers);
            ResponseEntity<EntityModel> response = restTemplate.postForEntity(
                    linkCache.get("universities"), request, EntityModel.class);

            return objectMapper.convertValue(response.getBody().getContent(), University.class);
        } catch (RestClientException e) {
            System.err.println("Error creating university: " + e.getMessage());
            throw e;
        }
    }

    public Module createModule(Module module) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Module> request = new HttpEntity<>(module, headers);
            ResponseEntity<EntityModel> response = restTemplate.postForEntity(
                    linkCache.get("modules"), request, EntityModel.class);

            return objectMapper.convertValue(response.getBody().getContent(), Module.class);
        } catch (RestClientException e) {
            System.err.println("Error creating module: " + e.getMessage());
            throw e;
        }
    }

    public University updateUniversity(Long id, University university) {
        try {
            URI universityUri = URI.create(linkCache.get("universities") + "/" + id);
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            HttpEntity<University> request = new HttpEntity<>(university, headers);

            restTemplate.put(universityUri, request);
            return restTemplate.getForObject(universityUri, University.class);
        } catch (RestClientException e) {
            System.err.println("Error updating university: " + e.getMessage());
            throw e;
        }
    }

    public Module updateModule(Long id, Module module) {
        try {
            URI moduleUri = URI.create(linkCache.get("modules") + "/" + id);
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            HttpEntity<Module> request = new HttpEntity<>(module, headers);

            restTemplate.put(moduleUri, request);
            return restTemplate.getForObject(moduleUri, Module.class);
        } catch (RestClientException e) {
            System.err.println("Error updating module: " + e.getMessage());
            throw e;
        }
    }

    public void deleteUniversity(Long id) {
        try {
            URI universityUri = URI.create(linkCache.get("universities") + "/" + id);
            restTemplate.delete(universityUri);
        } catch (RestClientException e) {
            System.err.println("Error deleting university: " + e.getMessage());
            throw e;
        }
    }

    public void deleteModule(Long id) {
        try {
            URI moduleUri = URI.create(linkCache.get("modules") + "/" + id);
            restTemplate.delete(moduleUri);
        } catch (RestClientException e) {
            System.err.println("Error deleting module: " + e.getMessage());
            throw e;
        }
    }

    public University getUniversityById(Long id) {
        try {
            URI universityUri = URI.create(linkCache.get("universities") + "/" + id);
            return restTemplate.getForObject(universityUri, University.class);
        } catch (RestClientException e) {
            System.err.println("Error fetching university by ID: " + e.getMessage());
            throw e;
        }
    }

    public Module getModuleById(Long id) {
        try {
            URI moduleUri = URI.create(linkCache.get("modules") + "/" + id);
            return restTemplate.getForObject(moduleUri, Module.class);
        } catch (RestClientException e) {
            System.err.println("Error fetching module by ID: " + e.getMessage());
            throw e;
        }
    }
}
