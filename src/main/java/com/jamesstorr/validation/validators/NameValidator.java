package com.jamesstorr.validation.validators;

import com.jamesstorr.validation.model.Customer;
import org.springframework.stereotype.Component;

import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.HashMap;
import java.util.Map;

@Component("nameValidator")
public class NameValidator implements Validator {

    public Integer validate(Customer customer) {
        return applyValidations(customer,
                this::checkFirstNameLength,
                this::checkLastNameLength,
                this::repeatedCharacters,
                this::checkConsecutiveIdenticalCharacters,
                this::checkForSequentialCharacters);
    }

    private int checkFirstNameLength(Customer customer) {
        int riskScore = 0;
        switch (customer.getFirstName().length()) {
            case 1 -> riskScore += 5;
            case 2 -> riskScore += 4;
            case 3 -> riskScore += 3;
        }
        return riskScore;
    }

    private int checkLastNameLength(Customer customer) {
        int riskScore = 0;
        switch (customer.getLastName().length()) {
            case 1 -> riskScore += 5;
            case 2 -> riskScore += 4;
            case 3 -> riskScore += 3;
        }
        return riskScore;
    }

    private int repeatedCharacters(Customer customer) {
        int score = 0;
        Map<Character, Integer> characterCount = new HashMap<>();
        for (char c : customer.getFirstName().toCharArray()) {
            characterCount.merge(c, 1, Integer::sum);
        }
        for (char c : customer.getLastName().toCharArray()) {
            characterCount.merge(c, 1, Integer::sum);
        }

        score += switch ((int) characterCount.entrySet()
                .stream()
                .filter(i -> i.getValue() > 1)
                .count()) {
            case 0 -> 0;
            case 1 -> 0;
            case 2 -> 4;
            case 3 -> 6;
            case 4 -> 8;
            default -> 10;
        };

        for (int count : characterCount.values()) {
            score += switch (count) {
                case 0 -> 0;
                case 1 -> 0;
                case 2 -> 2;
                case 3 -> 4;
                case 4 -> 6;
                default -> 8;
            };
        }
        return score;
    }

    private int checkConsecutiveIdenticalCharacters(Customer customer) {
        int score = 0;
        for (int i = 1; i < customer.getFirstName().length(); i++) {
            if (customer.getFirstName().charAt(i) == customer.getFirstName().charAt(i - 1)) {
                score += 3;
            }
        }
        for (int i = 1; i < customer.getLastName().length(); i++) {
            if (customer.getLastName().charAt(i) == customer.getLastName().charAt(i - 1)) {
                score += 3;
            }
        }
        return score;
    }

    private int checkForSequentialCharacters(Customer customer) {
        return checkSequential(customer.getFirstName()) + checkSequential(customer.getLastName());
    }

    private int checkSequential(String name) {
        if (name.length() > 1) {
            long seqLength = IntStream.range(1, name.length())
                    .filter(i -> name.charAt(i) == name.charAt(i - 1) + 1)
                    .count();
            return switch ((int) seqLength) {
                case 0 -> 0;
                case 1 -> 4;
                default -> 10;
            };
        }
        return 0;
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