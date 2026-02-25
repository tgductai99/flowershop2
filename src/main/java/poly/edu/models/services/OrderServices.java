package poly.edu.models.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import poly.edu.models.entities.Order;

public interface OrderServices {
	
	void save(Order order);
    
    void delete(Long id);
    
    List<Order> findAll();
    
    Order findById(Long id);
    
	Order addOrderDetailToOrder(Order order, Integer productId, Integer quantity);
	
	boolean validateStock(Order order);
	
	Page<Order> searchOrders(String keyword, String status, LocalDate fromDate, LocalDate toDate, Pageable pageable);
	
	void updateStatus(Long orderId, String status);
	
	Double calculateOrderTotal(Order order);
	
	List<Order> findAllDescById();
}
