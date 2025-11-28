package com.bankapp.dashboard.repository;

import com.bankapp.dashboard.model.AccountType;
import com.bankapp.dashboard.model.Accounts;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface AccountRepository extends MongoRepository<Accounts, String> {
    List<Accounts> findByUserId(String userId);
    List<Accounts> findByType(AccountType type);
}
