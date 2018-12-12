package com.ebsco.producer.example.controller;

import com.ebsco.producer.example.model.Employee;
import com.ebsco.producer.example.model.Response;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    static Set<Employee> employees = new HashSet<>();

    @RequestMapping(value = "/create", method = POST, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    public Response createEmployee(@RequestBody Employee employee) {
        employees.add(employee);
        Response response = new Response();
        response.setMessage("Saved details of employee " + employee.getName());
        return response;
    }


    @RequestMapping(value = "/{id}", method = GET, produces = APPLICATION_JSON_UTF8_VALUE)
    public Response retrieveEmployee(@PathVariable int id) {
        Response response = new Response();
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setName("ARUN");
        response.setMessage(employee.toString());
        return response;
       /*//Yet to be implemented
        return null;*/
    }
}