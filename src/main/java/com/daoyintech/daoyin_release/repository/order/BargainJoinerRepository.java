package com.daoyintech.daoyin_release.repository.order;

import com.daoyintech.daoyin_release.entity.order.bargain.BargainJoiner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2017/8/24.
 */
@Repository
public interface BargainJoinerRepository extends JpaRepository<BargainJoiner,Long> {

    @Query(value = "select * from bargain_joiners where bargain_order_id = ?1 order by created_at DESC ",nativeQuery = true)
    List<BargainJoiner> findByBargainOrderId(Long bargainId);

    boolean existsByJoinerIdAndBargainOrderId(Long joinerId, Long bargainOrderId);

    int countByBargainOrderId(Long id);

    List<BargainJoiner> findByBargainOrderIdOrderByCreatedAtAsc(Long id);

    List<BargainJoiner> findByJoinerIdOrderByCreatedAtDesc(Long userId);

}
