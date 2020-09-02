package hu.progmasters.gmistore.service;

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

    public LookupEntity getPaymentMethodByKey(String key) {
        return lookupRepository.findByDomainTypeAndLookupKey(DomainType.PAYMENT_METHOD, key);
    }

    public List<PaymentMethodDetails> getPaymentMethods() {
        List<LookupEntity> paymentMethods = lookupRepository.findByDomainType(DomainType.PAYMENT_METHOD);
        return paymentMethods.stream()
                .map(paymentMethod -> new PaymentMethodDetails(
                        paymentMethod.getId(), paymentMethod.getLookupKey(), paymentMethod.getDisplayName()))
                .collect(Collectors.toList());
    }

    public LookupEntity getOrderStatusByKey(String key) {
        return lookupRepository.findByDomainTypeAndLookupKey(DomainType.ORDER_STATUS, key);
    }
}
