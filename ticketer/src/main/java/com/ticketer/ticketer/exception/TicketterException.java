package com.ticketer.ticketer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class TicketterException extends RuntimeException {
    public TicketterException(String message) {
        super(message);
    }
}
