package com.backendSpring.securityBackend.controllers;

import com.backendSpring.securityBackend.models.Permission;
import com.backendSpring.securityBackend.models.Rol;
import com.backendSpring.securityBackend.services.RolServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rol")
public class RolController {
    @Autowired
    private RolServices rolServices;

    @GetMapping("/all")
    public List<Rol> getAllRoles(){
        return this.rolServices.index();
    }

    @GetMapping("/{id}")
    public Optional<Rol> getRolById(@PathVariable("id") int id){
        return this.rolServices.show(id);
    }

    @GetMapping("/validate/{idRol}")
    public ResponseEntity<Boolean> validatePermission(@PathVariable("idRol") int idRol, @RequestBody Permission permission){
        return this.rolServices.validateGrant(idRol, permission);
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Rol createRol(@RequestBody Rol rol){
        return this.rolServices.create(rol);
    }

    @PostMapping("/update/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Rol updateRol(@PathVariable("id") int id, @RequestBody Rol updatedRol){
        return this.rolServices.update(id, updatedRol);
    }

    @PostMapping("/update/{idRol}/add_permission/{idPermission}")
    public ResponseEntity<Rol> updateAddPermission(@PathVariable("idRol") int idRol, @PathVariable("idPermission") int idPermission){
        return this.rolServices.updateAppPermission(idRol,idPermission);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public boolean deleteRol(@PathVariable("id") int id){
        return this.rolServices.delete(id);
    }
}
