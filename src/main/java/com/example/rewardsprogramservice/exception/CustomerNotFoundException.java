package com.example.rewardsprogramservice.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerNotFoundException extends RuntimeException {
    private String errorCode;
    private String errorMessage;
}
