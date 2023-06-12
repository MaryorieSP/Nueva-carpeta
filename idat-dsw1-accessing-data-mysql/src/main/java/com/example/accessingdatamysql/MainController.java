package com.example.accessingdatamysql;

import javax.management.ConstructorParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;
import java.util.Map;
import java.lang.String;
import java.lang.Object;

@Controller // This means that this class is a Controller
@RequestMapping(path="/mascotas") // This means URL's start with /demo (after Application path)
public class MainController {
  @Autowired // This means to get the bean called userRepository
         // Which is auto-generated by Spring, we will use it to handle the data
  private MascotaRepository mascotaRepository;

  @Autowired
  private JdbcTemplate jdbcTemplate;



  @PostMapping(path="/add") // POST http://localhost:8080/demo/add
  public @ResponseBody String addNewUser (@RequestParam String nombre
      , @RequestParam String raza,@RequestParam String propietario) {
    // @ResponseBody means the returned String is the response, not a view name
    // @RequestParam means it is a parameter from the GET or POST request

    Mascota n = new Mascota();
    n.setNombre(nombre);
    n.setRaza(raza);
    n.setPropietario(propietario);
    mascotaRepository.save(n);
    return "Saved";
  }

  @GetMapping(path="/all") // GET http://localhost:8080/demo/all
  public @ResponseBody Iterable<Mascota> getAllMascotas() {
    // This returns a JSON or XML with the users
    return mascotaRepository.findAll();
  }

  @GetMapping(path="/ver/{id}") // GET http://localhost:8080/demo/all
  public @ResponseBody Mascota getMascota(@PathVariable("id") Integer id) {
    return mascotaRepository.findById(id).orElse(null);
  }

  @PutMapping(path="/edit")
  public @ResponseBody String editMascota(@RequestParam Integer id, @RequestParam String nombre
  , @RequestParam String raza,@RequestParam String propietario) 
  {
    Mascota mascota = mascotaRepository.findById(id).orElse(null);
    if (mascota != null) {
      mascota.setNombre(nombre);
      mascota.setRaza(raza);
      mascota.setPropietario(propietario);
      mascotaRepository.save(mascota);
      return "Edited";
    }
    return "Not found";
  }

  @DeleteMapping(path="/del")
  public @ResponseBody String deleteMascota(@RequestParam Integer id) {
    Mascota mascota = mascotaRepository.findById(id).orElse(null);
    if (mascota != null) {
      mascotaRepository.delete(mascota);
      return "Deleted";
    }
    return "Not found";
  }

  @GetMapping(path="/get/report")
  public @ResponseBody List getReport() {
    String sql = "SELECT CONCAT(nombre, ' ==> ',raza, ' ==> ',propietario) as reporte FROM mascota";
    List<Map<String, Object>> queryResult = jdbcTemplate.queryForList(sql);
    return queryResult;
  }




}