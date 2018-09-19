package com.daoyintech.daoyin_release.controller.customer.user;

import com.daoyintech.daoyin_release.entity.user.User;
import com.daoyintech.daoyin_release.enums.ResultEnum;
import com.daoyintech.daoyin_release.response.ResultResponse;
import com.daoyintech.daoyin_release.response.agent.AgentRankResponse;
import com.daoyintech.daoyin_release.service.UserAgentLevelService;
import com.daoyintech.daoyin_release.service.UserService;
import com.daoyintech.daoyin_release.utils.ResultResponseUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author pei on 2018/08/21
 */
@RestController
@RequestMapping("/userAgentLevel")
public class UserAgentLevelController extends BaseUserController{

    @Autowired
    private UserAgentLevelService userAgentLevelService;

    @Autowired
    private UserService userService;

    /**
     *获取合伙人信息
     * */
    @ApiOperation("获取合伙人信息,团队,系数,提成")
    @RequestMapping("/agentRankInfo")
    public ResultResponse findAgentRankingInfo(){
        String unionId = getCurrentUnionId();
        if (StringUtils.isEmpty(unionId)){
            return ResultResponseUtil.error(ResultEnum.OBJECT_ERROR.getCode(),"获取用户unionId失败");
        }
        User user = userService.findByUnionId(unionId);
        AgentRankResponse agentRankResponse = userAgentLevelService.buildAgentRankResponse(user);
        return ResultResponseUtil.success(agentRankResponse);
    }

}









