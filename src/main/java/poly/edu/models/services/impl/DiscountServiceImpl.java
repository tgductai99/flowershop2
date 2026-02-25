package poly.edu.models.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import poly.edu.models.entities.Discount;
import poly.edu.models.repositories.DiscountRepository;
import poly.edu.models.services.DiscountServices;

@Service
public class DiscountServiceImpl implements DiscountServices {

	DiscountRepository discountRepository;
	
	@Override
	public void save(Discount discount) {
		// TODO Auto-generated method stub
		discountRepository.save(discount);
	}

	@Override
	public void delete(Integer id) {
		// TODO Auto-generated method stub
		discountRepository.deleteById(id);
	}

	@Override
	public List<Discount> findAll() {
		// TODO Auto-generated method stub
		return discountRepository.findAll();
	}

	@Override
	public Discount findById(Integer id) {
		// TODO Auto-generated method stub
		return discountRepository.findById(id).orElse(null);
	}

	@Override
	public List<Discount> filter(String keyword, String discountType, Boolean active) {
		// TODO Auto-generated method stub
		List<Discount> discounts = discountRepository.findAll();
		
		return discounts.stream().filter(d -> {
			
			if (keyword != null && !keyword.isBlank()) {
				boolean productMatched = d.getProduct() != null 
						&& d.getProduct().getName() != null 
						&& d.getProduct().getName().toLowerCase().contains(keyword.toLowerCase());
				if (!productMatched)
					return false;
			}
			
			if (discountType != null && !discountType.isBlank()) {
				boolean discountTypematched = d.getProduct() != null 
						&& d.getDiscountType() != null && d.getDiscountType().toLowerCase().equals(discountType.toLowerCase());
				if (!discountTypematched) 
					return false;
			}
			
			if(active != null) {
				if(!active.equals(d.getActive()))
					return false;
			}
			
			return true;
		}).toList();
	}
	
}
