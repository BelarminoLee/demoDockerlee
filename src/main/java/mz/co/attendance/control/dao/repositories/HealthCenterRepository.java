package mz.co.attendance.control.dao.repositories;

import mz.co.attendance.control.dao.entities.healhCenter.HealthCenter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HealthCenterRepository extends JpaRepository<HealthCenter, Long> {

    List<HealthCenter> findAllByDistrict_Id(Long district);
}
