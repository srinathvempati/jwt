package com.sri.jwt.controller;

import com.sri.jwt.entity.JwtRequest;
import com.sri.jwt.entity.JwtResponse;
import com.sri.jwt.service.JwtService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class JwtController {

    @Autowired
    private JwtService jwtService;

    @PostMapping({"/authenticate"})
    public JwtResponse createJwtToken(@RequestBody JwtRequest jwtRequest) throws Exception {
        return jwtService.createJwtToken(jwtRequest);
    }
    
    @PostMapping("/refresh-token")
    public JwtResponse refreshJwtToken(@RequestBody Map<String, String> body) throws Exception {
        String refreshToken = body.get("refreshToken");
        return jwtService.refreshAccessToken(refreshToken);
    }
}
