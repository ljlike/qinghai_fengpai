package com.daoyintech.daoyin_release.repository.order;

import com.daoyintech.daoyin_release.entity.order.bargain.BargainOrder;
import com.daoyintech.daoyin_release.enums.bargain.BargainOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2017/8/23.
 */
@Repository
public interface BargainOrderRepository extends JpaRepository<BargainOrder,Long> {

    BargainOrder findByOrderId(Long id);

    BargainOrder findByOrderNo(String orderNo);

    /**
     * @param productId
     * @param userId
     * @param status
     * @return
     */
    BargainOrder findByProductIdAndUserIdAndStatus(Long productId, Long userId, BargainOrderStatus status);


}
