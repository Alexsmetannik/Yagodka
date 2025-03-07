package org.yagodka.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

    @GetMapping("/dashboard")
    public String dashboard() {
        return "Welcome to your dashboard!";
    }

    @GetMapping("/logout")
    public String logout() {
        return "Logged out successfully!";
    }
}
