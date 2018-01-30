package com.example.wave_first.controller;

import com.example.wave_first.entity.*;
import com.example.wave_first.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;

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
    public ResponseEntity<Collection> showUsers() {
        Collection<User> users = new HashSet<>();
        for (User user : userRepo.findAll()) {
            users.add(user);
        }

        return new ResponseEntity<Collection>(users, HttpStatus.OK);
    }

    @RequestMapping(value = "/users", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteUser(@RequestBody User user) {
        userRepo.delete(user);
        return new ResponseEntity<Object>(userRepo.findAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ResponseEntity<Object> updateUser(@RequestBody User user) {
        User needle_user = userRepo.findUserByName(user.getName());
        needle_user.setRole(user.getRole());
        needle_user.setPassword(user.getPassword());
        userRepo.save(needle_user);
        return new ResponseEntity<Object>(userRepo.findAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ResponseEntity<Object> registerUser(User user) {
        if (userRepo.findUserByName(user.getName()) != null) {
            return new ResponseEntity<Object>(userRepo.findUserByName(user.getName()), HttpStatus.ALREADY_REPORTED);
        } else {
            User newuser = new User();
            newuser.setName(user.getName());
            newuser.setPassword(user.getPassword());
            newuser.setRole(user.getRole());
            userRepo.save(newuser);
            return new ResponseEntity<Object>(userRepo.findUserByName(user.getName()), HttpStatus.OK);
        }
    }

    @RequestMapping("/schedule")
    public ResponseEntity<Collection> showSchedule() {
        Collection<ScheduleRest> scheduleRests = new HashSet<>();
        for (Schedule schedule : scheduleRepo.findAll()) {

            ScheduleRest tmp_schedule = new ScheduleRest();
            Presentation tmp_presentation = presentationRepo.findOne(schedule.getPresentation_id());
            Room tmp_room = roomRepo.findOne(schedule.getRoom_id());
            String speakers = "";
            for (UserPresentation userPresentation : usPrRepo.findUserPresentationByPresentationId(schedule.getPresentation_id())) {
                User tmp_user = userRepo.findOne(userPresentation.getUser_id());
                speakers += tmp_user.getName() + ", ";
            }
            tmp_schedule.setUsers(speakers);
            tmp_schedule.setPresTitle(tmp_presentation.getTitle());
            tmp_schedule.setPresTheme(tmp_presentation.getTheme());
            tmp_schedule.setStartTime(schedule.getStart_time());
            tmp_schedule.setEndTime(schedule.getEnd_time());
            tmp_schedule.setRoomName(tmp_room.getNumber());

            scheduleRests.add(tmp_schedule);
        }
        return new ResponseEntity<Collection>(scheduleRests, HttpStatus.OK);
    }

    @RequestMapping(value = "/ownpres", method = RequestMethod.GET)
    public ResponseEntity<Collection> showOwnPres() {

        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userRepo.findUserByName(user.getUsername());
        Collection<UserPresentation> userPresentations = new HashSet<>();
        for (UserPresentation uspr : usPrRepo.findUserPresentationByUserId(currentUser.getId())) {
            userPresentations.add(uspr);
        }
        return new ResponseEntity<Collection>(userPresentations, HttpStatus.OK);
    }

    @RequestMapping(value = "/ownpres", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteOwnPres(@RequestBody Presentation presentation) {
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userRepo.findUserByName(user.getUsername());
        if (usPrRepo.findUserPresentationByPresentationId(presentation.getId()) == null) {
            return new ResponseEntity<>(presentation, HttpStatus.NOT_FOUND);
        }
        //удаляет себя из авторов презентации
        for (UserPresentation uspr : usPrRepo.findUserPresentationByUserId(currentUser.getId())) {
            if (uspr.getUser_id() == currentUser.getId() && uspr.getPresentation_id() == presentation.getId()) {
                usPrRepo.delete(uspr);
            }
        }
        //если был последним автором то удаляем саму прзентацию
        if (usPrRepo.findUserPresentationByPresentationId(presentation.getId()) == null) {
            presentationRepo.delete(presentation.getId());
        }
        return new ResponseEntity<>(presentation, HttpStatus.OK);
    }

   // @RequestMapping(value = "new_pres", method = RequestMethod.POST)
    /*public ResponseEntity<Object> addNewPresentation(@RequestBody ScheduleRest scheduleRest) {
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userRepo.findUserByName(user.getUsername());

        Presentation presentation = new Presentation(scheduleRest.getPresTitle(), scheduleRest.getPresTheme());

        UserPresentation userPresentation = new UserPresentation(currentUser.getId(), presentation.getId());

        Room room = roomRepo.findRoomByNumbr(scheduleRest.getRoomName());
        Schedule schedule = new Schedule();
        //добавить можно презентацию либо до либо после всех презентаций в этой аудитории

        for (Schedule schedule1 : scheduleRepo.findScheduleByRoomId(room.getId()))
            if(
                    (scheduleRest.getStartTime().before(schedule1.getStart_time())
                            && scheduleRest.getStartTime().before(schedule1.getEnd_time())
                    )
                            ||
                            (scheduleRest.getStartTime().after(schedule1.getStart_time())
                                    && scheduleRest.getStartTime().after(schedule1.getEnd_time())
                            )
                    )

            {

            }
            //если нашлась хоть одна не удовлетворяющая условиям
            else {

                return new ResponseEntity<>(schedule, HttpStatus.BAD_REQUEST);

            }
        schedule.setRoom_id(room.getId());
        schedule.setPresentation_id(presentation.getId());
        schedule.setStart_time(scheduleRest.getStartTime());
        schedule.setEnd_time(scheduleRest.getEndTime());
        scheduleRepo.save(schedule);
        presentationRepo.save(presentation);
        usPrRepo.save(userPresentation);
        return new ResponseEntity<>(schedule, HttpStatus.OK);
    }*/
}
