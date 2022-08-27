package com.example.rewardsprogramservice.repoitory;

import com.example.rewardsprogramservice.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    List<Purchase> findByCustomerIdAndCreatedAtGreaterThan(Long postId, Date date);
}