package com.example.rewardsprogramservice.service.impl;

import com.example.rewardsprogramservice.entity.Customer;
import com.example.rewardsprogramservice.entity.Purchase;
import com.example.rewardsprogramservice.exception.CanNotCalculateRewardPointsException;
import com.example.rewardsprogramservice.exception.CustomerNotFoundException;
import com.example.rewardsprogramservice.exception.NoPurchaseHistoryFoundException;
import com.example.rewardsprogramservice.repoitory.CustomerRepository;
import com.example.rewardsprogramservice.repoitory.PurchaseRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RewardProgramServiceTest {
    @Mock
    private PurchaseRepository purchaseRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private RewardProgramService service;

    @Test
    public void whenGivenId_shouldReturnPoints_ifFound() {
        Customer customer = new Customer();
        customer.setId(89L);


        List<Purchase> purchases = getPurchasesTestList1();

        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        when(purchaseRepository.findByCustomerIdAndCreatedAtGreaterThan(any(), any())).thenReturn(purchases);

        Map<String, Double> expected = service.getRewardsForTheLastThreeMonthsByCustomerId(customer.getId());

        assertThat(expected.size()).isEqualTo(2);
        //90 points
        expected.entrySet().forEach(stringDoubleEntry -> assertThat(stringDoubleEntry.getValue()).isEqualTo(90));

        verify(customerRepository).findById(customer.getId());
        verify(purchaseRepository).findByCustomerIdAndCreatedAtGreaterThan(any(), any());
    }

    @Test
    public void whenGivenId_shouldReturnNoPoints() {
        Customer customer = new Customer();
        customer.setId(89L);


        List<Purchase> purchases = getPurchasesTestList2();

        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        when(purchaseRepository.findByCustomerIdAndCreatedAtGreaterThan(any(), any())).thenReturn(purchases);

        Map<String, Double> expected = service.getRewardsForTheLastThreeMonthsByCustomerId(customer.getId());

        assertThat(expected.size()).isEqualTo(2);
        //0 points
        expected.entrySet().forEach(stringDoubleEntry -> assertThat(stringDoubleEntry.getValue()).isEqualTo(0));
    }

    @Test
    public void whenGivenId_shouldReturnPoints_2() {
        Customer customer = new Customer();
        customer.setId(89L);


        List<Purchase> purchases = getPurchasesTestList3();

        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        when(purchaseRepository.findByCustomerIdAndCreatedAtGreaterThan(any(), any())).thenReturn(purchases);

        Map<String, Double> expected = service.getRewardsForTheLastThreeMonthsByCustomerId(customer.getId());

        assertThat(expected.size()).isEqualTo(2);
        //30 points
        expected.entrySet().forEach(stringDoubleEntry -> assertThat(stringDoubleEntry.getValue()).isEqualTo(30));
    }

    @Test
    public void whenGivenId_shouldReturnPoints_4() {
        Customer customer = new Customer();
        customer.setId(89L);


        List<Purchase> purchases = getPurchasesTestList4();

        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        when(purchaseRepository.findByCustomerIdAndCreatedAtGreaterThan(any(), any())).thenReturn(purchases);

        Map<String, Double> expected = service.getRewardsForTheLastThreeMonthsByCustomerId(customer.getId());

        assertThat(expected.size()).isEqualTo(4);
        //30 points
        expected.entrySet().forEach(stringDoubleEntry -> {
            if (!stringDoubleEntry.getKey().equals("Total")) {
                assertThat(stringDoubleEntry.getValue()).isEqualTo(30);
            } else {
                assertThat(stringDoubleEntry.getValue()).isEqualTo(90);
            }
        });
    }

    private List<Purchase> getPurchasesTestList1() {
        Purchase purchase = new Purchase();
        purchase.setId(1L);
        purchase.setDetails("Test");
        purchase.setAmount(120.0);
        purchase.setCreatedAt(new Date());
        purchase.setUpdatedAt(new Date());
        List<Purchase> purchases = new ArrayList<>();
        purchases.add(purchase);
        return purchases;
    }

    private List<Purchase> getPurchasesTestList2() {
        Purchase purchase = new Purchase();
        purchase.setId(1L);
        purchase.setDetails("Test");
        purchase.setAmount(20.0);
        purchase.setCreatedAt(new Date());
        purchase.setUpdatedAt(new Date());
        List<Purchase> purchases = new ArrayList<>();
        purchases.add(purchase);
        return purchases;
    }

    private List<Purchase> getPurchasesTestList3() {
        Purchase purchase = new Purchase();
        purchase.setId(1L);
        purchase.setDetails("Test");
        purchase.setAmount(80.0);
        purchase.setCreatedAt(new Date());
        purchase.setUpdatedAt(new Date());
        List<Purchase> purchases = new ArrayList<>();
        purchases.add(purchase);
        return purchases;
    }

    private List<Purchase> getPurchasesTestList4() {
        Purchase purchase = new Purchase();
        purchase.setId(1L);
        purchase.setDetails("Test");
        purchase.setAmount(80.0);
        purchase.setCreatedAt(new Date());
        purchase.setUpdatedAt(new Date());

        Purchase purchaseOneMonthOld = new Purchase();
        purchaseOneMonthOld.setId(1L);
        purchaseOneMonthOld.setDetails("Test");
        purchaseOneMonthOld.setAmount(80.0);
        purchaseOneMonthOld.setCreatedAt(getBeforeMonth(Calendar.MONTH, 1));
        purchaseOneMonthOld.setUpdatedAt(getBeforeMonth(Calendar.MONTH, 1));

        Purchase purchaseThreeMonthOld = new Purchase();
        purchaseThreeMonthOld.setId(1L);
        purchaseThreeMonthOld.setDetails("Test");
        purchaseThreeMonthOld.setAmount(80.0);
        purchaseThreeMonthOld.setCreatedAt(getBeforeMonth(Calendar.MONTH, 3));
        purchaseThreeMonthOld.setUpdatedAt(getBeforeMonth(Calendar.MONTH, 3));

        List<Purchase> purchases = new ArrayList<>();
        purchases.add(purchase);
        purchases.add(purchaseOneMonthOld);
        purchases.add(purchaseThreeMonthOld);
        return purchases;
    }

    private Date getBeforeMonth(int month, int i) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(month, -i);
        return cal.getTime();
    }

    @Test(expected = CustomerNotFoundException.class)
    public void should_throw_exception_when_customer_doesnt_exist_findById() {
        Customer customer = new Customer();
        customer.setId(89L);
        customer.setName("Test Name");

        given(customerRepository.findById(anyLong())).willReturn(Optional.ofNullable(null));
        service.getRewardsForTheLastThreeMonthsByCustomerId(customer.getId());
    }

    @Test(expected = NoPurchaseHistoryFoundException.class)
    public void should_throw_exception_when_customer_doesnt_exist_NoPurchaseHistory() {
        Customer customer = new Customer();
        customer.setId(89L);
        customer.setName("Test Name");

        given(customerRepository.findById(anyLong())).willReturn(Optional.ofNullable(customer));
        service.getRewardsForTheLastThreeMonthsByCustomerId(customer.getId());
    }

    @Test(expected = CanNotCalculateRewardPointsException.class)
    public void should_throw_exception_when_customer_doesnt_exist_CanNotCalculateRewardPoints() {
        Customer customer = new Customer();
        customer.setId(89L);

        // no amount and date
        Purchase purchase = new Purchase();
        purchase.setId(1L);
        purchase.setDetails("Test");
        List<Purchase> purchases = new ArrayList<>();
        purchases.add(purchase);

        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        when(purchaseRepository.findByCustomerIdAndCreatedAtGreaterThan(any(), any())).thenReturn(purchases);

        Map<String, Double> expected = service.getRewardsForTheLastThreeMonthsByCustomerId(customer.getId());
    }
}
