package com.daoyintech.daoyin_release.entity.order;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * Created by xuzhaolin on 2017/5/18.
 */
@Data
public class OrderDate {

    @Temporal(TemporalType.TIMESTAMP)
    private Date sendAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date finishedAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date canceledAt; //取消时间

    @Temporal(TemporalType.TIMESTAMP)
    private Date payAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date refundAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date loseAt;//失效时间


}
