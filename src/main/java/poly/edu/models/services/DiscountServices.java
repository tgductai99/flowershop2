package poly.edu.models.services;

import java.util.List;

import poly.edu.models.entities.Discount;

public interface DiscountServices {
	
	void save(Discount discount);
    
    void delete(Integer id);
    
    List<Discount> findAll();
    
    Discount findById(Integer id);
    
    List<Discount> filter(String keyword, String discountType, Boolean active);
}
