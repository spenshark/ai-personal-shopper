package spring.ai.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.ai.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
