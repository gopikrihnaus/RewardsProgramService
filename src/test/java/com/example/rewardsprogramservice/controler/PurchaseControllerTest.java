package com.example.rewardsprogramservice.controler;

import com.example.rewardsprogramservice.entity.Purchase;
import com.example.rewardsprogramservice.exception.CustomerNotFoundException;
import com.example.rewardsprogramservice.service.impl.PurchaseService;
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
@WebMvcTest(PurchaseController.class)
public class PurchaseControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private PurchaseService purchaseService;

    private static final String API_PREFIX = "/api/v1/purchase/";

    @Test
    public void listCustomerById_whenGetMethod() throws Exception {

        Purchase purchase = new Purchase();
        purchase.setDetails("Test Name");
        purchase.setId(89L);

        given(purchaseService.findById(purchase.getId())).willReturn(purchase);

        mvc.perform(get(API_PREFIX + purchase.getId().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("details", is(purchase.getDetails())));
    }

    @Test
    public void should_throw_exception_when_cutomer_doesnt_exist() throws Exception {
        Purchase purchase = new Purchase();
        purchase.setId(89L);
        purchase.setDetails("Test Name");

        Mockito.doThrow(new CustomerNotFoundException()).when(purchaseService).findById(purchase.getId());

        mvc.perform(get(API_PREFIX + purchase.getId().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void listAllCustomers_whenGetMethod()
            throws Exception {

        Purchase purchase = new Purchase();
        purchase.setDetails("Test name");

        List<Purchase> allCustomers = Arrays.asList(purchase);

        given(purchaseService
                .findAllPurchases())
                .willReturn(allCustomers);

        mvc.perform(get(API_PREFIX)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].details", is(purchase.getDetails())));
    }

    @Test
    public void removeCustomerById_whenDeleteMethod() throws Exception {
        Purchase purchase = new Purchase();
        purchase.setDetails("Test Name");
        purchase.setId(89L);

        doNothing().when(purchaseService).delete(purchase.getId());

        mvc.perform(delete(API_PREFIX + purchase.getId().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void should_throw_exception_when_user_doesnt_exist() throws Exception {
        Purchase purchase = new Purchase();
        purchase.setId(89L);
        purchase.setDetails("Test Name");

        Mockito.doThrow(new CustomerNotFoundException()).when(purchaseService).delete(purchase.getId());

        mvc.perform(delete(API_PREFIX + purchase.getId().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    public void createCustomer_whenPostMethod() throws Exception {

        Purchase purchase = new Purchase();
        purchase.setDetails("Test Name");

        given(purchaseService.save(purchase)).willReturn(purchase);

        mvc.perform(post(API_PREFIX)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(purchase)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.details", is(purchase.getDetails())));
    }

    @Test
    public void updateCustomer_whenPutUser() throws Exception {

        Purchase purchase = new Purchase();
        purchase.setDetails("Test Name");
        purchase.setId(89L);
        given(purchaseService.update(purchase.getId(), purchase)).willReturn(purchase);

        ObjectMapper mapper = new ObjectMapper();

        mvc.perform(put(API_PREFIX + purchase.getId().toString())
                .content(mapper.writeValueAsString(purchase))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("details", is(purchase.getDetails())));
    }

    @Test
    public void should_throw_exception_when_user_doesnt_exist_update() throws Exception {
        Purchase purchase = new Purchase();
        purchase.setId(89L);
        purchase.setDetails("Test Name");

        Mockito.doThrow(new CustomerNotFoundException()).when(purchaseService).update(purchase.getId(), purchase);
        ObjectMapper mapper = new ObjectMapper();

        mvc.perform(put(API_PREFIX + purchase.getId().toString())
                .content(mapper.writeValueAsString(purchase))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
