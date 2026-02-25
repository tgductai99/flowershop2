package poly.edu.models.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import poly.edu.models.entities.OrderDetail;
import poly.edu.models.repositories.OrderDetailRepository;
import poly.edu.models.services.OrderDetailServices;

@Service
public class OrderDetailServiceImpl implements OrderDetailServices {
	
	@Autowired
	OrderDetailRepository orderDetailRepository;
	
	@Override
	public void save(OrderDetail orderDetail) {
		// TODO Auto-generated method stub
		orderDetailRepository.save(orderDetail);
	}
	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		orderDetailRepository.deleteById(id);
	}

	@Override
	public List<OrderDetail> findAll() {
		// TODO Auto-generated method stub
		return orderDetailRepository.findAll();
	}

	@Override
	public OrderDetail findById(Long id) {
		// TODO Auto-generated method stub
		return orderDetailRepository.findById(id).orElse(null);
	}

}
