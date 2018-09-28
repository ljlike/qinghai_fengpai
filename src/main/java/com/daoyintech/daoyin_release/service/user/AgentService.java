package com.daoyintech.daoyin_release.service.user;

import com.daoyintech.daoyin_release.entity.agent.AgentBean;
import com.daoyintech.daoyin_release.entity.user.User;
import com.daoyintech.daoyin_release.enums.agent.ApplyStatus; /**
 * @author pei on 2018/08/22
 */
public interface AgentService {

    AgentBean findByUserIdAndIsSuccess(User user, ApplyStatus status);


}
