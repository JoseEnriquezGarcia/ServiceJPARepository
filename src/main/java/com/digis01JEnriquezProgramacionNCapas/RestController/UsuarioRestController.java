package com.digis01JEnriquezProgramacionNCapas.RestController;

import com.digis01JEnriquezProgramacionNCapas.DAO.IDireccionDAO;
import com.digis01JEnriquezProgramacionNCapas.DAO.IUsuarioDAO;
import com.digis01JEnriquezProgramacionNCapas.JPA.Colonia;
import com.digis01JEnriquezProgramacionNCapas.JPA.Direccion;
import com.digis01JEnriquezProgramacionNCapas.JPA.Result;
import com.digis01JEnriquezProgramacionNCapas.JPA.ResultFile;
import com.digis01JEnriquezProgramacionNCapas.JPA.Rol;
import com.digis01JEnriquezProgramacionNCapas.JPA.Usuario;
import com.digis01JEnriquezProgramacionNCapas.JPA.UsuarioDireccion;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/usuario")
public class UsuarioRestController {

    @Autowired
    IUsuarioDAO iUsuarioDAO;
    @Autowired
    IDireccionDAO iDireccionDAO;

    @GetMapping
    public ResponseEntity GetAll() {
        Result result = new Result();
        try {
            result.objects = new ArrayList<>();
            List<Usuario> usuarios = iUsuarioDAO.findAll();

            for (Usuario usuario : usuarios) {
                UsuarioDireccion usuarioDireccion = new UsuarioDireccion();
                usuarioDireccion.Usuario = new Usuario();
                usuarioDireccion.Usuario = usuario;
                usuarioDireccion.Direcciones = iDireccionDAO.findDireccion(usuario.getIdUsuario());
                result.objects.add(usuarioDireccion);
            }

            result.correct = true;
        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
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

    @GetMapping("getAllById/{IdUsuario}")
    public ResponseEntity GetAllById(@PathVariable int IdUsuario) {
        Result result = new Result();

        try {
            UsuarioDireccion usuarioDireccion = new UsuarioDireccion();
            usuarioDireccion.Usuario = new Usuario();

            usuarioDireccion.Usuario = iUsuarioDAO.findById(IdUsuario).orElse(null);

            List<Direccion> listaDirecciones = iDireccionDAO.findDireccion(usuarioDireccion.Usuario.getIdUsuario());
            usuarioDireccion.Direcciones = new ArrayList<>();
            for (Direccion direcciones : listaDirecciones) {
                Direccion direccion = new Direccion();
                direccion = direcciones;
                usuarioDireccion.Direcciones.add(direccion);
            }

            result.object = usuarioDireccion;
            result.correct = true;
        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
            result.object = null;
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

    @GetMapping("getById/{IdUsuario}")
    public ResponseEntity GetById(@PathVariable int IdUsuario) {
        Result result = new Result();
        try {
            result.object = iUsuarioDAO.findById(IdUsuario).orElse(null);
            result.correct = true;
        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
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
        usuarioDireccion.Usuario.setStatus(1);
        Result result = new Result();

        try {
            iUsuarioDAO.save(usuarioDireccion.Usuario);
            Direccion direccion = new Direccion();
            direccion = usuarioDireccion.Direccion;
            direccion.Usuario = usuarioDireccion.Usuario;
            iDireccionDAO.save(usuarioDireccion.Direccion);
            result.correct = true;
        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }

        if (result.correct == true) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result.errorMessage);
        }
    }

    @PutMapping("update")
    public ResponseEntity Update(@RequestBody Usuario usuario) {
        Result result = new Result();
        try {

            Usuario usuarioFromDB = iUsuarioDAO.findById(usuario.getIdUsuario()).orElse(null);

            usuarioFromDB.setUserName(usuario.getUserName());
            usuarioFromDB.setNombre(usuario.getNombre());
            usuarioFromDB.setApellidoPaterno(usuario.getApellidoPaterno());
            usuarioFromDB.setApellidoMaterno(usuario.getApellidoMaterno());
            usuarioFromDB.setEmail(usuario.getEmail());
            usuarioFromDB.setPassword(usuario.getPassword());
            usuarioFromDB.setFechaNacimiento(usuario.getFechaNacimiento());
            usuarioFromDB.setSexo(usuario.getSexo());
            usuarioFromDB.setTelefono(usuario.getTelefono());
            usuarioFromDB.setCelular(usuario.getCelular());
            usuarioFromDB.setCURP(usuario.getCURP());
            usuarioFromDB.setImagen(usuario.getImagen());
            usuarioFromDB.Rol.setIdRol(usuario.Rol.getIdRol());

            iUsuarioDAO.save(usuarioFromDB);
            result.correct = true;
        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }

        if (result.correct == true) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result.errorMessage);
        }
    }

    @PatchMapping("updateStatus/{IdUsuario}/{Status}")
    public ResponseEntity UpdateStatus(@PathVariable int IdUsuario, @PathVariable int Status) {
        Result result = new Result();

        if (result.correct == true) {
            return ResponseEntity.ok().body(result);
        } else {
            return ResponseEntity.badRequest().body(result.errorMessage);
        }
    }

    @DeleteMapping("delete/{IdUsuario}")
    public ResponseEntity Delete(@PathVariable int IdUsuario) {
        Result result = new Result();

        try {
            List<Direccion> listaDirecciones = iDireccionDAO.findDireccion(IdUsuario);
            for (Direccion direccion : listaDirecciones) {
                iDireccionDAO.delete(direccion);
            }

            iUsuarioDAO.deleteById(IdUsuario);

            result.correct = true;
        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }

        if (result.correct == true) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result.errorMessage);
        }
    }

//    @PostMapping("getAllDinamico")
//    public ResponseEntity GetAllDinamico(@RequestBody Usuario usuario) {
//        Result result = usuarioDAOImplementation.GetAllDinamico(usuario);
//        if (result.correct == true) {
//            if (result.objects.isEmpty()) {
//                return ResponseEntity.noContent().build();
//            } else {
//                return ResponseEntity.ok(result);
//            }
//        } else {
//            return ResponseEntity.internalServerError().body(result.errorMessage);
//        }
//    }
//    
//    //Java Streams

//    @PostMapping("getAllDinamico")
//    public ResponseEntity GetAllDinamico(@RequestBody Usuario usuario) {
//        Result result = new Result();
//
//        List<Usuario> listaUsuarios = iUsuarioDAO.findAll();
//        result.objects = new ArrayList();
//
//        for (Usuario usuarios : listaUsuarios) {
//            UsuarioDireccion usuarioDireccion = new UsuarioDireccion();
//            usuarioDireccion.Usuario = new Usuario();
//            usuarioDireccion.Usuario = usuarios;
//            usuarioDireccion.Direcciones = iDireccionDAO.findDireccion(usuarioDireccion.Usuario.getIdUsuario());            
//            result.objects.add(usuarioDireccion);
//        }
//        
//        result.objects = result.objects.stream()
//                .map(u -> (Usuario) u)
//                .filter(u
//                        -> u.Usuario.getNombre().toLowerCase().contains(usuario.getNombre().toLowerCase())
//                || u.Usuario.getApellidoPaterno().toLowerCase().contains(usuario.getApellidoPaterno().toLowerCase())
//                || u.Usuario.getApellidoPaterno().toLowerCase().contains(usuario.getApellidoMaterno().toLowerCase())
//                )
//                .collect(Collectors.toList());
//        if (usuario.getStatus() != null) {
//            result.objects = result.objects.stream()
//                    .map(u -> (UsuarioDireccion) u)
//                    .filter(u -> u.Usuario.getStatus() == (usuario.getStatus()))
//                    .collect(Collectors.toList());
//        }
//
//        if (usuario.Rol.getIdRol() != null && usuario.Rol.getIdRol() != 0) {
//            result.objects = result.objects.stream()
//                    .map(u -> (UsuarioDireccion) u)
//                    .filter(u -> u.Usuario.Rol.getIdRol() == (usuario.Rol.getIdRol()))
//                    .collect(Collectors.toList());
//        }
//
//        if (result.correct == true) {
//            if (result.objects.isEmpty()) {
//                return ResponseEntity.noContent().build();
//            } else {
//                return ResponseEntity.ok(result);
//            }
//        } else {
//            return ResponseEntity.internalServerError().body(result.errorMessage);
//        }
//    }
    
//    @GetMapping("orden/{bandera}") 
//    public ResponseEntity Ordenar (@PathVariable int bandera){
//        Result result = usuarioDAOImplementation.GetAll();
//        
//        if(bandera == 1){
//        //NOMBRE
//        result.objects = result.objects.stream()
//                .map(u -> (UsuarioDireccion) u)
//                .sorted(Comparator.comparing(u -> u.Usuario.getNombre()))
//                .collect(Collectors.toList());
//        }
//        
//        //APaterno
////        result.objects = result.objects.stream()
////                .map(u -> (UsuarioDireccion) u)
////                .sorted(Comparator.comparing(u -> u.Usuario.getApellidoPaterno()))
////                .collect(Collectors.toList());
//        
//        //AMaterno
////        result.objects = result.objects.stream()
////                .map(u -> (UsuarioDireccion) u)
////                .sorted(Comparator.comparing(u -> u.Usuario.getApellidoMaterno()))
////                .collect(Collectors.toList());
//        
//        //ROl
////        result.objects = result.objects.stream()
////                .map(u -> (UsuarioDireccion) u)
////                .sorted(Comparator.comparingInt(u -> u.Usuario.Rol.getIdRol()))
////                .collect(Collectors.toList());
//        
//         //Status
////        result.objects = result.objects.stream()
////                .map(u -> (UsuarioDireccion) u)
////                .sorted(Comparator.comparingInt(u -> u.Usuario.getStatus()))
////                .collect(Collectors.toList());
//
//        if(result.correct){
//         if(result.objects.isEmpty()){
//             return ResponseEntity.noContent().build();
//         }else{
//             return ResponseEntity.ok(result);
//         }   
//        }else{
//            return ResponseEntity.internalServerError().body(result.errorMessage);
//        }
//    }
//
//    @PostMapping("cargaMasiva")
//    public ResponseEntity CargaMasiva(@RequestParam("archivo") MultipartFile archivo) {
//
//        if (archivo != null && !archivo.isEmpty()) {
//            try {
//                Result result = new Result();
//                String tipoArchivo = archivo.getOriginalFilename().split("\\.")[1];
//
//                String root = System.getProperty("user.dir"); //Obtener direccion del proyecto en el equipo
//                String path = "src/main/resources/static/archivos";//Path relativo dentro del proyecto
//                String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmSS"));
//                String absolutePath = root + "/" + path + "/" + fecha + archivo.getOriginalFilename();
//                archivo.transferTo(new File(absolutePath));
//
//                //Leer el archivo
//                List<UsuarioDireccion> listaUsuarios = new ArrayList();
//
//                if (tipoArchivo.equals("txt")) {
//                    listaUsuarios = LecturaArchivoTXT(new File(absolutePath));
//                } else {
//                    listaUsuarios = LecturaArchivoExcel(new File(absolutePath));
//                }
//
//                //Valida el Archivo
//                List<ResultFile> listaErrores = ValidarArchivo(listaUsuarios);
//
//                if (listaErrores.isEmpty()) {
//                    //Procesa el archivo
//                    result.correct = true;
//                    result.object = absolutePath;
//                    return ResponseEntity.ok(result);
//                } else {
//                    //Salio mal y envio la lista de errores
//                    result.correct = false;
//                    result.objects = new ArrayList<>();
//                    for (ResultFile errores : listaErrores) {
//                        result.objects.add(errores);
//                    }
//                    return ResponseEntity.notFound().build();
//                }
//            } catch (Exception ex) {
//                return ResponseEntity.badRequest().body(ex);
//            }
//        } else {
//            return ResponseEntity.internalServerError().build();
//        }
//
//    }
//
//    @PostMapping("cargaMasiva/procesar")
//    public ResponseEntity Procesar(@RequestBody String absolutePath) {
//        Result result = new Result();
//
//        try {
//            String[] extension = absolutePath.split("\\.");
//            String tipoArchivo = extension[extension.length-1];
//
//            List<UsuarioDireccion> listaUsuarios = new ArrayList<>();
//
//            if (tipoArchivo.equals("txt")) {
//                listaUsuarios = LecturaArchivoTXT(new File(absolutePath));
//            } else {
//                listaUsuarios = LecturaArchivoExcel(new File(absolutePath));
//            }
//
//            for (UsuarioDireccion usuarioDireccion : listaUsuarios) {
//                usuarioDAOImplementation.Add(usuarioDireccion);
//            }
//
//            result.correct = true;
//            return ResponseEntity.ok(result);
//        } catch (Exception e) {
//            result.correct = false;
//            return ResponseEntity.internalServerError().body(result);
//        }
//    }
//
//    public List<UsuarioDireccion> LecturaArchivoTXT(File archivo) {
//        List<UsuarioDireccion> listaUsuarios = new ArrayList<>();
//
//        try (FileReader fileReader = new FileReader(archivo); BufferedReader bufferedReader = new BufferedReader(fileReader);) {
//            String linea;
//            while ((linea = bufferedReader.readLine()) != null) {
//                String[] campos = linea.split("\\|");
//                UsuarioDireccion usuarioDireccion = new UsuarioDireccion();
//                usuarioDireccion.Usuario = new Usuario();
//                usuarioDireccion.Usuario.setUserName(campos[0]);
//                usuarioDireccion.Usuario.setNombre(campos[1]);
//                usuarioDireccion.Usuario.setApellidoPaterno(campos[2]);
//                usuarioDireccion.Usuario.setApellidoMaterno(campos[3]);
//                usuarioDireccion.Usuario.setEmail(campos[4]);
//                usuarioDireccion.Usuario.setPassword(campos[5]);
//                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); //Dar el formato a la fecha
//                usuarioDireccion.Usuario.setFechaNacimiento(formatter.parse(campos[6]));
//                usuarioDireccion.Usuario.setSexo(campos[7].charAt(0));
//                usuarioDireccion.Usuario.setTelefono(campos[8]);
//                usuarioDireccion.Usuario.setCelular(campos[9]);
//                usuarioDireccion.Usuario.setCURP(campos[10]);
//
//                usuarioDireccion.Usuario.Rol = new Rol();
//                usuarioDireccion.Usuario.Rol.setIdRol(Integer.parseInt(campos[11]));
//                usuarioDireccion.Usuario.setImagen(null);
//                usuarioDireccion.Usuario.setStatus(Integer.parseInt(campos[12]));
//
//                usuarioDireccion.Direccion = new Direccion();
//                usuarioDireccion.Direccion.setCalle(campos[13]);
//                usuarioDireccion.Direccion.setNumeroInterior(campos[14]);
//                usuarioDireccion.Direccion.setNumeroExterior(campos[15]);
//
//                usuarioDireccion.Direccion.Colonia = new Colonia();
//                usuarioDireccion.Direccion.Colonia.setIdColonia(Integer.parseInt(campos[16]));
//
//                listaUsuarios.add(usuarioDireccion);
//
//            }
//        } catch (Exception ex) {
//            ResponseEntity.badRequest().body(ex);
//        }
//        return listaUsuarios;
//    }
//
//    public List<UsuarioDireccion> LecturaArchivoExcel(File archivo) {
//        List<UsuarioDireccion> listaUsuarios = new ArrayList<>();
//        try (XSSFWorkbook woorkBook = new XSSFWorkbook(archivo)) {
//            for (Sheet sheet : woorkBook) {
//                for (Row row : sheet) {
//                    UsuarioDireccion usuarioDireccion = new UsuarioDireccion();
//                    usuarioDireccion.Usuario = new Usuario();
//
//                    usuarioDireccion.Usuario.setUserName(row.getCell(0).toString());
//                    usuarioDireccion.Usuario.setNombre(row.getCell(1).toString());
//                    usuarioDireccion.Usuario.setApellidoPaterno(row.getCell(2).toString());
//                    usuarioDireccion.Usuario.setApellidoMaterno(row.getCell(3) != null ? row.getCell(3).toString() : "X");
//                    usuarioDireccion.Usuario.setEmail(row.getCell(4).toString());
//                    usuarioDireccion.Usuario.setPassword(row.getCell(5).toString());
//                    usuarioDireccion.Usuario.setFechaNacimiento(row.getCell(6).getDateCellValue());
//                    usuarioDireccion.Usuario.setSexo(row.getCell(7).toString().charAt(0));
//                    DataFormatter dataFormatter = new DataFormatter();
//                    usuarioDireccion.Usuario.setTelefono(dataFormatter.formatCellValue(row.getCell(8)));
//                    usuarioDireccion.Usuario.setCelular(dataFormatter.formatCellValue(row.getCell(9)));
//                    usuarioDireccion.Usuario.setCURP(row.getCell(10) != null ? (row.getCell(10).getStringCellValue()) : null);
//                    usuarioDireccion.Usuario.Rol = new Rol();
//                    usuarioDireccion.Usuario.Rol.setIdRol(row.getCell(11) != null ? ((int) row.getCell(11).getNumericCellValue()) : 3);
//                    usuarioDireccion.Usuario.setImagen(null);
//                    usuarioDireccion.Usuario.setStatus(row.getCell(12) != null ? ((int) row.getCell(12).getNumericCellValue()) : 1);
//
//                    usuarioDireccion.Direccion = new Direccion();
//                    usuarioDireccion.Direccion.setCalle(row.getCell(13).getStringCellValue());
//                    usuarioDireccion.Direccion.setNumeroInterior(dataFormatter.formatCellValue(row.getCell(14)));
//                    usuarioDireccion.Direccion.setNumeroExterior(dataFormatter.formatCellValue(row.getCell(15)));
//
//                    usuarioDireccion.Direccion.Colonia = new Colonia();
//                    usuarioDireccion.Direccion.Colonia.setIdColonia(row.getCell(16) != null ? (int) row.getCell(16).getNumericCellValue() : 0);
//
//                    listaUsuarios.add(usuarioDireccion);
//                }
//            }
//        } catch (Exception ex) {
//            ResponseEntity.badRequest().body(ex);
//        }
//        return listaUsuarios;
//    }
//
//    public List<ResultFile> ValidarArchivo(List<UsuarioDireccion> listaUsuarios) {
//        List<ResultFile> listaErrores = new ArrayList<>();
//
//        if (listaUsuarios == null) {
//            listaErrores.add(new ResultFile(0, "La lista es nula", "La lista es nula"));
//        } else if (listaUsuarios.isEmpty()) {
//            listaErrores.add(new ResultFile(0, "La lista esta vacia", "La lista esta vacia"));
//        } else {
//            int fila = 1;
//            for (UsuarioDireccion usuarioDireccion : listaUsuarios) {
//
//                if (usuarioDireccion.Usuario.getUserName() == null || usuarioDireccion.Usuario.getUserName().equals("")) {
//                    listaErrores.add(new ResultFile(fila, usuarioDireccion.Usuario.getUserName(), "El UserName es obligatorio"));
//                }
//
//                if (usuarioDireccion.Usuario.getNombre() == null || usuarioDireccion.Usuario.getNombre().equals("")) {
//                    listaErrores.add(new ResultFile(fila, usuarioDireccion.Usuario.getNombre(), "El Nombre es Obligatorio"));
//                }
//
//                if (usuarioDireccion.Usuario.getApellidoPaterno() == null || usuarioDireccion.Usuario.getApellidoPaterno().equals("")) {
//                    listaErrores.add(new ResultFile(fila, usuarioDireccion.Usuario.getApellidoPaterno(), "El Apellido Paterno es Obligatorio"));
//                }
//
//                if (usuarioDireccion.Usuario.getEmail() == null || usuarioDireccion.Usuario.getEmail().equals("")) {
//                    listaErrores.add(new ResultFile(fila, usuarioDireccion.Usuario.getEmail(), "El Email es obligatorio"));
//                }
//
//                if (usuarioDireccion.Usuario.getPassword() == null || usuarioDireccion.Usuario.getPassword().equals("")) {
//                    listaErrores.add(new ResultFile(fila, usuarioDireccion.Usuario.getPassword(), "La contrsaeña es obligatoria"));
//                }
//
//                if (usuarioDireccion.Usuario.getFechaNacimiento() == null || usuarioDireccion.Usuario.getFechaNacimiento().equals("")) {
//                    listaErrores.add(new ResultFile(fila, usuarioDireccion.Usuario.getFechaNacimiento().toString(), "La fecha es obligatoria"));
//                }
//
//                if (String.valueOf(usuarioDireccion.Usuario.getSexo()) == null || String.valueOf(usuarioDireccion.Usuario.getSexo()).equals("")) {
//                    listaErrores.add(new ResultFile(fila, String.valueOf(usuarioDireccion.Usuario.getSexo()), "No se le asignado el sexo, el campo es obligatorio"));
//                }
//
//                if (usuarioDireccion.Usuario.getTelefono() == null || usuarioDireccion.Usuario.getTelefono().equals("")) {
//                    listaErrores.add(new ResultFile(fila, usuarioDireccion.Usuario.getTelefono(), "Telefono es oblogatorio"));
//                }
//
//                if (Integer.toString(usuarioDireccion.Usuario.Rol.getIdRol()) == null || usuarioDireccion.Usuario.Rol.getIdRol() == 0 || Integer.toString(usuarioDireccion.Usuario.Rol.getIdRol()).equals("")) {
//                    listaErrores.add(new ResultFile(fila, Integer.toString(usuarioDireccion.Usuario.Rol.getIdRol()), "El rol es obligatorio"));
//                }
//
//                if (Integer.toString(usuarioDireccion.Usuario.getStatus()) == null || Integer.toString(usuarioDireccion.Usuario.getStatus()).equals("")) {
//                    listaErrores.add(new ResultFile(fila, Integer.toString(usuarioDireccion.Usuario.getStatus()), "El Status es obligatorio"));
//                }
//
//                if (usuarioDireccion.Direccion.getCalle() == null || usuarioDireccion.Direccion.getCalle().equals("")) {
//                    listaErrores.add(new ResultFile(fila, usuarioDireccion.Direccion.getCalle(), "La calle es un campo obligatorio"));
//                }
//
//                if (usuarioDireccion.Direccion.getNumeroExterior() == null || usuarioDireccion.Direccion.getNumeroExterior().equals("")) {
//                    listaErrores.add(new ResultFile(fila, usuarioDireccion.Direccion.getNumeroExterior(), "El numero exterior es obligatorio"));
//                }
//
//                if (Integer.toString(usuarioDireccion.Direccion.Colonia.getIdColonia()) == null || Integer.toString(usuarioDireccion.Direccion.Colonia.getIdColonia()).equals("")) {
//                    listaErrores.add(new ResultFile(fila, Integer.toString(usuarioDireccion.Direccion.Colonia.getIdColonia()), "La colonia es obligatoria"));
//                }
//                fila++;
//            }
//        }
//        return listaErrores;
//    }
}
