package com.daoyintech.daoyin_release.entity.product;

import com.daoyintech.daoyin_release.entity.common.AbstractEntity;
import com.daoyintech.daoyin_release.response.DefinitionResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties(value = {"handler","hibernateLazyInitializer","fieldHandler"})
public class Product extends AbstractEntity {

    @Column
    private String name; //商品名称

    @Column
    private String description; //商品描述

    @Column
    private String iconKey; //图片路径

    @Column
    private Integer inventory = 0; //商品库存

    @Column
    private Long subProductTypeId; //商品分类ID

    @Column
    private BigDecimal showPrice = new BigDecimal(0);  //标价

    @Column
    private BigDecimal sellPrice = new BigDecimal(0); //实际售价

    @Column
    private Integer hotSort = 0; //热度

    @Column
    private Boolean isSell = false; //是否上架

    @Column
    private Boolean isRecommendation = false; //人气推荐的商品


    @Transient
    private String url;

    @Column
    private BigDecimal pointPercent = new BigDecimal(0.2);//积分最大抵扣比率

    @Column
    private Boolean isLuxury = false;//是否奢侈品

    @Column
    private Integer initSellCount = 0;

    @Column
    private Integer showSellCount = 0;

    public String url(){
        return DefinitionResponse.getImgUrl(getIconKey());
    }

}
