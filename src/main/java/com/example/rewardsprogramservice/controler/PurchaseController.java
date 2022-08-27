package com.example.rewardsprogramservice.controler;

import com.example.rewardsprogramservice.entity.Purchase;
import com.example.rewardsprogramservice.service.impl.PurchaseService;
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
@RequestMapping("/purchase")
public class PurchaseController {

    private final PurchaseService purchaseService;

    @Autowired
    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Purchase>> getAllPurchases() {
        return ResponseEntity.ok().body(purchaseService.findAllPurchases());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Purchase> getPurchaseById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(purchaseService.findById(id));
    }

    @PostMapping("/")
    public ResponseEntity<Purchase> createPurchase(@RequestBody Purchase purchase) {
        final Purchase data = new Purchase();
        data.setAmount(purchase.getAmount());
        data.setCustomer(purchase.getCustomer());
        data.setDetails(purchase.getDetails());
        data.setCreatedAt(new Date());
        data.setUpdatedAt(new Date());

        final Purchase purchaseSaved = purchaseService.save(data);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(purchaseSaved.getId())
                .toUri();
        return ResponseEntity.created(uri).body(purchaseSaved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Purchase> updateCustomer(@PathVariable("id") long id, @RequestBody Purchase purchase) {
        return ResponseEntity.ok().body(purchaseService.update(id, purchase));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTutorial(@PathVariable("id") long id) {
        purchaseService.delete(id);
    }
}
