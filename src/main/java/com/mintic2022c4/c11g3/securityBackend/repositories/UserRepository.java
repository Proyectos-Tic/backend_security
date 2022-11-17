package com.mintic2022c4.c11g3.securityBackend.repositories;

import com.mintic2022c4.c11g3.securityBackend.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
}
