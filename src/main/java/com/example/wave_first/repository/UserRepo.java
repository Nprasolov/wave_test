package com.example.wave_first.repository;

import com.example.wave_first.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends CrudRepository<User, Long> {
    User findUserByName(String name);
}
