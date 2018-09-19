package com.daoyintech.daoyin_release.response.member;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class MemberDetailResponse {

    List<MemberItemResponse> shoppingRecord = new ArrayList<>();

    private String name;

    private String avatar;

    private Integer partnersCount;

    private BigDecimal salesAmount;

    private BigDecimal integral;
}
