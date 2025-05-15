
package com.digis01JEnriquezProgramacionNCapas.DAO;

import com.digis01JEnriquezProgramacionNCapas.JPA.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUsuarioDAO extends JpaRepository<Usuario, Integer>{ 
    
    
}
