package com.daoyintech.daoyin_release.response.order;

import com.daoyintech.daoyin_release.entity.order.Order;
import lombok.Data;

/**
 * Created by pei
 */
@Data
public class ExpressCompanyResponse {

    private String companyName;

    private String expressNo;

    public static ExpressCompanyResponse expressCompanyTransformExpressCompanyResponse(Order order) {
        ExpressCompanyResponse expressCompanyResponse = new ExpressCompanyResponse();
        expressCompanyResponse.setCompanyName(order.getExpressName());
        expressCompanyResponse.setExpressNo(order.getExpressNo());
        return expressCompanyResponse;
    }


}
