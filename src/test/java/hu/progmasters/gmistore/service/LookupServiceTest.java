package hu.progmasters.gmistore.service;

import hu.progmasters.gmistore.dto.PaymentMethodDetails;
import hu.progmasters.gmistore.dto.product.ProductCategoryDetails;
import hu.progmasters.gmistore.enums.DomainType;
import hu.progmasters.gmistore.model.LookupEntity;
import hu.progmasters.gmistore.repository.LookupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LookupServiceTest {

    private LookupService lookupService;

    @Mock
    private LookupRepository lookupRepositoryMock;

    @Spy
    private List<LookupEntity> lookupEntityListMock;

    private final Supplier<LookupEntity> paymentMethodSupplier = () -> {
        LookupEntity paymentMethod = new LookupEntity();
        paymentMethod.setDomainType(DomainType.PAYMENT_METHOD);
        paymentMethod.setLookupKey("testKey");
        return paymentMethod;
    };

    private final Supplier<LookupEntity> mainProductCategorySupplier = () -> {
        LookupEntity mainProductCategory = new LookupEntity();
        mainProductCategory.setId(1L);
        mainProductCategory.setDomainType(DomainType.PRODUCT_CATEGORY);
        mainProductCategory.setLookupKey("main_category");
        mainProductCategory.setDisplayName("mainCategory");
        mainProductCategory.setDisplayFlag(true);
        mainProductCategory.setOrderSequence(0);
        return mainProductCategory;
    };

    @BeforeEach
    public void init() {
        lookupService = new LookupService(lookupRepositoryMock);
        lookupEntityListMock = new ArrayList<>();
    }

    @Test
    public void testGetPaymentByKeyShouldThrowEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> lookupService.getPaymentMethodByKey("Test"));
    }

    @Test
    public void testGetPaymentMethod() {
        LookupEntity paymentMethod = paymentMethodSupplier.get();

        when(lookupRepositoryMock
                .findByDomainTypeAndLookupKey(DomainType.PAYMENT_METHOD, "testKey"))
                .thenReturn(Optional.of(paymentMethod));

        LookupEntity testKey = lookupService.getPaymentMethodByKey("testKey");

        assertNotNull(testKey);
        assertEquals("testKey", testKey.getLookupKey());
        verify(lookupRepositoryMock, times(1))
                .findByDomainTypeAndLookupKey(DomainType.PAYMENT_METHOD, "testKey");
    }

    @Test
    public void testGetPaymentMethodShouldReturnTwo() {
        LookupEntity paymentMethod1 = paymentMethodSupplier.get();
        LookupEntity paymentMethod2 = paymentMethodSupplier.get();
        paymentMethod2.setLookupKey("test 2");

        lookupEntityListMock.add(paymentMethod1);
        lookupEntityListMock.add(paymentMethod2);

        when(lookupRepositoryMock.findByDomainType(DomainType.PAYMENT_METHOD)).thenReturn(lookupEntityListMock);

        List<PaymentMethodDetails> paymentMethods = lookupService.getPaymentMethods();

        assertNotNull(paymentMethods);
        assertEquals(2, paymentMethods.size());
        verify(lookupRepositoryMock, timeout(1)).findByDomainType(DomainType.PAYMENT_METHOD);
    }

    @Test
    public void testGetMainProductCategoriesShouldReturnTwo() {
        LookupEntity mainCategory1 = mainProductCategorySupplier.get();
        LookupEntity mainCategory2 = mainProductCategorySupplier.get();
        mainCategory2.setLookupKey("main_category2");
        mainCategory2.setDisplayName("mainCategory2");

        lookupEntityListMock.add(mainCategory1);
        lookupEntityListMock.add(mainCategory2);

        when(lookupRepositoryMock
                .findByDomainTypeAndParentIsNull(DomainType.PRODUCT_CATEGORY))
                .thenReturn(lookupEntityListMock);

        List<ProductCategoryDetails> mainProductCategories = lookupService.getMainProductCategories();
        assertNotNull(mainProductCategories);
        assertEquals(2, mainProductCategories.size());
        verify(lookupRepositoryMock, times(1))
                .findByDomainTypeAndParentIsNull(DomainType.PRODUCT_CATEGORY);
    }

    @Test
    public void testGetSubProductCategoriesShouldReturnTwo() {
        LookupEntity mainCategory = mainProductCategorySupplier.get();
        LookupEntity subCategory1 = mainProductCategorySupplier.get();
        subCategory1.setParent(mainCategory);
        LookupEntity subCategory2 = mainProductCategorySupplier.get();
        subCategory2.setParent(mainCategory);

        lookupEntityListMock.add(subCategory1);
        lookupEntityListMock.add(subCategory2);

        when(lookupRepositoryMock.findById(1L)).thenReturn(Optional.of(mainCategory));
        when(lookupRepositoryMock
                .findByDomainTypeAndParent(DomainType.PRODUCT_CATEGORY, mainCategory))
                .thenReturn(lookupEntityListMock);

        List<ProductCategoryDetails> subProductCategories =
                lookupService.getSubProductCategories(mainCategory.getId());

        assertNotNull(subProductCategories);
        assertEquals(2, subProductCategories.size());
        verify(lookupRepositoryMock, times(1))
                .findByDomainTypeAndParent(DomainType.PRODUCT_CATEGORY, mainCategory);
    }
}
