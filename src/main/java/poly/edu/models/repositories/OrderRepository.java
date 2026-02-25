package poly.edu.models.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import poly.edu.models.entities.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
	
	@Query("SELECT o FROM Order o WHERE " +
		   "(:keyword IS NULL OR :keyword = '' OR " +
		   "LOWER(o.account.fullname) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
		   "LOWER(o.account.username) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
		   "CONCAT('', o.id) LIKE CONCAT('%', :keyword, '%')) AND " +
		   "(:status IS NULL OR :status = '' OR o.status = :status) AND " +
		   "(:fromDate IS NULL OR o.createDate >= :fromDate) AND " +
		   "(:toDate IS NULL OR o.createDate <= :toDate)")
	Page<Order> searchOrders(@Param("keyword") String keyword,
							  @Param("status") String status,
							  @Param("fromDate") LocalDate fromDate,
							  @Param("toDate") LocalDate toDate,
							  Pageable pageable);
	
	List<Order> findByStatus(String status);
}
