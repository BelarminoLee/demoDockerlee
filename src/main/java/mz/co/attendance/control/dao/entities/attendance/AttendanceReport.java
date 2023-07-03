package mz.co.attendance.control.dao.entities.attendance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AttendanceReport implements Serializable {
    private static final long serialVersionUID = 1L;
    private long countableDays;
    private List<AttendanceReportRaw> attendanceRaws;

    public AttendanceReport() {
        this.attendanceRaws = new ArrayList<>();
    }

    public long getCountableDays() {
        return countableDays;
    }

    public void setCountableDays(long countableDays) {
        this.countableDays = countableDays;
    }

    public List<AttendanceReportRaw> getAttendanceRaws() {
        return attendanceRaws;
    }

    public void setAttendanceRaws(List<AttendanceReportRaw> attendanceRaws) {
        this.attendanceRaws = attendanceRaws;
    }
}
