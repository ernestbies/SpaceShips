package com.shipsgame.domain.dto;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="Error order")
public class ErrorOrder extends RuntimeException {
    public ErrorOrder(String message){
        super(message);
    }
}
