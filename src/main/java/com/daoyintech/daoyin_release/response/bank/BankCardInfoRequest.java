package com.daoyintech.daoyin_release.response.bank;

import lombok.Data;

/**
 * @author pei on 2018/08/22
 */
@Data
public class BankCardInfoRequest {

    private String bankCardNum;

    private Long openBank;

    private String holdCardName;

}
