package com.example.rewardsprogramservice.service.impl;

import com.example.rewardsprogramservice.entity.Customer;
import com.example.rewardsprogramservice.exception.CustomerNotFoundException;
import com.example.rewardsprogramservice.repoitory.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(final CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer findById(Long id) {
        return this.customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("100", "Customer with given id not found"));
    }

    public List<Customer> findAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    public void delete(Long id) {
        customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("100", "Customer with given id not found"));

        customerRepository.deleteById(id);
    }

    public Customer update(Long id, Customer newCustomer) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("100", "Customer with given id not found"));

        newCustomer.setId(id);
        newCustomer.setCreatedAt(customer.getCreatedAt());
        newCustomer.setUpdatedAt(new Date());
        return customerRepository.save(newCustomer);

    }
}
