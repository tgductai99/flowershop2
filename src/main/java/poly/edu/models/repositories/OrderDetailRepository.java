package poly.edu.models.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import poly.edu.models.entities.OrderDetail;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
	
	@Query("SELECT COUNT(od) FROM OrderDetail od WHERE od.product.id = :productId")
	long countByProductId(@Param("productId") Integer productId);
	
	List<OrderDetail> findByProductId(Integer productId);
}
