package com.backendSpring.securityBackend.repositories;

import com.backendSpring.securityBackend.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> { //<Model, ID_Type>
}
