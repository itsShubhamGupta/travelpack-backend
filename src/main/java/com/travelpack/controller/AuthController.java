package com.travelpack.controller;


import com.travelpack.dto.JwtResponse;
import com.travelpack.dto.RegisterRequest;
import com.travelpack.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/auth")
public class AuthController {



    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("signup")
    public ResponseEntity<Map<String,String>> signup(@Valid @RequestBody RegisterRequest registerRequest)
    {
        authService.signup(registerRequest);
        HashMap<String,String> hm=new HashMap<>();
        hm.put("message","register successfully");
        return new ResponseEntity<>(hm, HttpStatus.OK);
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> login(@RequestBody RegisterRequest userDto)
    {
        return new ResponseEntity<>(authService.login(userDto),HttpStatus.OK);
    }
}
