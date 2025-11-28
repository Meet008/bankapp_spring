package com.bankapp.dashboard.config;

import com.bankapp.dashboard.model.Users;
import com.mongodb.client.MongoClients;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;

@Configuration
@RequiredArgsConstructor
public class MongoConfig {

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    @Bean
    public MongoTemplate mongoTemplate() {
        MongoTemplate template = new MongoTemplate(
                MongoClients.create(mongoUri),
                "bankdashboard"
        );

        // ensure unique index on email for Users collection
       var indexName= template.indexOps(Users.class)
                .createIndex(
                        new Index()
                                .on("email", Sort.Direction.ASC)
                                .unique()
                );

        return template;
    }
}
