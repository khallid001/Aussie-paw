package com.dogstore.dogsellingapp.service;

import com.dogstore.dogsellingapp.dto.*;
import com.dogstore.dogsellingapp.model.Role;
import com.dogstore.dogsellingapp.model.User;
import com.dogstore.dogsellingapp.repository.UserRepository;
import com.dogstore.dogsellingapp.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;

    public MessageResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("User already exists with email: " + request.getEmail());
        }

        String code = String.format("%06d", new Random().nextInt(999999));

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .verified(false)
                .verificationCode(code)
                .build();

        userRepository.save(user);
        emailService.sendVerificationCode(request.getEmail(), code);

        return new MessageResponse("Registration successful. Please check your email for the verification code.");
    }

    public MessageResponse verify(VerifyRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + request.getEmail()));

        if (user.isVerified()) {
            return new MessageResponse("Account is already verified.");
        }

        if (!request.getCode().equals(user.getVerificationCode())) {
            throw new IllegalArgumentException("Invalid verification code.");
        }

        user.setVerified(true);
        user.setVerificationCode(null);
        userRepository.save(user);

        return new MessageResponse("Account verified successfully.");
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials."));

        if (!user.isVerified()) {
            throw new IllegalStateException("Account is not verified. Please verify your email first.");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials.");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        return new AuthResponse(token, user.getRole().name());
    }
}
