package com.daoyintech.daoyin_release.controller.customer.order.cart;

import com.daoyintech.daoyin_release.controller.customer.user.BaseUserController;
import com.daoyintech.daoyin_release.entity.order.LineItem;
import com.daoyintech.daoyin_release.enums.ResultEnum;
import com.daoyintech.daoyin_release.response.ResultResponse;
import com.daoyintech.daoyin_release.response.order.LineItemRequest;
import com.daoyintech.daoyin_release.response.result.LineItemResult;
import com.daoyintech.daoyin_release.service.CartService;
import com.daoyintech.daoyin_release.utils.ResultResponseUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController extends BaseUserController {

    @Autowired
    private CartService cartService;

    @GetMapping
    @ApiOperation("获取购物车列表")
    public ResultResponse findAll() {
        List<LineItemResult> results = cartService.getLineItemResultsByUserId(getCurrentUnionId());
        if (results.size()>0){
            return ResultResponseUtil.success(results);
        }else {
            return ResultResponseUtil.error(ResultEnum.CART_EMPTY.getCode(),ResultEnum.CART_EMPTY.getMessage());
        }

    }

    @PostMapping("/product/addOrUpdate")
    @ApiOperation("添加或更新商品到购物车")
    public ResultResponse create(@ApiParam("所购商品的信息:productId,formatId,colorId,quantity") @RequestBody LineItemRequest lineItemReceived) {
        Long cartId = cartService.getCurrentCartIdByUnionId(getCurrentUnionId());
        LineItem lineItem = new LineItem();
        BeanUtils.copyProperties(lineItemReceived,lineItem);
        lineItem.setCartId(cartId);
        return ResultResponseUtil.success(cartService.saveOrUpdateLineItemByLineItemId(lineItem));
    }

    @PostMapping()
    @ApiOperation("将所选商品移除购物车")
    public ResultResponse delete(@ApiParam("需要移除的商品所对应在购物车列表中的id") Long lineItemId) {
        cartService.delete(lineItemId);
        return ResultResponseUtil.success();
    }
}
