package com.mintic2022c4.c11g3.securityBackend.services;

import com.mintic2022c4.c11g3.securityBackend.models.Permission;
import com.mintic2022c4.c11g3.securityBackend.models.Rol;
import com.mintic2022c4.c11g3.securityBackend.repositories.PermissionRepository;
import com.mintic2022c4.c11g3.securityBackend.repositories.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class RolServices {
    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PermissionRepository permissionRepository;

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
        if(newRol.getIdRol() == null){
            if(newRol.getName() != null)
                return this.rolRepository.save(newRol);
            else{
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request contains no name.");
            }
        }
        else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provided object contains an existing id.");
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
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Provided Rol id does not exist");
            }
        }
        else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Rol id");
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

    public ResponseEntity<Rol> updateAddPermission(int idRol, int idPermission){
        Optional<Rol> rol = this.rolRepository.findById(idRol);
        if(rol.isPresent()){
            Optional<Permission> permission = this.permissionRepository.findById(idPermission);
            if (permission.isPresent()){
                Set<Permission> tempPermissions = rol.get().getPermissions();
                if (tempPermissions.contains(permission.get()))
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "this rol already has that permission");
                else {
                    tempPermissions.add(permission.get());
                    rol.get().setPermissions(tempPermissions);
                    return new ResponseEntity<>(this.rolRepository.save(rol.get()), HttpStatus.CREATED);
                }
            }
            else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Provided rol.id does not exist.");
            }
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Provided rol.id does not exist.");
        }
    }

    public ResponseEntity<Boolean> validateGrant(int idRol, Permission permission){
        boolean isGrant = false;
        Optional<Rol> rol = this.rolRepository.findById(idRol);
        if(rol.isPresent()){
            for(Permission rolPermission: rol.get().getPermissions()){
                if (rolPermission.getUrl().equals(permission.getUrl()) &&
                        rolPermission.getMethod().equals(permission.getMethod())){
                    isGrant = true;
                    break;
                }
            }
            if(isGrant)
                return new ResponseEntity<>(true, HttpStatus.OK);
            else
                return new ResponseEntity<>(false, HttpStatus.UNAUTHORIZED);

        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Provided rol id does not exist");
        }
    }
}
