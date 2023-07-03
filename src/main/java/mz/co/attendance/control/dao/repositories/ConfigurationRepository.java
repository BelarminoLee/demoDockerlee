package mz.co.attendance.control.dao.repositories;

import mz.co.attendance.control.dao.entities.configuration.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {

    Optional<Configuration> getConfigurationByProperty(String property);
}
