package com.hms.HospitalMS.repository;
import java.util.Optional; import org.springframework.data.jpa.repository.JpaRepository;
import com.hms.HospitalMS.model.User;
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);
}
