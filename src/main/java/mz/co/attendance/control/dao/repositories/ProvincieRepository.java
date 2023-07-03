package mz.co.attendance.control.dao.repositories;

import mz.co.attendance.control.dao.entities.province.Province;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProvincieRepository extends JpaRepository<Province, Long> {
}
