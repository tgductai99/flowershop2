package poly.edu.models.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import poly.edu.models.entities.Product;
import poly.edu.models.repositories.DiscountRepository;
import poly.edu.models.repositories.OrderDetailRepository;
import poly.edu.models.repositories.ProductRepository;
import poly.edu.models.services.ProductServices;

@Service
public class ProductServiceImpl implements ProductServices {

	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	OrderDetailRepository orderDetailRepository;
	
	@Autowired
	DiscountRepository discountRepository;
	
	@Override
	public void save(Product product) {
		// TODO Auto-generated method stub
		productRepository.save(product);
	}

	@Override
	@Transactional
	public Product delete(Integer id) {
		// Check if product can be deleted
		if (!canDelete(id)) {
			throw new RuntimeException("Cannot delete product. It is referenced in orders or discounts. Consider marking it as unavailable instead.");
		}
		
		productRepository.deleteById(id);
		return productRepository.findById(id).orElse(null);
	}

	@Override
	public List<Product> findAll() {
		// TODO Auto-generated method stub
		return productRepository.findAll();
	}

	@Override
	public Product findById(Integer id) {
		// TODO Auto-generated method stub
		return productRepository.findById(id).orElse(null);
	}

	@Override
	public List<Product> filter(String keyword, String categoryId) {
		// TODO Auto-generated method stub
		List<Product> products = productRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));;
		
		return products.stream().filter(p -> {
			
			if(keyword != null && !keyword.isBlank()) {
				boolean productFound = p.getName() != null && p.getName().toLowerCase().contains(keyword.toLowerCase());
				if (!productFound)
					return false;
			}
			
			if (categoryId != null && !categoryId.isBlank()) {
				boolean categoryIdMatched= p.getCategory().getId() != null && p.getCategory().getId().equals(categoryId);
				if (!categoryIdMatched) {
					return false;
				}
			}
			
			return true;
		}).toList();
	}

	@Override
	public List<Product> filterAndSort(String keyword, String categoryId, String sortBy) {
		// TODO Auto-generated method stub
		List<Product> filtered = filter(keyword, categoryId);
		
		if (sortBy != null && !sortBy.isBlank()) {
			return switch (sortBy) {
				case "price-asc" -> filtered.stream()
					.sorted((p1, p2) -> Double.compare(p1.getPrice(), p2.getPrice()))
					.toList();
				case "price-desc" -> filtered.stream()
					.sorted((p1, p2) -> Double.compare(p2.getPrice(), p1.getPrice()))
					.toList();
				default -> filtered;
			};
		}
		
		return filtered;
	}

	@Override
	public List<Product> findAllDescById() {
		// TODO Auto-generated method stub
		return productRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
	}

	@Override
	public boolean canDelete(Integer id) {
		// Check if product is referenced in any orders or discounts
		long orderCount = orderDetailRepository.countByProductId(id);
		long discountCount = discountRepository.countByProductId(id);
		
		return orderCount == 0 && discountCount == 0;
	}

	@Override
	@Transactional
	public void softDelete(Integer id) {
		// Mark product as unavailable instead of deleting
		Product product = findById(id);
		if (product != null) {
			product.setAvailable(false);
			productRepository.save(product);
		}
	}
}
