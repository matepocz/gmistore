package hu.progmasters.gmistore.service;

import hu.progmasters.gmistore.dto.OrderDto;
import hu.progmasters.gmistore.enums.EnglishAlphabet;
import hu.progmasters.gmistore.enums.OrderStatus;
import hu.progmasters.gmistore.model.Order;
import hu.progmasters.gmistore.model.Product;
import hu.progmasters.gmistore.model.User;
import hu.progmasters.gmistore.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    private String generateOrderUniqueId() {
        EnglishAlphabet[] alphabet = EnglishAlphabet.values();
        StringBuilder generatedId = new StringBuilder();
        generatedId.append("GMI-");
        for (int i = 0; i < 5; i++) {
            generatedId.append(alphabet[generateRandomNumberForLetters()]);
        }
        generatedId.append("-");
        generatedId.append(generateFiveDigitNumber());
        Optional<Order> order = orderRepository.findOrdersByGeneratedUniqueId(generatedId.toString());
        if (order.isPresent()) {
            return generatedId.toString();
        } else {
            return generateOrderUniqueId();
        }
    }

    private int generateRandomNumberForLetters() {
        Random random = new Random();
        return random.nextInt(EnglishAlphabet.values().length);
    }

    private int generateFiveDigitNumber() {
        return ThreadLocalRandom.current().nextInt(100000, 1000000);
    }

    public List<OrderDto> getAllOrders() {
        List<Order> allOrders = orderRepository.findAll();
        return allOrders.stream().map(order -> mapOrderToOrderDto(order)).collect(Collectors.toList());
    }

    public OrderDto getOrderByGeneratedUniqueId(String generatedUniqueId) {
        Optional<Order> optionalOrder = orderRepository.findOrdersByGeneratedUniqueId(generatedUniqueId);
        if (optionalOrder.isPresent()) {
            Order orderByGeneratedUniqueId = optionalOrder.get();
            return mapOrderToOrderDto(orderByGeneratedUniqueId);
        } else {
            return null;
        }
    }

    public void registerOrder(OrderDto orderDto) {
        Order order = mapOrderDtoToOrder(orderDto);
        orderRepository.save(order);
    }

    public void deleteOrderById(Long id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isPresent()) {
            orderRepository.deleteById(id);
        }
    }

    public OrderDto setOrderDeliveryDateById(Long id, LocalDateTime deliveryDate, OrderDto orderDto) {
        OrderDto updatedOrderDto = null;
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            updateOrderValues(orderDto, order);
            Order updatedOrder = orderRepository.save(order);
            updatedOrderDto = mapOrderToOrderDto(updatedOrder);
        }
        return updatedOrderDto;
    }

    private void updateOrderValues(OrderDto orderDto, Order order) {
        order.setGeneratedUniqueId(orderDto.getGeneratedUniqueId());
        order.setStatus(OrderStatus.valueOf(orderDto.getStatus().toUpperCase()));
        order.setQuantity(orderDto.getQuantity());
        order.setDate(orderDto.getDate());
        order.setDeliveryDate(orderDto.getDeliveryDate());
        order.setProductList(orderDto.getProductList());
        order.setUser(orderDto.getUser());
    }

    private OrderDto mapOrderToOrderDto(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setGeneratedUniqueId(order.getGeneratedUniqueId());
        orderDto.setStatus(order.getStatus().getDisplayName());
        orderDto.setQuantity(order.getQuantity());
        orderDto.setDate(order.getDate());
        orderDto.setDeliveryDate(order.getDeliveryDate());
        orderDto.setProductList(order.getProductList());
        orderDto.setUser(order.getUser());
        return orderDto;
    }

    private Order mapOrderDtoToOrder(OrderDto orderDto) {
        Order order = new Order();
        order.setId(orderDto.getId());
        order.setGeneratedUniqueId(orderDto.getGeneratedUniqueId());
        order.setStatus(OrderStatus.valueOf(orderDto.getStatus().toUpperCase()));
        order.setQuantity(orderDto.getQuantity());
        order.setDate(orderDto.getDate());
        order.setDeliveryDate(orderDto.getDeliveryDate());
        order.setProductList(orderDto.getProductList());
        order.setUser(orderDto.getUser());
        return order;
    }
}
