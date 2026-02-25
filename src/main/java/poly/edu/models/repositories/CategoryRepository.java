package poly.edu.models.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import poly.edu.models.entities.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {

}
