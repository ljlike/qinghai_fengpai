package com.daoyintech.daoyin_release.controller.customer.user;

import com.daoyintech.daoyin_release.entity.user.User;
import com.daoyintech.daoyin_release.entity.user.UserProfit;
import com.daoyintech.daoyin_release.entity.user.UserProfitApplet;
import com.daoyintech.daoyin_release.enums.ResultEnum;
import com.daoyintech.daoyin_release.enums.user.UserProfitType;
import com.daoyintech.daoyin_release.repository.user.UserProfitRepository;
import com.daoyintech.daoyin_release.repository.user.UserRepository;
import com.daoyintech.daoyin_release.response.ResultResponse;
import com.daoyintech.daoyin_release.response.member.*;
import com.daoyintech.daoyin_release.service.UserProfitAppletService;
import com.daoyintech.daoyin_release.service.UserProfitService;
import com.daoyintech.daoyin_release.service.UserService;
import com.daoyintech.daoyin_release.utils.DateUtils;
import com.daoyintech.daoyin_release.utils.ResultResponseUtil;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yaml.snakeyaml.constructor.BaseConstructor;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.List;

/**
 * @author pei on 2018/08/23
 */
@Slf4j
@RestController
@RequestMapping("/profit")
public class UserProfitController extends BaseUserController{

    @Autowired
    private UserProfitService userProfitService;

    @Autowired
    private UserProfitRepository userProfitRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfitAppletService userProfitAppletService;


/*
    @ApiOperation("我的合伙人")
    @GetMapping("/members")
    public ResultResponse getMembers() {
        User user = userService.findByUnionId(getCurrentUnionId());
        List<UserProfit> directProfits = userProfitService.findByToUserIdAndUserProfitTypeExceptMyself(user.getId(), UserProfitType.direct);
        List<MemberResponse> partnersResponses = userProfitService.getMembers(userProfitService.directPartners(directProfits));
        List<MemberResponse> membersResponses = userProfitService.getMembers(userProfitService.directMembers(directProfits));

        ResponseMember partnersResponseMember = new ResponseMember();
        partnersResponseMember.setCount(partnersResponses.size());
        partnersResponseMember.setList(partnersResponses);

        ResponseMember responseMembers = new ResponseMember();
        responseMembers.setCount(membersResponses.size());
        responseMembers.setList(membersResponses);

        MembersResponse membersResponse = new MembersResponse();
        membersResponse.setMembers(responseMembers);
        membersResponse.setPartners(partnersResponseMember);
        return ResultResponseUtil.success(membersResponse);
    }
*/



    @ApiOperation("我的合伙人")
    @GetMapping("/members")
    public ResultResponse getMembers() {
        String unionId = getCurrentUnionId();
        if (StringUtils.isEmpty(unionId)){
            return ResultResponseUtil.error(ResultEnum.OBJECT_ERROR.getCode(),"获取用户unionId失败");
        }
        User user = userService.findByUnionId(unionId);
        List<UserProfitApplet> directProfits = userProfitAppletService.findByToUserIdAndIsProfit(user.getId(), Boolean.TRUE);
        List<MemberResponse> partnersResponses = userProfitAppletService.userProfitAppletToMemberResponse(directProfits);
        ResponseMember partnersResponseMember = new ResponseMember();
        partnersResponseMember.setCount(partnersResponses.size());
        partnersResponseMember.setList(partnersResponses);
        MembersResponse membersResponse = new MembersResponse();
        membersResponse.setPartners(partnersResponseMember);
        return ResultResponseUtil.success(membersResponse);
    }



/*
    @ApiOperation("单个合伙人详情")
    @GetMapping("/members/{memberId}")
    public ResultResponse getMember(@PathVariable("memberId") Long memberId) {
        UserProfit profit = userProfitRepository.getOne(memberId);
        User fromUser = userRepository.getOne(profit.getFromUserId());

        MemberDetailResponse memberDetailResponse = new MemberDetailResponse();
        memberDetailResponse.setAvatar(fromUser.getAvatar());
        memberDetailResponse.setName(fromUser.getNickName());
        memberDetailResponse.setSalesAmount(userProfitService.calSalesAmount(fromUser,profit));
        BigDecimal integral = userProfitService.calIntegral(fromUser,profit);
        if(integral.doubleValue() == 0.00){
            memberDetailResponse.setIntegral(new BigDecimal(0));
        }else {
            memberDetailResponse.setIntegral(integral);
        }
        */
/*String nickname = null;
        try {
            nickname = URLDecoder.decode(fromUser.getNickName(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            log.error("{}:nickname编码错误:{}", DateUtils.getStringDate(),e.getMessage());
        }*//*

        List<UserProfit> directProfitList = userProfitService.findByToUserIdAndUserProfitTypeExceptMyself(fromUser.getId(), UserProfitType.direct);
        List<UserProfit> directProfits = userProfitService.directPartners(directProfitList);
        memberDetailResponse.setPartnersCount(directProfits.size());
        memberDetailResponse.setShoppingRecord(Lists.transform(directProfits, directProfit -> {
                User subFromUser = userRepository.getOne(directProfit.getFromUserId());
                MemberItemResponse itemResponse = new MemberItemResponse();
                itemResponse.setName(subFromUser.getNickName());
                itemResponse.setAvatar(subFromUser.getAvatar());
                itemResponse.setId(directProfit.getId());
            */
/*try {
                itemResponse.setName(URLDecoder.decode(subFromUser.getNickName(), "utf-8"));
            } catch (UnsupportedEncodingException e) {
                log.error("{}:nickname编码错误:{}", DateUtils.getStringDate(),e.getMessage());
            }*//*

                List<UserProfit> subUserProfitList  = userProfitService.findByToUserId(subFromUser.getId());
                List<UserProfit> subUserProfits = userProfitService.removeNotAgent(subUserProfitList);
                List<UserProfit> userProfits = userProfitService.directPartners(subUserProfits);
                if (userProfits == null || userProfits.size() == 0){
                    itemResponse.setSubPartnersCount(0);
                }else{
                    itemResponse.setSubPartnersCount(userProfits.size());
                }
                BigDecimal decimal = userProfitService.calSalesAmount(subFromUser, directProfit);
                itemResponse.setSalesAmount(decimal);

                BigDecimal subIntegral = userProfitService.calIntegral(subFromUser,directProfit);
                if (subIntegral.doubleValue() == 0.00){
                    itemResponse.setSubIntegral(new BigDecimal(0));
                }else {
                    itemResponse.setSubIntegral(subIntegral);
                }
                UserProfit subProfit = userProfitService.findByToUserIdAndFromUserId(profit.getToUserId(),subFromUser.getId());
                BigDecimal integralForMe = new BigDecimal(0);
                if (subProfit != null){
                    integralForMe = userProfitService.calIntegral(subFromUser,subProfit);
                }
                itemResponse.setIntegral(integralForMe);
                return itemResponse;
            }));
        return ResultResponseUtil.success(memberDetailResponse);
    }

*/



}











