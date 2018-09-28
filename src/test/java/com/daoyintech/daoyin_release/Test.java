package com.daoyintech.daoyin_release;


import com.daoyintech.daoyin_release.response.DefinitionResponse;
import com.daoyintech.daoyin_release.response.reservation.ReservationResponse;
import com.daoyintech.daoyin_release.utils.helper.ReservationResponseHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Test {

    public static void main(String[] args) {

        /*List<Integer> numbers = Arrays.asList(3, 2, 2, 3, 7, 3, 5);

        IntSummaryStatistics stats = numbers.stream().mapToInt((x) -> x).summaryStatistics();

        System.out.println("列表中最大的数 : " + stats.getMax());
        System.out.println("列表中最小的数 : " + stats.getMin());
        System.out.println("所有数之和 : " + stats.getSum());
        System.out.println("平均数 : " + stats.getAverage());*/

        String userName = "鲁江";
        String phone = "13999736630";
        String appointmentDate = "2018-12-12";
        String reservationType = "拍照";

        String content = new StringBuilder("用户:").append(userName).append("\r\n")
                .append("联系方式:").append(phone).append("\r\n")
                .append("预约时间:").append(appointmentDate).append("\r\n")
                .append("预约项目:").append(reservationType).append("\r\n")
                .append("请尽快联系客户,进一步确认预约时间!")
                .toString();
        System.out.println(content);
    }



}
