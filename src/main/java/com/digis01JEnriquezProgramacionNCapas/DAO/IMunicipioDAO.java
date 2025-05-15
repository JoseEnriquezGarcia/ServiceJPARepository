package com.digis01JEnriquezProgramacionNCapas.DAO;

import com.digis01JEnriquezProgramacionNCapas.JPA.Municipio;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface IMunicipioDAO extends JpaRepository<Municipio, Integer>{
    
    @Query(value = "FROM Municipio WHERE Estado.IdEstado = ?1")
    List<Municipio> findMunicipioIdEstado(int IdEstado);
}
