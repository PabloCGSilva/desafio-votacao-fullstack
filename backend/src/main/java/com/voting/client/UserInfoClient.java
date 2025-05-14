// src/main/java/com/voting/client/UserInfoClient.java
package com.voting.client;

import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class UserInfoClient {

    private final Random random = new Random();

    public enum VoteAbility {
        ABLE_TO_VOTE, UNABLE_TO_VOTE
    }

    /**
     * Validates if a CPF is valid
     *
     * @param cpf CPF to be validated
     * @return true if valid, false otherwise
     */
    public boolean isValidCpf(String cpf) {
        // Remove non-digit characters
        cpf = cpf.replaceAll("\\D", "");

        // Check length and repeated digits
        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        // Calculate first verification digit
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
        }
        int remainder = (sum * 10) % 11;
        int digit1 = (remainder == 10) ? 0 : remainder;

        // Calculate second verification digit
        sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
        }
        remainder = (sum * 10) % 11;
        int digit2 = (remainder == 10) ? 0 : remainder;

        // Check if calculated digits match provided ones
        return Character.getNumericValue(cpf.charAt(9)) == digit1
                && Character.getNumericValue(cpf.charAt(10)) == digit2;
    }

    /**
     * Checks if a user with the given CPF can vote
     *
     * @param cpf CPF of the user
     * @return ABLE_TO_VOTE or UNABLE_TO_VOTE
     * @throws NotFoundException if CPF is invalid
     */
    public VoteAbility checkUserAbilityToVote(String cpf) throws NotFoundException {
        if (!isValidCpf(cpf)) {
            throw new NotFoundException("CPF not found or invalid");
        }

        // Return random result (70% chance of being able to vote)
        // as required by Bonus Task 1
        return random.nextDouble() < 0.7 ? VoteAbility.ABLE_TO_VOTE : VoteAbility.UNABLE_TO_VOTE;
    }

    public static class NotFoundException extends RuntimeException {

        public NotFoundException(String message) {
            super(message);
        }
    }
}
