package poly.edu.models.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import poly.edu.models.entities.Category;
import poly.edu.models.repositories.CategoryRepository;
import poly.edu.models.services.CategoryServices;

@Service
public class CategoryServiceImpl implements CategoryServices {
	
	@Autowired
	CategoryRepository categoryRepository;
	
	@Override
	public void save(Category category) {
		// TODO Auto-generated method stub
		categoryRepository.save(category);
	}

	@Override
	public void delete(String id) {
		// TODO Auto-generated method stub
		categoryRepository.deleteById(id);
	}

	@Override
	public List<Category> findAll() {
		// TODO Auto-generated method stub
		return categoryRepository.findAll();
	}

	@Override
	public Category findById(String id) {
		// TODO Auto-generated method stub
		return categoryRepository.findById(id).orElse(null);
	}

	@Override
	public List<Category> filter(String keyword) {
		// TODO Auto-generated method stub
		List<Category> categories = categoryRepository.findAll();
		
		return categories.stream().filter(c -> {
			if (keyword != null && !keyword.isBlank()) {
				boolean keywordMatched = c.getName() != null && c.getName().toLowerCase().trim().contains(keyword.toLowerCase().trim());
				if (!keywordMatched) {
					return false;
				}
			}
			return true;
		}).toList();
	}

}
