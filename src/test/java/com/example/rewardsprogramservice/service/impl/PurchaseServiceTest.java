package com.example.rewardsprogramservice.service.impl;

import com.example.rewardsprogramservice.entity.Customer;
import com.example.rewardsprogramservice.entity.Purchase;
import com.example.rewardsprogramservice.exception.NoPurchaseHistoryFoundException;
import com.example.rewardsprogramservice.repoitory.PurchaseRepository;
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
public class PurchaseServiceTest {
    @Mock
    private PurchaseRepository purchaseRepository;

    @InjectMocks
    private PurchaseService purchaseService;

    @Test
    public void whenGivenId_shouldReturnCustomer_ifFound() {
        Purchase purchase = new Purchase();
        purchase.setId(89L);

        when(purchaseRepository.findById(purchase.getId())).thenReturn(Optional.of(purchase));

        Purchase expected = purchaseService.findById(purchase.getId());

        assertThat(expected).isSameAs(purchase);
        verify(purchaseRepository).findById(purchase.getId());
    }

    @Test(expected = NoPurchaseHistoryFoundException.class)
    public void should_throw_exception_when_customer_doesnt_exist_findById() {
        Purchase purchase = new Purchase();
        purchase.setId(89L);
        purchase.setDetails("Test Name");

        given(purchaseRepository.findById(anyLong())).willReturn(Optional.ofNullable(null));
        purchaseService.findById(purchase.getId());
    }

    @Test
    public void findAllCustomers() {
        List<Purchase> purchases = new ArrayList();
        purchases.add(new Purchase());

        given(purchaseRepository.findAll()).willReturn(purchases);

        List<Purchase> expected = purchaseService.findAllPurchases();

        assertEquals(expected, purchases);
        verify(purchaseRepository).findAll();
    }

    @Test
    public void whenGivenId_shouldDeleteUser_ifFound(){
        Purchase purchase = new Purchase();
        purchase.setDetails("Test Name");
        purchase.setId(1L);

        when(purchaseRepository.findById(purchase.getId())).thenReturn(Optional.of(purchase));

        purchaseService.delete(purchase.getId());
        verify(purchaseRepository).deleteById(purchase.getId());
    }

    @Test(expected = RuntimeException.class)
    public void should_throw_exception_when_customer_doesnt_exist_delete() {
        Customer customer = new Customer();
        customer.setId(89L);
        customer.setName("Test Name");

        given(purchaseRepository.findById(anyLong())).willReturn(Optional.ofNullable(null));
        purchaseService.delete(customer.getId());
    }

    @Test
    public void whenSaveUser_shouldReturnUser() {
        Purchase purchase = new Purchase();
        purchase.setDetails("Test Name");

        when(purchaseRepository.save(ArgumentMatchers.any(Purchase.class))).thenReturn(purchase);

        Purchase created = purchaseService.save(purchase);

        assertThat(created.getDetails()).isSameAs(purchase.getDetails());
        verify(purchaseRepository).save(purchase);
    }

    @Test
    public void whenGivenId_shouldUpdateUser_ifFound() {
        Purchase purchase = new Purchase();
        purchase.setId(89L);
        purchase.setDetails("Test Name");

        Purchase newPurchase = new Purchase();
        newPurchase.setDetails("New Test Name");

        given(purchaseRepository.findById(purchase.getId())).willReturn(Optional.of(purchase));
        purchaseService.update(purchase.getId(), newPurchase);

        verify(purchaseRepository).save(newPurchase);
        verify(purchaseRepository).findById(purchase.getId());
    }

    @Test(expected = RuntimeException.class)
    public void should_throw_exception_when_customer_doesnt_exist_update() {
        Purchase purchase = new Purchase();
        purchase.setId(89L);
        purchase.setDetails("Test Name");

        Purchase newPurchase = new Purchase();
        newPurchase.setId(90L);
        purchase.setDetails("New Test Name");

        given(purchaseRepository.findById(anyLong())).willReturn(Optional.ofNullable(null));
        purchaseService.update(purchase.getId(), newPurchase);
    }
}
