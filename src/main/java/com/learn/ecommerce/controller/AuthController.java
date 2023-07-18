package com.learn.ecommerce.controller;

import com.learn.ecommerce.entity.Users;
import com.learn.ecommerce.model.JwtResponse;
import com.learn.ecommerce.model.LoginRequest;
import com.learn.ecommerce.model.RefreshTokenRequest;
import com.learn.ecommerce.model.SignUpRequest;
import com.learn.ecommerce.security.jwt.JwtUtils;
import com.learn.ecommerce.security.service.UsersDetailsImpl;
import com.learn.ecommerce.security.service.UsersDetailsServiceImpl;
import com.learn.ecommerce.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@RestController
@RequestMapping("/auth")
@EnableWebMvc
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UsersService usersService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UsersDetailsServiceImpl usersDetailsService;

    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticateUser(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        SecurityContextHolder.getContext()
                .setAuthentication(authentication);
        String token = jwtUtils.generateJwtToken(authentication);
        String refreshToken = jwtUtils.generatRefreshJwtToken(authentication);
        UsersDetailsImpl principal = (UsersDetailsImpl) authentication.getPrincipal();

        return ResponseEntity.ok().body(new JwtResponse(token, refreshToken, principal.getUsername(), principal.getEmail(), principal.getRoles()));
    }

    @PostMapping("/signup")
    public Users signUp(@RequestBody SignUpRequest signUpRequest) {
        Users users = new Users();

        users.setId(signUpRequest.getUsername());
        users.setEmail(signUpRequest.getEmail());
        users.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        users.setName(signUpRequest.getName());
        users.setRoles("user");

        usersService.insert(users);

        return users;
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<JwtResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        String token = request.getRefreshToken();
        boolean valid = jwtUtils.validateJwtToken(token);

        if (!valid) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        String username = jwtUtils.getUsernameFromJwtToken(token);
        UsersDetailsImpl usersDetails = (UsersDetailsImpl) usersDetailsService.loadUserByUsername(username);
        Authentication authentication = new UsernamePasswordAuthenticationToken(usersDetails,
                null, usersDetails.getAuthorities());
        String newToken = jwtUtils.generateJwtToken(authentication);
        String refreshToken = jwtUtils.generatRefreshJwtToken(authentication);
        return ResponseEntity.ok(new JwtResponse(newToken, refreshToken, username, usersDetails.getEmail(), usersDetails.getRoles()));

    }
}
