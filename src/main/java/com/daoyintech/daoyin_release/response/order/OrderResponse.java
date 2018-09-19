package com.daoyintech.daoyin_release.response.order;
import com.daoyintech.daoyin_release.entity.order.LineItem;
import com.daoyintech.daoyin_release.entity.order.Order;
import com.daoyintech.daoyin_release.entity.order.bargain.BargainOrder;
import com.daoyintech.daoyin_release.entity.product.ProductFormat;
import com.daoyintech.daoyin_release.repository.product.ProductRepository;
import com.daoyintech.daoyin_release.repository.product.ProductColorRepository;
import com.daoyintech.daoyin_release.repository.product.ProductFormatRepository;
import com.daoyintech.daoyin_release.utils.BigDecimalDeal;
import com.google.common.collect.Lists;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * pei
 */
@Data
public class OrderResponse {

    private String orderNo;

    private Integer status;

    private Boolean isBargain;

    private BargainResponse bargain;

    private List<LineItemDetailResponse> lineItems;

    private Date createdAt;

    private OrderTimeResponse orderTime;

    private DeliveryResponse delivery;

    private ExpressCompanyResponse express;

    private Double usedIntegral;

    private BigDecimal price;

    private BigDecimal initPrice = new BigDecimal(0);

    private Boolean isMyselfPick;

    public static OrderResponse bargainTransformOrderResponse(Order order,
                                                              BargainOrder bargainOrder,
                                                              List<LineItem> lineItems,
                                                              ProductRepository productRepository,
                                                              ProductFormatRepository productFormatRepository,
                                                              ProductColorRepository productColorRepository) {
        OrderResponse orderResponse = getOrderResponse(order,lineItems,
                productRepository,productFormatRepository,productColorRepository);

        orderResponse.setDelivery(DeliveryResponse.deliveryTransformDeliveryResponse(order));
        orderResponse.setExpress(ExpressCompanyResponse.expressCompanyTransformExpressCompanyResponse(order));
        orderResponse.setOrderTime(OrderTimeResponse.orderTimeTransformOrderTimeResponse(order));
        if (bargainOrder != null){
            LineItem item = lineItems.get(0);
            orderResponse.setBargain(BargainResponse.bargainOrderTransformBargainResponse(bargainOrder,order,item));
        }
        if (order.getUsedIntegral() != null){
            orderResponse.setUsedIntegral(order.getUsedIntegral().doubleValue());
        }
        return orderResponse;
    }


    private static OrderResponse getOrderResponse(Order order, List<LineItem> lineItems,
                                               ProductRepository productRepository,
                                               ProductFormatRepository productFormatRepository,
                                               ProductColorRepository productColorRepository){

        List<LineItemDetailResponse> lineItemDetailResponses = Lists.transform(lineItems, lineItem ->
                LineItemDetailResponse.buildByLineItem(
                        lineItem,lineItem.getProductId() != null ? productRepository.getOne(lineItem.getProductId()):null,
                        lineItem.getFormatId() != null ? productFormatRepository.getOne(lineItem.getFormatId()):null,
                        lineItem.getColorId() != null ? productColorRepository.getOne(lineItem.getColorId()):null));

        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setCreatedAt(order.getCreatedAt());
        orderResponse.setOrderNo(order.getOrderNo());
        //TODO
        BigDecimal.valueOf(initTotalPrice(lineItems,productFormatRepository)).setScale(2,BigDecimal.ROUND_HALF_DOWN);

        orderResponse.setInitPrice(new BigDecimal(0));

        orderResponse.setPrice(BigDecimal.valueOf(totalPrice(lineItems)).setScale(2,BigDecimal.ROUND_HALF_DOWN));

        orderResponse.setStatus(order.getStatus().ordinal());
        orderResponse.setIsMyselfPick(order.getIsMyselfPick());
        if (order.getOrderType().ordinal() == 1){
            orderResponse.setIsBargain(true);
        }else {
            orderResponse.setIsBargain(false);
        }
        orderResponse.setLineItems(lineItemDetailResponses);
        return orderResponse;
    }

    public static Double initTotalPrice(List<LineItem> items,
                                        ProductFormatRepository productFormatRepository){
        Double sum = 0.0;
        for(int i = 0;i < items.size(); i++){
            LineItem lineItem = items.get(i);
            ProductFormat format = productFormatRepository.getOne(lineItem.getFormatId());
            sum += lineItem.initTotalPrice(format.getSellPrice());
        }
        return sum;
    }

    public static Double totalPrice(List<LineItem> items){
        Double sum = 0.0;
        for(int i = 0;i < items.size(); i++){
            LineItem lineItem = items.get(i);
            sum += lineItem.totalPrice();
        }
        return sum;
    }

    public static OrderResponse transformOrderResponse(Order order,
                                                       List<LineItem> lineItems,
                                                       ProductRepository productRepository,
                                                       ProductFormatRepository productFormatRepository,
                                                       ProductColorRepository productColorRepository) {
        OrderResponse orderResponse = getOrderResponse(order,
                                                        lineItems,
                                                        productRepository,
                                                        productFormatRepository,
                                                        productColorRepository
                                                        );
        orderResponse.setDelivery(DeliveryResponse.deliveryTransformDeliveryResponse(order));
        orderResponse.setInitPrice(BigDecimal.valueOf(totalPrice(lineItems)).setScale(2,BigDecimal.ROUND_HALF_DOWN));
        orderResponse.setExpress(ExpressCompanyResponse.expressCompanyTransformExpressCompanyResponse(order));
        orderResponse.setOrderTime(OrderTimeResponse.orderTimeTransformOrderTimeResponse(order));
        return orderResponse;
    }



}