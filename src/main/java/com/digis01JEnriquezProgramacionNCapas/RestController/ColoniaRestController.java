package com.digis01JEnriquezProgramacionNCapas.RestController;

import com.digis01JEnriquezProgramacionNCapas.DAO.IColoniaDAO;
import com.digis01JEnriquezProgramacionNCapas.JPA.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/colonia")
public class ColoniaRestController {

    @Autowired
    IColoniaDAO iColoniaDAO;

    @GetMapping("byIdMunicipio/{IdMunicipio}")
    public ResponseEntity GetByIdMunicipio(@PathVariable int IdMunicipio) {
        Result result = new Result();

        try {
            result.objects = iColoniaDAO.findColoniaIdMunicipio(IdMunicipio);
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
                return ResponseEntity.ok().body(result);
            }
        } else {
            return ResponseEntity.internalServerError().body(result.errorMessage);
        }
    }

    @GetMapping("getByCp/{CP}")
    public ResponseEntity GetByCodigoPostal(@PathVariable String CP) {
        Result result = new Result();
        
        try {
            result.objects = iColoniaDAO.findColoniaCodigoPostal(CP);
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
                return ResponseEntity.ok().body(result);
            }
        } else {
            return ResponseEntity.internalServerError().body(result.errorMessage);
        }
    }
}
