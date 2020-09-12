package hu.progmasters.gmistore.service;

import hu.progmasters.gmistore.dto.MainCategoryDetails;
import hu.progmasters.gmistore.dto.PaymentMethodDetails;
import hu.progmasters.gmistore.dto.ProductCategoryDetails;
import hu.progmasters.gmistore.enums.DomainType;
import hu.progmasters.gmistore.model.LookupEntity;
import hu.progmasters.gmistore.repository.LookupRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class LookupService {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(LookupService.class);

    private final LookupRepository lookupRepository;

    @Autowired
    public LookupService(LookupRepository lookupRepository) {
        this.lookupRepository = lookupRepository;
    }

    /**
     * Fetch a payment method by it's key
     *
     * @param key The given key
     * @return A LookupEntity
     */
    public LookupEntity getPaymentMethodByKey(String key) {
        return lookupRepository.findByDomainTypeAndLookupKey(DomainType.PAYMENT_METHOD, key);
    }

    /**
     * Fetch all the payment methods
     *
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
     *
     * @param key The given key
     * @return A LookupEntity
     */
    public LookupEntity getOrderStatusByKey(String key) {
        return lookupRepository.findByDomainTypeAndLookupKey(DomainType.ORDER_STATUS, key);
    }

    /**
     * Fetch all active main product categories
     *
     * @return A list of MainProductCategoryDetails
     */
    public List<ProductCategoryDetails> getMainProductCategories() {
        return lookupRepository.findByDomainTypeAndParentIsNull(DomainType.PRODUCT_CATEGORY)
                .stream()
                .filter(LookupEntity::isDisplayFlag)
                .map(ProductCategoryDetails::new)
                .collect(Collectors.toList());
    }

    /**
     * Fetch all active sub categories for the given main category
     *
     * @param id The given main category's unique ID
     * @return A list of subcategories
     */
    public List<ProductCategoryDetails> getSubProductCategories(Long id) {
        Optional<LookupEntity> categoryById = lookupRepository.findById(id);
        return categoryById.map(
                lookupEntity -> lookupRepository.findByDomainTypeAndParent(DomainType.PRODUCT_CATEGORY, lookupEntity)
                        .stream()
                        .filter(LookupEntity::isDisplayFlag)
                        .map(ProductCategoryDetails::new)
                        .collect(Collectors.toList())).orElse(null);
    }

    /**
     * Fetch category by the given lookup key
     *
     * @param key The given lookup key
     * @return A LookupEntity
     */
    public LookupEntity getCategoryByKey(String key) {
        return lookupRepository.findByDomainTypeAndLookupKey(DomainType.PRODUCT_CATEGORY, key);
    }

    /**
     * Fetch all main and subcategories
     *
     * @return A List of MainCategoryDetails DTO
     */
    public List<MainCategoryDetails> getCategories() {
        return lookupRepository.findByDomainTypeAndParentIsNull(DomainType.PRODUCT_CATEGORY)
                .stream()
                .filter(LookupEntity::isDisplayFlag)
                .map(category -> {
                    List<ProductCategoryDetails> subCategories =
                            lookupRepository.findByDomainTypeAndParent(DomainType.PRODUCT_CATEGORY, category)
                                    .stream()
                                    .filter(LookupEntity::isDisplayFlag)
                                    .map(ProductCategoryDetails::new)
                                    .collect(Collectors.toList());
                    return new MainCategoryDetails(category, subCategories);
                })
                .collect(Collectors.toList());
    }
}
