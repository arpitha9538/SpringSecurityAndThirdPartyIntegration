package org.sample.controllers;

import org.sample.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class AuthController {

    private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    TokenService tokenService;

    /**
     * This will give the currently authenticated user's information.
     */
    @GetMapping("/")
    public String home(Principal principal) {
        return "Hello, " + principal.getName();
    }

    /**
     * This will check whether the user has the necessary authorization.
     */
    @PreAuthorize("hasAuthority('SCOPE_read')")
    @GetMapping("/secure")
    public String secure() {
        return "This is secured!";
    }


    /**
     * Generates an authentication token for the currently authenticated user.
     */
    @PostMapping("/token")
    public String token(Authentication authentication) {
        System.out.println("Token requested for user: '{}'" + authentication.getName());
        String token = tokenService.generateToken(authentication);
        System.out.println("Token granted: {}" + token);
        return token;
    }

}
