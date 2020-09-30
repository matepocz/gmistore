package hu.progmasters.gmistore.service;

import hu.progmasters.gmistore.dto.inventory.InventorySoldProductsDto;
import hu.progmasters.gmistore.model.CartItem;
import hu.progmasters.gmistore.model.Inventory;
import hu.progmasters.gmistore.model.Product;
import hu.progmasters.gmistore.repository.InventoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    /**
     * Creates an Inventory object for the given Product with the given product count
     *
     * @param product           The actual Product object
     * @param quantityAvailable The given product count
     */
    public void saveInventory(Product product, int quantityAvailable) {
        Inventory inventory = new Inventory(product, quantityAvailable);
        inventoryRepository.save(inventory);
    }

    /**
     * Attempts to find an Inventory object by a given Product object
     *
     * @param product The actual Product object
     * @return An Inventory object if successful, null otherwise
     */
    public Inventory findInventoryByProduct(Product product) {
        Optional<Inventory> inventoryByProduct = inventoryRepository.findByProduct(product);
        return inventoryByProduct.orElse(null);
    }

    /**
     * Updates the available and sold quantities for a List of Products
     *
     * @param items A Set of CartItems
     */
    public void updateAvailableAndSoldQuantities(Set<CartItem> items) {
        for (CartItem item : items) {
            Inventory inventoryByProduct = findInventoryByProduct(item.getProduct());
            if (inventoryByProduct != null) {
                inventoryByProduct.setQuantityAvailable(inventoryByProduct.getQuantityAvailable() - item.getCount());
                inventoryByProduct.setQuantitySold(inventoryByProduct.getQuantitySold() + item.getCount());
                inventoryByProduct.setUpdated(LocalDateTime.now());
            }
        }
    }

    public InventorySoldProductsDto getIncomeSpentByInventory() {

        List<Inventory> all = inventoryRepository.findAll();
        List<InventorySoldProductsDto> inventory = all.stream().map(inv -> {
            int quantityAvailable = inv.getQuantityAvailable();
            int quantitySold = inv.getQuantitySold();
            Double discount = (100.0 - inv.getProduct().getDiscount())/100;
            double price = inv.getProduct().getPrice() * discount;
            Double priceGross = inv.getProduct().getPriceGross();
            int quantity = quantityAvailable + quantitySold;
            double substitutedPriceFromOriginalPrice = discount / 100 * inv.getProduct().getPrice();
            return new InventorySoldProductsDto(
                    quantitySold * price,
                    quantity * priceGross,
                    quantitySold * substitutedPriceFromOriginalPrice);
        }).collect(Collectors.toList());

        Double income = inventory.stream().mapToDouble(InventorySoldProductsDto::getIncome).sum();
        Double spent = inventory.stream().mapToDouble(InventorySoldProductsDto::getSpent).sum();
        Double discount = inventory.stream().mapToDouble(InventorySoldProductsDto::getDiscount).sum();

        return new InventorySoldProductsDto(income, spent,discount);
    }
}
