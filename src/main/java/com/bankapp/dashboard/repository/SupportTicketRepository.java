package com.bankapp.dashboard.repository;

import com.bankapp.dashboard.model.SupportTicket;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface SupportTicketRepository extends MongoRepository<SupportTicket, String> {
    List<SupportTicket> findByUserId(String userId);
    List<SupportTicket> findByStatus(String status);
}
