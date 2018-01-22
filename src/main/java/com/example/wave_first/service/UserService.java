package com.example.wave_first.service;

import com.example.wave_first.entity.User;
import com.example.wave_first.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.HashSet;

@Service("UserService")
public class UserService implements UserDetailsService {

    @Autowired
    UserRepo userRepo;

    public void saveUser(User user) {
        userRepo.save(user);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String name) {
        User user = userRepo.findUserByName(name);
        Collection<GrantedAuthority> roles = new HashSet<>();
        roles.add(new SimpleGrantedAuthority(user.getRole()));
        Collection<GrantedAuthority> auth = roles;
        return new org.springframework.security.core.userdetails
                .User(user.getName(), user.getPassword(), true, true, true, true, auth);
    }

}
