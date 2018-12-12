package com.ebsco.consumer.example.dao;

import com.ebsco.consumer.example.model.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/**
 * Created by aganapathy on 12/12/17.
 */
@Component
public class EmployeeConsumerDao {

    private final RestTemplate restTemplate;

    @Value("${employeeProducer.url}")
    private final String employeeProducerUrl;

    public EmployeeConsumerDao(RestTemplate restTemplate, @Value("${employeeProducer.url}") String employeeProducerUrl) {
        this.restTemplate = restTemplate;
        this.employeeProducerUrl = employeeProducerUrl;
    }

    public Response getEmployeeById(){
        URI uri = UriComponentsBuilder.fromUriString(employeeProducerUrl)
                .path("/employee/1")
                .build()
                .toUri();
        ResponseEntity<Response> response = restTemplate.getForEntity(uri, Response.class);
        return response.getBody();
    }
}
