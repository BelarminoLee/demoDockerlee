package mz.co.attendance.control.service.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {"", "/", "attendance-control"})
public class RootRestController {

    /**
     * REST API for check REST service availability
     * <p>
     * GET http://localhost:8082/api/
     *
     */
    @GetMapping("")
    public ResponseEntity<String> ping() {
        return new ResponseEntity("OK", HttpStatus.OK);
    }

}
