package com.jamesstorr.validation.validators;

import com.jamesstorr.validation.model.Customer;

public interface Validator {
    Integer validate(Customer customer);
}
