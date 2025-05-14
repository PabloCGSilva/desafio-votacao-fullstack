// src/main/java/com/voting/exception/VotingException.java
package com.voting.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class VotingException extends RuntimeException {

    public VotingException(String message) {
        super(message);
    }
}
