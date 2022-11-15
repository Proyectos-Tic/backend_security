package com.backendSpring.securityBackend.services;

import com.backendSpring.securityBackend.models.Rol;
import com.backendSpring.securityBackend.repositories.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RolServices {
    @Autowired
    private RolRepository rolRepository;

    /**
     *
     * @return List<Rol>
     */
    public List<Rol> index(){
    return (List<Rol>) this.rolRepository.findAll();
    }

    /**
     *
     * @param id
     * @return Optional<Rol>
     */
    public Optional<Rol> show(int id){
    return this.rolRepository.findById(id);
    }

    /**
     *
     * @param newRol
     * @return Rol
     */
    public Rol create(Rol newRol){
    if(newRol.getIdRol() == null){
        // Name restriction to create a rol
        if (newRol.getName() != null){
            return this.rolRepository.save(newRol);
        }
        else {
            // TODO 400 BadRequest
            return  newRol;
        }
    }
    else {
        // TODO Validate if exists, 400 BadRequest
        return newRol;
    }
    }

    /**
     *
     * @param id
     * @param updatedRol
     * @return Rol
     */
    public Rol update(int id, Rol updatedRol){
        if(id>0){
            Optional<Rol> tempRol = this.show(id);
            if(tempRol.isPresent()){
                // Name restriction to update
                if (updatedRol.getName()!=null){
                    tempRol.get().setName(updatedRol.getName());
                }
                // Description restriction to update
                if (updatedRol.getDescription()!=null){
                    tempRol.get().setDescription(updatedRol.getDescription());
                }
                return this.rolRepository.save(tempRol.get());
            }
            else {
                // TODO 404 NotFound
                return  updatedRol;
            }
        }
        else {
            // TODO 400 BadRequest
            return updatedRol;
        }
    }

    /**
     *
     * @param id
     * @return boolean
     */
    public boolean delete(int id){
        return this.show(id).map( rol -> {
            this.rolRepository.delete(rol);
            return true;
        } ).orElse(false);
    }
}
