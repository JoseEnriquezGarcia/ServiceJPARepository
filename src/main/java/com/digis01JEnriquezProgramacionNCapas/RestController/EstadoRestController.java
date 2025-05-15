package com.digis01JEnriquezProgramacionNCapas.RestController;

import com.digis01JEnriquezProgramacionNCapas.DAO.IEstadoDAO;
import com.digis01JEnriquezProgramacionNCapas.JPA.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/estado")
public class EstadoRestController {

    @Autowired
    IEstadoDAO iEstadoDAO;

    @GetMapping("byIdPais/{IdPais}")
    public ResponseEntity GetByIdPais(@PathVariable int IdPais) {
        Result result = new Result();
        
        try {
            result.objects = iEstadoDAO.findEstadoIdPais(IdPais);
            result.correct = true;
        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }

        if (result.correct == true) {
            if (result.objects.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.ok(result);
            }
        } else {
            return ResponseEntity.internalServerError().body(result.errorMessage);
        }
    }
}
