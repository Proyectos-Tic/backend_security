package com.mintic2022c4.c11g3.securityBackend.services;

import com.mintic2022c4.c11g3.securityBackend.models.User;
import com.mintic2022c4.c11g3.securityBackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class UserServices {
    @Autowired
    private UserRepository userRepository;

    /**
     * Get the complete list of users.
     *
     * @return
     */
    public List<User> index(){
        return (List<User>)this.userRepository.findAll();
    }

    /**
     * Get a specific user providing its id
     *
     * @param id
     * @return
     */
    public Optional<User> show(int id){
        return this.userRepository.findById(id);
    }

    public Optional<User> showByNickname (String nickname) {
        Optional<User> user = this.userRepository.findByNickname(nickname);
        if(user.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user with the requested nickname exists.");
        }
        return user;
    }

    public Optional<User> showByEmail(String email){
        Optional<User> user = this.userRepository.findByEmail(email);
        if(user.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user with the requested email exists.");
        }
        return user;
    }

    /**
     * Create a new user assuming the given object does not have an id
     *
     * @param newUser
     * @return
     */
    public User create(User newUser){
        if(newUser.getId() == null){
            if(newUser.getEmail() != null && newUser.getNickname() != null
                    && newUser.getPassword() != null && newUser.getRol() != null){

                Optional<User> tempUser = this.showByNickname(newUser.getNickname());
                if (tempUser.isPresent())
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "User nickname has already been taken.");

                tempUser = this.showByNickname(newUser.getEmail());
                if (tempUser.isPresent())
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "User email has already been used.");

                newUser.setPassword(this.convertToSHA256(newUser.getPassword()));
                return this.userRepository.save(newUser);
            }
            else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "The request does not contain sufficient data to create a new user.");
            }
        }
        else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "The request contains an id which may cause conflict during the creation process.");
        }
    }

    /**
     * Update an existing user providing its id and an object with the update values.
     *
     * @param id
     * @param updatedUser
     * @return
     */
    public User update(int id, User updatedUser){
        if(id > 0){
            Optional<User> tempUser = this.show(id);
            if (tempUser.isPresent()){
                if (updatedUser.getNickname() != null)
                    tempUser.get().setNickname(updatedUser.getNickname());
                if (updatedUser.getPassword() != null)
                    tempUser.get().setPassword(this.convertToSHA256(updatedUser.getPassword()));
                if (updatedUser.getRol() != null)
                    tempUser.get().setRol(updatedUser.getRol());
                try{
                    return this.userRepository.save(tempUser.get());
                }
                catch(Exception e){
                    e.printStackTrace();
                    throw new ResponseStatusException(HttpStatus.CONFLICT,
                            "Some of the new values entered in the request may be unique " +
                                    "and already existing within the db.");
                }
            }
            else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The requested user could not be found.");
            }
        }
        else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The request contains an invalid user id.");
        }
    }

    /**
     * Delete an existing user providing its id.
     *
     * @param id
     * @return
     */
    public ResponseEntity<Boolean> delete(int id){
        Boolean success = this.show(id).map(user -> {
            this.userRepository.delete(user);
            return true;
        }).orElse(false);
        if (success)
            return new ResponseEntity<>(true, HttpStatus.NO_CONTENT);
        else
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Requested user could not be deleted, recommended verifying its existence.");
    }

    /**
     *
     * @param user
     * @return
     */
    public ResponseEntity<User> login(User user){
        User response;
        if (user.getEmail() != null && user.getPassword() != null) {
            String email = user.getEmail();
            String password = this.convertToSHA256(user.getPassword());
            Optional<User> result = this.userRepository.login(email, password);
            if(result.isEmpty())
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
            else
                response = result.get();
        }
        else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public String convertToSHA256(String password){
        MessageDigest md = null;
        try{
            md = MessageDigest.getInstance("SHA-256");
        }
        catch(NoSuchAlgorithmException e){
            e.printStackTrace();
            return null;
        }
        byte[] hash = md.digest(password.getBytes());
        StringBuffer sb = new StringBuffer();
        for(byte b: hash)
            sb.append( String.format("%02x", b));
        return sb.toString();
    }
}
