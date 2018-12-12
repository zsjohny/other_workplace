package com.ebsco.producer.example.controller;

import com.ebsco.producer.example.AbstractTest;
import com.ebsco.producer.example.model.Employee;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.cloud.contract.wiremock.restdocs.SpringCloudContractRestDocs;
import org.springframework.cloud.contract.wiremock.restdocs.WireMockRestDocs;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by aganapathy on 12/11/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureRestDocs(outputDir = "target/snippets")
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@DirtiesContext
public class EmployeeControllerTest extends AbstractTest{

    @Autowired private MockMvc mockMvc;
    private JacksonTester<Employee> json;

    @Before
    public void setup() {
        ObjectMapper objectMappper = new ObjectMapper();
        // Possibly configure the mapper
        JacksonTester.initFields(this, objectMappper);
    }


    @Test
    public void createEmployeeTest() throws Exception{
        Employee employee = new Employee();
        employee.setName("Arun");

        mockMvc.perform(MockMvcRequestBuilders.post("/employee/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.write(employee).getJson()))
                .andExpect(status().is2xxSuccessful())
                .andDo(WireMockRestDocs.verify()
                        //						.jsonPath("$[?(@.name != null)]")
                        .contentType(MediaType.valueOf("application/json"))
                        .stub("shouldCreateEmployee"))
                .andDo(MockMvcRestDocumentation.document("shouldCreateEmployee",
                SpringCloudContractRestDocs.dslContract()));
    }
}