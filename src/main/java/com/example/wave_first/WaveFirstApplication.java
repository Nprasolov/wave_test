package com.example.wave_first;

import com.example.wave_first.entity.Presentation;
import com.example.wave_first.entity.User;
import com.example.wave_first.repository.PresentationRepo;
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
    @Autowired
    private PresentationRepo presRepo;

    public static void main(String[] args) {
        SpringApplication.run(WaveFirstApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        User user = new User();
        user.setName("User");
        user.setRole("Role");
        userRepo.save(user);

        Presentation pres= new Presentation();
        pres.setTitle("New IT tech");
        pres.setTheme("Internet of things");
        presRepo.save(pres);

        Presentation pres2= new Presentation();
        pres2.setTitle("2018 and JS");
        pres2.setTheme("Internet of things");
        presRepo.save(pres2);

        System.out.println("######################################");

        Iterable <Presentation> presentations=presRepo.findAll();
        for (Presentation item:presentations ) {
            System.out.println(item.toString());
       }
        //
    }
}
