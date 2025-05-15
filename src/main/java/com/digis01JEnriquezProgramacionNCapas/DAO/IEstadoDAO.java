package com.digis01JEnriquezProgramacionNCapas.DAO;

import com.digis01JEnriquezProgramacionNCapas.JPA.Estado;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IEstadoDAO extends JpaRepository<Estado, Integer>{
    
    @Query(value = "FROM Estado WHERE Pais.IdPais = ?1")
    List<Estado> findEstadoIdPais(int IdPais);
    
}
