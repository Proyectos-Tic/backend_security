package com.mintic2022c4.c11g3.securityBackend.controllers;

import com.mintic2022c4.c11g3.securityBackend.models.Permission;
import com.mintic2022c4.c11g3.securityBackend.services.PermissionServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/permission")
public class PermissionController {
    @Autowired
    private PermissionServices permissionServices;

    @GetMapping("/all")
    public List<Permission> getAllPermission(){
        return this.permissionServices.index();
    }

    @GetMapping("/{id}")
    public Optional<Permission> getPermissionById (@PathVariable("id") int id){
        return this.permissionServices.show(id);
    }

    @PostMapping("/insert")
    public Permission insertPermission(@RequestBody Permission permission){
        return this.permissionServices.create(permission);
    }

    @PutMapping("/update/{id}")
    public Permission updatePermission(@PathVariable("id") int id,@RequestBody Permission permission){
        return this.permissionServices.update(id, permission);
    }

    @DeleteMapping("/delete/{id}")
    public boolean deleteUser(@PathVariable("id") int id){
        return this.permissionServices.delete(id);
    }
}
