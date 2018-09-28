package com.daoyintech.daoyin_release.service.user;

import com.daoyintech.daoyin_release.entity.user.User;
import com.daoyintech.daoyin_release.response.agent.AgentRankResponse;

/**
 * @author pei on 2018/08/21
 */
public interface UserAgentLevelService {


    AgentRankResponse buildAgentRankResponse(User user);


}
