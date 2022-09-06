package com.example.rewardsprogramservice.controler;

import com.example.rewardsprogramservice.entity.Customer;
import com.example.rewardsprogramservice.service.impl.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok().body(customerService.findAllCustomers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(customerService.findById(id));
    }

    @PostMapping("/")
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        final Customer data = new Customer();
        data.setEmail(customer.getEmail());
        data.setSurname(customer.getSurname());
        data.setName(customer.getName());
        data.setCreatedAt(new Date());
        data.setUpdatedAt(new Date());

        final Customer customerSaved = customerService.save(data);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(customerSaved.getId())
                .toUri();

        return ResponseEntity.created(uri).body(customerSaved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable("id") long id, @RequestBody Customer customer) {
        return ResponseEntity.ok().body(customerService.update(id, customer));

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTutorial(@PathVariable("id") long id) {
        customerService.delete(id);
    }
}
