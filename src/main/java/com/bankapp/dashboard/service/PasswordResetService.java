package com.bankapp.dashboard.service;

import com.bankapp.dashboard.dto.PasswordResetDTO;
import com.bankapp.dashboard.dto.PasswordResetRequestDTO;
import com.bankapp.dashboard.model.PasswordResetToken;
import com.bankapp.dashboard.model.Users;
import com.bankapp.dashboard.repository.PasswordResetTokenRepository;
import com.bankapp.dashboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;

    private static final int EXPIRATION_MINUTES = 30;

    // Request reset -> generate token
    public String requestPasswordReset(PasswordResetRequestDTO request) {
        Users user = userRepository.findByEmail(request.getEmail());
        if (user == null) {
            return null;
        }

        String token = UUID.randomUUID().toString();
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(EXPIRATION_MINUTES);

        PasswordResetToken resetToken = new PasswordResetToken(user.getId(), token, expiry);
        tokenRepository.save(resetToken);

        // TODO: send token via email with link like: http://yourfrontend.com/reset?token=...
        return token;
    }

    // Perform actual reset
    public void resetPassword(PasswordResetDTO dto) {
        Optional<PasswordResetToken> tokenOpt = tokenRepository.findByToken(dto.getToken());
        if (tokenOpt.isEmpty()) {
            throw new RuntimeException("Invalid password reset token");
        }

        PasswordResetToken token = tokenOpt.get();
        if (token.isExpired()) {
            tokenRepository.delete(token);
            throw new RuntimeException("Password reset token has expired");
        }

        Users user = userRepository.findById(token.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);

        // delete token after successful reset
        tokenRepository.delete(token);
    }
}
