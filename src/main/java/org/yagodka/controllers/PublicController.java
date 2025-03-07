package org.yagodka.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
public class PublicController {

    @GetMapping("/login")
    public String loginPage() {
        return "Please login at /login (use POST request with username and password)";
    }

    @GetMapping("/logout-success")
    public String logoutSuccess() {
        return "You have been logged out successfully!";
    }
}
