package mz.co.attendance.control.dao.repositories;

import mz.co.attendance.control.dao.entities.district.District;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DistrictRepository extends JpaRepository <District, Long> {

    List<District> findAllByProvince_Id(Long province);
}
