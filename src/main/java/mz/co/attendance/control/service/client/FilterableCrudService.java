package mz.co.attendance.control.service.client;

import mz.co.attendance.control.dao.entities.base.AuditBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface FilterableCrudService<T extends AuditBean> extends CrudService<T> {

    Page<T> findAnyMatching(Optional<String> filter, Pageable pageable);

    long countAnyMatching(Optional<String> filter);

}