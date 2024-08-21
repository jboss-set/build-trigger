package io.xstefank.exception;

public class PNCIndexNotLoadedException extends Exception {
    public PNCIndexNotLoadedException(Exception e) {
        super(e);
    }

    public PNCIndexNotLoadedException(String message) {
        super(message);
    }
}
