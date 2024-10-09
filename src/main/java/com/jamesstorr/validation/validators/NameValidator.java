package com.jamesstorr.validation.validators;

import com.jamesstorr.validation.model.Customer;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.IntStream;

@Component("nameValidator")
public class NameValidator implements Validator {

    @Override
    public Integer validate(Customer customer) {

        int riskScore = 0;

        //firstname length check
        switch(customer.getFirstName().length()) {
            case 1 -> riskScore += 5;
            case 2 -> riskScore += 4;
            case 3 -> riskScore += 3;
        }
        System.out.println("After first name length check: " + riskScore);

        //surname length check
        switch(customer.getLastName().length()) {
            case 1 -> riskScore += 5;
            case 2 -> riskScore += 4;
            case 3 -> riskScore += 3;
        }
        System.out.println("After last name length check: " + riskScore);

        riskScore += repeatedCharacters(customer.getFirstName());
        riskScore += repeatedCharacters(customer.getLastName());
        System.out.println("After repeated Character Check: " + riskScore);

        riskScore += checkConsecutiveIdenticalCharacters(customer.getFirstName());
        riskScore += checkConsecutiveIdenticalCharacters(customer.getLastName());
        System.out.println("After consecutive character check: " + riskScore);

        riskScore += checkForSequentialCharacters(customer.getFirstName());
        riskScore += checkForSequentialCharacters(customer.getLastName());
        System.out.println("After sequential check: " + riskScore);

        return riskScore;
    }

    private int repeatedCharacters(String name){
        int score = 0;
        Map<Character,Integer> characterCount = new HashMap<>();
        for(char c : name.toCharArray()){
            characterCount.merge(c,1, Integer::sum);
        }
        score += switch((int) characterCount.entrySet()
                .stream()
                .filter(i -> i.getValue() > 1)
                .count())
               {
            case 0 -> 0;
            case 1 -> 0;
            case 2 -> 4;
            case 3  -> 6;
            case 4 -> 8;
            default -> 10;
        };

        for(int count : characterCount.values()){
            score += switch(count){
                case 0 -> 0;
                case 1 -> 0;
                case 2 -> 2;
                case 3  -> 4;
                case 4 -> 6;
                default -> 8;
            };
        }

        return score;
    }

    private int checkConsecutiveIdenticalCharacters(String name) {
        int score = 0;
        for (int i = 1; i < name.length(); i++) {
            if (name.charAt(i) == name.charAt(i - 1)) {
                score += 3;
            }
        }
        return score;
    }

    private int checkForSequentialCharacters(String name) {
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
}
