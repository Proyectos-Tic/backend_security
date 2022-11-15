package com.backendSpring.securityBackend.controllers;

import com.backendSpring.securityBackend.models.User;
import com.backendSpring.securityBackend.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/by_id/{id}")
    public Optional<User> getUserById(@PathVariable("id") int id){
        return this.userServices.show(id);
    }

    @GetMapping("/by_nickname/{nickname}")
    public Optional<User> getNicknameById(@PathVariable("nickname") String nickname){
        return this.userServices.showByNickname(nickname);
    }

    @GetMapping("/by_email/{email}")
    public Optional<User> getEmailById(@PathVariable("email") String email){
        return this.userServices.showByEmail(email);
    }

    //TODO DAO
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public User loginUser(@RequestBody User user){
        return this.userServices.login(user);
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody User user){
        return this.userServices.create(user);
    }

    @PostMapping("/update/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public User updateUser(@PathVariable("id") int id, @RequestBody User updatedUser){
        return this.userServices.update(id, updatedUser);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public boolean deleteUser(@PathVariable("id") int id){
        return this.userServices.delete(id);
    }

}
