package com.digis01JEnriquezProgramacionNCapas.JPA;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Rol {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idrol")
    private Integer IdRol;
    
    @Column(name = "nombre")
    private String Nombre;
    
    public Integer getIdRol(){
        return IdRol;
    }
    
    public void setIdRol(Integer IdRol){
        this.IdRol = IdRol;
    }
    
    public String getNombre(){
        return Nombre;
    }
    
    public void setNombre(String Nombre){
        this.Nombre = Nombre;
    }
}
