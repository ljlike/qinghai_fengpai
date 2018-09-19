package com.daoyintech.daoyin_release.response.draw;

import com.daoyintech.daoyin_release.enums.bargain.BargainHelpType;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;

/**
 * @author pei on 2018/09/10
 */
@Data
public class JoinerUserResponse {

    private String userName;

    private String userHeadUrl;

    private BigDecimal drawIntegral;

    @Enumerated(EnumType.ORDINAL)
    private BargainHelpType type;

}






