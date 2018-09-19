package com.daoyintech.daoyin_release.response.result;

import com.daoyintech.daoyin_release.response.DefinitionResponse;
import com.google.common.collect.Lists;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class LineItemResult {

    private Long lineItemId;

    private Integer inventory;

    private Integer quantity;

    private Long productId;

    private String productName;

    private String iconKey;

    private BigDecimal sellPrice;

    private Long colorId;

    private String colorName;

    private Long formatId;

    private String formatName;

    public static List<LineItemResult> lineItemResultsBuild(List<LineItemResult> lineItemResults) {
        return Lists.transform(lineItemResults, input -> {
            LineItemResult result = new LineItemResult();
            result.setLineItemId(input.getLineItemId());
            result.setQuantity(input.getQuantity());
            result.setInventory(input.getInventory());
            result.setProductId(input.getProductId());
            result.setProductName(input.getProductName());
            result.setIconKey(DefinitionResponse.getImgUrl(input.getIconKey()));
            result.setSellPrice(input.getSellPrice());
            result.setColorId(input.getColorId());
            result.setColorName(input.getColorName());
            result.setFormatId(input.getFormatId());
            result.setFormatName(input.getFormatName());
            return result;
        });
    }

    public static LineItemResult lineItemResultBuild(LineItemResult lineItemResult){
        lineItemResult.setIconKey(DefinitionResponse.getImgUrl(lineItemResult.getIconKey()));
        return lineItemResult;
    }
}
