package com.bankapp.dashboard.repository;

import com.bankapp.dashboard.model.Users;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<Users, String> {
    Users findByEmail(String email);
}
