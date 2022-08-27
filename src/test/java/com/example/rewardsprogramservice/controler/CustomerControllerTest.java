package com.example.rewardsprogramservice.controler;

import com.example.rewardsprogramservice.entity.Customer;
import com.example.rewardsprogramservice.exception.CustomerNotFoundException;
import com.example.rewardsprogramservice.service.impl.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CustomerService customerService;

    @Test
    public void listCustomerById_whenGetMethod() throws Exception {

        Customer customer = new Customer();
        customer.setName("Test Name");
        customer.setId(89L);

        given(customerService.findById(customer.getId())).willReturn(customer);

        mvc.perform(get("/customer/" + customer.getId().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", is(customer.getName())));
    }

    @Test
    public void should_throw_exception_when_cutomer_doesnt_exist() throws Exception {
        Customer customer = new Customer();
        customer.setId(89L);
        customer.setName("Test Name");

        Mockito.doThrow(new CustomerNotFoundException()).when(customerService).findById(customer.getId());

        mvc.perform(get("/customer/" + customer.getId().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void listAllCustomers_whenGetMethod()
            throws Exception {

        Customer customer = new Customer();
        customer.setName("Test name");

        List<Customer> allCustomers = Arrays.asList(customer);

        given(customerService
                .findAllCustomers())
                .willReturn(allCustomers);

        mvc.perform(get("/customer/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(customer.getName())));
    }

    @Test
    public void removeCustomerById_whenDeleteMethod() throws Exception {
        Customer customer = new Customer();
        customer.setName("Test Name");
        customer.setId(89L);

        doNothing().when(customerService).delete(customer.getId());

        mvc.perform(delete("/customer/" + customer.getId().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void should_throw_exception_when_user_doesnt_exist() throws Exception {
        Customer customer = new Customer();
        customer.setId(89L);
        customer.setName("Test Name");

        Mockito.doThrow(new CustomerNotFoundException()).when(customerService).delete(customer.getId());

        mvc.perform(delete("/customer/" + customer.getId().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    public void createCustomer_whenPostMethod() throws Exception {

        Customer customer = new Customer();
        customer.setName("Test Name");

        given(customerService.save(customer)).willReturn(customer);

        mvc.perform(post("/customer/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(customer)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(customer.getName())));
    }
    @Test
    public void updateCustomer_whenPutUser() throws Exception {

        Customer customer = new Customer();
        customer.setName("Test Name");
        customer.setId(89L);
        given(customerService.update(customer.getId(), customer)).willReturn(customer);

        ObjectMapper mapper = new ObjectMapper();

        mvc.perform(put("/customer/" + customer.getId().toString())
                .content(mapper.writeValueAsString(customer))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", is(customer.getName())));
    }

    @Test
    public void should_throw_exception_when_user_doesnt_exist_update() throws Exception {
        Customer customer = new Customer();
        customer.setId(89L);
        customer.setName("Test Name");

        Mockito.doThrow(new CustomerNotFoundException()).when(customerService).update(customer.getId(), customer);
        ObjectMapper mapper = new ObjectMapper();

        mvc.perform(put("/customer/" + customer.getId().toString())
                .content(mapper.writeValueAsString(customer))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}