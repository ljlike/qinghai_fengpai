package com.daoyintech.daoyin_release.response.member;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MemberItemResponse {
    private Long id;
    private String name;
    private String avatar;
    private Integer subPartnersCount;
    private BigDecimal salesAmount;
    private BigDecimal subIntegral;
    private BigDecimal integral;

    /*private BigDecimal profit;
    private Date expiredAt = new Date();*/
}
