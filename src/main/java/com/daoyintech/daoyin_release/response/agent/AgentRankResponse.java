package com.daoyintech.daoyin_release.response.agent;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author pei on 2018/08/21
 */
@Data
public class AgentRankResponse {

    private Integer agentNum;

    private BigDecimal personagePercent;

    private BigDecimal teamPercent;

    public static AgentRankResponse buildResponse(Integer agentNum,BigDecimal personagePercent,BigDecimal teamPercent){
        AgentRankResponse agentRankResponse = new AgentRankResponse();
        agentRankResponse.setAgentNum(agentNum);
        agentRankResponse.setPersonagePercent(personagePercent);
        agentRankResponse.setTeamPercent(teamPercent);
        return agentRankResponse;
    }



}
