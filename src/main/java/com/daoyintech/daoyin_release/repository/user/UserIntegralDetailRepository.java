package com.daoyintech.daoyin_release.repository.user;

import com.daoyintech.daoyin_release.entity.user.integral.UserIntegralDetail;
import com.daoyintech.daoyin_release.enums.user.UserIntegralFromType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserIntegralDetailRepository extends PagingAndSortingRepository<UserIntegralDetail,Long> {
    Page<UserIntegralDetail> findByUserId(Long userId, Pageable pageable);

    @Query(value = "select * from user_integral_detail d where d.user_id = ?1 ORDER BY 'createdAt' DESC limit ?2,?3",nativeQuery = true)
    List<UserIntegralDetail> findByUserId(Long userId, int pageNumber, int pageSize);

    List<UserIntegralDetail> findByUserId(Long userId);

    List<UserIntegralDetail> findByUserIdAndUserIntegralFromTypeOrderByCreatedAtDesc(Long userId, UserIntegralFromType type, Pageable pageable);


    List<UserIntegralDetail> findByUserIntegralFromTypeAndOrderIdAndUserId(UserIntegralFromType userIntegralFromType,Long orderId,Long userId);
}
