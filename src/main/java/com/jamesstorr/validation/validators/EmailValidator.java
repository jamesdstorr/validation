package com.jamesstorr.validation.validators;


import com.jamesstorr.validation.model.Customer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component("emailValidator")
public class EmailValidator implements Validator {


    final List<String> badDomains = new ArrayList<>(List.of(
            "baddomain.com",
            "xitroo.com"
    ));


    @Override
    public Integer validate(Customer customer) {
        return applyValidations(customer,
                this::validateDomain,
                this::localPartRepeatedCharacters);
    }

    private int validateDomain(Customer customer){
        String domain = customer.getEmail().substring(customer.getEmail().indexOf("@") +1 ).toLowerCase();
        return badDomains.contains(domain) ? 20:0;
    }

    private int localPartRepeatedCharacters(Customer customer){
        String localPart = customer.getEmail().substring(0,customer.getEmail().indexOf("@"));
        int score = 0;
        Map<Character, Integer> characterCount = new HashMap<>();
        for (char c : localPart.toCharArray()) {
            characterCount.merge(c, 1, Integer::sum);
        }
        score += switch ((int) characterCount.entrySet()
                .stream()
                .filter(i -> i.getValue() > 1)
                .count()) {
            case 0 -> 0;
            case 1 -> 0;
            case 2 -> 1;
            case 3 -> 3;
            case 4 -> 4;
            default -> 5;
        };

        for (int count : characterCount.values()) {
            score += switch (count) {
                case 0 -> 0;
                case 1 -> 0;
                case 2 -> 1;
                case 3 -> 2;
                case 4 -> 3;
                default -> 5;
            };
        }
        return score;

    }





    @SafeVarargs
    private final int applyValidations(Customer customer, Function<Customer, Integer>... validations) {
        int riskScore = 0;
        for (Function<Customer, Integer> validation : validations) {
            riskScore += validation.apply(customer);
        }
        return riskScore;
    }
}
