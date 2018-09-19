package com.daoyintech.daoyin_release.mappers;

import com.daoyintech.daoyin_release.response.result.LineItemResult;

import java.util.List;

public interface LineItemMapper {

     List<LineItemResult> selectLineItemResultsByUserId(String unionId);

     LineItemResult selectLineItemResultByLineItemId(Long lineItemId);
}
