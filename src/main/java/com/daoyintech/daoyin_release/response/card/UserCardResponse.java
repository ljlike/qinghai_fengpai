package com.daoyintech.daoyin_release.response.card;

import lombok.Data;

/**
 * @author pei on 2018/7/12
 */
@Data
public class UserCardResponse {

    private Long id;

    private String cardId;

    private String userCardCode;

    private Integer status;

    private Long expire;



    private String title;
    /**
     * 1 法务卷
     * 2 心理卷
     * */
    private Integer type;

}
