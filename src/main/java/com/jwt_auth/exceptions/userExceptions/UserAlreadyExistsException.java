package com.jwt_auth.exceptions.userExceptions;

public class UserAlreadyExistsException extends UserException {

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
