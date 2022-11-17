package com.mintic2022c4.c11g3.securityBackend.services;

import com.mintic2022c4.c11g3.securityBackend.models.User;
import com.mintic2022c4.c11g3.securityBackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    /**
     * Create a new user assuming the given object does not have an id
     *
     * @param newUser
     * @return
     */
    public User create(User newUser){
        if(newUser.getId() == null){
            if(newUser.getNickname() != null && newUser.getNickname() != null && newUser.getPassword() != null)
                return this.userRepository.save(newUser);
            else {
                // TODO 400 BAD REQUEST ?
                return newUser;
            }
        }
        else{
            // TODO VALIDATE IF USER EXISTS ? 400 BAD REQUEST ?
            return newUser;
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
                    tempUser.get().setPassword(updatedUser.getPassword());
                return this.userRepository.save(tempUser.get());
            }
            else {
                // TODO 404 NOT FOUND ?
                return updatedUser;
            }
        }
        else {
            // TODO 400 BAD REQUEST ? id <= 0
            return updatedUser;
        }
    }

    /**
     * Delete an existing user providing its id.
     * @param id
     * @return
     */
    public boolean delete(int id){
        Boolean success = this.show(id).map(user -> {
            this.userRepository.delete(user);
            return true;
        }).orElse(false);
        return success;
    }
}