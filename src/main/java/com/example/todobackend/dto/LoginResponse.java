package com.example.todobackend.dto;

public class LoginResponse {
    private String token;
    private UserInfo user;
    public LoginResponse(String t, Integer id, String u, String e) {
        this.token = t; this.user = new UserInfo(id, u, e);
    }
    public String getToken() { return token; }
    public UserInfo getUser() { return user; }
    public static class UserInfo {
        private Integer id; private String username; private String email;
        public UserInfo(Integer id, String u, String e) { this.id = id; this.username = u; this.email = e; }
        public Integer getId() { return id; }
        public String getUsername() { return username; }
        public String getEmail() { return email; }
    }
}
