package com.cg.oam.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.oam.entity.Customer;
import com.cg.oam.entity.Order;
import com.cg.oam.exception.MedicineNotFoundException;
import com.cg.oam.exception.OrderNotFoundException;
import com.cg.oam.model.OrderModel;
import com.cg.oam.repository.CustomerRepository;

import com.cg.oam.repository.OrderRepository;
@Service
public class OrderServiceImpl implements IOrderService{
	@Autowired
	private OrderRepository ordRepo;
	@Autowired
	private EMParser parser;
	@Autowired
	private CustomerRepository customerRepository;
	
	public OrderServiceImpl() {
		
	}
	public OrderServiceImpl(OrderRepository repo) {
		super();
		this.ordRepo = repo;
		this.parser=new EMParser();
	}
	
	
	/**
	 * @return the parser
	 */
	public EMParser getParser() {
		return parser;
	}
	/**
	 * @param parser the parser to set
	 */
	public void setParser(EMParser parser) {
		this.parser = parser;
	}
	@Override
	public OrderModel add(OrderModel orderModel) throws OrderNotFoundException {
		if (orderModel != null) {
			if (ordRepo.existsById(orderModel.getOrderId())) {
				throw new OrderNotFoundException("Order with Id " + orderModel.getOrderId() + " is exist already");
			} 
			else {
				orderModel = parser.parse(ordRepo.save(parser.parse(orderModel)));
			}
		}
		return orderModel;
	}


	@Transactional
	@Override
	public OrderModel save(OrderModel orderModel) throws OrderNotFoundException {
			return orderModel = parser.parse(ordRepo.save(parser.parse(orderModel)));
		
	}
	
	
	@Override
	public OrderModel modify(OrderModel orderModel, Long orderId) throws OrderNotFoundException {
		if(orderModel != null) {
			if(!ordRepo.existsById(orderId)) {
				throw new OrderNotFoundException("no such id");
			}
			orderModel = parser.parse(ordRepo.save(parser.parse(orderModel)));
		}
		return orderModel;
	}

	@Override
	public void deleteById(Long orderId) {
		ordRepo.deleteById(orderId);
		
	}

	@Override
	public OrderModel findById(Long orderId) {
		return parser.parse(ordRepo.findById(orderId).orElse(null));
	}


	@Override
	public List<OrderModel> findAll() {
		//Group by Medicine
		return ordRepo.findAll().stream().map(parser::parse).collect(Collectors.toList());
	}
	
	@Override
	public List<OrderModel> findAllByCustomerId(Long customerId){
		Optional<Customer> customerOptional = customerRepository.findById(customerId);	
		List<Order> orders = customerOptional.get().getOrder();	
		return orders.stream().map(parser::parse).collect(Collectors.toList());
	}
	
	@Override
	public List<OrderModel> findAllByOrderDate(LocalDate orderDate) {
		return ordRepo.findAllByOrderDate(orderDate).stream().map(parser::parse).collect(Collectors.toList());
	}

}
