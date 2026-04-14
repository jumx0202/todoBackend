package com.example.todobackend.service;

import com.example.todobackend.common.BusinessException;
import com.example.todobackend.common.Result;
import com.example.todobackend.config.JwtUtil;
import com.example.todobackend.dto.LoginRequest;
import com.example.todobackend.dto.LoginResponse;
import com.example.todobackend.dto.RegisterRequest;
import com.example.todobackend.entity.User;
import com.example.todobackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired private UserRepository userRepository;
    @Autowired private JwtUtil jwtUtil;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public Result<LoginResponse> register(RegisterRequest req) {
        if (userRepository.existsByUsername(req.getUsername()))
            throw new BusinessException("用户名已存在");
        if (userRepository.existsByEmail(req.getEmail()))
            throw new BusinessException("邮箱已被注册");
        User u = new User();
        u.setUsername(req.getUsername());
        u.setPassword(encoder.encode(req.getPassword()));
        u.setEmail(req.getEmail());
        userRepository.save(u);
        return Result.ok("注册成功");
    }

    public Result<LoginResponse> login(LoginRequest req) {
        User u = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new BusinessException(401, "用户名或密码错误"));
        if (!encoder.matches(req.getPassword(), u.getPassword()))
            throw new BusinessException(401, "用户名或密码错误");
        String token = jwtUtil.generateToken(u.getId(), u.getUsername());
        return Result.ok(new LoginResponse(token, u.getId(), u.getUsername(), u.getEmail()));
    }
}
