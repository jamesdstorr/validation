package com.jamesstorr.validation.validators;


import com.jamesstorr.validation.model.Customer;
import org.springframework.stereotype.Component;

@Component("emailValidator")
public class EmailValidator implements Validator {
    @Override
    public Integer validate(Customer customer) {
        return 1;
    }
}
