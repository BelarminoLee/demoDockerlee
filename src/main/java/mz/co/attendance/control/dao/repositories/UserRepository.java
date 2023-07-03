package mz.co.attendance.control.dao.repositories;

import mz.co.attendance.control.dao.entities.security.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT t FROM User t WHERE LOWER(t.username) LIKE LOWER(CONCAT('%', :keyword,'%')) OR LOWER(t.email) LIKE LOWER(CONCAT('%', :keyword,'%'))")
    User findByEmailOrUsernameIgnoreCase(String keyword);

    Page<User> findBy(Pageable pageable);

    Page<User> findByEmailLikeIgnoreCaseOrFirstNameLikeIgnoreCaseOrLastNameLikeIgnoreCase(
            String emailLike, String firstNameLike, String lastNameLike, Pageable pageable);

    long countByEmailLikeIgnoreCaseOrFirstNameLikeIgnoreCaseOrLastNameLikeIgnoreCase(
            String emailLike, String firstNameLike, String lastNameLike);
}