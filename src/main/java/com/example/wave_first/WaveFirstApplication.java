package com.example.wave_first;

import com.example.wave_first.entity.*;
import com.example.wave_first.repository.*;
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
        user.setRole("Admin");
        user.setPassword("Password");
        userRepo.save(user);

        User user2 = new User();
        user2.setName("Danila");
        user2.setRole("Admin");
        user2.setPassword("A");
        userRepo.save(user2);

        Presentation pres= new Presentation();
        pres.setTitle("New IT tech");
        pres.setTheme("Internet of things");
        presRepo.save(pres);

        Presentation pres2= new Presentation();
        pres2.setTitle("2018 and JS");
        pres2.setTheme("Internet of things");
        presRepo.save(pres2);

        Room room1=new Room();
        room1.setNumber((long) 314);
        roomRepo.save(room1);

        Room room2=new Room();
        room2.setNumber((long) 212);
        roomRepo.save(room2);

        UserPresentation uspr1=new UserPresentation();
        uspr1.setPresentation_id((long) 1);
        uspr1.setUser_id((long) 1);
        usPrRepo.save(uspr1);


        UserPresentation uspr2=new UserPresentation();
        uspr2.setPresentation_id((long) 1);
        uspr2.setUser_id((long) 1);
        usPrRepo.save(uspr2);

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
