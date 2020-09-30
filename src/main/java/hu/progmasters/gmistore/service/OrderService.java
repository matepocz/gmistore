package hu.progmasters.gmistore.service;

import hu.progmasters.gmistore.dto.AddressDetails;
import hu.progmasters.gmistore.dto.CustomerDetails;
import hu.progmasters.gmistore.dto.order.*;
import hu.progmasters.gmistore.dto.product.ProductListDetailDto;
import hu.progmasters.gmistore.enums.EnglishAlphabet;
import hu.progmasters.gmistore.enums.OrderStatus;
import hu.progmasters.gmistore.model.*;
import hu.progmasters.gmistore.repository.OrderRepository;
import hu.progmasters.gmistore.repository.OrderStatusHistoryRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
@Transactional
public class OrderService {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final UserService userService;
    private final CartService cartService;
    private final LookupService lookupService;
    private final InventoryService inventoryService;
    private final EmailSenderService emailSenderService;
    private final OrderStatusHistoryRepository orderStatusHistoryRepository;
    private final DateService dateService;

    @Autowired
    public OrderService(OrderRepository orderRepository, UserService userService, CartService cartService,
                        LookupService lookupService, InventoryService inventoryService, DateService dateService,
                        EmailSenderService emailSenderService, OrderStatusHistoryRepository orderStatusHistoryRepository) {
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.cartService = cartService;
        this.dateService = dateService;
        this.lookupService = lookupService;
        this.inventoryService = inventoryService;
        this.emailSenderService = emailSenderService;
        this.orderStatusHistoryRepository = orderStatusHistoryRepository;

    }

    /**
     * Attempts to fetch a registered user's details
     *
     * @return A CustomerDetails DTO
     */
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

    /**
     * Attempts to create a new Order
     *
     * @param orderRequest A DTO containing the required details
     * @param session      The actual session object
     * @return A boolean, true if successful, false otherwise
     */
    public boolean createOrder(OrderRequest orderRequest, HttpSession session) {
        Cart actualCart = cartService.getActualCart(session);
        if (actualCart.getItems().isEmpty()) {
            return false;
        }
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User userByUsername = userService.getUserByUsername(username);

        userByUsername.setPhoneNumber(orderRequest.getPhoneNumber());
        updateCustomerAddresses(orderRequest, userByUsername);
        Order order = setOrderDetails(actualCart, userByUsername);
        order.setPaymentMethod(lookupService.getPaymentMethodByKey(orderRequest.getPaymentMethod()));
        OrderStatusHistory newOrderStatus = new OrderStatusHistory(OrderStatus.ORDERED);
        order.getOrderStatusList().add(newOrderStatus);
        order.setStatus(newOrderStatus.getStatus());
        setPaymentStatusForOrder(order);
        saveOrderItems(actualCart, order);
        orderRepository.save(order);
        inventoryService.updateAvailableAndSoldQuantities(actualCart.getItems());
        cartService.deleteCart(actualCart.getId());
        buildAndSendOrderConfirmationEmail(order);
        LOGGER.info("New Order, unique id: {}, username: {}", order.getUniqueId(), userByUsername.getUsername());
        return true;
    }

    private void setPaymentStatusForOrder(Order order) {
        if (order.getPaymentMethod().equals(lookupService.getPaymentMethodByKey("CASH"))) {
            order.getOrderStatusList().add(new OrderStatusHistory(OrderStatus.WAITING_PAYMENT));
        } else if (order.getPaymentMethod().equals(lookupService.getPaymentMethodByKey("BANK_CARD"))) {
            order.getOrderStatusList().add(new OrderStatusHistory(OrderStatus.PAYMENT_SUCCESS));
        }
    }

    private void updateCustomerAddresses(OrderRequest orderRequest, User user) {
        Address shippingAddress = mapAddressDetailsToAddress(orderRequest.getShippingAddress());
        if (user.getShippingAddress() != null) {
            Address currentShippingAddress = user.getShippingAddress();
            updateAddress(currentShippingAddress, shippingAddress);
        } else {
            user.setShippingAddress(shippingAddress);
        }

        Address billingAddress = mapAddressDetailsToAddress(orderRequest.getBillingAddress());
        if (user.getBillingAddress() != null) {
            Address currentBillingAddress = user.getBillingAddress();
            updateAddress(currentBillingAddress, billingAddress);
        } else {
            user.setBillingAddress(billingAddress);
        }
    }

    private void updateAddress(Address currentAddress, Address newAddress) {
        currentAddress.setCity(newAddress.getCity());
        currentAddress.setStreet(newAddress.getStreet());
        currentAddress.setNumber(newAddress.getNumber());
        if (newAddress.getDoor() != null) {
            currentAddress.setDoor(newAddress.getDoor());
        }
        if (newAddress.getFloor() != null) {
            currentAddress.setFloor(newAddress.getFloor());
        }
        currentAddress.setCountry(newAddress.getCountry());
        currentAddress.setPostcode(newAddress.getPostcode());
    }

    private Address mapAddressDetailsToAddress(AddressDetails addressDetails) {
        Address address = new Address();
        address.setCity(addressDetails.getCity());
        address.setStreet(addressDetails.getStreet());
        address.setNumber(addressDetails.getNumber());
        if (addressDetails.getDoor() != null) {
            address.setDoor(addressDetails.getDoor());
        }
        if (addressDetails.getFloor() != null) {
            address.setFloor(addressDetails.getFloor());
        }
        address.setCountry(addressDetails.getCountry());
        address.setPostcode(addressDetails.getPostcode());
        return address;
    }

    private Order setOrderDetails(Cart actualCart, User userByUsername) {
        Order order = new Order();
        order.setDeliveryCost(actualCart.getShippingMethod().getCost());
        order.setExpectedDeliveryDate(actualCart.getExpectedDeliveryDate());
        order.setOrderedAt(LocalDateTime.now());
        order.setItemsTotalPrice(actualCart.getItemsTotalPrice());
        order.setTotalPrice(actualCart.getTotalPrice());
        order.setShippingMethod(actualCart.getShippingMethod());
        order.setDeliveryAddress(setOrderAddress(userByUsername.getShippingAddress()));
        order.setInvoiceAddress(setOrderAddress(userByUsername.getBillingAddress()));
        order.setUser(userByUsername);
        order.setUniqueId(generateUniqueId());
        return order;
    }

    private Address setOrderAddress(Address address) {
        Address orderAddress = new Address();
        orderAddress.setCity(address.getCity());
        orderAddress.setStreet(address.getStreet());
        orderAddress.setNumber(address.getNumber());
        orderAddress.setFloor(address.getFloor());
        orderAddress.setDoor(address.getDoor());
        orderAddress.setPostcode(address.getPostcode());
        orderAddress.setCountry(address.getCountry());
        return orderAddress;
    }

    private void buildAndSendOrderConfirmationEmail(Order order) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(order.getUser().getEmail());
        mailMessage.setSubject("Rendelés részletező (" + order.getUniqueId() + ")");
        mailMessage.setFrom("gmistarter@gmail.com");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Köszönjük a rendelést!" + "\n\n");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String orderedAt = order.getOrderedAt().format(formatter);
        String expectedDelivery = order.getExpectedDeliveryDate().format(formatter);
        expectedDelivery = expectedDelivery.substring(0, expectedDelivery.indexOf(' '));
        stringBuilder.append("Rendelés rögzítésének ideje: ").append(orderedAt).append("\n");
        stringBuilder.append("Várható szállítás dátuma: ").append(expectedDelivery).append("\n\n");
        stringBuilder.append("Rendelés részletei: \n");

        order.getItems().forEach(item -> stringBuilder
                .append(item.getQuantity())
                .append(" x ")
                .append(item.getProduct().getName())
                .append("  ")
                .append(item.getPrice())
                .append(" Ft /db \n"));

        stringBuilder.append("\n");
        stringBuilder.append("Szállítási cim: \n");
        stringBuilder.append(order.getDeliveryAddress().toString()).append("\n\n");
        stringBuilder.append("Számlázási cím: \n");
        stringBuilder.append(order.getInvoiceAddress().toString()).append("\n");
        stringBuilder.append("\nTermékek összege: ").append(order.getItemsTotalPrice().intValue()).append(" Ft \n");
        stringBuilder.append("Szállítás költsége: ").append(order.getDeliveryCost().intValue()).append(" Ft \n");
        stringBuilder.append("Fizetendő végösszeg: ").append(order.getTotalPrice().intValue()).append(" Ft \n \n");

        stringBuilder.append("GMI Store team");

        mailMessage.setText(stringBuilder.toString());
        emailSenderService.sendEmail(mailMessage);
    }

    private void saveOrderItems(Cart actualCart, Order order) {
        Set<OrderItem> orderItems = new HashSet<>();
        for (CartItem item : actualCart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(item.getProduct());
//          orderItem.setProduct(addItemProduct(item.getProduct()));
            orderItem.setQuantity(item.getCount());
            orderItem.setPrice(item.getProduct().getPrice());
            orderItems.add(orderItem);
        }
        order.setItems(orderItems);
    }

    private Product addItemProduct(Product product) {
        Product itemProduct = new Product();
        itemProduct.setName(product.getName());
        itemProduct.setProductCode(product.getProductCode());
        itemProduct.setFeatures(product.getFeatures());
        itemProduct.setPictureUrl(product.getPictureUrl());
        itemProduct.setPictures(product.getPictures());
        itemProduct.setPrice(product.getPrice());
        itemProduct.setDiscount(product.getDiscount());
        itemProduct.setAverageRating(product.getAverageRating());
        return itemProduct;
    }

    private String generateUniqueId() {
        EnglishAlphabet[] alphabet = EnglishAlphabet.values();
        String generatedId = IntStream.range(0, 6)
                .mapToObj(i -> String.valueOf(alphabet[generateRandomNumberForLetters()]))
                .collect(Collectors.joining("", "GMI", String.valueOf(generateRandomDigits())));
        Optional<Order> order = orderRepository.findOrderByUniqueId(generatedId);
        return order.isPresent() ? generateUniqueId() : generatedId;
    }

    public List<OrderListDto> getAllOrders() {
        return orderRepository.findAllByOrderListDetails();
    }

    public Set<ProductListDetailDto> getAllProductsOrderedByUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Set<OrderItem> allOrderedProductsByUser = orderRepository.findAllOrderedProductsByUser(authentication.getName());

        if (allOrderedProductsByUser != null) {
            LOGGER.debug("User order details found! username: {}", authentication.getName());
            return allOrderedProductsByUser.stream()
                    .map(this::mapOrderItemToProduct)
                    .collect(Collectors.toSet());
        }
        LOGGER.info("User order details not found! username: {}", authentication.getName());
        return null;
    }

    private ProductListDetailDto mapOrderItemToProduct(OrderItem orderItem) {
        ProductListDetailDto product = new ProductListDetailDto();
        product.setId(orderItem.getId());
        product.setName(orderItem.getProduct().getName());
        product.setProductCode(orderItem.getProduct().getProductCode());
        product.setFeatures(orderItem.getProduct().getFeatures());
        product.setPictureUrl(orderItem.getProduct().getPictureUrl());
        product.setPictures(orderItem.getProduct().getPictures());
        product.setPrice(orderItem.getProduct().getPrice());
        product.setDiscount(orderItem.getProduct().getDiscount());
        product.setAverageRating(orderItem.getProduct().getAverageRating());
        product.setOrderItemId(orderRepository.findOrderIdByItemId(orderItem.getId()));
        return product;
    }

    public Stream<String> getStatusOptions() {
        return lookupService.getAllStatusOptions().stream().map(LookupEntity::getDisplayName);
    }

    public Set<OrderStatusOptionsDto> getOrderStatusEnumOptions() {
        return Arrays
                .stream(OrderStatus.values())
                .map(status -> new OrderStatusOptionsDto(status.toString(), status.getDisplayName()))
                .collect(Collectors.toSet());
    }

    private int generateRandomNumberForLetters() {
        return new Random().nextInt(EnglishAlphabet.values().length);
    }

    private int generateRandomDigits() {
        return ThreadLocalRandom.current().nextInt(100000, 1000000);
    }

    public OrderDto getOrderDetailsByUniqueId(String id) {
        Order order = orderRepository.findOrderByUniqueId(id)
                .orElseThrow(() ->
                        new NoSuchElementException("No order found with id  + id "));
        return new OrderDto(order);
    }

    public void updateDeliveryAddress(String id, AddressDetails addressDetails) {
        Optional<Order> orderByUniqueId = orderRepository.findOrderByUniqueId(id);
        if (orderByUniqueId.isPresent()) {
            Address deliveryAddress = orderByUniqueId.get().getDeliveryAddress();
            updateOrderAddress(addressDetails, deliveryAddress);
        }
    }

    public void updateInvoiceAddress(String id, AddressDetails addressDetails) {
        Optional<Order> orderByUniqueId = orderRepository.findOrderByUniqueId(id);
        if (orderByUniqueId.isPresent()) {
            Address invoiceAddress = orderByUniqueId.get().getInvoiceAddress();
            updateOrderAddress(addressDetails, invoiceAddress);
        }
    }

    private void updateOrderAddress(AddressDetails addressDetails, Address address) {
        address.setCity(addressDetails.getCity());
        address.setCountry(addressDetails.getCountry());
        address.setDoor(addressDetails.getDoor());
        address.setFloor(addressDetails.getFloor());
        address.setNumber(addressDetails.getNumber());
        address.setPostcode(addressDetails.getPostcode());
        address.setStreet(addressDetails.getStreet());
    }

    public void updateStatus(String id, String status) {
        Optional<Order> orderByUniqueId = orderRepository.findOrderByUniqueId(id);
        if (orderByUniqueId.isPresent()) {
            Order order = orderByUniqueId.get();
            List<OrderStatusHistory> orderStatusList = order.getOrderStatusList();
            OrderStatusHistory orderStatusHistory = new OrderStatusHistory(OrderStatus.valueOf(status));
            orderStatusHistoryRepository.save(orderStatusHistory);
            orderStatusList.add(orderStatusHistory);
            order.setStatus(OrderStatus.valueOf(status));
        }
    }

    public IncomeByDaysDto getIncomePerOrder(String criteria) {
        DateService.CreateDates dates = dateService.stringToDate(criteria);
        List<IncomePerOrderDto> allByOrderedAt = orderRepository.findAllByOrderedAt(dates.getStart(), dates.getEnd());

        Map<String, Double> orderDates = new TreeMap<>();
        for (IncomePerOrderDto orderRegistration : allByOrderedAt) {
            int year = orderRegistration.getDate().getYear();
            int month = orderRegistration.getDate().getMonthValue();
            int dayOfMonth = orderRegistration.getDate().getDayOfMonth();
            String date = year + "." + month + "." + dayOfMonth;

            orderDates.merge(date, orderRegistration.getIncome(), Double::sum);
        }

        Set<String> strings = orderDates.keySet();
        Collection<Double> values = orderDates.values();

        return new IncomeByDaysDto(values,strings);

    }

}
