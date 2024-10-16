package com.jamesstorr.validation.model;

import lombok.Data;

@Data
public class Customer {
    private String firstName;
    private String lastName;
    private String email;
    private Address address;
}
