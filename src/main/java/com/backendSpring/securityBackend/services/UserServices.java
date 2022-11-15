package com.backendSpring.securityBackend.services;

import com.backendSpring.securityBackend.models.User;
import com.backendSpring.securityBackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
/**
 *
 */
public class UserServices {
    @Autowired //Connect correctly
    private UserRepository userRepository;

    /**
     *
     * @return
     */
    public List<User> index(){
        return (List<User>) this.userRepository.findAll();
    }

    public Optional<User> show(int id){
        return  this.userRepository.findById(id);
    }

    public User create(User newUser){
        if ( newUser.getIdUser() == null){
            if(newUser.getEmail() != null && newUser.getNickname() != null && newUser.getPassword() != null){
                return this.userRepository.save(newUser);
            }else {
                // TODO 400 BadRequest
                return  newUser;
            }
        }
        else {
            // TODO: validate if id exists, 400 BadRequest
            return newUser;
        }
    }

    public User update(int id, User updatedUser){
        if(id>0){
            Optional<User> tempUser = this.show(id);
            if(tempUser.isPresent()){
                // Update everything except nickname
                if(updatedUser.getNickname()!=null)
                    //tempUser.get() -> User
                    tempUser.get().setNickname(updatedUser.getNickname());
                if(updatedUser.getPassword()!=null)
                    tempUser.get().setPassword(updatedUser.getPassword());
                return this.userRepository.save(tempUser.get());
            }
            else {
                //TODO: 404 NotFound
                return updatedUser;
            }
        }
        else {
            // TODO: 400 BadRequest, id <= 0
            return updatedUser;
        }
    }

    public boolean delete(int id){
        return this.show(id).map( user -> {
            this.userRepository.delete(user);
            return true;
        }).orElse(false);
    }
}
