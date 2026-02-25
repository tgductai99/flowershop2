package poly.edu.models.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import poly.edu.models.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

}
