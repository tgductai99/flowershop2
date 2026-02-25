package poly.edu.models.services;

import java.util.List;

import poly.edu.models.entities.OrderDetail;

public interface OrderDetailServices {
	
	void save(OrderDetail orderDetail);
    
    void delete(Long id);    
    
    List<OrderDetail> findAll();
    
    OrderDetail findById(Long id);
}
