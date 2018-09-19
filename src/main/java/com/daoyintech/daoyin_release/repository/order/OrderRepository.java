package com.daoyintech.daoyin_release.repository.order;

import com.daoyintech.daoyin_release.entity.order.Order;
import com.daoyintech.daoyin_release.enums.bargain.BargainOrderStatus;
import com.daoyintech.daoyin_release.enums.order.OrderStatus;
import com.daoyintech.daoyin_release.enums.order.OrderType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Created by Administrator on 2017/8/22.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    Order findByOrderNo(String orderNo);


    @Query(value = "SELECT * FROM orders WHERE user_id = ?1 ORDER BY created_at DESC",nativeQuery = true)
    List<Order> findByUserId(Long id);

    @Query(value = "SELECT o.* FROM bargain_orders b,orders o WHERE b.order_id = o.id AND b.user_id=o.user_id AND o.user_id = ?1 AND o.order_type = ?2 AND b.status = ?3",nativeQuery = true)
    List<Order> queryBargainOrder(Long id, OrderType orderType, BargainOrderStatus status);

    List<Order> findByUserIdAndOrderTypeAndStatus(Long userId,OrderType orderType,OrderStatus status);

    List<Order> findByUserIdAndStatusOrderByUpdatedAtDesc(Long id, OrderStatus orderStatus);

    Page<Order> findAll(Pageable pageable);
}
