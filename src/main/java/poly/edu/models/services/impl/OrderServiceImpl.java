package poly.edu.models.services.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import poly.edu.models.entities.Order;
import poly.edu.models.entities.OrderDetail;
import poly.edu.models.entities.Product;
import poly.edu.models.repositories.OrderRepository;
import poly.edu.models.repositories.ProductRepository;
import poly.edu.models.services.OrderServices;

@Service
public class OrderServiceImpl implements OrderServices {
	
	@Autowired
	OrderRepository orderRepository;	
	
	@Autowired
	ProductRepository productRepository;
	
	@Override
	@Transactional
	public void save(Order order) {
		orderRepository.save(order);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		orderRepository.deleteById(id);
	}
	
	@Override
	public List<Order> findAll() {
		return orderRepository.findAll();
	}

	@Override
	public Order findById(Long id) {
		return orderRepository.findById(id).orElse(null);
	}

	@Override
	public Order addOrderDetailToOrder(Order order, Integer productId, Integer quantity) {
		Product product = productRepository.findById(productId).orElse(null);

		if (product == null) {
			throw new RuntimeException("Product not found");
		}
		
		// Check if product is available
		if (!product.getAvailable()) {
			throw new RuntimeException("Product is not available");
		}
		
		// Check if product has enough stock
		if (product.getAmount() < quantity) {
			throw new RuntimeException("Insufficient stock");
		}
		
		// Check if quantity is valid
		if (quantity <= 0) {
			throw new RuntimeException("Invalid quantity");
		}

        // Check if product already in cart
        for (OrderDetail item : order.getOrderDetails()) {
            if (item.getProduct().getId().equals(productId)) {
            	int newQuantity = item.getQuantity() + quantity;
            	// Check if total quantity exceeds stock
            	if (product.getAmount() < newQuantity) {
            		throw new RuntimeException("Insufficient stock");
            	}
                item.setQuantity(newQuantity);
                return order;
            }
        }
        
        // New cart
        Order newOrder = new Order();
        
        // New item
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setProduct(product);
        orderDetail.setPrice(product.getPrice());
        orderDetail.setQuantity(quantity);
        
        //Add cart into item
        orderDetail.setOrder(newOrder);
        
        //Add item into cart
        order.getOrderDetails().add(orderDetail);
        		
		return order;
	}
	
	@Override
	public boolean validateStock(Order order) {
		for (OrderDetail od : order.getOrderDetails()) {
			Product product = productRepository.findById(od.getProduct().getId()).orElse(null);
			if (product.getAmount() < od.getQuantity()) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public Page<Order> searchOrders(String keyword, String status, LocalDate fromDate, LocalDate toDate, Pageable pageable) {
		return orderRepository.searchOrders(keyword, status, fromDate, toDate, pageable);
	}
	
	@Override
	@Transactional
	public void updateStatus(Long orderId, String status) {
		Order order = orderRepository.findById(orderId)
			.orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
		order.setStatus(status);
		orderRepository.save(order);
	}
	
	@Override
	public Double calculateOrderTotal(Order order) {
		if (order == null || order.getOrderDetails() == null) {
			return 0.0;
		}
		return order.getOrderDetails().stream()
			.mapToDouble(od -> od.getPrice() * od.getQuantity())
			.sum();
	}

	@Override
	public List<Order> findAllDescById() {
		// TODO Auto-generated method stub
	    return orderRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
	}
}
