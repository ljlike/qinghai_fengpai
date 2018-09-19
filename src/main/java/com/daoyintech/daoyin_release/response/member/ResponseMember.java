package com.daoyintech.daoyin_release.response.member;

import lombok.Data;

import java.util.List;

@Data
public class ResponseMember {

    private List<MemberResponse> list;

    private Integer count;
}
