package hu.progmasters.gmistore.repository;

import hu.progmasters.gmistore.dto.order.OrderListDto;
import hu.progmasters.gmistore.model.Order;
import hu.progmasters.gmistore.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findOrderByUniqueId(String id);

    @Query("select new hu.progmasters.gmistore.dto.order.OrderListDto(" +
            "o.uniqueId," +
            "o.user.username," +
            "o.orderedAt," +
            "o.status.displayName," +
            "o.totalPrice) from Order as o")
    List<OrderListDto> findAllByOrderListDetails();

    @Query("select o.items from Order as o where o.user.username=:name")
    Set<OrderItem> findAllOrderedProductsByUser(@Param("name") String username);

    @Query("select o.uniqueId from Order as o join o.items as i where i.id=:item_id")
    String findOrderId(@Param("item_id") Long id);

    //    @Query("select new hu.progmasters.gmistore.dto.order.OrderListDto(o) from Order as o")
//    List<OrderListDto> findAllByOrderListDetails();
}
