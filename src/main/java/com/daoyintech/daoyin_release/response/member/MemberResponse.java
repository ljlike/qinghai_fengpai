package com.daoyintech.daoyin_release.response.member;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MemberResponse {
    private Long id;
    private String avatar;
    private BigDecimal integral; //此处是积分
    private String name;
    private BigDecimal salesAmount; //当月销售金额
    private Integer subPartnersCount; //下级合伙人数量


}
