package com.daoyintech.daoyin_release.service.impl.user;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.daoyintech.daoyin_release.entity.order.Order;
import com.daoyintech.daoyin_release.entity.order.bargain.BargainJoiner;
import com.daoyintech.daoyin_release.entity.order.bargain.BargainOrder;
import com.daoyintech.daoyin_release.entity.product.Product;
import com.daoyintech.daoyin_release.entity.user.User;
import com.daoyintech.daoyin_release.entity.user.integral.UserIntegral;
import com.daoyintech.daoyin_release.entity.user.integral.UserIntegralDetail;
import com.daoyintech.daoyin_release.enums.Integral.IntegralConstant;
import com.daoyintech.daoyin_release.enums.bargain.BargainHelpType;
import com.daoyintech.daoyin_release.enums.bargain.BargainOrderStatus;
import com.daoyintech.daoyin_release.enums.order.OrderType;
import com.daoyintech.daoyin_release.enums.user.UserIntegralFromType;
import com.daoyintech.daoyin_release.repository.order.BargainJoinerRepository;
import com.daoyintech.daoyin_release.repository.order.BargainOrderRepository;
import com.daoyintech.daoyin_release.repository.product.ProductRepository;
import com.daoyintech.daoyin_release.repository.user.UserIntegralDetailRepository;
import com.daoyintech.daoyin_release.repository.user.UserIntegralRepository;
import com.daoyintech.daoyin_release.repository.user.UserRepository;
import com.daoyintech.daoyin_release.response.ResultResponse;
import com.daoyintech.daoyin_release.response.draw.DrawRecordResponse;
import com.daoyintech.daoyin_release.response.draw.JoinerUserResponse;
import com.daoyintech.daoyin_release.service.*;
import com.daoyintech.daoyin_release.service.order.bargain.BargainJoinerService;
import com.daoyintech.daoyin_release.service.order.bargain.BargainOrderService;
import com.daoyintech.daoyin_release.service.user.UserIntegralDetailService;
import com.daoyintech.daoyin_release.service.user.UserIntegralService;
import com.daoyintech.daoyin_release.utils.DateUtils;
import com.daoyintech.daoyin_release.utils.ResultResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author pei on 2018/08/09
 */
@Slf4j
@Service
public class UserIntegralServiceImpl implements UserIntegralService {

    @Autowired
    private UserIntegralRepository userIntegralRepository;

    @Autowired
    private BargainOrderRepository bargainOrderRepository;

    @Autowired
    private BargainJoinerRepository bargainJoinerRepository;

    @Autowired
    private UserIntegralDetailService userIntegralDetailService;

    @Autowired
    private UserIntegralDetailRepository userIntegralDetailRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private DrawService drawService;

    @Value("${pay.freight_price}")
    private BigDecimal freightPrice;

    @Value("${integral.integral_money_percent}")
    private BigDecimal integralMoneyPercent;

    @Value("${bargain_help.draw_product_total_percent}")
    private BigDecimal drawProductTotalPercent;

    @Value("${bargain_help.draw_product_second_percent}")
    private BigDecimal drawProductSecondPercent;

    @Value("${qiniu.domain}")
    private String domain;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    private Random random = new Random();

    @Override
    public UserIntegral findByUserId(Long userId) {
        return userIntegralRepository.findByUserId(userId);
    }

    @Override
    public UserIntegral findByUser(User user) {
        UserIntegral integral = userIntegralRepository.findByUserId(user.getId());
        if (StringUtils.isEmpty(integral)){
            UserIntegral userIntegral = new UserIntegral();
            userIntegral.setUserId(user.getId());
            userIntegral.setIntegral(new BigDecimal(0));
            UserIntegral integral1 = userIntegralRepository.save(userIntegral);
            return integral1;
        }
        return integral;
    }

    @Override
    public UserIntegral save(UserIntegral userIntegral) {
        return userIntegralRepository.save(userIntegral);
    }

    //    用户减少积分
    public UserIntegral updateUserPoint(BigDecimal point, User user) {
        UserIntegral integral = findByUser(user);
        log.info("{}:用户积分变动:变化用户 id = {} 原本积分 integral = {}",DateUtils.getStringDate(),user.getId(),integral.getIntegral());
        integral.setIntegral(integral.getIntegral().add(point));
        UserIntegral integral1 = userIntegralRepository.save(integral);
        log.info("{}:用户积分变动:积分变动 = {} : 变化后的积分 = {}", DateUtils.getStringDate(),point,integral1.getIntegral());
        return integral;
    }

    /**
     * 新规抽奖算法
     * */
    @Async
    @Override
    public synchronized void userIntegralDraw(Order order) {
        if (order.getOrderType().equals(OrderType.砍价订单)) {
            BargainOrder bargainOrder = bargainOrderRepository.findByOrderId(order.getId());
            List<BargainJoiner> joiners = bargainJoinerRepository.findByBargainOrderId(bargainOrder.getId());
            for (BargainJoiner joiner : joiners){
                //String token = bargainOrder.getOrderNo()+"_"+joiner.getJoinerId();
                User joinerUser = userRepository.getOne(joiner.getJoinerId());

                //新规则积分抽奖
                Integer userInteger = drawService.drawJackpotIntegral();

                log.info("{}:用户抽取的积分:{}:{}",DateUtils.getStringDate(),joinerUser.getNickName(),userInteger);
                //每个参与者获得积分
                updateUserPoint(new BigDecimal(userInteger),joinerUser);
                userIntegralDetailService.buildUserIntegralDetail(order, joinerUser,new BigDecimal(userInteger), UserIntegralFromType.帮助朋友);
                //积分卷
                if (BargainHelpType.getType(3).equals(joiner.getType())){
                    User orderUser = userRepository.getOne(order.getUserId());

                    Integer integer = drawService.drawJackpotIntegral();

                    updateUserPoint(new BigDecimal(integer),orderUser);
                    userIntegralDetailService.buildUserIntegralDetail(order, orderUser,new BigDecimal(integer), UserIntegralFromType.积分卷);
                }
            }
        }
    }


    @Override
    public ResultResponse findAllMyHelp(User user) {
        List<DrawRecordResponse> recordResponseList = new ArrayList<>();
        //参与的所有订单
        List<BargainJoiner> myJoiners = bargainJoinerRepository.findByJoinerIdOrderByCreatedAtDesc(user.getId());
        for (BargainJoiner bargainJoiner : myJoiners) {
            //分享订单
            BargainOrder bargainOrder = bargainOrderRepository.findById(bargainJoiner.getBargainOrderId()).orElse(null);
            if (StringUtils.isEmpty(bargainOrder) || !bargainOrder.getStatus().equals(BargainOrderStatus.完成)){
                continue;
            }
            DrawRecordResponse drawRecordResponse = new DrawRecordResponse();
            //查询买家信息
            User buyUser = userRepository.getOne(bargainOrder.getUserId());
            drawRecordResponse.setUserName(buyUser.getNickName());
            drawRecordResponse.setUserHeadUrl(buyUser.getAvatar());
            //单个订单所有参与者
            List<BargainJoiner> joinerList = bargainJoinerRepository
                    .findByBargainOrderId(bargainOrder.getId());
            List<JoinerUserResponse> joinerUserResponseList = new ArrayList<>();

            if (!StringUtils.isEmpty(joinerList) && joinerList.size() != 0){
                for (BargainJoiner joiner : joinerList) {
                    JoinerUserResponse joinerUserResponse = new JoinerUserResponse();
                    if (!StringUtils.isEmpty(joiner.getType())){
                        if (joiner.getType().equals(BargainHelpType.getType(4)) ||
                            joiner.getType().equals(BargainHelpType.getType(5)) ||
                            joiner.getType().equals(BargainHelpType.getType(6))){
                            joinerUserResponse.setType(BargainHelpType.线下活动卷);
                        }else{
                            joinerUserResponse.setType(joiner.getType());
                        }
                    }else{
                        continue;
                    }
                    joinerUserResponse.setUserHeadUrl(joiner.getAvatar());
                    joinerUserResponse.setUserName(joiner.getNickName());
                    //用户抽中的积分
                    List<UserIntegralDetail> userIntegralDetails = userIntegralDetailRepository
                            .findByUserIntegralFromTypeAndOrderIdAndUserId(UserIntegralFromType.帮助朋友,bargainOrder.getOrderId(), joiner.getJoinerId());
                    //判断是否抽中积分卷
                    if (joiner.getType().equals(BargainHelpType.getType(3))){
                        List<UserIntegralDetail> detailList = userIntegralDetailRepository
                                .findByUserIntegralFromTypeAndOrderIdAndUserId(UserIntegralFromType.积分卷,
                                        bargainOrder.getOrderId(), buyUser.getId());
                        if (StringUtils.isEmpty(detailList) || detailList.size() == 0){
                            if (!StringUtils.isEmpty(userIntegralDetails) && userIntegralDetails.size() != 0){
                                drawRecordResponse.setCardIntegral(userIntegralDetails.get(userIntegralDetails.size() -1).getIntegral());
                            }else{
                                drawRecordResponse.setCardIntegral(new BigDecimal(0));
                            }
                        }else{
                            drawRecordResponse.setCardIntegral(detailList.get(0).getIntegral());
                        }
                    }else if (StringUtils.isEmpty(drawRecordResponse.getCardIntegral())){
                        drawRecordResponse.setCardIntegral(new BigDecimal(0));
                    }
                    //参与者获得的积分
                    if (!StringUtils.isEmpty(userIntegralDetails) && userIntegralDetails.size() != 0){
                        joinerUserResponse.setDrawIntegral(userIntegralDetails.get(0).getIntegral());
                    }else{
                        joinerUserResponse.setDrawIntegral(new BigDecimal(0));
                    }
                    joinerUserResponseList.add(joinerUserResponse);
                }
            }
            drawRecordResponse.setJoinerUserResponseList(joinerUserResponseList);
            drawRecordResponse.setPayMoney(bargainOrder.getPrice());
            Product product = productRepository.getOne(bargainOrder.getProductId());
            drawRecordResponse.setProductName(product.getName());
            drawRecordResponse.setProductUrl(domain+"/"+product.getIconKey());
            recordResponseList.add(drawRecordResponse);
        }
        return ResultResponseUtil.success(recordResponseList);
    }


















    /**
     * 随机积分规则算法
     * */
    private Integer randomDrawIntegralType(String orderNo){
        //查询redis参与者已抽中的积分规则
        List<Integer> existType = redisIntegralList(orderNo);
        //1 奖池 1份
        //2 商品总价20%的80% 1份
        //3,4 商品总价20%的20% 2份
        List<Integer> drawType = new ArrayList<Integer>(){{
            add(1);
            add(2);
            add(3);
            add(4);
        }};
        drawType.removeAll(existType);
        //随机索引
        int typesIndex = Math.abs(random.nextInt() % drawType.size());
        Integer type = drawType.get(typesIndex);
        existType.add(type);
        //存redis,已随机到的积分规则类型
        if (existType.size() >= 4){
            redisTemplate.opsForValue().set(orderNo,drawType.toString(),2, TimeUnit.HOURS);
        }else{
            redisTemplate.opsForValue().set(orderNo,existType.toString(),2, TimeUnit.HOURS);
        }
        return type;
    }
    /**
     * 获取redis中已经抽到的积分规则
     * */
    private List<Integer> redisIntegralList(String orderNo){
        //查询redis参与者已抽中的积分规则
        List<Integer> existType = new ArrayList<>();
        String existInteger = redisTemplate.opsForValue().get(orderNo);
        JSONArray array = JSON.parseArray(existInteger);
        if (!StringUtils.isEmpty(array)){
            for (int i =0 ; i < array.size() ; i++){
                Integer integer = array.getInteger(i);
                existType.add(integer);
            }
        }
        return existType;
    }
    /**
     *计算随机金额积分
     * 获取随机积分
     * */
    private Integer randomDrawPrice(Integer type,BigDecimal money,String orderNo){
        //取初步随机比例
        String drawScale = redisTemplate.opsForValue().get(orderNo + "_" + IntegralConstant.BARGAINORDER_PRODUCT_SCALE);
        BigDecimal drawScaleBigD = null;
        if (StringUtils.isEmpty(drawScale)){
            //初步随机比例 10%~20%
            drawScaleBigD = randomPriceDraw(10);
            redisTemplate.opsForValue().set(orderNo + "_" + IntegralConstant.BARGAINORDER_PRODUCT_SCALE,drawScaleBigD.toString(),2,TimeUnit.HOURS);
        }else {
            drawScaleBigD = new BigDecimal(drawScale);
        }
        Integer userIntegral = 0;
        switch (type){
            case 1:  //奖池
                String strIntegralTotal = redisTemplate.opsForValue().get(IntegralConstant.JACKPOT_DRAW_INTEGRAL);
                //平台/辛运奖
                Integer potType = randomJackPotType();
                //奖池积分具体区间随机积分
                Integer potIntegral = randomJackPotIntegral(potType);
                if (potIntegral >= Integer.valueOf(strIntegralTotal)){
                    potIntegral = Integer.valueOf(strIntegralTotal);
                }
                if (potIntegral <= 200 && potIntegral > 100){
                    userIntegral = randomDrawStartEnd(100);
                }else if (potIntegral == 0){
                    //幸运大奖
                    int integra = random.nextInt(99) + 1;
                    int ratio = random.nextInt(19) + 1;
                    userIntegral = new BigDecimal(integra).multiply(new BigDecimal(ratio)).intValue();
                }else{
                    //平台将 0~100
                    userIntegral = randomDrawIntegral(potIntegral);
                }
                //TODO 新规1
                if (userIntegral >= Integer.valueOf(strIntegralTotal)){
                    userIntegral = Integer.valueOf(strIntegralTotal);
                }
                //剩余积分
                BigDecimal bigDecimal = new BigDecimal(strIntegralTotal).subtract(new BigDecimal(userIntegral));
                //存剩余积分
                redisTemplate.opsForValue().set(IntegralConstant.JACKPOT_DRAW_INTEGRAL,bigDecimal.toString());
                break;
            case 2:  //商品总价(20%)改成随机的80% 1份
                //10%~20%
                BigDecimal price = money.multiply(drawScaleBigD).multiply(drawProductSecondPercent).setScale(2, BigDecimal.ROUND_HALF_UP);
                //BigDecimal price = money.multiply(drawProductTotalPercent).multiply(drawProductSecondPercent).setScale(2, BigDecimal.ROUND_HALF_UP);
                BigDecimal integral = price.divide(integralMoneyPercent, 2, BigDecimal.ROUND_HALF_UP);
                userIntegral = randomDrawIntegral(integral.intValue());
                break;
            case 3:  //商品总价(20%)改成随机的20% 1份
                List<Integer> ThreeExistType = redisIntegralList(orderNo);
                String threeJoinerIntegral = "0";
                if (ThreeExistType.contains(4)){
                    String token = orderNo + "_" + 4;
                    //另一个人抽取的积分
                    threeJoinerIntegral = redisTemplate.opsForValue().get(token);
                }
                //10%~20%
                BigDecimal threePrice = money.multiply(drawScaleBigD).multiply(drawProductTotalPercent).setScale(2,BigDecimal.ROUND_HALF_UP);
                //BigDecimal threePrice = money.multiply(drawProductTotalPercent).multiply(drawProductTotalPercent).setScale(2,BigDecimal.ROUND_HALF_UP);
                //商品可抽奖积分
                BigDecimal threeIntegral = threePrice.divide(integralMoneyPercent, 2, BigDecimal.ROUND_HALF_UP);
                //计算剩余的积分
                BigDecimal subtract = threeIntegral.subtract(new BigDecimal(threeJoinerIntegral));
                //随机出的积分
                userIntegral = randomDrawIntegral(subtract.intValue());
                //还可抽取的积分
                String token = orderNo + "_" + type;
                redisTemplate.opsForValue().set(token,userIntegral.toString(),2,TimeUnit.HOURS);
                break;
            case 4:  //商品总价(20%)改成随机的20% 1份
                //10%~20%
                List<Integer> FourExistType = redisIntegralList(orderNo);
                String FourJoinerIntegral = "0";
                if (FourExistType.contains(3)){
                    String threeToken = orderNo + "_" + 3;
                    //另一个人抽取的积分
                    FourJoinerIntegral = redisTemplate.opsForValue().get(threeToken);
                }

                BigDecimal fourPrice = money.multiply(drawScaleBigD).multiply(drawProductTotalPercent).setScale(2,BigDecimal.ROUND_HALF_UP);
                //BigDecimal fourPrice = money.multiply(drawProductTotalPercent).multiply(drawProductTotalPercent).setScale(2,BigDecimal.ROUND_HALF_UP);
                BigDecimal fourIntegral = fourPrice.divide(integralMoneyPercent, 2, BigDecimal.ROUND_HALF_UP);
                //计算剩余的积分
                BigDecimal decimal = fourIntegral.subtract(new BigDecimal(FourJoinerIntegral));
                //随机出的积分
                userIntegral = randomDrawIntegral(decimal.intValue());
                //还可抽取的积分
                String threeToken = orderNo + "_" + type;
                redisTemplate.opsForValue().set(threeToken,userIntegral.toString(),2,TimeUnit.HOURS);
                break;
        }
        return userIntegral;
    }
    /**
     *奖池分类算法 平台/辛运奖
     * */
    private Integer randomJackPotType(){
        //1平台奖 / 0~100
        //2幸运奖 / 100~200
        List<Integer> drawType = new ArrayList<Integer>(){{
            add(1);
            add(2);
        }};
        //随机索引
        int typesIndex = Math.abs(random.nextInt() % drawType.size());
        Integer type = drawType.get(typesIndex);
        return type;
    }
    /**
     *辛运奖 3类上线奖
     * */
    private Integer randomJackPotLuck(){
        //1  8888  6666
        //2  3333  1111
        //3  888  666  333
        List<Integer> drawType = new ArrayList<Integer>(){{
            add(1);
            add(2);
            add(3);
        }};
        //随机索引
        int typesIndex = Math.abs(random.nextInt() % drawType.size());
        Integer type = drawType.get(typesIndex);
        return type;
    }
    /**
     * 奖池积分具体区间随机积分
     * */
    private Integer randomJackPotIntegral(Integer type){
        //用来随机的积分
        Integer integral = 0;
        if (type == 1){
            //平台
            Integer potType = randomJackPotType();
            integral = potType == 1 ? 100 : 200;
        }else if (type == 2){
            //幸运奖
          /*  Integer potLuck = randomJackPotLuck();
            Integer luck = randomJackPotLuck();
            integral = potLuck == 1 ? (randomJackPotType() == 1 ? 8888 : 6666)
                    : potLuck == 2 ? (randomJackPotType() == 1 ? 3333 : 1111)
                    : luck == 1 ? 888 : luck == 2 ? 666 : 333;*/
        }
        return integral;
    }
    /**
     * 随机积分算法
     * */
    private Integer randomDrawIntegral(Integer integral){
        if (integral <= 0){
            return 1;
        }
        int anInt = random.nextInt(integral);
        if (anInt == 0){
            return 1;
        }
        return anInt;
    }
    /**
     * 随机积分算法
     * */
    private Integer randomDrawStartEnd(Integer start){
        if (start <= 0){
            return 1;
        }
        int anInt = random.nextInt(start+1)+start;
        if (anInt == 0){
            return 1;
        }
        return anInt;
    }
    /**
     * 随机区间计算积分
     * */
    private BigDecimal randomPriceDraw(Integer start){
        int anInt = random.nextInt(start+1)+start;
        return new BigDecimal(anInt).divide(new BigDecimal(100),2,BigDecimal.ROUND_HALF_UP);
    }


}













