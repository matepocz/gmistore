package hu.progmasters.gmistore.service;

import hu.progmasters.gmistore.dto.ShippingMethodItem;
import hu.progmasters.gmistore.model.ShippingMethod;
import hu.progmasters.gmistore.repository.ShippingMethodRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShippingServiceTest {

    private ShippingService shippingService;

    @Mock
    private ShippingMethodRepository shippingMethodRepositoryMock;

    @Spy
    private List<ShippingMethod> shippingMethodListMock = new ArrayList<>();

    private final Supplier<ShippingMethod> shippingMethodSupplier = () -> {
        ShippingMethod shippingMethod = new ShippingMethod();
        shippingMethod.setCost(50);
        shippingMethod.setDays(2);
        shippingMethod.setMethod("Test 1");
        return shippingMethod;
    };

    @BeforeEach
    public void init() {
        this.shippingService = new ShippingService(shippingMethodRepositoryMock);
        shippingMethodListMock = new ArrayList<>();
    }

    @Test
    public void testGetShippingDataReturnsTwo() {
        ShippingMethod shippingMethod1 = shippingMethodSupplier.get();
        shippingMethod1.setId(1);
        ShippingMethod shippingMethod2 = shippingMethodSupplier.get();
        shippingMethod2.setId(2);

        shippingMethodListMock.add(shippingMethod1);
        shippingMethodListMock.add(shippingMethod2);

        when(shippingMethodRepositoryMock.findAll()).thenReturn(shippingMethodListMock);

        List<ShippingMethodItem> shippingData = shippingService.getShippingData();

        assertEquals(2, shippingData.size());
        verify(shippingMethodRepositoryMock, times(1)).findAll();
    }

    @Test
    public void testGetShippingDataReturnsEmptyList() {
        when(shippingMethodRepositoryMock.findAll()).thenReturn(shippingMethodListMock);

        List<ShippingMethodItem> shippingData = shippingService.getShippingData();

        assertEquals(0, shippingData.size());
        verify(shippingMethodRepositoryMock, times(1)).findAll();
    }

    @Test
    public void testGetInitialShippingMethodShouldReturnNull() {
        when(shippingMethodRepositoryMock.findAll()).thenReturn(shippingMethodListMock);

        ShippingMethod initialShippingMethod = shippingService.getInitialShippingMethod();
        assertNull(initialShippingMethod);
        verify(shippingMethodRepositoryMock, times(1)).findAll();
    }

    @Test
    public void testGetInitialShippingMethodReturnsTestOne() {
        shippingMethodListMock.add(shippingMethodSupplier.get());
        when(shippingMethodRepositoryMock.findAll()).thenReturn(shippingMethodListMock);

        ShippingMethod initialShippingMethod = shippingService.getInitialShippingMethod();
        assertNotNull(initialShippingMethod);
        verify(shippingMethodRepositoryMock, times(1)).findAll();
    }

    @Test
    public void testFetchShippingMethodShouldReturnTestOne() {
        ShippingMethod shippingMethod = shippingMethodSupplier.get();

        when(shippingMethodRepositoryMock.findByMethod("Test 1")).thenReturn(Optional.of(shippingMethod));

        ShippingMethod foundShippingMethod = shippingService.fetchShippingMethod("Test 1");

        assertNotNull(foundShippingMethod);
        assertEquals("Test 1", foundShippingMethod.getMethod());
        assertEquals(2, foundShippingMethod.getDays());
        verify(shippingMethodRepositoryMock, times(1)).findByMethod("Test 1");
    }

    @Test
    public void testFetchShippingMethodShouldReturnEmptyOptional() {
        when(shippingMethodRepositoryMock.findByMethod(any())).thenReturn(Optional.empty());

        ShippingMethod shippingMethod = shippingService.fetchShippingMethod("Test 1");

        assertNull(shippingMethod);
        verify(shippingMethodRepositoryMock, times(1)).findByMethod("Test 1");
    }

    @Test
    public void testCalculateExpectedShippingDate() {
        ShippingMethod shippingMethod = shippingMethodSupplier.get();

        LocalDateTime expectedDate = LocalDateTime.now();
        ZoneId zoneId = ZoneId.of("Europe/Budapest");
        if (ZonedDateTime.of(expectedDate, zoneId).getHour() >= 14) {
            expectedDate = expectedDate.plusDays(1);
        }
        expectedDate = expectedDate.plusDays(shippingMethod.getDays());
        if (expectedDate.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
            expectedDate = expectedDate.plusDays(2);
        }
        if (expectedDate.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
            expectedDate = expectedDate.plusDays(1);
        }
        LocalDateTime actualResult = shippingService.calculateExpectedShippingDate(shippingMethod);

        assertNotNull(actualResult);
        assertEquals(expectedDate.getDayOfWeek(), actualResult.getDayOfWeek());
    }
}
