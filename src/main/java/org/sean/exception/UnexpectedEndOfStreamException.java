package org.sean.exception;

public class UnexpectedEndOfStreamException extends Exception {
    public UnexpectedEndOfStreamException(String message) {
        super(message);
    }

    public UnexpectedEndOfStreamException() {
        super();
    }
}
