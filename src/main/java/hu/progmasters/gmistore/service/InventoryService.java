package hu.progmasters.gmistore.service;

import hu.progmasters.gmistore.model.CartItem;
import hu.progmasters.gmistore.model.Inventory;
import hu.progmasters.gmistore.model.Product;
import hu.progmasters.gmistore.repository.InventoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public void saveInventory(Product product, int quantityAvailable) {
        Inventory inventory = new Inventory(product, quantityAvailable);
        inventoryRepository.save(inventory);
    }

    public Inventory findInventoryByProduct(Product product) {
        Optional<Inventory> inventoryByProduct = inventoryRepository.findByProduct(product);
        return inventoryByProduct.orElse(null);
    }

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
}
