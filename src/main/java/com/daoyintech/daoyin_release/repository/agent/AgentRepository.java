package com.daoyintech.daoyin_release.repository.agent;

import com.daoyintech.daoyin_release.entity.agent.AgentBean;
import com.daoyintech.daoyin_release.entity.user.User;
import com.daoyintech.daoyin_release.enums.agent.ApplyStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2017/6/19.
 */
@Repository
public interface AgentRepository extends CrudRepository<AgentBean,Long> {
    AgentBean findByUserId(Long id);

    AgentBean findByUserIdAndIsSuccess(Long userId, ApplyStatus isSuccess);

    List<AgentBean> findByIsSuccess(ApplyStatus isSuccess);
    List<AgentBean> findByIsSuccess(ApplyStatus isSuccess, Pageable pageable);


}
