package mz.co.attendance.control.service.rest;

import mz.co.attendance.control.dao.entities.attendance.Attendance;
import mz.co.attendance.control.dao.entities.attendance.NewAttendance;
import mz.co.attendance.control.dao.entities.configuration.Configuration;
import mz.co.attendance.control.dao.repositories.ConfigurationRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping(value = "/configuration")
public class ConfigurationRestController {

    private final ConfigurationRepository configurationRepository;

    public ConfigurationRestController(ConfigurationRepository configurationRepository) {
        this.configurationRepository = configurationRepository;
    }

    /**
     * REST API for getting configuration list
     * <p>
     * GET http://localhost:8082/api/configuration/list
     */
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<Configuration>> getAll() {
        return new ResponseEntity(configurationRepository.findAll(), HttpStatus.OK);
    }

    /**
     * REST API for getting configuration info
     * <p>
     * GET http://localhost:8082/api/configuration/byProperty/APP_NAME
     *
     * @param property property of the configuration
     */
    @GetMapping(value = "/byProperty/{property}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Configuration> getByProperty(@PathVariable String property) {
        if (StringUtils.isBlank(property)) {
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Missing Argument. " + "Please certify that Property was passed");
        }
        Optional<Configuration> configuration = configurationRepository.getConfigurationByProperty(property);
        if (!configuration.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Configuration With property " + property + " was not found.");
        }
        return new ResponseEntity(configuration.get(), HttpStatus.OK);
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Attendance> updateConfiguration(@PathVariable long id, @RequestBody Configuration configuration) {
        try {
            if (Objects.isNull(configuration)) {
                throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Configuration info is empty");
            }
            if (!configurationRepository.existsById(id)) {
                throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Specified Configuration doesn't exist ");
            }
            configurationRepository.save(configuration);
            return new ResponseEntity(configuration, HttpStatus.OK);
        } catch (DataIntegrityViolationException exc) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Configuration '" + configuration.getProperty() + "' is already registered. ", exc);
        }
    }
}
