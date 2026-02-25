package poly.edu.models.services;

import java.util.List;

import poly.edu.models.entities.Product;

public interface ProductServices {
	
	void save(Product product);
    
    Product delete(Integer id);    
    
    List<Product> findAll();
    
    Product findById(Integer id);
    
    List<Product> filter(String keyword, String categoryId);
    
    List<Product> filterAndSort(String keyword, String categoryId, String sortBy);
    
    List<Product> findAllDescById();
    
    boolean canDelete(Integer id);
    
    void softDelete(Integer id);
}
