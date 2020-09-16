package hu.progmasters.gmistore.repository;

import hu.progmasters.gmistore.dto.order.OrderListDto;
import hu.progmasters.gmistore.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

//    @Query("select new hu.progmasters.gmistore.dto.order.OrderListDto(o) from Order as o")
//    List<OrderListDto> findAllByOrderListDetails();
}
