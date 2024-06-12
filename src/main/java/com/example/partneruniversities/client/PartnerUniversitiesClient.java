package com.example.partneruniversities.client;

import com.example.partneruniversities.model.Module;
import com.example.partneruniversities.model.University;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.client.Traverson;
import org.springframework.hateoas.mediatype.hal.Jackson2HalModule;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class PartnerUniversitiesClient {

    private static final Logger logger = LoggerFactory.getLogger(PartnerUniversitiesClient.class);
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
        } catch (Exception e) {
            logger.error("Error initializing links: {}", e.getMessage());
        }
    }

    private URI getLink(String rel) {
        URI uri = linkCache.get(rel);
        if (uri == null) {
            throw new RuntimeException("Link not found: " + rel);
        }
        return uri;
    }

    public List<EntityModel<University>> getAllUniversities() {
        URI uri = getLink("universities");
        logger.info("Fetching all universities from URL: {}", uri);
        ResponseEntity<PagedModel<EntityModel<University>>> response = restTemplate.exchange(
                uri,
                org.springframework.http.HttpMethod.GET,
                null,
                new org.springframework.core.ParameterizedTypeReference<PagedModel<EntityModel<University>>>() {});
        return response.getBody().getContent().stream().collect(Collectors.toList());
    }

    public EntityModel<University> getUniversityById(Long id) {
        URI uri = UriComponentsBuilder.fromUri(getLink("universities")).pathSegment("{id}").buildAndExpand(id).toUri();
        logger.info("Fetching university with ID: {} from URL: {}", id, uri);
        ResponseEntity<EntityModel<University>> response = restTemplate.exchange(
                uri,
                org.springframework.http.HttpMethod.GET,
                null,
                new org.springframework.core.ParameterizedTypeReference<EntityModel<University>>() {});
        return response.getBody();
    }

    public List<EntityModel<University>> searchUniversities(Map<String, String> params) {
        URI uri = UriComponentsBuilder.fromUri(getLink("universities")).pathSegment("search").build().toUri();
        logger.info("Searching universities with params: {} from URL: {}", params, uri);
        ResponseEntity<PagedModel<EntityModel<University>>> response = restTemplate.exchange(
                uri,
                org.springframework.http.HttpMethod.GET,
                new HttpEntity<>(params),
                new org.springframework.core.ParameterizedTypeReference<PagedModel<EntityModel<University>>>() {});
        return response.getBody().getContent().stream().collect(Collectors.toList());
    }

    public List<EntityModel<Module>> getAllModules() {
        URI uri = getLink("modules");
        logger.info("Fetching all modules from URL: {}", uri);
        ResponseEntity<PagedModel<EntityModel<Module>>> response = restTemplate.exchange(
                uri,
                org.springframework.http.HttpMethod.GET,
                null,
                new org.springframework.core.ParameterizedTypeReference<PagedModel<EntityModel<Module>>>() {});
        List<EntityModel<Module>> modules = response.getBody().getContent().stream().collect(Collectors.toList());
        logger.info("Fetched {} modules", modules.size());
        return modules;
    }

    public EntityModel<Module> getModuleById(Long id) {
        URI uri = UriComponentsBuilder.fromUri(getLink("modules")).pathSegment("{id}").buildAndExpand(id).toUri();
        logger.info("Fetching module with ID: {} from URL: {}", id, uri);
        try {
            ResponseEntity<EntityModel<Module>> response = restTemplate.exchange(
                    uri,
                    org.springframework.http.HttpMethod.GET,
                    null,
                    new org.springframework.core.ParameterizedTypeReference<EntityModel<Module>>() {});
            logger.info("Fetched module data: {}", response.getBody().getContent().toString());
            return response.getBody();
        } catch (HttpClientErrorException e) {
            logger.error("Error fetching module: {}", e.getResponseBodyAsString());
            throw e;
        }
    }

    public EntityModel<University> createUniversity(String universityJson) {
        URI uri = getLink("universities");
        logger.info("Creating university at URL: {}", uri);
        logger.info("University data: {}", universityJson);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        try {
            ResponseEntity<EntityModel<University>> response = restTemplate.exchange(
                    uri,
                    org.springframework.http.HttpMethod.POST,
                    new HttpEntity<>(universityJson, headers),
                    new org.springframework.core.ParameterizedTypeReference<EntityModel<University>>() {});
            return response.getBody();
        } catch (HttpClientErrorException e) {
            logger.error("Error creating university: {}", e.getResponseBodyAsString());
            throw e;
        }
    }

    public EntityModel<University> updateUniversity(Long id, String universityJson) {
        URI uri = UriComponentsBuilder.fromUri(getLink("universities")).pathSegment("{id}").buildAndExpand(id).toUri();
        logger.info("Updating university with ID: {} at URL: {}", id, uri);
        logger.info("University data: {}", universityJson);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        try {
            restTemplate.exchange(
                    uri,
                    org.springframework.http.HttpMethod.PUT,
                    new HttpEntity<>(universityJson, headers),
                    new org.springframework.core.ParameterizedTypeReference<EntityModel<University>>() {});
            return getUniversityById(id);
        } catch (HttpClientErrorException e) {
            logger.error("Error updating university: {}", e.getResponseBodyAsString());
            throw e;
        }
    }

    public void deleteUniversity(Long id) {
        URI uri = UriComponentsBuilder.fromUri(getLink("universities")).pathSegment("{id}").buildAndExpand(id).toUri();
        logger.info("Deleting university with ID: {} from URL: {}", id, uri);
        try {
            restTemplate.exchange(
                    uri,
                    org.springframework.http.HttpMethod.DELETE,
                    null,
                    Void.class);
        } catch (HttpClientErrorException e) {
            logger.error("Error deleting university: {}", e.getResponseBodyAsString());
            throw e;
        }
    }

    public EntityModel<Module> createModule(String moduleJson) {
        URI uri = getLink("modules");
        logger.info("Creating module at URL: {}", uri);
        logger.info("Module data: {}", moduleJson);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        try {
            ResponseEntity<EntityModel<Module>> response = restTemplate.exchange(
                    uri,
                    org.springframework.http.HttpMethod.POST,
                    new HttpEntity<>(moduleJson, headers),
                    new org.springframework.core.ParameterizedTypeReference<EntityModel<Module>>() {});
            logger.info("Created module data: {}", response.getBody().getContent().toString());
            return response.getBody();
        } catch (HttpClientErrorException e) {
            logger.error("Error creating module: {}", e.getResponseBodyAsString());
            throw e;
        }
    }

    public void deleteModule(Long id) {
        URI uri = UriComponentsBuilder.fromUri(getLink("modules")).pathSegment("{id}").buildAndExpand(id).toUri();
        logger.info("Deleting module with ID: {} from URL: {}", id, uri);
        try {
            restTemplate.exchange(
                    uri,
                    org.springframework.http.HttpMethod.DELETE,
                    null,
                    Void.class);
        } catch (HttpClientErrorException e) {
            logger.error("Error deleting module: {}", e.getResponseBodyAsString());
            throw e;
        }
    }

    public List<EntityModel<Module>> getModulesByUniversityId(Long universityId) {
        URI uri = UriComponentsBuilder.fromUri(getLink("universities")).pathSegment("{universityId}", "modules").buildAndExpand(universityId).toUri();
        logger.info("Fetching modules for university ID: {} from URL: {}", universityId, uri);
        ResponseEntity<PagedModel<EntityModel<Module>>> response = restTemplate.exchange(
                uri,
                org.springframework.http.HttpMethod.GET,
                null,
                new org.springframework.core.ParameterizedTypeReference<PagedModel<EntityModel<Module>>>() {});
        return response.getBody().getContent().stream().collect(Collectors.toList());
    }
}
