package com.daoyintech.daoyin_release.controller.customer.order.bargain;

import com.daoyintech.daoyin_release.controller.customer.user.BaseUserController;
import com.daoyintech.daoyin_release.entity.order.Order;
import com.daoyintech.daoyin_release.entity.order.bargain.BargainJoiner;
import com.daoyintech.daoyin_release.entity.order.bargain.BargainOrder;
import com.daoyintech.daoyin_release.entity.product.Product;
import com.daoyintech.daoyin_release.entity.product.ProductFormat;
import com.daoyintech.daoyin_release.entity.user.User;
import com.daoyintech.daoyin_release.enums.ResultEnum;
import com.daoyintech.daoyin_release.response.RedPacketMsgResponse;
import com.daoyintech.daoyin_release.response.ResultResponse;
import com.daoyintech.daoyin_release.response.gift.GiftShareResponse;
import com.daoyintech.daoyin_release.service.*;
import com.daoyintech.daoyin_release.utils.DateUtils;
import com.daoyintech.daoyin_release.utils.ResultResponseUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author pei on 2018/08/20
 */
@Slf4j
@RestController
@RequestMapping("/bargains")
public class BargainOrderController extends BaseUserController{

    @Autowired
    private UserService userService;

    @Autowired
    private BargainOrderService bargainOrderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private BargainJoinerService joinerService;

    @Autowired
    private BargainJoinerHelpService bargainJoinerHelpService;

    @Autowired
    private BargainJoinerHelpService helpService;

    @Autowired
    private UserRelationShipAppletService userRelationShipAppletService;

    @Value("${wx.host}")
    private String host;


    /**
     * 创建分享购订单
     * */
    @ApiOperation("创建分享购订单")
    @GetMapping("/new")
    public ResultResponse newBargainOrder(@RequestParam("productId") Long productId,
                                          @RequestParam("formatId") Long formatId,
                                          @RequestParam(name = "colorId",required = false)Long colorId) {
        String unionId = getCurrentUnionId();
        if (StringUtils.isEmpty(unionId)){
            return ResultResponseUtil.error(ResultEnum.OBJECT_ERROR.getCode(),"获取用户unionId失败");
        }
        User user = userService.findByUnionId(unionId);
        BargainOrder bargainOrder = bargainOrderService.create(productId, user,formatId,colorId);
        //if(bargainOrder.getId() != null) {
            //TODO 消息通知
            //wxBargainTemplateService.sendBargainOrderCreatedAt(bargainOrder);
        //}
        return ResultResponseUtil.success(bargainOrder.getOrderNo());
    }

    @ApiOperation("福袋抓取/打开,页面-数据")
    @GetMapping("/{bargainOrderNo}/show")
    public ResultResponse showBargainOrder(@PathVariable String bargainOrderNo) {
        String unionId = getCurrentUnionId();
        if (StringUtils.isEmpty(unionId)){
            return ResultResponseUtil.error(ResultEnum.OBJECT_ERROR.getCode(),"获取用户unionId失败");
        }
        GiftShareResponse giftShareResponse = new GiftShareResponse();
        User user = userService.findByUnionId(unionId);
        /*String nick_name = null;
        try {
            nick_name = URLDecoder.decode(user.getNickName(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            log.error("{}:nickname编码错误:{}", DateUtils.getStringDate(),e.getMessage());
        }*/
        user.setNickName(user.getNickName());
        giftShareResponse.setUser(user);
        BargainOrder bargainOrder = bargainOrderService.findOrderByOrderNo(bargainOrderNo);
        giftShareResponse.setBargainOrder(bargainOrder);
        giftShareResponse.setExpiredAt(String.valueOf(bargainOrder.getExpiredAt().getTime()));
        /*try {
            String nickName = URLDecoder.decode(userRepository.getOne(bargainOrder.getUserId()).getNickName(), "utf-8");
            giftShareResponse.setName(nickName);
        } catch (UnsupportedEncodingException e) {
            log.error("{}:nickname编码错误:{}", DateUtils.getStringDate(),e.getMessage());
        }*/
        giftShareResponse.setBargainStatus(bargainOrder.getStatus().toString());
        Product product = productService.findProductByProductId(bargainOrder.getProductId());
        ProductFormat format = productService.findProductFormatById(bargainOrder.getFormatId());
        giftShareResponse.setProduct(product);
        giftShareResponse.setSell_price(format.getSellPrice());
        //giftShareResponse.setHelpCutPriceUrl(host + "/bargains/" + bargainOrderNo +"/helpCutPrice");
        //参与者
        List<BargainJoiner> joiners = joinerService.getJoinersByBargainOrder(bargainOrder);
        giftShareResponse.setJoiners(joiners);
        //能否开红包
        Boolean redPacketClickAble = bargainJoinerHelpService.isBargainCanHelp(bargainOrder,user);
        giftShareResponse.setRedPacketClickAble(redPacketClickAble);
        //giftShareResponse.setCountJoiners(joiners.size());
        //砍价金额
        //giftShareResponse.setCutPrice(bargainOrderService.calCutPrice(bargainOrder));
        //giftShareResponse.setJoiners_sort_by_date(bargainJoinerHelpService.getJoinersByDate(bargainOrder));
        //List<BargainJoiner> joiners7_11 = bargainJoinerHelpService.getJoinersByDate(bargainOrder).subList(6,11);
        //Collections.reverse(joiners7_11);
        //giftShareResponse.setJoiners7_11(joiners7_11);
        //giftShareResponse.setTitle(joiners.size() + "人参与帮助");
        //giftShareResponse.setFinishUrl(host+"/bargains/"+bargainOrderNo+"/finish");
        //WxJsapiSignature config = getWxJsApi();
        //giftShareResponse.setJsApi(config);
        //giftShareResponse.setShareUrl(host + "/bargains/" + bargainOrderNo + "/share");
        giftShareResponse.setProductImageUrl(productService.findProductByProductId(bargainOrder.getProductId()).url());
        log.info("{}:福袋抓取/打开,页面-数据:{}",DateUtils.getStringDate(),giftShareResponse);
        return ResultResponseUtil.success(giftShareResponse);
    }


    @ApiOperation("福袋抓取/打开,分享页面-数据")
    @GetMapping("/{bargainOrderNo}/share")
    public ResultResponse shareBargainOrder(@PathVariable String bargainOrderNo) {
        String unionId = getCurrentUnionId();
        if (StringUtils.isEmpty(unionId)){
            return ResultResponseUtil.error(ResultEnum.OBJECT_ERROR.getCode(),"获取用户unionId失败");
        }
        GiftShareResponse giftShareResponse = new GiftShareResponse();
        User user = userService.findByUnionId(unionId);
        BargainOrder bargainOrder = bargainOrderService.findOrderByOrderNo(bargainOrderNo);
        //发起者,本人页面
        if (bargainJoinerHelpService.isSelf(bargainOrder,user)){
            return ResultResponseUtil.error(ResultEnum.PARAM_ERROR.getCode(),host + "/bargains/" + bargainOrder.getOrderNo()+"/show");
        }
        //giftShareResponse.setIsSubsrible(user.getIsSubscribe());
      /*  String nick_name = null;
        try {
            nick_name = URLDecoder.decode(user.getNickName(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            log.error("{}:nickname编码错误:{}", DateUtils.getStringDate(),e.getMessage());
        }*/
        user.setNickName(user.getNickName());
        giftShareResponse.setUser(user);
        giftShareResponse.setBargainOrder(bargainOrder);
        giftShareResponse.setExpiredAt(String.valueOf(bargainOrder.getExpiredAt().getTime()));
        /*try {
            String nickName = URLDecoder.decode(userRepository.getOne(bargainOrder.getUserId()).getNickName(), "utf-8");
            giftShareResponse.setName(nickName);
        } catch (UnsupportedEncodingException e) {
            log.error("{}:nickname编码错误:{}", DateUtils.getStringDate(),e.getMessage());
        }*/
        giftShareResponse.setBargainStatus(bargainOrder.getStatus().toString());
        Product product = productService.findProductByProductId(bargainOrder.getProductId());
        ProductFormat format = productService.findProductFormatById(bargainOrder.getFormatId());
        giftShareResponse.setProduct(product);
        giftShareResponse.setSell_price(format.getSellPrice());
        //giftShareResponse.setQr_code(bargainOrder.getQrCodeUrl());
        //Boolean isCutPrice = joinerService.isUserJoin(bargainOrder,user);
        //giftShareResponse.setIsCutPrice(isCutPrice);
        //giftShareResponse.setHelpCutPriceUrl(host + "/bargains/" + bargainOrderNo +"/helpCutPrice");
        List<BargainJoiner> joiners = joinerService.getJoinersByBargainOrder(bargainOrder);
        giftShareResponse.setJoiners(joiners);
        Boolean redPacketClickAble = bargainJoinerHelpService.isBargainCanHelp(bargainOrder,user);
        giftShareResponse.setRedPacketClickAble(redPacketClickAble);
        //giftShareResponse.setCountJoiners(joiners.size());
        //giftShareResponse.setCutPrice(bargainOrderService.calCutPrice(bargainOrder));
        //giftShareResponse.setJoiners_sort_by_date(bargainJoinerHelpService.getJoinersByDate(bargainOrder));
        //List<BargainJoiner> joiners7_11 = bargainJoinerHelpService.getJoinersByDate(bargainOrder).subList(6,11);
        //Collections.reverse(joiners7_11);
        //giftShareResponse.setJoiners7_11(joiners7_11);
        //giftShareResponse.setTitle(joiners.size() + "人参与帮助");
        //WxJsapiSignature config = getWxJsApi();
        //giftShareResponse.setJsApi(config);
        //giftShareResponse.setShareUrl(host + "/bargains/" + bargainOrderNo + "/share");
        //giftShareResponse.setDaoYinUrl(host+"/welcome");
        giftShareResponse.setProductImageUrl(productService.findProductByProductId(bargainOrder.getProductId()).url());
        //是否是最大砍价人数
        giftShareResponse.setIsMaxHelpCount(bargainJoinerHelpService.isMaxHelpCount(bargainOrder));
        log.info("{}:福袋抓取/打开,页面-数据:{}",DateUtils.getStringDate(),giftShareResponse);
        return ResultResponseUtil.success(giftShareResponse);
    }

    @ApiOperation("结束分享")
    @GetMapping("/{bargainOrderNo}/finish")
    public ResultResponse finishBargainOrder(@PathVariable String bargainOrderNo) {
        Order order = bargainOrderService.finishBargainOrder(bargainOrderNo);
        com.daoyintech.daoyin_release.response.RequestParam requestParam = new com.daoyintech.daoyin_release.response.RequestParam();
        requestParam.setOrderNo(order.getOrderNo());
        return ResultResponseUtil.success(requestParam);
    }




    /**
     * 帮忙打开福袋  释放吧小程序规则
     * */
    @ApiOperation("帮忙打开福袋")
    @GetMapping("/{bargainOrderNo}/helpCutPrice")
    public ResultResponse helpDraw(@PathVariable(name = "bargainOrderNo") String bargainOrderNo){
        String unionId = getCurrentUnionId();
        if (StringUtils.isEmpty(unionId)){
            return ResultResponseUtil.error(ResultEnum.OBJECT_ERROR.getCode(),"获取用户unionId失败");
        }
        User user = userService.findByUnionId(unionId);
        BargainOrder bargainOrder = bargainOrderService.findOrderByOrderNo(bargainOrderNo);

        userRelationShipAppletService.createRelationShipAndProfitApplet(user.getId(),bargainOrder.getUserId());

        //判断是否符合抽奖条件
        int falseType = bargainJoinerHelpService.checkisCanCatch(bargainOrder,user);
        if (falseType != 0) {
            String msg = null;
            switch (falseType) {
                case 1:
                    msg = "未登录，请重新进入小程序";
                    break;
                case 2:
                    msg = "福袋已经抓满，装不下啦！";
                    break;
                case 3:
                    msg = "您已经抓过了哟";
                    break;
            }
            return ResultResponseUtil.error(falseType,msg);
        }
        //抽奖 创建抽奖记录
        BargainJoiner bargainJoiner = helpService.bargainDrawHelp(bargainOrder,user);
        RedPacketMsgResponse redPacketMsgResponse = RedPacketMsgResponse.buildRedPacketMafResponse(bargainJoiner);
        return ResultResponseUtil.success(redPacketMsgResponse);
    }














/*
    */
/**
     * 帮忙打开福袋  释放吧公众号规则
     * *//*

    @ApiOperation("帮忙打开福袋")
    @GetMapping("/{bargainOrderNo}/helpDraw")
    public ResultResponse helpCutPrice(@PathVariable(name = "bargainOrderNo") String bargainOrderNo){
        User user = userService.findByUnionId(getCurrentUnionId());
        BargainOrder bargainOrder = bargainOrderService.findOrderByOrderNo(bargainOrderNo);
        //挂级
        userRelationshipService.createBargainRelationship(user,bargainOrder.getUserId());

        int falseType = bargainJoinerHelpService.checkisCanCatch(bargainOrder,user);
        if (falseType != 0) {
            String msg;
            switch (falseType) {
                case 1:
                    msg = "未登录，请重新进入商城";
                    break;
                case 2:
                    msg = "福袋已经抓满，装不下啦！";
                    break;
                case 3:
                    msg = "您已经抓过了哟";
                    break;
                case 4:
                    msg = "请关注后再抓取";
                    break;
                default:
                    msg = "机器故障，请再次尝试";
            }
            return ResultResponseUtil.error(falseType,msg);
        }
        BargainJoiner bargainJoiner = helpService.bargainHelp(bargainOrder,user);
        RedPacketMsgResponse redPacketMsgResponse = RedPacketMsgResponse.buildRedPacketMafResponse(bargainJoiner);
        return ResultResponseUtil.success(redPacketMsgResponse);
    }
*/






}





















