package com.example.iot_dashboard.repository;

import com.example.iot_dashboard.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
}
