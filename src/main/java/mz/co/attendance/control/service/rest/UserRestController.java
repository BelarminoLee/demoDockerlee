package mz.co.attendance.control.service.rest;


import mz.co.attendance.control.dao.entities.attendance.Attendance;
import mz.co.attendance.control.dao.entities.configuration.Configuration;
import mz.co.attendance.control.dao.entities.security.User;
import mz.co.attendance.control.dao.entities.ussd.UssdSession;
import mz.co.attendance.control.dao.repositories.UserRepository;
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
@RequestMapping(value = "/user")
public class UserRestController {

    private final UserRepository userRepository;

    public UserRestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping(value = "/paginated", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<User>> getAll(@RequestParam("page") int page, @RequestParam("maxResults") int maxResults){
        Pageable pageable = PageRequest.of(page, maxResults);
        return new ResponseEntity(userRepository.findAll(pageable), HttpStatus.OK);
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<User> updateUser(@PathVariable long id, @RequestBody User user) {
        try {
            if (Objects.isNull(user)) {
                throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "User info is empty");
            }
            if (!userRepository.existsById(id)) {
                throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Specified User doesn't exist ");
            }
            userRepository.save(user);
            return new ResponseEntity(user, HttpStatus.OK);
        } catch (DataIntegrityViolationException exc) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User '" + user.getUsername() + "' is already registered. ", exc);
        }
    }

}
