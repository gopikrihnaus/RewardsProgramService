package com.example.rewardsprogramservice.service.impl;

import com.example.rewardsprogramservice.entity.Customer;
import com.example.rewardsprogramservice.entity.Purchase;
import com.example.rewardsprogramservice.exception.CanNotCalculateRewardPointsException;
import com.example.rewardsprogramservice.exception.CustomerNotFoundException;
import com.example.rewardsprogramservice.exception.NoPurchaseHistoryFoundException;
import com.example.rewardsprogramservice.repoitory.CustomerRepository;
import com.example.rewardsprogramservice.repoitory.PurchaseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormatSymbols;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RewardProgramService {

    private final PurchaseRepository purchaseRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public RewardProgramService(final CustomerRepository customerRepository,
                                final PurchaseRepository purchaseRepository) {
        this.customerRepository = customerRepository;
        this.purchaseRepository = purchaseRepository;
    }

    public Map<String, Double> getRewardsForTheLastThreeMonthsByCustomerId(Long customerId) {
        Date calTime = getBeforeMonth(Calendar.MONTH, 3);

        Optional<Customer> customer = customerRepository.findById(customerId);
        if (!customer.isPresent()) {
            throw new CustomerNotFoundException("100", "No customer found with this id");
        }

        List<Purchase> purchases = purchaseRepository.findByCustomerIdAndCreatedAtGreaterThan(customerId, calTime);

        if (!purchases.isEmpty()) {
            try {
                // points by month name
                final Map<String, Double> pointsByMonth = getPointsByMonth(purchases);

                log.info(pointsByMonth.toString());

                // all 3 months
                double points = getPoints(purchases);
                pointsByMonth.put("Total", points);
                return pointsByMonth;
            } catch (Exception exception) {
                throw new CanNotCalculateRewardPointsException("102", "Can Not Calculate Reward Points, please check the entered data");
            }
        } else {
            throw new NoPurchaseHistoryFoundException("101", "No Purchase Records Found for this customer in the last three months");
        }
    }


    private Date getBeforeMonth(int month, int i) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(month, -i);
        return cal.getTime();
    }

    private Map<String, Double> getPointsByMonth(List<Purchase> purchases) {
        // group the data by month
        final Map<Integer, List<Purchase>> purchasesByMonth = purchases.stream()
                .collect(Collectors.groupingBy(purchase -> purchase.getCreatedAt().getMonth()));

        // calculate points for each month
        final Map<String, Double> pointsByMonth = new LinkedHashMap<>();
        purchasesByMonth.forEach((key, purchaseList) -> {
            if (!purchaseList.isEmpty()) {
                double points = getPoints(purchaseList);
                pointsByMonth.put(getMonthFromIntValue(key), points);
            }
        });
        return pointsByMonth;
    }

    /**
     * <ul>
     *      <li>2 points for every dollar spent over $100 in each transaction
     *      <li>plus 1 point for every dollar spent over $50 in each transaction
     * <ul/>
     *
     * @param purchases
     * @return points
     */
    private double getPoints(List<Purchase> purchases) {
        double points = 0.0;
        for (Purchase purchase : purchases) {
            Double amount = purchase.getAmount();
            if (amount > 100.0) {
                points += (amount - 100) * 2 + 50;
            } else if (amount > 50.0 && amount <= 100.0) {
                points += (amount - 50);
            } else {
                points += 0;
            }
        }
        return points;
    }

    private String getMonthFromIntValue(int m) {
        String monthName = " ";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (m >= 0 && m <= 11) {
            monthName = months[m];
        }
        return monthName;
    }

}
