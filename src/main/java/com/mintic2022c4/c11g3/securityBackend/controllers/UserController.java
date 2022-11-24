package com.mintic2022c4.c11g3.securityBackend.controllers;

import com.mintic2022c4.c11g3.securityBackend.models.User;
import com.mintic2022c4.c11g3.securityBackend.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserServices userServices;

    @GetMapping("/all")
    public List<User> getAllUsers(){
        return this.userServices.index();
    }

    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable("id") int id){
        return this.userServices.show(id);
    }

    @GetMapping("/by_nickname/{nickname}>")
    public Optional<User> getUserByNickname(@PathVariable("nickname") String nickname){
        return this.userServices.showByNickname(nickname);
    }

    @GetMapping("/by_email/{email}")
    public Optional<User> getUserByEmail(@PathVariable("email") String email){
        return this.userServices.showByEmail(email);
    }

    @PostMapping("/insert")
    public User insertUser(@RequestBody User user){
        return this.userServices.create(user);
    }

    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestBody User user){
        return this.userServices.login(user);
    }

    @PutMapping("/update/{id}")
    public User updateUser(@PathVariable("id") int id, @RequestBody User user){
        return this.userServices.update(id, user);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteUser(@PathVariable("id") int id){
        return this.userServices.delete(id);
    }
}
