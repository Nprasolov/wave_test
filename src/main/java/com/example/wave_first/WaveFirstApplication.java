package com.example.wave_first;

import com.example.wave_first.entity.*;
import com.example.wave_first.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
@EnableJpaRepositories
public class WaveFirstApplication implements CommandLineRunner{

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PresentationRepo presRepo;

    @Autowired
    private UsPrRepo usPrRepo;
    @Autowired
    private ScheduleRepo scheduleRepo;

    @Autowired
    private RoomRepo roomRepo;

    public static void main(String[] args) {
        SpringApplication.run(WaveFirstApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {


        User user = new User();
        user.setName("Ben");
        user.setRole("Presenter");
        user.setPassword("B");
        userRepo.save(user);

        User user2 = new User();
        user2.setName("Danila");
        user2.setRole("Presenter");
        user2.setPassword("A");
        userRepo.save(user2);

        User admin = new User();
        admin.setName("admin");
        admin.setRole("Admin");
        admin.setPassword("123456");
        userRepo.save(admin);

        Set<User> userSet=new HashSet<>();
        userSet.add(user);
        userSet.add(user2);
        Presentation pres= new Presentation("New IT tech","Internet of things");
        presRepo.save(pres);

        Presentation pres2= new Presentation("2018 and JS","Internet of things");
        //pres2.setUserSet(userSet);
        presRepo.save(pres2);

        Room room1=new Room();
        room1.setNumber((long) 314);
        roomRepo.save(room1);

        Room room2=new Room();
        room2.setNumber((long) 212);
        roomRepo.save(room2);

        UserPresentation uspr1=new UserPresentation((long) 1,(long) 1);
        usPrRepo.save(uspr1);

        UserPresentation uspr2=new UserPresentation((long) 1,(long) 2);
        usPrRepo.save(uspr2);

        UserPresentation uspr3=new UserPresentation((long) 2,(long) 2);
        usPrRepo.save(uspr3);

        Schedule schedule1=new Schedule();
        schedule1.setPresentation_id((long) 1);
        schedule1.setRoom_id((long)1);
        scheduleRepo.save(schedule1);

        Schedule schedule2=new Schedule();
        schedule2.setPresentation_id((long) 1);
        schedule2.setRoom_id((long)1);
        scheduleRepo.save(schedule2);

        System.out.println("######################################");

        Iterable <Presentation> presentations=presRepo.findAll();
        for (Presentation item:presentations ) {
            System.out.println(item.toString());
       }
        System.out.println("######################################");

        Iterable <User> users=userRepo.findAll();
        for (User item2:users ) {
            System.out.println(item2.toString());
        }
        //
    }
}
