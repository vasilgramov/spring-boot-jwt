package com.jwt_auth.security.controllers;

import com.jwt_auth.models.binding_models.LoginUser;
import com.jwt_auth.models.view_models.AuthenticationResponse;
import com.jwt_auth.security.JwtTokenUtil;
import com.jwt_auth.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class AuthenticationController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userDetailsService;

    @RequestMapping(value = "${jwt.route.authentication.path}", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginUser authenticationRequest, Device device) throws AuthenticationException {

        // Perform the security
        final Authentication authentication = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Reload password post-security so we can generate token
        final UserDetails userDetails = this.userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = this.jwtTokenUtil.generateToken(userDetails, device);

        // Return the token
        return new ResponseEntity<>(new AuthenticationResponse(token), HttpStatus.OK);
    }

    @RequestMapping(value = "${jwt.route.authentication.refresh}", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
        String token = request.getHeader(this.tokenHeader);

        // Successfully returning refreshed token
        String refreshedToken = this.jwtTokenUtil.refreshToken(token);
        return new ResponseEntity<>(new AuthenticationResponse(refreshedToken), HttpStatus.OK);
    }
}
