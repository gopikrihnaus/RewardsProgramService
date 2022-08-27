package com.example.rewardsprogramservice.controler;

import com.example.rewardsprogramservice.service.impl.RewardProgramService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/rewards")
public class RewardProgramController {

    private RewardProgramService rewardProgram;

    @Autowired
    public RewardProgramController(final RewardProgramService rewardProgram) {
        this.rewardProgram = rewardProgram;
    }

    @GetMapping("/{customerId}/lastThreeMonth")
    public ResponseEntity<Map<String, Double>> getRewardsForTheLastThreeMonthsByCustomerId(@PathVariable Long customerId) {
        Map<String, Double> rewardsForTheLastThreeMonthsByCustomerId = rewardProgram.getRewardsForTheLastThreeMonthsByCustomerId(customerId);
        return new ResponseEntity<>(rewardsForTheLastThreeMonthsByCustomerId, HttpStatus.OK);
    }
}