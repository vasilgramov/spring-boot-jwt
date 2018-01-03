package com.jwt_auth.controllers;

import com.jwt_auth.models.view_models.ViewUser;
import com.jwt_auth.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AdminController {

    private final UserService userService;

    @Autowired
    private AdminController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ResponseEntity<List<ViewUser>> getAllUsers() {
        List<ViewUser> allUsers = this.userService.getAllUsers();

        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }
}
