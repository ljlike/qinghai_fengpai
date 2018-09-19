package com.daoyintech.daoyin_release.repository.order;

import com.daoyintech.daoyin_release.entity.order.LineItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LineItemRepository extends JpaRepository<LineItem,Long> {

    List<LineItem> findByOrderId(Long orderId);

    Integer countByCartId(Long cartId);

    LineItem findByCartIdAndProductIdAndFormatIdAndColorId(Long cartId,Long productId,Long formatId,Long colorId);

}