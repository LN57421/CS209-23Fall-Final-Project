package edu.sustech.cs209a.java2finalprojectdemo.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException() {
        super("Bad request parameter");
    }
}
