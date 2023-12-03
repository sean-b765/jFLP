package org.sean.exception;

public class EndOfStreamException extends Exception {
    public EndOfStreamException(String message) {
        super(message);
    }

    public EndOfStreamException() {
        super();
    }
}
