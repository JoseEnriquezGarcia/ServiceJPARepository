package com.digis01JEnriquezProgramacionNCapas.RestController;

import com.digis01JEnriquezProgramacionNCapas.DAO.IRolDAO;
import com.digis01JEnriquezProgramacionNCapas.JPA.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rol")
public class RolRestController {

    @Autowired
    IRolDAO iRolDAO;

    @GetMapping("getAll")
    public ResponseEntity GetAll() {
        Result result = new Result();
        
        try {
            result.objects = iRolDAO.findAll();
            result.correct = true;
        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
            result.objects = null;
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
