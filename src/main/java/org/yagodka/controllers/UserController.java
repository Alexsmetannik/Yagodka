package org.yagodka.controllers;

import org.yagodka.models.User;
import org.yagodka.services.AuthService;
import org.yagodka.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id,
                                        @RequestHeader("Authorization") String token) {
        if (authService.validateToken(token)) {
            User user = userService.getUserById(id);
            if (user != null) {
                return ResponseEntity.ok(user);
            }
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
