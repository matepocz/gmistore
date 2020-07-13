package hu.progmasters.gmistore.service;

import hu.progmasters.gmistore.model.Inventory;
import hu.progmasters.gmistore.model.Product;
import hu.progmasters.gmistore.repository.InventoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
