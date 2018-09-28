package com.daoyintech.daoyin_release.service.impl.user;

import com.daoyintech.daoyin_release.entity.agent.AgentBean;
import com.daoyintech.daoyin_release.entity.user.User;
import com.daoyintech.daoyin_release.enums.agent.ApplyStatus;
import com.daoyintech.daoyin_release.repository.agent.AgentRepository;
import com.daoyintech.daoyin_release.service.user.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AgentServiceImpl implements AgentService {

    @Autowired
    private AgentRepository agentRepository;

    @Override
    public AgentBean findByUserIdAndIsSuccess(User user, ApplyStatus isSuccess) {
        AgentBean agent = agentRepository.findByUserIdAndIsSuccess(user.getId(),isSuccess);
        return agent;
    }

}

