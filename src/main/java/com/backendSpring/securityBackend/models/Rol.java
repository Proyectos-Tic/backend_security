package com.backendSpring.securityBackend.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "rol")
public class Rol implements Serializable { //Efficient obj management
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idRol;
    private String name;
    private String description;
    //Cascade: to protect foreign keys for PKeys troubles
    //Mapped by: kind of "foreign key"
    @OneToMany(cascade = {CascadeType.PERSIST}, mappedBy = "rol")
    @JsonIgnoreProperties("rol")
    private List<User> users;

    public Integer getIdRol() {
        return idRol;
    }

    public void setIdRol(Integer idRol) {
        this.idRol = idRol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
