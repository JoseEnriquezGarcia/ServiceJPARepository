package com.digis01JEnriquezProgramacionNCapas.RestController;

import com.digis01JEnriquezProgramacionNCapas.DAO.IDireccionDAO;
import com.digis01JEnriquezProgramacionNCapas.JPA.Direccion;
import com.digis01JEnriquezProgramacionNCapas.JPA.Result;
import com.digis01JEnriquezProgramacionNCapas.JPA.UsuarioDireccion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/direccion")
public class DireccionRestController {

    @Autowired
    IDireccionDAO iDireccionDAO;

    @GetMapping("getById/{IdDireccion}")
    public ResponseEntity GetById(@PathVariable int IdDireccion) {
        Result result = new Result();

        try {
            result.object = iDireccionDAO.findById(IdDireccion).orElse(null);
            result.correct = true;
        } catch (Exception ex) {
            result.correct = true;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
            result.objects = null;
        }

        if (result.correct == true) {
            if (result.object == null) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.ok(result);
            }
        } else {
            return ResponseEntity.internalServerError().body(result.errorMessage);
        }
    }

    @PostMapping("add")
    public ResponseEntity Add(@RequestBody UsuarioDireccion usuarioDireccion) {
        Result result = new Result();

        try {
            usuarioDireccion.Direccion.Usuario = usuarioDireccion.Usuario;
            iDireccionDAO.save(usuarioDireccion.Direccion);
            result.correct = true;
        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }

        if (result.correct) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result.errorMessage);
        }
    }

    @PutMapping("update")
    public ResponseEntity Update(@RequestBody Direccion direccion) {
        Result result = new Result();

        try {
            Direccion direccionFromDB = iDireccionDAO.findById(direccion.getIdDireccion()).orElse(null);
            direccionFromDB.setCalle(direccion.getCalle());
            direccionFromDB.setNumeroInterior(direccion.getNumeroInterior());
            direccionFromDB.setNumeroExterior(direccion.getNumeroExterior());
            direccionFromDB.Usuario.setIdUsuario(direccion.Usuario.getIdUsuario());
            iDireccionDAO.save(direccionFromDB);
            result.correct = true;
        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }

        if (result.correct) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result.errorMessage);
        }
    }

    @DeleteMapping("delete/{IdDireccion}")
    public ResponseEntity Delete(@PathVariable int IdDireccion) {
        Result result = new Result();

        try {
            iDireccionDAO.deleteById(IdDireccion);
            result.correct = true;
        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }

        if (result.correct) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result.errorMessage);
        }
    }
}
