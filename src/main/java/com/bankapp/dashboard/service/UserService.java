package com.bankapp.dashboard.service;

import com.bankapp.dashboard.model.Role;
import com.bankapp.dashboard.model.Users;
import com.bankapp.dashboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public Users findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<Users> findByRole(Role role) {
        return userRepository.findByRole(role);
    }


    public Users getById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    public boolean deleteUserById(String id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Delete all users with a given email, returns count
    public long deleteAllByEmail(String email) {
        return userRepository.deleteByEmail(email);
    }
}
