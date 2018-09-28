package com.daoyintech.daoyin_release.service.impl.user;

import com.daoyintech.daoyin_release.entity.user.User;
import com.daoyintech.daoyin_release.entity.user.UserProfit;
import com.daoyintech.daoyin_release.enums.user.UserProfitType;
import com.daoyintech.daoyin_release.response.agent.AgentRankResponse;
import com.daoyintech.daoyin_release.service.user.UserAgentLevelService;
import com.daoyintech.daoyin_release.service.user.UserProfitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author pei on 2018/08/21
 */
@Service
public class UserAgentLevelServiceImpl implements UserAgentLevelService {

    @Autowired
    private UserProfitService userProfitService;

    @Value("${integral.direct_profit_percent}")
    private BigDecimal direct_profit_percent;
    @Value("${integral.indirect_profit_percent}")
    private BigDecimal indirect_profit_percent;
    @Value("${integral.team_person}")
    private int teamPerson;

    @Override
    public AgentRankResponse buildAgentRankResponse(User user) {
        BigDecimal personagePercent = direct_profit_percent;
        BigDecimal teamPercent = indirect_profit_percent;
        List<UserProfit> userProfitList = userProfitService.findByToUserIdAndUserProfitTypeExceptMyself(user.getId(), UserProfitType.direct);
        List<UserProfit> userProfits = userProfitService.directPartners(userProfitList);
        if (userProfits.size() < teamPerson){
            teamPercent = new BigDecimal(0);
        }
        return AgentRankResponse.buildResponse(userProfits.size(),personagePercent,teamPercent);
    }
}
