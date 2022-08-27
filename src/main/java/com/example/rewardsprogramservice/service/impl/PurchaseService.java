package com.example.rewardsprogramservice.service.impl;

import com.example.rewardsprogramservice.entity.Customer;
import com.example.rewardsprogramservice.entity.Purchase;
import com.example.rewardsprogramservice.exception.CustomerNotFoundException;
import com.example.rewardsprogramservice.exception.NoPurchaseHistoryFoundException;
import com.example.rewardsprogramservice.repoitory.PurchaseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;

    @Autowired
    public PurchaseService(final PurchaseRepository purchaseRepository) {
        this.purchaseRepository = purchaseRepository;
    }

    public Purchase findById(Long id) {
        return this.purchaseRepository.findById(id)
                .orElseThrow(() -> new NoPurchaseHistoryFoundException("200", "Purchase with given id not found"));
    }

    public List<Purchase> findAllPurchases() {
        return purchaseRepository.findAll();
    }

    public Purchase save(Purchase purchase) {
        return purchaseRepository.save(purchase);
    }

    public void delete(Long id) {
        purchaseRepository.findById(id)
                .orElseThrow(() -> new NoPurchaseHistoryFoundException("200", "Purchase with given id not found"));

        purchaseRepository.deleteById(id);
    }

    public Purchase update(Long id, Purchase purchase) {
        Purchase purchaseOld = purchaseRepository.findById(id)
                .orElseThrow(() -> new NoPurchaseHistoryFoundException("200", "Purchase with given id not found"));

        purchase.setId(id);
        purchase.setCreatedAt(purchaseOld.getCreatedAt());
        purchase.setUpdatedAt(new Date());
        return purchaseRepository.save(purchase);

    }

}
