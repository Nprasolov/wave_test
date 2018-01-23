package com.example.wave_first.controller;

import com.example.wave_first.entity.*;
import com.example.wave_first.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

    @RequestMapping(value = "/users", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteUser(@RequestBody User user){
        userRepo.delete(user);
        return new ResponseEntity<Object>(userRepo.findAll(),HttpStatus.OK);
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ResponseEntity<Object> updateUser(@RequestBody User user){
        User needle_user=userRepo.findUserByName(user.getName());
        needle_user.setRole(user.getRole());
        needle_user.setPassword(user.getPassword());
        userRepo.save(needle_user);
        return new ResponseEntity<Object>(userRepo.findAll(),HttpStatus.OK);
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ResponseEntity<Object> registerUser(User user){
        if(userRepo.findUserByName(user.getName())!=null){
            return new ResponseEntity<Object>(userRepo.findUserByName(user.getName()),HttpStatus.ALREADY_REPORTED);
        }
        else{
            User newuser=new User();
            newuser.setName(user.getName());
            newuser.setPassword(user.getPassword());
            newuser.setRole(user.getRole());
            userRepo.save(newuser);
        return new ResponseEntity<Object>(userRepo.findUserByName(user.getName()),HttpStatus.OK);}
    }

    @RequestMapping("/schedule")
    public ResponseEntity<Collection> showSchedule(){
        Collection<ScheduleRest> scheduleRests=new HashSet<>();
        for(Schedule schedule: scheduleRepo.findAll()){

            ScheduleRest tmp_schedule=new ScheduleRest();
            Presentation tmp_presentation=presentationRepo.findOne(schedule.getPresentation_id());
            Room tmp_room=roomRepo.findOne(schedule.getRoom_id());


            tmp_schedule.setPresTitle(tmp_presentation.getTitle());
            tmp_schedule.setPresTheme(tmp_presentation.getTheme());
            tmp_schedule.setStartTime(schedule.getStart_time());
            tmp_schedule.setEndTime(schedule.getEnd_time());
            tmp_schedule.setRoomName(tmp_room.getNumber());

            scheduleRests.add(tmp_schedule);
        }
        return new ResponseEntity<Collection>(scheduleRests,HttpStatus.OK);
    }
}
