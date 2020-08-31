package hu.progmasters.gmistore.service;

import hu.progmasters.gmistore.dto.AddressDetails;
import hu.progmasters.gmistore.dto.CustomerDetails;
import hu.progmasters.gmistore.enums.EnglishAlphabet;
import hu.progmasters.gmistore.model.*;
import hu.progmasters.gmistore.repository.OrderRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Transactional
public class OrderService {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final UserService userService;
    private final CartService cartService;
    private final LookupService lookupService;

    @Autowired
    public OrderService(OrderRepository orderRepository, UserService userService, CartService cartService,
                        LookupService lookupService) {
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.cartService = cartService;
        this.lookupService = lookupService;
    }

    public CustomerDetails getCustomerDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByUsername(authentication.getName());
        if (user != null) {
            LOGGER.debug("Customer details found! username: {}", authentication.getName());
            return setValuesForCustomerDetails(user);
        }
        LOGGER.info("Customer details not found! username: {}", authentication.getName());
        return null;
    }

    private CustomerDetails setValuesForCustomerDetails(User user) {
        CustomerDetails customerDetails = new CustomerDetails();
        if (user.getShippingAddress() != null) {
            AddressDetails shippingAddress = new AddressDetails(user.getShippingAddress());
            customerDetails.setShippingAddress(shippingAddress);
        } else {
            customerDetails.setShippingAddress(null);
        }

        if (user.getBillingAddress() != null) {
            AddressDetails billingAddress = new AddressDetails(user.getBillingAddress());
            customerDetails.setBillingAddress(billingAddress);
        } else {
            customerDetails.setBillingAddress(null);
        }

        if (user.getPhoneNumber() != null) {
            customerDetails.setPhoneNumber(user.getPhoneNumber());
        } else {
            customerDetails.setPhoneNumber(null);
        }

        customerDetails.setFirstName(user.getFirstName());
        customerDetails.setLastName(user.getLastName());
        customerDetails.setEmail(user.getEmail());
        return customerDetails;
    }

    public boolean createOrder(CustomerDetails customerDetails, HttpSession session) {
        Cart actualCart = cartService.getActualCart(session);
        if (actualCart.getItems().isEmpty()) {
            return false;
        }
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User userByUsername = userService.getUserByUsername(username);

        userByUsername.setPhoneNumber(customerDetails.getPhoneNumber());
        updateCustomerAddresses(customerDetails, userByUsername);
        Order order = setOrderDetails(actualCart, userByUsername);
        saveOrderItems(actualCart, order);
        orderRepository.save(order);
        cartService.deleteCart(actualCart.getId());
        LOGGER.info("New Order, unique id: {}, username: {}", order.getUniqueId(), userByUsername.getUsername());
        return true;
    }

    private void updateCustomerAddresses(CustomerDetails customerDetails, User user) {
        Address shippingAddress = mapAddressDetailsToAddress(customerDetails.getShippingAddress());
        user.setShippingAddress(shippingAddress);
        Address billingAddress = mapAddressDetailsToAddress(customerDetails.getBillingAddress());
        user.setBillingAddress(billingAddress);
        //TODO creates new instances?
    }

    private Address mapAddressDetailsToAddress(AddressDetails addressDetails) {
        Address address = new Address();
        address.setCity(addressDetails.getCity());
        address.setStreet(addressDetails.getStreet());
        address.setNumber(addressDetails.getNumber());
        address.setDoor(addressDetails.getDoor());
        address.setFloor(addressDetails.getFloor());
        address.setCountry(addressDetails.getCountry());
        address.setPostcode(addressDetails.getPostcode());
        return address;
    }

    private Order setOrderDetails(Cart actualCart, User userByUsername) {
        Order order = new Order();
        order.setDeliveryCost(actualCart.getShippingMethod().getCost());
        order.setExpectedDeliveryDate(actualCart.getExpectedDeliveryDate());
        order.setOrderedAt(LocalDateTime.now());
        order.setTotalPrice(actualCart.getTotalPrice());
        order.setUser(userByUsername);
        order.setUniqueId(generateUniqueId());

        order.setPaymentMethod(lookupService.getPaymentMethodByKey("BANK_CARD"));
        //TODO continue the work on Payment methods
        return order;
    }

    private void saveOrderItems(Cart actualCart, Order order) {
        Set<OrderItem> orderItems = new HashSet<>();
        for (CartItem item : actualCart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getCount());
            orderItem.setPrice(item.getProduct().getPrice());
            orderItems.add(orderItem);
        }
        order.setItems(orderItems);
    }

    private String generateUniqueId() {
        EnglishAlphabet[] alphabet = EnglishAlphabet.values();
        String generatedId = IntStream.range(0, 6)
                .mapToObj(i -> String.valueOf(alphabet[generateRandomNumberForLetters()]))
                .collect(Collectors.joining("", "GMI", String.valueOf(generateRandomDigits())));
        Optional<Order> order = orderRepository.findOrderByUniqueId(generatedId);
        return order.isPresent() ? generateUniqueId() : generatedId;
        //TODO change the pattern of this unique ID?
    }

    private int generateRandomNumberForLetters() {
        return new Random().nextInt(EnglishAlphabet.values().length);
    }

    private int generateRandomDigits() {
        return ThreadLocalRandom.current().nextInt(100000, 1000000);
    }
}
