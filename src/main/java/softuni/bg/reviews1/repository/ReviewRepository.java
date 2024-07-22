package softuni.bg.reviews1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.bg.reviews1.model.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {
}
