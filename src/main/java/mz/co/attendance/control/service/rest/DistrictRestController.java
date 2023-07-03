package mz.co.attendance.control.service.rest;

import mz.co.attendance.control.dao.entities.district.District;
import mz.co.attendance.control.dao.entities.province.Province;
import mz.co.attendance.control.dao.repositories.DistrictRepository;
import mz.co.attendance.control.dao.repositories.ProvincieRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/district")
public class DistrictRestController {

    private final DistrictRepository districtRepository;
    private final ProvincieRepository provincieRepository;


    public DistrictRestController(DistrictRepository districtRepository, ProvincieRepository provincieRepository) {
        this.districtRepository = districtRepository;
        this.provincieRepository = provincieRepository;
    }

    @GetMapping(value = "/allBy",
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<District>> getAllBy(@RequestParam("province") long province){
        return new ResponseEntity(districtRepository.findAllByProvince_Id(province), HttpStatus.OK);
    }
}
