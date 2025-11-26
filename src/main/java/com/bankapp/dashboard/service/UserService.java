package com.bankapp.dashboard.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.bankapp.dashboard.repository.UserRepository;
import com.bankapp.dashboard.model.Users;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    public Users createUser(Users user) {
        return userRepository.save(user);
    }

    public Users getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
