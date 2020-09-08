package hu.progmasters.gmistore.service;

import hu.progmasters.gmistore.dto.ShippingMethodItem;
import hu.progmasters.gmistore.model.ShippingMethod;
import hu.progmasters.gmistore.repository.ShippingMethodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ShippingService {

    public static final int ORDER_DEADLINE = 14;

    private final ShippingMethodRepository shippingMethodRepository;

    @Autowired
    public ShippingService(ShippingMethodRepository shippingMethodRepository) {
        this.shippingMethodRepository = shippingMethodRepository;
    }

    /**
     * Fetch all the ShippingMethods
     * @return A list of ShippingMethod DTOs
     */
    public List<ShippingMethodItem> getShippingData() {
        List<ShippingMethod> shippingMethods = shippingMethodRepository.findAll();
        return shippingMethods.stream().map(ShippingMethodItem::new).collect(Collectors.toList());
    }

    /**
     * Fetch an initial ShippingMethod, used for newly created Carts
     * @return A ShippingMethod object
     */
    public ShippingMethod getInitialShippingMethod() {
        Optional<ShippingMethod> first = shippingMethodRepository.findAll().stream().findFirst();
        return first.orElse(null);
    }

    /**
     * Attempts to fetch a ShippingMethod by it's name as a String
     * @param method The given method's name, as String
     * @return If found a ShippingMethod object, null otherwise
     */
    public ShippingMethod fetchShippingMethod(String method) {
        Optional<ShippingMethod> byMethod = shippingMethodRepository.findByMethod(method);
        return byMethod.orElse(null);
    }

    /**
     * Calculates an expected delivery date
     * @param shippingMethod A ShippingMethod object
     * @return A LocalDateTime object with the expected date
     */
    public LocalDateTime calculateExpectedShippingDate(ShippingMethod shippingMethod) {
        LocalDateTime expectedDate = checkOrderDeadline();
        int addedDays = 0;
        while (addedDays < shippingMethod.getDays()) {
            expectedDate = expectedDate.plusDays(1);
            DayOfWeek dayOfWeek = expectedDate.getDayOfWeek();
            if (!(dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY)) {
                addedDays++;
            }
        }
        return expectedDate;
    }

    private LocalDateTime checkOrderDeadline() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        ZoneId zoneId = ZoneId.of("Europe/Budapest");
        if (ZonedDateTime.of(currentDateTime, zoneId).getHour() >= ORDER_DEADLINE) {
            currentDateTime = currentDateTime.plusDays(1);
        }
        return currentDateTime;
    }
}
