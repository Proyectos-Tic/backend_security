package com.backendSpring.securityBackend.services;

import com.backendSpring.securityBackend.models.Permission;
import com.backendSpring.securityBackend.repositories.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PermissionServices {

    @Autowired
    private PermissionRepository permissionRepository;

    /**
     *
     * @return List<Permission>
     */
    public List<Permission> index(){
        return (List<Permission>) this.permissionRepository.findAll();
    }

    /**
     *
     * @param id
     * @return Optional<Permission>
     */
    public Optional<Permission> show(int id){
        return this.permissionRepository.findById(id);
    }

    /**
     *
     * @param newPermission
     * @return Permission
     */
    public Permission create(Permission newPermission){
        if(newPermission.getIdPermission() == null){
            if(newPermission.getMethod()!=null && newPermission.getUrl()!=null){
                return this.permissionRepository.save(newPermission);
            }
            else {
                // TODO: 400 BadRequest
                return newPermission;
            }
        }
        else {
            // TODO 400 BadRequest, validate if id exists
            return newPermission;
        }
    }

    /**
     *
     * @param id
     * @param updatedPermission
     * @return Permission
     */
    public Permission update(int id, Permission updatedPermission){
        if(id>0){
            // Find the object to update
            Optional<Permission> tempPermission = this.permissionRepository.findById(id);
            // Validate if it actually exists
            if(tempPermission.isPresent()){
                // Update url if exists
                if(updatedPermission.getUrl()!=null){
                    tempPermission.get().setUrl(updatedPermission.getUrl());
                }
                if(updatedPermission.getMethod()!=null)
                    tempPermission.get().setMethod(updatedPermission.getMethod());
                return this.permissionRepository.save(tempPermission.get());
            }
            else {
                // TODO 404 NotFound
                return updatedPermission;
            }
        }
        else {
            // TODO 400 BadRequest id<=0
            return updatedPermission;
        }
    }

    /**
     *
     * @param id
     * @return boolean
     */
    public boolean delete(int id){
     return this.show(id).map( permission -> {
         this.permissionRepository.delete(permission);
         return true;
     } ).orElse(false);
    }
}
