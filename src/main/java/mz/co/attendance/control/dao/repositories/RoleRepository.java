package mz.co.attendance.control.dao.repositories;


import mz.co.attendance.control.dao.entities.security.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoleRepository extends JpaRepository<Role, String> {

    public static final String FIND_ALL_ROLES = "SELECT role_id FROM ru_role";

    @Query(value = FIND_ALL_ROLES, nativeQuery = true)
    String[] getAllRoles();
}