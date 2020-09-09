package hu.progmasters.gmistore.service;

import hu.progmasters.gmistore.dto.MainProductCategoryDetails;
import hu.progmasters.gmistore.dto.PaymentMethodDetails;
import hu.progmasters.gmistore.enums.DomainType;
import hu.progmasters.gmistore.model.LookupEntity;
import hu.progmasters.gmistore.repository.LookupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LookupService {

    private final LookupRepository lookupRepository;

    @Autowired
    public LookupService(LookupRepository lookupRepository) {
        this.lookupRepository = lookupRepository;
    }

    /**
     * Fetch a payment method by it's key
     * @param key The given key
     * @return A LookupEntity
     */
    public LookupEntity getPaymentMethodByKey(String key) {
        return lookupRepository.findByDomainTypeAndLookupKey(DomainType.PAYMENT_METHOD, key);
    }

    /**
     * Fetch all the payment methods
     * @return A List of DTOs
     */
    public List<PaymentMethodDetails> getPaymentMethods() {
        List<LookupEntity> paymentMethods = lookupRepository.findByDomainType(DomainType.PAYMENT_METHOD);
        return paymentMethods.stream()
                .map(paymentMethod -> new PaymentMethodDetails(
                        paymentMethod.getId(), paymentMethod.getLookupKey(), paymentMethod.getDisplayName()))
                .collect(Collectors.toList());
    }

    /**
     * Fetch an order status by it's key
     * @param key The given key
     * @return A LookupEntity
     */
    public LookupEntity getOrderStatusByKey(String key) {
        return lookupRepository.findByDomainTypeAndLookupKey(DomainType.ORDER_STATUS, key);
    }

    /**
     * Fetch all active main product categories
     * @return A list of MainProductCategoryDetails
     */
    public List<MainProductCategoryDetails> getMainProductCategories() {
        return lookupRepository.findByDomainTypeAndParentIsNull(DomainType.PRODUCT_CATEGORY)
                .stream()
                .filter(LookupEntity::isDisplayFlag)
                .map(MainProductCategoryDetails::new)
                .collect(Collectors.toList());
    }
}
