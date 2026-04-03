package com.example.stockcontroller.exception;

public class MyResourceNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public MyResourceNotFoundException(String msg) {
        super(msg);
    }
}