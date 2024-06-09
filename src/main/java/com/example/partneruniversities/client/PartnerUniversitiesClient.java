package com.example.partneruniversities.client;

import com.example.partneruniversities.model.Module;
import com.example.partneruniversities.model.University;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.client.Traverson;
import org.springframework.hateoas.mediatype.hal.Jackson2HalModule;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

public class PartnerUniversitiesClient {

    private static final String DISPATCHER_URL = "http://localhost:8080/";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public PartnerUniversitiesClient() {
        this.restTemplate = new RestTemplate();
        Traverson traverson = new Traverson(URI.create(DISPATCHER_URL), MediaTypes.HAL_JSON);
        traverson.setRestOperations(restTemplate);
        this.objectMapper = new ObjectMapper().registerModule(new Jackson2HalModule());
    }

    public University createUniversity(University university) {
        EntityModel<University> universityEntityModel = restTemplate.postForObject(
                DISPATCHER_URL + "universities", university, EntityModel.class);
        return objectMapper.convertValue(universityEntityModel.getContent(), University.class);
    }

    public Module createModule(Module module) {
        EntityModel<Module> moduleEntityModel = restTemplate.postForObject(
                DISPATCHER_URL + "modules", module, EntityModel.class);
        return objectMapper.convertValue(moduleEntityModel.getContent(), Module.class);
    }

    public University updateUniversity(Long id, University university) {
        restTemplate.put(DISPATCHER_URL + "universities/" + id, university);
        return restTemplate.getForObject(DISPATCHER_URL + "universities/" + id, University.class);
    }

    public Module updateModule(Long id, Module module) {
        restTemplate.put(DISPATCHER_URL + "modules/" + id, module);
        return restTemplate.getForObject(DISPATCHER_URL + "modules/" + id, Module.class);
    }

    public void deleteUniversity(Long id) {
        restTemplate.delete(DISPATCHER_URL + "universities/" + id);
    }

    public void deleteModule(Long id) {
        restTemplate.delete(DISPATCHER_URL + "modules/" + id);
    }

    public University getUniversityById(Long id) {
        return restTemplate.getForObject(DISPATCHER_URL + "universities/" + id, University.class);
    }

    public Module getModuleById(Long id) {
        return restTemplate.getForObject(DISPATCHER_URL + "modules/" + id, Module.class);
    }
}