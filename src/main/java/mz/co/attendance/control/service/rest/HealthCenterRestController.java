package mz.co.attendance.control.service.rest;

import mz.co.attendance.control.dao.entities.attendance.Attendance;
import mz.co.attendance.control.dao.entities.healhCenter.HealthCenter;
import mz.co.attendance.control.dao.entities.province.Province;
import mz.co.attendance.control.dao.repositories.DistrictRepository;
import mz.co.attendance.control.dao.repositories.HealthCenterRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = "/healthCenter")
public class HealthCenterRestController {

    private final HealthCenterRepository healthCenterRepository;
    private final DistrictRepository districtRepository;

    public HealthCenterRestController(HealthCenterRepository healthCenterRepository, DistrictRepository districtRepository) {
        this.healthCenterRepository = healthCenterRepository;
        this.districtRepository = districtRepository;
    }

   @GetMapping(value ="/allBy",
   produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<HealthCenter>> getAllBy(@RequestParam("district") long district){
        return new ResponseEntity(healthCenterRepository.findAllByDistrict_Id(district), HttpStatus.OK);
    }

    @GetMapping(value ="/all",
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<HealthCenter>> getAll(){
        return new ResponseEntity(healthCenterRepository.findAll(), HttpStatus.OK);
    }
    @GetMapping(value = "/paginated", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<Attendance>> getAll(@RequestParam("page") int page, @RequestParam("maxResults") int maxResults){
        Pageable pageable = PageRequest.of(page, maxResults);
        return new ResponseEntity(healthCenterRepository.findAll(pageable), HttpStatus.OK);
    }

    @PostMapping(value = "/create",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<HealthCenter> createHealthCenter(@RequestBody HealthCenter newHealthCenter) {
        try {
            if (Objects.isNull(newHealthCenter)) {
                throw new ResponseStatusException(
                        HttpStatus.PRECONDITION_FAILED, "Health Center info is empty");
            }
            HealthCenter healthCenter = healthCenterRepository.save(newHealthCenter);

            return new ResponseEntity<>(healthCenter, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException exc) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Health Center with the name '"
                    + newHealthCenter.getName() + "' is already registered. Please, provide a different name", exc);
        }
    }

}

