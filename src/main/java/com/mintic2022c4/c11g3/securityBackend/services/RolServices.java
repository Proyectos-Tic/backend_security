package com.mintic2022c4.c11g3.securityBackend.services;

import com.mintic2022c4.c11g3.securityBackend.models.Rol;
import com.mintic2022c4.c11g3.securityBackend.repositories.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RolServices {
    @Autowired
    private RolRepository rolRepository;

    /**
     * Get a list of all the Roles from the db
     * @return
     */
    public List<Rol> index(){
        return (List<Rol>)this.rolRepository.findAll();
    }

    /**
     * Get the info of a specific Rol providing its id
     * @param id
     * @return
     */
    public Optional<Rol> show(int id){
        return this.rolRepository.findById(id);
    }

    /**
     * Create a new Role, this method also verifies if the provided object already has an id.
     * @param newRol
     * @return
     */
    public Rol create(Rol newRol){
        if(newRol.getId() == null){
            if(newRol.getName() != null)
                return this.rolRepository.save(newRol);
            else{
                // TODO 400 BAD REQUEST ?
                return newRol;
            }
        }
        else{
            //TODO 400 BAD REQUEST VALIDATE IF EXISTS
            return newRol;
        }
    }

    /**
     * Update an existing role.
     * @param id
     * @param updatedRol
     * @return
     */
    public Rol update(int id, Rol updatedRol){
        if(id > 0){
            Optional<Rol> tempRol = this.show(id);
            if(tempRol.isPresent()){
                if (updatedRol.getName() != null)
                    tempRol.get().setName(updatedRol.getName());
                if (updatedRol.getDescription() != null)
                    tempRol.get().setDescription(updatedRol.getDescription());
                return this.rolRepository.save(tempRol.get());
            }
            else {
                // TODO 404 NOT FOUND ?
                return updatedRol;
            }
        }
        else {
            // TODO 400 BAD REQUEST ? id <= 0
            return updatedRol;
        }
    }

    /**
     * Delete an existing role providing its id
     * @param id
     * @return
     */
    public boolean delete(int id){
        Boolean success = this.show(id).map(rol -> {
            this.rolRepository.delete(rol);
            return true;
        }).orElse(false);
        return success;
    }
}
