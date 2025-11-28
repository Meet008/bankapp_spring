package com.bankapp.dashboard.model;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

@Data // Lombok: getters, setters, toString, equals, hashCode
@NoArgsConstructor // Lombok: no-args constructor
@AllArgsConstructor // Lombok: constructor with all fields
@Document(collection = "users") // Maps to MongoDB "users" collection

public class Users {
    @Id
    private String id;
    private String name;

    @Indexed(unique = true)
    private String email;

    private String password;
    private String phone;
    private String address;
    private String avatarUrl;
    private Role role; // ENUM: ADMIN or CUSTOMER
}
