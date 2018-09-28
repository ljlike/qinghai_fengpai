package com.daoyintech.daoyin_release.controller.customer.order;

import com.daoyintech.daoyin_release.controller.customer.user.BaseUserController;
import com.daoyintech.daoyin_release.entity.order.LineItem;
import com.daoyintech.daoyin_release.entity.order.Order;
import com.daoyintech.daoyin_release.entity.user.User;
import com.daoyintech.daoyin_release.enums.ResultEnum;
import com.daoyintech.daoyin_release.enums.order.OrderStatus;
import com.daoyintech.daoyin_release.response.ResultResponse;
import com.daoyintech.daoyin_release.response.order.BargainOrderRequest;
import com.daoyintech.daoyin_release.response.order.OrderLineItemRequest;
import com.daoyintech.daoyin_release.response.order.OrderNewRequest;
import com.daoyintech.daoyin_release.response.order.OrderResponse;
import com.daoyintech.daoyin_release.service.order.cart.LineItemService;
import com.daoyintech.daoyin_release.service.order.OrderService;
import com.daoyintech.daoyin_release.service.user.UserService;
import com.daoyintech.daoyin_release.utils.ResultResponseUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

/**
 * @author pei on 2018/08/14
 */
@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController extends BaseUserController{

    @Autowired
    private LineItemService lineItemService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    /**
     * 下单
     * @param orderNewRequest
     * @return
     */
    @ApiOperation("下订单")
    @PostMapping("/new")
    public ResultResponse newOrder(@RequestBody OrderNewRequest orderNewRequest){
        Long addressId = orderNewRequest.getAddressId();
        if (StringUtils.isEmpty(addressId)){
            return ResultResponseUtil.error(ResultEnum.ORDER_ADDRESS_NULL.getCode(),ResultEnum.ORDER_ADDRESS_NULL.getMessage());
        }
        //库存是否充足 --库存
        List<OrderLineItemRequest> orderLineItemRequests = orderNewRequest.getItems();
        List<LineItem> items = new ArrayList<>();
        for (OrderLineItemRequest orderLineItemRequest : orderLineItemRequests) {
            LineItem item = lineItemService.getOne(orderLineItemRequest.getLineItemId());
            items.add(item);
        }
        Boolean inventory = lineItemService.handleProductInventory(items);
        if (inventory){
            return ResultResponseUtil.error(ResultEnum.INVENTORY_ERROR.getCode(),ResultEnum.INVENTORY_ERROR.getMessage());
        }
        //创建订单记录
        String unionId = getCurrentUnionId();
        if (StringUtils.isEmpty(unionId)){
            return ResultResponseUtil.error(ResultEnum.OBJECT_ERROR.getCode(),"获取用户unionId失败");
        }
        Order order = orderService.createOrder(orderNewRequest,unionId);
        //支付数据
        String ip = getUserIp();
        SortedMap<Object, Object> packageP = orderService.getPayInfo(order, unionId, ip);
        return ResultResponseUtil.success(packageP);
    }


    /**
     * 支付帮助订单
     *
     * @return
     */
    @ApiOperation("支付帮助订单,分享购")
    @PostMapping("/bargainPay")
    public ResultResponse bargainPay(@RequestBody BargainOrderRequest bargainOrderRequest){
        Long addressId = bargainOrderRequest.getAddressId();
        if (StringUtils.isEmpty(addressId)){
            return ResultResponseUtil.error(ResultEnum.ORDER_ADDRESS_NULL.getCode(),ResultEnum.ORDER_ADDRESS_NULL.getMessage());
        }
        Order order = orderService.saveOrderByOrderNo(bargainOrderRequest);
        if (order.getStatus().equals(OrderStatus.待发货) || order.getStatus().equals(OrderStatus.待收货)
            ||order.getStatus().equals(OrderStatus.已完成)){
            return ResultResponseUtil.error(ResultEnum.ORDER_PREPAY_ERROR.getCode(),"请不要重复支付同一订单");
        }
        //创建订单记录
        String unionId = getCurrentUnionId();
        //支付数据
        String ip = getUserIp();
        SortedMap<Object, Object> payInfo = orderService.getPayInfo(order, unionId, ip);
        return ResultResponseUtil.success(payInfo);
    }


    /**
     * 重新支付订单
     * @param orderNo
     * @return
     */
    @ApiOperation("重新支付订单")
    @GetMapping("/{orderNo}/rePay")
    public ResultResponse rePay(@PathVariable String orderNo){
        Order order = orderService.findOrderByOrderNo(orderNo);
        if (StringUtils.isEmpty(order.getCity()) ||
            StringUtils.isEmpty(order.getProvince()) ||
            StringUtils.isEmpty(order.getPhone()) ||
            StringUtils.isEmpty(order.getStreet())){
           return ResultResponseUtil.error(ResultEnum.ORDER_ADDRESS_NULL.getCode(),ResultEnum.ORDER_ADDRESS_NULL.getMessage());
        }
        //创建订单记录
        String unionId = getCurrentUnionId();
        if (StringUtils.isEmpty(unionId)){
            return ResultResponseUtil.error(ResultEnum.OBJECT_ERROR.getCode(),"获取用户unionId失败");
        }
        //支付数据
        String ip = getUserIp();
        SortedMap<Object, Object> payInfo = orderService.getPayInfo(order, unionId, ip);
        return ResultResponseUtil.success(payInfo);
     }



    /**
     * 确认收货
     * @param orderNo
     * @return
     */
    @ApiOperation("修改订单状态,收货完成")
    @GetMapping("/{orderNo}/receive")
    public ResultResponse updateOrderStatus(@PathVariable String orderNo){
        orderService.updateOrderStatus(orderNo);
        return ResultResponseUtil.success();
    }



    /**
     *取消订单
     * @param orderNo
     * @return
     */
    @ApiOperation("取消订单")
    @GetMapping("/{orderNo}/cancel")
    public ResultResponse delete(@ApiParam("订单号") @PathVariable String orderNo){
        Boolean isSuccess = orderService.deleteOrder(orderNo);
        String message = "";
        if (isSuccess){
            message = "订单取消成功";
        }else {
            message = "订单取消失败";
        }
        return ResultResponseUtil.success(message);
    }


    /**
     * 订单列表
     * */
    @ApiOperation("订单列表")
    @GetMapping
    public ResultResponse findAll(@ApiParam("状态 11:所有订单,0待付款,1代发货,2待收货,3:分享中")
                                  @RequestParam("status") Integer status){
        String unionId = getCurrentUnionId();
        if (StringUtils.isEmpty(unionId)){
            return ResultResponseUtil.error(ResultEnum.OBJECT_ERROR.getCode(),"获取用户unionId失败");
        }
        User user = userService.findByUnionId(unionId);
        List<OrderResponse> list = orderService.findByOrderStatus(status,user);
        log.info("订单列表:list={}",list);
        return ResultResponseUtil.success(list);
    }


    /**
     * 查询订单详情
     * @param orderNo
     * @return
     */
    @ApiOperation("查询订单详情")
    @GetMapping("/{orderNo}")
    public ResultResponse findOrderById(@PathVariable String orderNo){
        OrderResponse orderResponse = orderService.findByOrderNo(orderNo);
        return ResultResponseUtil.success(orderResponse);
    }




}




