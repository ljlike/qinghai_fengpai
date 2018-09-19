package com.daoyintech.daoyin_release.response.order;

import lombok.Data;

@Data
public class LineItemRequest {

    private Long productId;

    private Long formatId;

    private Long colorId;

    private Integer quantity;
}
