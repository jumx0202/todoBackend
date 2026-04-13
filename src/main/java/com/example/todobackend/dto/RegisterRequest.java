package com.example.todobackend.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Set;

public class RegisterRequest {
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 50)
    private String username;
    @NotBlank(message = "密码不能为空")
    @Size(min = 6)
    private String password;
    @NotBlank @Email
    private String email;
    public String getUsername() { return username; }
    public void setUsername(String u) { this.username = u; }
    public String getPassword() { return password; }
    public void setPassword(String p) { this.password = p; }
    public String getEmail() { return email; }
    public void setEmail(String e) { this.email = e; }
}
