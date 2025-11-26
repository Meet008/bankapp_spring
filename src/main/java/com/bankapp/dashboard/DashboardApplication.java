package com.bankapp.dashboard;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootApplication
public class DashboardApplication implements CommandLineRunner {

    private final MongoTemplate mongoTemplate;

    // Inject MongoTemplate from MongoConfig
    public DashboardApplication(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void run(String... args) {
        System.out.println("MongoTemplate created successfully.");
        System.out.println("Connected to DB: " + mongoTemplate.getDb().getName());
    }

    public static void main(String[] args) {
        SpringApplication.run(DashboardApplication.class, args);
    }
}
