package repository;

import model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Optional<Category> findByCategoryname(String categoryname);
    List<Category> findByCategorynameContainingIgnoreCase(String keyword);
}
