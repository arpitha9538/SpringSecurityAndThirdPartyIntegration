package org.sample.exception;

public class ProfileNotFoundException extends Exception{
    public ProfileNotFoundException(String message) {
        super(message);
    }
}
