package com.jwt_auth.services;

import com.jwt_auth.exceptions.userExceptions.InvalidRegisterException;
import com.jwt_auth.exceptions.userExceptions.UserAlreadyExistsException;
import com.jwt_auth.models.binding_models.RegisterUser;
import com.jwt_auth.models.view_models.ViewUser;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    void register(RegisterUser registerUser) throws InvalidRegisterException, UserAlreadyExistsException;

    ViewUser getUserData(String username);

    List<ViewUser> getAllUsers();
}
