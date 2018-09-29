package com.daoyintech.daoyin_release.service;

import com.daoyintech.daoyin_release.entity.order.Order;

/**
 * @author pei on 2018/09/25
 */
public interface DrawStatisticsService {


    void productStatistics(Order saveOrder);


    void drawStatistics();
}
