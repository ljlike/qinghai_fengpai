package com.daoyintech.daoyin_release.response.gift;

import com.daoyintech.daoyin_release.entity.order.bargain.BargainJoiner;
import com.daoyintech.daoyin_release.entity.order.bargain.BargainOrder;
import com.daoyintech.daoyin_release.entity.product.Product;
import com.daoyintech.daoyin_release.entity.user.User;
import lombok.Data;
import me.chanjar.weixin.common.bean.WxJsapiSignature;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author pei on 2018/08/20
 */
@Data
public class GiftShareResponse {

    private User user;

    private BargainOrder bargainOrder;

    private Product product;

    List<BargainJoiner> joiners;

    List<BargainJoiner> joiners_sort_by_date;

    List<BargainJoiner> joiners7_11;

    private String name;

    private String bargainStatus;

    private BigDecimal sell_price;

    private BigDecimal cutPrice;

    private String helpCutPriceUrl;

    private boolean redPacketClickAble;

    private int countJoiners;

    private String title;

    private String finishUrl;

    private String shareUrl;

    private String productImageUrl;

    private WxJsapiSignature jsApi;


    //分享
    private Boolean isSubsrible;

    private String qr_code;

    private Boolean isCutPrice;

    private String daoYinUrl;

    private Boolean isMaxHelpCount;


    private String expiredAt;

}
