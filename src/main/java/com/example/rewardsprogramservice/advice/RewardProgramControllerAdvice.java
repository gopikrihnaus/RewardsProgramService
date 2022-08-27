package com.example.rewardsprogramservice.advice;

import com.example.rewardsprogramservice.exception.CanNotCalculateRewardPointsException;
import com.example.rewardsprogramservice.exception.CustomerNotFoundException;
import com.example.rewardsprogramservice.exception.NoPurchaseHistoryFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class RewardProgramControllerAdvice {

    @ExceptionHandler(CanNotCalculateRewardPointsException.class)
    public ResponseEntity<String> handleCanNotCalculateRewardPoints(CanNotCalculateRewardPointsException canNotCalculateRewardPointsException) {
        return new ResponseEntity<>(canNotCalculateRewardPointsException.getErrorMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoPurchaseHistoryFoundException.class)
    public ResponseEntity<String> handleNoPurchaseHistoryFoundException(NoPurchaseHistoryFoundException noPurchaseHistoryFoundException) {
        return new ResponseEntity<>(noPurchaseHistoryFoundException.getErrorMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<String> handleCustomerNotFoundException(CustomerNotFoundException customerNotFoundException) {
        return new ResponseEntity<>(customerNotFoundException.getErrorMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handle(Exception exception, HttpServletRequest request, HttpServletResponse response) {
        if (exception instanceof NullPointerException) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
