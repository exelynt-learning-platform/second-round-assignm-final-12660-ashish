package com.ecommerce.auth;

import com.ecommerce.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.ecommerce.dto.AuthRequest;
import com.ecommerce.entity.User;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.config.JwtUtil;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository repo;
    private final JwtUtil jwt;

    @PostMapping("/register")
    public String register(@RequestBody AuthRequest req) {

        User user = new User(
                null,
                req.getUsername(),
                req.getPassword(),
                Role.USER   // 👈 IMPORTANT
        );

        repo.save(user);
        return "Registered";
    }
    @PostMapping("/login")
    public String login(@RequestBody AuthRequest req) {
        User u = repo.findByUsername(req.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!u.getPassword().equals(req.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return jwt.generateToken(u.getUsername());
    }
}
