package com.daoyintech.daoyin_release.service.reservation;

import com.daoyintech.daoyin_release.entity.reservation.Reservation;
import com.daoyintech.daoyin_release.response.ResultResponse;
import com.daoyintech.daoyin_release.response.reservation.ReservationResponse;

import java.util.Date;
import java.util.List;

/**
 * Created by lj on 2018/9/19 9:54
 */
public interface ReservationService {

    /**
     * 创建预约信息
     *
     * @param userName 预约人
     * @param phone    预约人电话
     * @param strDate  预约日期
     * @return
     */
    ResultResponse createReservationInfo(Long userId, String userName, String phone, String strDate, String reservationType);

    /**
     * 根据预约类型查询所有预约信息
     * @param type 预约类型
     * @return 预约信息
     */
    List<Reservation> selectByReservationType(String type);

    /**
     * 处理预约信息
     * @param id 预约信息id
     */
    void reservationDispose(String id);

    /**
     * 查找管理员定义的预约信息
     *
     * @return 预约信息
     */
    List<ReservationResponse> findReservationInfoDefinedByAdmin();

    /**
     * 创建或更新管理员定义的预约信息
     * @param strDate 预约的时间
     * @param maxReservationNumber 预约最大人数
     * @param isCameramanOut 摄影师是否外出
     * @return
     */
    ResultResponse createOrUpdateReservationInfoDefinedByAdmin(String strDate, String maxReservationNumber, String isCameramanOut);

    /**
     * 删除管理员定义的预约信息
     * @param strDate 要删除的预约日期
     * @return 结果
     */
    String deleteReservationInfoDefinedByAdmin(String strDate);

}
