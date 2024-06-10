package com.example.partneruniversities.client;

import com.example.partneruniversities.model.Module;
import com.example.partneruniversities.model.University;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.client.Traverson;
import org.springframework.hateoas.mediatype.hal.Jackson2HalModule;
import org.springframework.hateoas.server.core.TypeReferences;
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
        linkCache.put("universities", URI.create(traverson.follow("universities").asLink().getHref()));
        linkCache.put("modules", URI.create(traverson.follow("modules").asLink().getHref()));
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
        URI moduleUri = URI.create(linkCache.get("modules") + "/" + id);
        restTemplate.put(moduleUri, module);
        return restTemplate.getForObject(moduleUri, Module.class);
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
}
