package com.daoyintech.daoyin_release.response;

import com.daoyintech.daoyin_release.entity.order.bargain.BargainJoiner;
import lombok.Data;

/**
 * @author pei on 2018/08/21
 */
@Data
public class RedPacketMsgResponse {


    private String avatar;

    private String nickName;

    private RedPacketResponse redPacket;

    public static RedPacketMsgResponse buildRedPacketMafResponse(BargainJoiner bargainJoiner){
        RedPacketMsgResponse redPacketMsgResponse = new RedPacketMsgResponse();
        redPacketMsgResponse.setAvatar(bargainJoiner.getAvatar());
        redPacketMsgResponse.setNickName(bargainJoiner.getNickName());
        RedPacketResponse redPacketModel = RedPacketResponse.buildRedPacketModel(bargainJoiner);
        redPacketMsgResponse.setRedPacket(redPacketModel);
        return redPacketMsgResponse;
    }



}




