package com.daoyintech.daoyin_release.repository.user;

import com.daoyintech.daoyin_release.entity.user.UserProfitOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface UserProfitOrderRepository extends JpaRepository<UserProfitOrder,Long> {
    List<UserProfitOrder> findByUserProfitId(Long userProfitId);

    List<UserProfitOrder> findByOrderNo(String orderNo);

    List<UserProfitOrder> findByUserProfitIdAndCreatedAtAfterAndStatus(Long userProfitId, Date firstDay, Integer status);

    List<UserProfitOrder> findByUserProfitAppletIdAndCreatedAtAfterAndStatus(Long id, Date firstDayByMonth, int i);


}
