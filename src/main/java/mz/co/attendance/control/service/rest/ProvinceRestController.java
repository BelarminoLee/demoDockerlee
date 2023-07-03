package mz.co.attendance.control.service.rest;

import mz.co.attendance.control.dao.entities.province.Province;
import mz.co.attendance.control.dao.repositories.ProvincieRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = "/province")
public class ProvinceRestController {

    private final ProvincieRepository provincieRepository;

    public ProvinceRestController(ProvincieRepository provincieRepository) {
        this.provincieRepository = provincieRepository;
    }

    @GetMapping(value = "/all",
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<Province>> getAll() {
        return new ResponseEntity(provincieRepository.findAll(), HttpStatus.OK);
    }

    @PostMapping(value = "/create",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Province> createProvince(@RequestBody Province newProvince) {
        try {
            if (Objects.isNull(newProvince)) {
                throw new ResponseStatusException(
                        HttpStatus.PRECONDITION_FAILED, "Province info is empty");
            }
            Province province = provincieRepository.save(newProvince);

            return new ResponseEntity<>(province, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException exc) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Provincia '"
                    + newProvince.getName() + "' já registada no sistema. Introduza um novo nome", exc);
        }
    }


}
