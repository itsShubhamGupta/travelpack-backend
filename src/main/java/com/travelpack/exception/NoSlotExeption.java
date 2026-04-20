package com.travelpack.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoSlotExeption  extends RuntimeException{

    public NoSlotExeption(String message) {
        super(message);
    }
}
