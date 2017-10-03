package com.bytebazar.toocold2.exceptions;

public class EmptyDbException extends Exception {
    public EmptyDbException() {
    }

    public EmptyDbException(String message) {
        super(message);
    }
}
