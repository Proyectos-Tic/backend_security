package com.backendSpring.securityBackend.services;

import com.backendSpring.securityBackend.models.User;
import com.backendSpring.securityBackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
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
     * @return List<User>
     */
    public List<User> index(){
        return (List<User>) this.userRepository.findAll();
    }

    /**
     *
     * @param id
     * @return Optional<User>
     */
    public Optional<User> show(int id){
        return  this.userRepository.findById(id);
    }

    /**
     *
     * @param nickname
     * @return Optional<User>
     */
    public Optional<User> showByNickname(String nickname){
        return this.userRepository.findByNickname(nickname);
    }
    public Optional<User> showByEmail(String email){
        return this.userRepository.findByEmail(email);
    }
    /**
     *
     * @param newUser
     * @return User
     */
    public User create(User newUser){
        if ( newUser.getIdUser() == null){
            if(newUser.getEmail() != null && newUser.getNickname() != null && newUser.getPassword() != null){
                newUser.setPassword(this.convertToSHA256(newUser.getPassword()));
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

    /**
     *
     * @param id
     * @param updatedUser
     * @return User
     */
    public User update(int id, User updatedUser){
        if(id>0){
            Optional<User> tempUser = this.show(id);
            if(tempUser.isPresent()){
                // Update everything except nickname
                if(updatedUser.getNickname()!=null)
                    //tempUser.get() -> User
                    tempUser.get().setNickname(updatedUser.getNickname());
                if(updatedUser.getPassword()!=null)
                    tempUser.get().setPassword(this.convertToSHA256(updatedUser.getPassword()));
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

    /**
     *
     * @param id
     * @return boolean
     */
    public boolean delete(int id){
        return this.show(id).map( user -> {
            this.userRepository.delete(user);
            return true;
        }).orElse(false);
    }


    /**
     *
     * @param user
     * @return HashMap
     *
     */
    public User login(User user){
        User response = null;
        if(user.getPassword()!= null && user.getEmail()!=null){
            String email = user.getEmail();
            String password = this.convertToSHA256( user.getPassword() );
            Optional<User> result = this.userRepository.login(email, password);
            if(result.isEmpty()){
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                        "Invalid access.");
            }
            else {
                response = result.get();
            }
        }
        else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Mandatory fields had not been sent");
        }
        return response;
    }
    /**
     *
     * @param password
     * @return String
     */
    public String convertToSHA256(String password){
        MessageDigest md = null;
        try{
            md = MessageDigest.getInstance("SHA-256");

        }
        catch (NoSuchAlgorithmException e){
            e.printStackTrace();
            return null;
        }
        //Encrypt the password using its bytes equivalent
        byte[] hash = md.digest(password.getBytes());
        StringBuffer sb = new StringBuffer();
        for( byte b: hash){
            // Add each character in String representation to the buffer (casting from Byte HEX)
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
