package com.jwt_auth.services;

import com.jwt_auth.entities.User;
import com.jwt_auth.exceptions.userExceptions.InvalidRegisterException;
import com.jwt_auth.exceptions.userExceptions.UserAlreadyExistsException;
import com.jwt_auth.models.binding_models.RegisterUser;
import com.jwt_auth.models.view_models.ViewUser;
import com.jwt_auth.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User user = this.userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        }

        return user;
    }

    @Override
    public void register(RegisterUser registerUser) throws InvalidRegisterException, UserAlreadyExistsException {
        User user = new User();

        if (!registerUser.getPassword().equals(registerUser.getRepeatPassword())) {
            throw new InvalidRegisterException("Passwords do not match!");
        }

        if (this.userRepository.findByUsername(registerUser.getUsername()) != null)  {
            throw new UserAlreadyExistsException("Email already exists!");
        }

        /*
          add validating password logic:
                more that 6 characters; less that 100 characters
                containing special symbols
         */

        user.setUsername(registerUser.getUsername());
        user.setPassword(this.passwordEncoder.encode(registerUser.getPassword()));
        user.setFirstName(registerUser.getFirstName());
        user.setLastName(registerUser.getLastName());

        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);

        this.userRepository.saveAndFlush(user);
    }

    @Override
    public ViewUser getUserData(String username) {
        User byUsername = this.userRepository.findByUsername(username);

        ViewUser viewUser = new ViewUser();
        viewUser.setUsername(byUsername.getUsername());
        viewUser.setFirstName(byUsername.getFirstName());
        viewUser.setLastName(byUsername.getLastName());

        return viewUser;
    }

    @Override
    public List<ViewUser> getAllUsers() {

        List<User> users = this.userRepository.findAll();
        List<ViewUser> mappedUsers = new ArrayList<>();

        for (User user : users) {
            ViewUser viewUser = new ViewUser();

            viewUser.setUsername(user.getUsername());
            viewUser.setFirstName(user.getFirstName());
            viewUser.setLastName(user.getLastName());

            mappedUsers.add(viewUser);
        }

        return mappedUsers;
    }
}
