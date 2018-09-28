package com.daoyintech.daoyin_release.service.impl.user;

import com.daoyintech.daoyin_release.entity.user.User;
import com.daoyintech.daoyin_release.entity.user.UserProfitApplet;
import com.daoyintech.daoyin_release.entity.user.UserRelationship;
import com.daoyintech.daoyin_release.enums.user.RelationshipType;
import com.daoyintech.daoyin_release.repository.user.UserProfitAppletRepository;
import com.daoyintech.daoyin_release.repository.user.UserRelationshipRepository;
import com.daoyintech.daoyin_release.repository.user.UserRepository;
import com.daoyintech.daoyin_release.service.user.UserRelationShipAppletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.List;

@Service
@Slf4j
public class UserRelationShipAppletServiceImpl implements UserRelationShipAppletService {

    @Autowired
    private UserRelationshipRepository relationshipRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfitAppletRepository profitAppletRepository;

    /**
     * 创建用户关系及分润关系
     *
     * @param childId  参与者ID
     * @param parentId 发起者ID
     */
    @Async
    @Override
    public void createRelationShipAndProfitApplet(Long childId, Long parentId) {
        createRelationShipAndProfitApplet(RelationshipType.红包, childId, parentId);
    }

    /**
     * 创建用户关系及分润关系
     *
     * @param type     关系媒介
     * @param childId  参与者ID
     * @param parentId 发起者D
     */
    private void createRelationShipAndProfitApplet(RelationshipType type, Long childId, Long parentId) {
        if (createRelationshipOrNot(childId, parentId)) {
            createRelationShip(type, childId, parentId);
            if (checkIsAgent(parentId)) {
                if (checkFansCount(parentId)) {
                    createProfitApplet(childId, parentId, true);
                } else {
                    createProfitApplet(childId, parentId, false);
                }
            } else {
                UserRelationship parentRelationship = relationshipRepository.findByChildUserId(parentId);
                if (parentRelationship != null) {
                    Long[] ids = disposeRelationLink(parentRelationship);
                    List<User> users = userRepository.findByIsAgentTrueAndIdIn(ids);
                    if (users != null && users.size() > 0) {
                        Long[] agentIds = findSuperiorAgentIds(users);
                        Long agentId = filtrateSuperiorAgentId(ids, agentIds);
                        if (checkFansCount(agentId)) {
                            createProfitApplet(childId, agentId, true);
                        } else {
                            createProfitApplet(childId, agentId, false);
                        }
                    }
                }
            }
        }
    }

    /**
     * 检查用户是否具备创建关系的条件
     *
     * @param childId  参与者ID
     * @param parentId 发起者ID
     * @return
     */
    private Boolean createRelationshipOrNot(Long childId, Long parentId) {
        //判断参与者是否创建过关系
        UserRelationship childRelationship = relationshipRepository.findByChildUserId(childId);
        return childRelationship == null && !childId.equals(parentId);
    }

    /**
     * 筛选出最近的上级合伙人
     *
     * @param ids      发起者的所有上级用户ID
     * @param agentIds 发起者的所有上级合伙人Id
     * @return
     */
    private Long filtrateSuperiorAgentId(Long[] ids, Long[] agentIds) {
        for (int i = ids.length - 1; i >= 0; i--) {
            for (int j = 0; j < agentIds.length; j++) {
                if (ids[i].equals(agentIds[j])) {
                    return agentIds[j];
                }
            }
        }
        return null;
    }

    /**
     * 找到发起者的所有上级合伙人Id
     *
     * @param users 发起者的所有上级合伙人
     * @return
     */
    private Long[] findSuperiorAgentIds(List<User> users) {
        Long[] agentIds = new Long[users.size()];
        for (int i = 0; i < users.size(); i++) {
            agentIds[i] = users.get(i).getId();
        }
        return agentIds;
    }


    /**
     * 创建用户关系
     *
     * @param type     关系媒介
     * @param childId  参与者ID
     * @param parentId 发起者D
     */
    private UserRelationship createRelationShip(RelationshipType type, Long childId, Long parentId) {
        UserRelationship relationship = new UserRelationship();
        relationship.setRelationshipType(type);
        relationship.setChildUserId(childId);
        relationship.setParentId(parentId);
        relationship.setRelationshipLink(addRelationLink(childId, parentId));
        return relationshipRepository.save(relationship);
    }

    /**
     * 创建分润关系
     *
     * @param childId  参与者ID
     * @param parentId 发起者ID
     * @return
     */
    private UserProfitApplet createProfitApplet(Long childId, Long parentId, Boolean isProfit) {
        UserProfitApplet applet = new UserProfitApplet();
        applet.setFromUserId(childId);
        applet.setToUserId(parentId);
        applet.setIsProfit(isProfit);
        return profitAppletRepository.save(applet);
    }

    /**
     * 处理关系链(排除自己后转换为Long数组)
     *
     * @param parentRelationship 关系链
     * @return
     */
    private Long[] disposeRelationLink(UserRelationship parentRelationship) {
        String relationLink = parentRelationship.getRelationshipLink();
        relationLink = relationLink.substring(0, relationLink.lastIndexOf("-"));
        String[] strIds = relationLink.split("-");
        Long[] ids = new Long[strIds.length];
        for (int i = 0; i < strIds.length; i++) {
            ids[i] = Long.valueOf(strIds[i]);
        }
        return ids;
    }

    private User getCurrentUserById(Long parentId) {
        return userRepository.findById(parentId).orElse(null);
    }


    /**
     * 判断用户购买的粉丝数是否够用
     *
     * @param parentId
     * @return
     */
    private boolean checkFansCount(Long parentId) {
        Integer fansCount = getCurrentUserById(parentId).getFansCount();
        Integer ownFans = profitAppletRepository.countByToUserId(parentId);
        return fansCount - ownFans > 0;
    }

    /**
     * 判断用户是否是合伙人
     *
     * @param parentId
     */
    private Boolean checkIsAgent(Long parentId) {
        User user = getCurrentUserById(parentId);
        if (user == null) {
            return false;
        }
        return user.getIsAgent();
    }

    /**
     * 获取发起者的关系链
     *
     * @param parentId 发起者ID
     * @return 关系链
     */
    private String getParentRelationLink(Long parentId) {
        UserRelationship relationship = relationshipRepository.findByChildUserId(parentId);
        if (relationship != null) {
            return relationship.getRelationshipLink();
        } else {
            return "";
        }
    }

    /**
     * 添加关系链
     *
     * @param childId  参与者ID
     * @param parentId 发起者ID
     * @return 新关系链
     */
    private String addRelationLink(Long childId, Long parentId) {
        String link = getParentRelationLink(parentId);
        if (StringUtils.isEmpty(link)) {
            return parentId + "-" + childId;
        } else {
            //判断参与者与创建者是否互绑
            UserRelationship parentRelationship = relationshipRepository.findByChildUserIdAndParentId(parentId, childId);
            if(parentRelationship!=null){
                return parentId + "-" + childId;
            }
            return link + "-" + childId;
        }
    }

}