package poly.edu.models.services;

import java.util.List;

import poly.edu.models.entities.Category;

public interface CategoryServices {
	
	void save(Category category);
    
    void delete(String id);    
    
    List<Category> findAll();
    
    Category findById(String id);
    
    List<Category> filter(String keyword);
}
