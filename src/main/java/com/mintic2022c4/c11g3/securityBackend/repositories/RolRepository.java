package com.mintic2022c4.c11g3.securityBackend.repositories;

import com.mintic2022c4.c11g3.securityBackend.models.Rol;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolRepository extends CrudRepository<Rol, Integer> {
}
