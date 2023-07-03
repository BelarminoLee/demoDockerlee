package mz.co.attendance.control.dao.repositories;

import mz.co.attendance.control.dao.entities.employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findEmployeeByCellphone_Number(String cellPhone);
}
