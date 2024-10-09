package com.jamesstorr.validation.service;

import com.jamesstorr.validation.model.Customer;
import com.jamesstorr.validation.validators.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ValidatorServiceImpl implements ValidatorService {

    private final Map<String, Validator> validators;

    @Autowired
    public ValidatorServiceImpl(final Map<String, Validator> validators){
        this.validators = validators;
    }

    @Override
    public boolean validate(Customer customer) {
        Map<String, Integer> validationResults = new HashMap<>();
        validators.forEach((key, validator)-> validationResults.put(key, validator.validate(customer)));
        validationResults.forEach((k,v) -> System.out.println("%s: %d".formatted(k, v)));
        int total = validationResults.values().stream().mapToInt(Integer::intValue).sum();
        var median = total / 2;
        System.out.println("Median: " + median);
        return !(median >= 8);
    }
}
