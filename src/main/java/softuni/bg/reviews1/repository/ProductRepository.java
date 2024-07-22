package softuni.bg.reviews1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.bg.reviews1.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
}
