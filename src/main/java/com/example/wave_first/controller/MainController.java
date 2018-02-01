package com.example.wave_first.controller;

import com.example.wave_first.entity.*;
import com.example.wave_first.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.*;

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

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public Model signin(Model model, @RequestParam(value = "result", required = false, defaultValue = "0") String result) {
        model.addAttribute("result", result);
        return model;
    }


    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public RedirectView registerUser(@ModelAttribute User user, Model model, RedirectAttributes attributes) {
        if (userRepo.findUserByName(user.getName()) != null) {
            attributes.addAttribute("result", "Пользователь с таким именем уже существует");
            return new RedirectView("signup");
        } else {
            User newuser = new User();
            newuser.setName(user.getName());
            newuser.setPassword(user.getPassword());
            newuser.setRole("Listener");
            userRepo.save(newuser);
            return new RedirectView("home");
        }
    }


    @RequestMapping("/users")
    public Model showUsers(Model model, @RequestParam(value = "result", required = false, defaultValue = "0") String result) {
        Collection<User> users = new HashSet<>();
        for (User user : userRepo.findAll()) {
            users.add(user);
        }
        model.addAttribute("users", users);
        model.addAttribute("result", result);
        return model;
    }


    @RequestMapping(value = "/deleteuser", method = RequestMethod.GET)
    public RedirectView deleteUser(@RequestParam(value = "user_id", required = false, defaultValue = "-1") Long user_id, RedirectAttributes attributes) {
        User needle_user = userRepo.findOne(user_id);
        userRepo.delete(needle_user);
        attributes.addAttribute("result", "Корректно удален");
        //удаляем его из авторов и презентаций
        for (UserPresentation uspr : usPrRepo.findUserPresentationByUserId(user_id)) {
            Long pres_id = uspr.getPresentation_id();
            usPrRepo.delete(uspr);
            if (usPrRepo.findUserPresentationByPresentationId(pres_id) == null) {
                presentationRepo.delete(pres_id);
                Schedule schedule = scheduleRepo.findScheduleByPresentationId(pres_id);
                scheduleRepo.delete(schedule);
            }

        }

        return new RedirectView("users");

    }

    @RequestMapping(value = "/updateuser", method = RequestMethod.GET)
    public Model updateUser(@RequestParam(value = "user_id", required = false, defaultValue = "-1") Long user_id, Model model) {
        User needle_user = userRepo.findOne(user_id);
        model.addAttribute("user", needle_user);
        HashSet<String> roleset = new HashSet<>();
        roleset.add("Admin");
        roleset.add("Presenter");
        roleset.add("Listener");
        model.addAttribute("selection", roleset);
        model.addAttribute("selector", needle_user.getRole());
        return model;

    }

    @RequestMapping(value = "/updateuser", method = RequestMethod.POST)
    public RedirectView updateUserPost(@ModelAttribute User user, Model model, RedirectAttributes attributes) {
        User needle_user = userRepo.findOne(user.getId());
        needle_user.setName(user.getName());
        needle_user.setRole(user.getRole());
        userRepo.save(needle_user);
        return new RedirectView("/users");

    }

    @RequestMapping(value = "/ownpres", method = RequestMethod.GET)
    public Model showOwnPres(@RequestParam(value = "result", required = false, defaultValue = "0") String result, Model model) {

        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userRepo.findUserByName(user.getUsername());
        Collection<PresentationRest> userPresentationsRest = new HashSet<>();

        for (UserPresentation uspr : usPrRepo.findUserPresentationByUserId(currentUser.getId())) {

            Presentation tmpPresentation = presentationRepo.findOne(uspr.getPresentation_id());
            String authors = "";
            for (UserPresentation uspr2 : usPrRepo.findUserPresentationByPresentationId(uspr.getPresentation_id())) {
                User author = userRepo.findOne(uspr2.getUser_id());
                authors += " " + author.getName();
            }

            PresentationRest tmpUserPresentation = new PresentationRest();
            tmpUserPresentation.setId(uspr.getId());
            tmpUserPresentation.setPresentation_id(uspr.getPresentation_id());
            tmpUserPresentation.setUser_id(uspr.getUser_id());
            tmpUserPresentation.setPresentation_title(tmpPresentation.getTitle());
            tmpUserPresentation.setUsername(authors);

            userPresentationsRest.add(tmpUserPresentation);
        }
        model.addAttribute("presentation", userPresentationsRest);
        model.addAttribute("result", result);
        return model;
    }

    @RequestMapping(value = "/deleteownpres", method = RequestMethod.GET)
    public RedirectView deleteOwnPres(@RequestParam(value = "presentation_id", required = false, defaultValue = "-1") Long presentation_id, RedirectAttributes attributes) {
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userRepo.findUserByName(user.getUsername());

        int count = 0;
        Iterable<UserPresentation> tmpPres = usPrRepo.findUserPresentationByPresentationId(presentation_id);
        for (UserPresentation uspr : tmpPres) {
            count++;
        }
        if (count == 0) {
            attributes.addAttribute("result", "Ошибка удаления не найдена доступная презентация");
            return new RedirectView("ownpres");
        }

        //удаляет себя из авторов презентации
        for (UserPresentation uspr : usPrRepo.findUserPresentationByUserId(currentUser.getId())) {
            if (uspr.getUser_id() == currentUser.getId() && uspr.getPresentation_id() == presentation_id) {
                usPrRepo.delete(uspr);
            }
        }
        //если был последним автором то удаляем саму прзентацию
        if (usPrRepo.findUserPresentationByPresentationId(presentation_id) == null) {
            presentationRepo.delete(presentation_id);
            Schedule schedule = scheduleRepo.findScheduleByPresentationId(presentation_id);
            scheduleRepo.delete(schedule);
        }
        attributes.addAttribute("result", "Корректно удален из авторов");
        return new RedirectView("ownpres");
    }


    @RequestMapping(value = "/updatepresentation", method = RequestMethod.GET)
    public Model updatePresentation(@RequestParam(value = "pres_id", required = false, defaultValue = "-1") Long pres_id, Model model) {
        Presentation needle_pres = presentationRepo.findOne(pres_id);
        model.addAttribute("presentation", needle_pres);
        return model;
    }

    @RequestMapping(value = "/updatepresentation", method = RequestMethod.POST)
    public RedirectView updatePresentationPost(@ModelAttribute Presentation presentation, Model model, RedirectAttributes attributes) {
        Presentation needle_pres = presentationRepo.findOne(presentation.getId());
        needle_pres.setTitle(presentation.getTitle());
        needle_pres.setTheme(presentation.getTheme());
        presentationRepo.save(needle_pres);
        attributes.addAttribute("result", "Презентация изменена");
        return new RedirectView("/ownpres");
    }


    @RequestMapping(value = "/createpres", method = RequestMethod.GET)
    public Model cretePres(Model model, @RequestParam(value = "result", required = false, defaultValue = "0") String result) {
        model.addAttribute("result", result);
        return model;
    }


    @RequestMapping(value = "/createpres", method = RequestMethod.POST)
    public RedirectView createPresPost(@ModelAttribute Presentation presentation, Model model, RedirectAttributes attributes) {

            Presentation newpres=new Presentation();
            newpres.setTheme(presentation.getTheme());
            newpres.setTitle(presentation.getTitle());
            newpres=presentationRepo.save(newpres);

        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userRepo.findUserByName(user.getUsername());


        UserPresentation uspr=new UserPresentation();
            uspr.setPresentation_id(newpres.getId());
            uspr.setUser_id(currentUser.getId());
            usPrRepo.save(uspr);
        attributes.addAttribute("result", "Презентация сохранена");

        return new RedirectView("ownpres");
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



      /*@RequestMapping(value = "/users", method = RequestMethod.DELETE)
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
    }*/
    /*@RequestMapping(value = "/signup", method = RequestMethod.POST)
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
    }*/
    /* @RequestMapping(value = "/ownpres", method = RequestMethod.GET)
     public ResponseEntity<Collection> showOwnPres() {

         org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
         User currentUser = userRepo.findUserByName(user.getUsername());
         Collection<UserPresentation> userPresentations = new HashSet<>();
         for (UserPresentation uspr : usPrRepo.findUserPresentationByUserId(currentUser.getId())) {
             userPresentations.add(uspr);
         }
         return new ResponseEntity<Collection>(userPresentations, HttpStatus.OK);
     }*/
   /* @RequestMapping(value = "/ownpres", method = RequestMethod.DELETE)
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
    }*/

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
