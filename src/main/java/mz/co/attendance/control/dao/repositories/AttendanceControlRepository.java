package mz.co.attendance.control.dao.repositories;

import mz.co.attendance.control.dao.entities.attendance.Attendance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AttendanceControlRepository extends JpaRepository<Attendance, Long> {
    @Query("Select a from Attendance a where a.employee.id = ?1 and CAST(a.entryDate as date) = CURRENT_DATE")
    Attendance queryByCurrentDate(long id);
    Page<Attendance> findAll(Pageable pageable);

}
