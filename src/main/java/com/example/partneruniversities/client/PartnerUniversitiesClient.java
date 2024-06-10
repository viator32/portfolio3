package com.example.partneruniversities.client;

import com.example.partneruniversities.model.Module;
import com.example.partneruniversities.model.University;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.client.Traverson;
import org.springframework.hateoas.mediatype.hal.Jackson2HalModule;
import org.springframework.hateoas.server.core.TypeReferences;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
            linkCache.put("searchUniversities", URI.create(traverson.follow("universities").follow("search").asLink().getHref()));
        } catch (Exception e) {
            System.err.println("Error initializing links: " + e.getMessage());
            // Optionally log or handle the error as needed
        }
    }

    public University createUniversity(University university) {
        EntityModel<University> universityEntityModel = restTemplate.postForObject(
                linkCache.get("universities"), university, EntityModel.class);
        return objectMapper.convertValue(universityEntityModel.getContent(), University.class);
    }

    public Module createModule(Module module) {
        EntityModel<Module> moduleEntityModel = restTemplate.postForObject(
                linkCache.get("modules"), module, EntityModel.class);
        return objectMapper.convertValue(moduleEntityModel.getContent(), Module.class);
    }

    public University updateUniversity(Long id, University university) {
        URI universityUri = URI.create(linkCache.get("universities") + "/" + id);
        restTemplate.put(universityUri, university);
        return restTemplate.getForObject(universityUri, University.class);
    }

    public Module updateModule(Long id, Module module) {
        try {
            URI moduleUri = URI.create(linkCache.get("modules") + "/" + id);
            restTemplate.put(moduleUri, module);
            return restTemplate.getForObject(moduleUri, Module.class);
        } catch (RestClientException e) {
            System.err.println("Error updating module: " + e.getMessage());
            // Handle the error as needed, e.g., return null or throw a custom exception
            return null;
        }
    }

    public void deleteUniversity(Long id) {
        URI universityUri = URI.create(linkCache.get("universities") + "/" + id);
        restTemplate.delete(universityUri);
    }

    public void deleteModule(Long id) {
        URI moduleUri = URI.create(linkCache.get("modules") + "/" + id);
        restTemplate.delete(moduleUri);
    }

    public University getUniversityById(Long id) {
        URI universityUri = URI.create(linkCache.get("universities") + "/" + id);
        return restTemplate.getForObject(universityUri, University.class);
    }

    public Module getModuleById(Long id) {
        URI moduleUri = URI.create(linkCache.get("modules") + "/" + id);
        return restTemplate.getForObject(moduleUri, Module.class);
    }

    public List<University> searchUniversities(String name, String country, String departmentName, int page, int size, String sortBy, String direction) {
        try {
            URI searchUri = URI.create(linkCache.get("searchUniversities") +
                    "?name=" + name +
                    "&country=" + country +
                    "&departmentName=" + departmentName +
                    "&page=" + page +
                    "&size=" + size +
                    "&sortBy=" + sortBy +
                    "&direction=" + direction);

            Traverson.TraversalBuilder tb = traverson.follow(searchUri.getPath());
            return tb.toObject(new TypeReferences.CollectionModelType<EntityModel<University>>() {})
                    .getContent()
                    .stream()
                    .map(entityModel -> objectMapper.convertValue(entityModel.getContent(), University.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error during search: " + e.getMessage());
            // Optionally log or handle the error as needed
            return null;
        }
    }
}
