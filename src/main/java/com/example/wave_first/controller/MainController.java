package com.example.wave_first.controller;

import com.example.wave_first.entity.User;
import com.example.wave_first.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.HashSet;

@RestController
public class MainController {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PresentationRepo presentationRepo;
    @Autowired
    private RoomRepo roomRepo;
    @Autowired
    private ScheduleRepo scheduleRepo;
    @Autowired
    private UsPrRepo usPrRepo;

    @RequestMapping("/users")
    public ResponseEntity<Collection> showUsers(){
        Collection<User> users = new HashSet<>();
        for(User user:userRepo.findAll()){
            users.add(user);
        }

        return new ResponseEntity<Collection>(users, HttpStatus.OK);
    }
}
