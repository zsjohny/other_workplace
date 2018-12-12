package com.ebsco.consumer.example.service;

/**
 * Created by aganapathy on 12/12/17.
 */
import com.ebsco.consumer.example.dao.EmployeeConsumerDao;
import com.ebsco.consumer.example.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class EmployeeConsumerService {

    @Autowired EmployeeConsumerDao employeeDao;

    public Response getEmployeeDetails(){
        Response response = employeeDao.getEmployeeById();
        return response;
    }
}
