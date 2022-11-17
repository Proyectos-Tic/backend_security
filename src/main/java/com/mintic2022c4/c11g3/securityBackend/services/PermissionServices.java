package com.mintic2022c4.c11g3.securityBackend.services;

import com.mintic2022c4.c11g3.securityBackend.models.Permission;
import com.mintic2022c4.c11g3.securityBackend.repositories.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PermissionServices {
    @Autowired
    private PermissionRepository permissionRepository;

    /**
     * Get the complete list of permissions from the db.
     * @return
     */
    public List<Permission> index(){
        return (List<Permission>) this.permissionRepository.findAll();
    }

    /**
     * Get a specific permission providing its id.
     * @param id
     * @return
     */
    public Optional<Permission> show(int id){
        return this.permissionRepository.findById(id);
    }

    /**
     * Create a new permission, this method verifies if the given permission already has an id.
     * @param newPermission
     * @return
     */
    public Permission create(Permission newPermission){
        if(newPermission.getId() == null){
            if(newPermission.getUrl() != null && newPermission.getMethod() != null)
                return this.permissionRepository.save(newPermission);
            else {
                // TODO 400 BAD REQUEST ?
                return newPermission;
            }
        }
        else{
            // TODO VALIDATE IF PERMISSION EXISTS ? 400 BAD REQUEST ?
            return newPermission;
        }
    }

    /**
     * Update an existing permission providing its id and its updated version.
     * @param id
     * @param updatedPermission
     * @return
     */
    public Permission update(int id, Permission updatedPermission){
        if (id > 0){
            Optional<Permission> tempPermission = this.show(id);
            if (tempPermission.isPresent()){
                if(updatedPermission.getUrl() != null)
                    tempPermission.get().setUrl(updatedPermission.getUrl());
                if(updatedPermission.getMethod() != null)
                    tempPermission.get().setMethod(updatedPermission.getMethod());
                return this.permissionRepository.save(tempPermission.get());
            }
            else {
                // TODO 404 NOT FOUND ?
                return updatedPermission;
            }
        }
        else {
            // TODO 400 BAD REQUEST ? id <= 0
            return updatedPermission;
        }
    }

    /**
     * Delete an existing permission providing it id.
     * @param id
     * @return
     */
    public boolean delete(int id){
        Boolean success = this.show(id).map(permission -> {
            this.permissionRepository.delete(permission);
            return true;
        }).orElse(false);
        return success;
    }
}
