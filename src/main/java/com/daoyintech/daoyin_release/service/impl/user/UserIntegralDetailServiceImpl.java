package com.daoyintech.daoyin_release.service.impl.user;

import com.daoyintech.daoyin_release.entity.order.Order;
import com.daoyintech.daoyin_release.entity.user.User;
import com.daoyintech.daoyin_release.entity.user.integral.UserIntegralDetail;
import com.daoyintech.daoyin_release.enums.user.UserIntegralFromType;
import com.daoyintech.daoyin_release.repository.order.OrderRepository;
import com.daoyintech.daoyin_release.repository.user.UserIntegralDetailRepository;
import com.daoyintech.daoyin_release.repository.user.UserRepository;
import com.daoyintech.daoyin_release.response.user.UserIntegralDetailResponse;
import com.daoyintech.daoyin_release.service.user.UserIntegralDetailService;
import com.daoyintech.daoyin_release.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author pei on 2018/08/13
 */
@Slf4j
@Service
public class UserIntegralDetailServiceImpl implements UserIntegralDetailService{


    @Autowired
    private UserIntegralDetailRepository userIntegralDetailRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public List<UserIntegralDetail> findByUserIdAndType(User user, int page, int type) {
        int pageSize = 10;
        UserIntegralFromType integralFromType = UserIntegralFromType.getUserIntegralFromType(type);
        return userIntegralDetailRepository.findByUserIdAndUserIntegralFromTypeOrderByCreatedAtDesc(user.getId(),integralFromType, PageRequest.of(page, pageSize));
    }

    @Override
    public List<UserIntegralDetailResponse> transformUserIntegralDetailModel(List<UserIntegralDetail> userIntegralDetails) {
        List<UserIntegralDetailResponse> userIntegralDetailResponseList = new ArrayList<>();
        userIntegralDetails.forEach(userIntegralDetail -> {
            UserIntegralDetailResponse userIntegralDetailResponse = new UserIntegralDetailResponse();
            userIntegralDetailResponse.setIntegral(userIntegralDetail.getIntegral());
            userIntegralDetailResponse.setOrderId(userIntegralDetail.getOrderId());
            userIntegralDetailResponse.setUserId(userIntegralDetail.getUserId());
            userIntegralDetailResponse.setCreatedAt(userIntegralDetail.getCreatedAt().getTime());
            if (userIntegralDetail.getOrderId()!= null) {
                Order order = orderRepository.getOne(userIntegralDetail.getOrderId());
                userIntegralDetailResponse.setUserIntegralFromType(userIntegralDetail.intergralFromLabel(userRepository.getOne(order.getUserId())));
            }else {
                userIntegralDetailResponse.setUserIntegralFromType(userIntegralDetail.intergralFromLabel());
            }
            userIntegralDetailResponseList.add(userIntegralDetailResponse);
        });
        return userIntegralDetailResponseList;
    }

    @Override
    public UserIntegralDetail buildUserIntegralDetail(Order order, User user, BigDecimal integral, UserIntegralFromType fromType) {
        UserIntegralDetail userIntegralDetail = new UserIntegralDetail();
        if (order!=null) {
            userIntegralDetail.setOrderId(order.getId());
        }
        userIntegralDetail.setIntegral(integral);
        userIntegralDetail.setUserId(user.getId());
        userIntegralDetail.setUserIntegralFromType(fromType);
        UserIntegralDetail detail = userIntegralDetailRepository.save(userIntegralDetail);
        log.info("{}:积分详情记录成功: UserIntegralDetail = {}", DateUtils.getStringDate(),detail);
        return detail;
    }

    @Override
    public UserIntegralDetail save(UserIntegralDetail userIntegralDetail) {
        return userIntegralDetailRepository.save(userIntegralDetail);
    }

    @Override
    public List<UserIntegralDetail> findByUserIdToNewest(User user) {
        List<UserIntegralDetail> detailList = userIntegralDetailRepository.findByUserId(user.getId());
        List<UserIntegralDetail> collect = detailList.stream().sorted((u1, u2) -> u2.getCreatedAt().compareTo(u1.getCreatedAt())).limit(5).collect(Collectors.toList());
        return collect;
    }


}











