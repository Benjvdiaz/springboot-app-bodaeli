package com.bodaeli.spring.eshop.springboot_app_bodaeli.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.password}")
    private String adminPassword;

    public static class LoginRequest {
        private String username;
        private String password;
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class LoginResponse {
        private boolean success;
        public LoginResponse(boolean success) { this.success = success; }
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        boolean valid = request.getUsername().equals(adminUsername) &&
                        request.getPassword().equals(adminPassword);
        return new LoginResponse(valid);
    }
}