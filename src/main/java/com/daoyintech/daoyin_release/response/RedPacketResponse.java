package com.daoyintech.daoyin_release.response;

import com.daoyintech.daoyin_release.entity.order.bargain.BargainJoiner;
import com.daoyintech.daoyin_release.enums.bargain.BargainHelpType;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author pei on 2018/08/21
 */
@Data
public class RedPacketResponse {

    private int bargainHelpType = BargainHelpType.getType(0).ordinal();

    private BigDecimal cutPrice = new BigDecimal(0);

    public static RedPacketResponse buildRedPacketModel(BargainJoiner bargainJoiner){
        RedPacketResponse redPacketResponse = new RedPacketResponse();
        redPacketResponse.setBargainHelpType(bargainJoiner.getType().ordinal());
        redPacketResponse.setCutPrice(bargainJoiner.getBargainPrice());
        return redPacketResponse;
    }


}
