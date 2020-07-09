package hu.progmasters.gmistore.repository;

import hu.progmasters.gmistore.model.Order;
import hu.progmasters.gmistore.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}
