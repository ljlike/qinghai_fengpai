package com.daoyintech.daoyin_release.entity.order;

import com.daoyintech.daoyin_release.entity.common.AbstractEntity;
import com.daoyintech.daoyin_release.utils.BigDecimalDeal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "line_items")
@Data
@EqualsAndHashCode(callSuper = true)
public class LineItem extends AbstractEntity {

    @Column
    private Long orderId;

    @Column
    private Long productId;

    @Column
    private Long cartId;

    @Column
    private Long productGroupId;

    @Column
    private Long formatId;

    @Column
    private Long colorId;

    @Column
    private Integer quantity = 1;

    @Column
    private BigDecimal price = new BigDecimal(0);

    public Double totalPrice(){
        return BigDecimalDeal.stayTwoDecimal(getPrice().multiply(new BigDecimal(getQuantity())));
    }

    public Double initTotalPrice(BigDecimal sellPrice){
        return BigDecimalDeal.stayTwoDecimal(sellPrice.multiply(new BigDecimal(getQuantity())));
    }

}
