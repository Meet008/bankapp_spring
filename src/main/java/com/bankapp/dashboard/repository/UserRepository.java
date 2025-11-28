package com.bankapp.dashboard.repository;

import com.bankapp.dashboard.model.Role;
import com.bankapp.dashboard.model.Users;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<Users, String> {
    Users findByEmail(String email);
    List<Users> findByRole(Role role);
    Long deleteByEmail(String email);
}

