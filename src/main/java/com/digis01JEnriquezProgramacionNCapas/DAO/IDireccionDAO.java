package com.digis01JEnriquezProgramacionNCapas.DAO;

import com.digis01JEnriquezProgramacionNCapas.JPA.Direccion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IDireccionDAO extends JpaRepository<Direccion, Integer>{
    
    @Query(value = "FROM Direccion WHERE Usuario.IdUsuario = ?1")
    List<Direccion> findDireccion(int IdUsuario);
}
