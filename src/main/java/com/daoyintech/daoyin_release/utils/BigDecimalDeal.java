package com.daoyintech.daoyin_release.utils;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/6/1 0001.
 */
public class BigDecimalDeal {

    public static BigDecimal subAndStayTwoDecimal(BigDecimal param1,BigDecimal param2){
        BigDecimal total = param1.subtract(param2);
        return new BigDecimal(stayTwoDecimal(total));
    }


    public static BigDecimal addAndStayTwoDecimal(BigDecimal param1,BigDecimal param2){
        BigDecimal total = param1.add(param2);
        return new BigDecimal(stayTwoDecimal(total));
    }

    public static Double stayTwoDecimal(BigDecimal bigDecimal){
        return bigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

}
