package com.hms.HospitalMS.controller;
import java.util.*; import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*; import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.hms.HospitalMS.Security.JwtUtil; import com.hms.HospitalMS.model.User;
import com.hms.HospitalMS.repository.UserRepository;
@RestController @RequestMapping("/api/auth") @CrossOrigin(origins = "*")
public class AuthController {
    @Autowired private UserRepository repo;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtUtil jwtUtil;
    @PostMapping("/register")
    public String register(@RequestBody User user) {
        if (repo.findByUsername(user.getUsername()).isPresent()) return "Username already exists";
        user.setPassword(passwordEncoder.encode(user.getPassword())); repo.save(user);
        return "User registered successfully";
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        Optional<User> existing = repo.findByUsername(user.getUsername());
        if (existing.isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username");
        if (!passwordEncoder.matches(user.getPassword(), existing.get().getPassword()))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
        String token = jwtUtil.generateToken(existing.get().getUsername());
        return ResponseEntity.ok(Map.of("token", token));
    }
}
