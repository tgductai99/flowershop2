package poly.edu.models.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import poly.edu.models.entities.Discount;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Integer> {
	
	@Query("SELECT COUNT(d) FROM Discount d WHERE d.product.id = :productId")
	long countByProductId(@Param("productId") Integer productId);
	
	List<Discount> findByProductId(Integer productId);
}
