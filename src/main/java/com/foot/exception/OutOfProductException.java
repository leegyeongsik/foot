package com.foot.exception;

import org.springframework.http.ResponseEntity;

public class OutOfProductException extends RuntimeException {
    public OutOfProductException(String message){
        super(message);
    }

}
