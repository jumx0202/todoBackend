package com.example.todobackend.controller;

import com.example.todobackend.common.Result;
import com.example.todobackend.dto.*;
import com.example.todobackend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired private AuthService authService;

    @PostMapping("/register")
    public Result<?> register(@Valid @RequestBody RegisterRequest req) { return authService.register(req); }

    @PostMapping("/login")
    public Result<?> login(@Valid @RequestBody LoginRequest req) { return authService.login(req); }
}
