package mz.co.attendance.control.service.client;

import mz.co.attendance.control.dao.entities.security.User;
import mz.co.attendance.control.dao.entities.base.AuditBean;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.EntityNotFoundException;


public interface CrudService<T extends AuditBean> {

    JpaRepository<T, Long> getRepository();

    default T save(User currentUser, T entity) {
        return getRepository().saveAndFlush(entity);
    }

    default void delete(User currentUser, T entity) {
        if (entity == null) {
            throw new EntityNotFoundException();
        }
        getRepository().delete(entity);
    }

    default void delete(User currentUser, long id) {
        delete(currentUser, load(id));
    }

    default long count() {
        return getRepository().count();
    }

    default T load(long id) {
        T entity = getRepository().findById(id).orElse(null);
        if (entity == null) {
            throw new EntityNotFoundException();
        }
        return entity;
    }

    T createNew(User currentUser);
}