package spring.ai.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.ai.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
}
