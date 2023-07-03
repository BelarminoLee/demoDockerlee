package mz.co.attendance.control.dao.repositories;

import mz.co.attendance.control.dao.entities.employee.CellPhone;
import mz.co.attendance.control.enums.Language;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CellPhoneRepository extends JpaRepository<CellPhone, String> {

    CellPhone getByNumber(String s);
}
