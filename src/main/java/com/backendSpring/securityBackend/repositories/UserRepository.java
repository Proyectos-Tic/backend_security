package com.backendSpring.securityBackend.repositories;

import com.backendSpring.securityBackend.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> { //<Model, ID_Type>

    @Query(value = "SELECT * FROM user WHERE email=? and password=?;",nativeQuery = true)
    public Optional<User> login(String email, String password);
}
