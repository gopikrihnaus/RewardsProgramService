package com.example.rewardsprogramservice.service.impl;

import com.example.rewardsprogramservice.entity.Customer;
import com.example.rewardsprogramservice.exception.CustomerNotFoundException;
import com.example.rewardsprogramservice.repoitory.CustomerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    public void whenGivenId_shouldReturnCustomer_ifFound() {
        Customer customer = new Customer();
        customer.setId(89L);

        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));

        Customer expected = customerService.findById(customer.getId());

        assertThat(expected).isSameAs(customer);
        verify(customerRepository).findById(customer.getId());
    }

    @Test(expected = CustomerNotFoundException.class)
    public void should_throw_exception_when_customer_doesnt_exist_findById() {
        Customer customer = new Customer();
        customer.setId(89L);
        customer.setName("Test Name");

        given(customerRepository.findById(anyLong())).willReturn(Optional.ofNullable(null));
        customerService.findById(customer.getId());
    }

    @Test
    public void findAllCustomers() {
        List<Customer> customers = new ArrayList();
        customers.add(new Customer());

        given(customerRepository.findAll()).willReturn(customers);

        List<Customer> expected = customerService.findAllCustomers();

        assertEquals(expected, customers);
        verify(customerRepository).findAll();
    }

    @Test
    public void whenGivenId_shouldDeleteUser_ifFound(){
        Customer customer = new Customer();
        customer.setName("Test Name");
        customer.setId(1L);

        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));

        customerService.delete(customer.getId());
        verify(customerRepository).deleteById(customer.getId());
    }

    @Test(expected = RuntimeException.class)
    public void should_throw_exception_when_customer_doesnt_exist_delete() {
        Customer customer = new Customer();
        customer.setId(89L);
        customer.setName("Test Name");

        given(customerRepository.findById(anyLong())).willReturn(Optional.ofNullable(null));
        customerService.delete(customer.getId());
    }

    @Test
    public void whenSaveUser_shouldReturnUser() {
        Customer customer = new Customer();
        customer.setName("Test Name");

        when(customerRepository.save(ArgumentMatchers.any(Customer.class))).thenReturn(customer);

        Customer created = customerService.save(customer);

        assertThat(created.getName()).isSameAs(customer.getName());
        verify(customerRepository).save(customer);
    }

    @Test
    public void whenGivenId_shouldUpdateUser_ifFound() {
        Customer customer = new Customer();
        customer.setId(89L);
        customer.setName("Test Name");

        Customer newCustomer = new Customer();
        newCustomer.setName("New Test Name");

        given(customerRepository.findById(customer.getId())).willReturn(Optional.of(customer));
        customerService.update(customer.getId(), newCustomer);

        verify(customerRepository).save(newCustomer);
        verify(customerRepository).findById(customer.getId());
    }

    @Test(expected = RuntimeException.class)
    public void should_throw_exception_when_customer_doesnt_exist_update() {
        Customer customer = new Customer();
        customer.setId(89L);
        customer.setName("Test Name");

        Customer newCustomer = new Customer();
        newCustomer.setId(90L);
        customer.setName("New Test Name");

        given(customerRepository.findById(anyLong())).willReturn(Optional.ofNullable(null));
        customerService.update(customer.getId(), newCustomer);
    }
}