package com.jamesstorr.validation.controller;

import com.jamesstorr.validation.model.Customer;
import com.jamesstorr.validation.service.ValidatorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    private final ValidatorService validatorService;

    public CustomerController(ValidatorService validatorService) {
        this.validatorService = validatorService;
    }

    @GetMapping
    public ResponseEntity<Boolean> validateCustomer(@RequestBody Customer customer) {
        return ResponseEntity.status(HttpStatus.OK).body(validatorService.validate(customer));
    }

}
