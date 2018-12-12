package com.ebsco.producer.example;

import org.junit.Before;

import com.ebsco.producer.example.controller.EmployeeController;
import io.restassured.module.mockmvc.RestAssuredMockMvc;

/**
 * Created by aganapathy on 12/9/17.
 */
public class EmployeeBase {

    @Before
    public void setup() {
        RestAssuredMockMvc.standaloneSetup(new EmployeeController());
    }
}
