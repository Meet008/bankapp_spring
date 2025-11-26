package com.bankapp.dashboard.model;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data // Lombok: getters, setters, toString, equals, hashCode
@NoArgsConstructor // Lombok: no-args constructor
@AllArgsConstructor // Lombok: constructor with all fields
@Document(collection = "users") // Maps to MongoDB "users" collection

public class Users {
    @Id
    private String id;
    private String name;
    private String email;
    private String password; // Store hashed for security!
    private String phone;
}
