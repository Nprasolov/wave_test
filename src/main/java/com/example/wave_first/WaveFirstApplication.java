package com.example.wave_first;

import com.example.wave_first.entity.User;
import com.example.wave_first.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class WaveFirstApplication implements CommandLineRunner{

    @Autowired
    private UserRepo userRepo;

    public static void main(String[] args) {
        SpringApplication.run(WaveFirstApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        User user = new User();
        user.setName("User");
        user.setRole("Role");
        userRepo.save(user);
        //
    }
}
