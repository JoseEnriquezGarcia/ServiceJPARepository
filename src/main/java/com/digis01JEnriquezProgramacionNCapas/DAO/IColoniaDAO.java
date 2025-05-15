package com.digis01JEnriquezProgramacionNCapas.DAO;

import com.digis01JEnriquezProgramacionNCapas.JPA.Colonia;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IColoniaDAO extends JpaRepository<Colonia, Integer>{
    
    @Query(value = "FROM Colonia WHERE Municipio.IdMunicipio = ?1")
    List<Colonia> findColoniaIdMunicipio(int idMunicipio);
    
    @Query(value = "FROM Colonia WHERE CodigoPostal = ?1")
    List<Colonia> findColoniaCodigoPostal(String CodigoPostal);
}
