package com.daoyintech.daoyin_release.service.impl;

import com.daoyintech.daoyin_release.entity.user.User;
import com.daoyintech.daoyin_release.entity.user.UserRelationship;
import com.daoyintech.daoyin_release.enums.user.RelationshipType;
import com.daoyintech.daoyin_release.enums.user.UserProfitType;
import com.daoyintech.daoyin_release.repository.user.UserRelationshipRepository;
import com.daoyintech.daoyin_release.service.UserProfitService;
import com.daoyintech.daoyin_release.service.UserRelationshipService;
import com.daoyintech.daoyin_release.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRelationshipServiceImpl implements UserRelationshipService {

    @Autowired
    private UserRelationshipRepository relationshipRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserProfitService userProfitService;

    //todo 砍价用户合伙人关系及分润建立
    @Override
    public void createBargainRelationship(User child, Long parentId) {
        createRelationship(RelationshipType.商品小程序砍价, child, parentId);
    }

    //todo 二维码用户合伙人关系及分润建立
    @Override
    public void createRelationship(User child, Long parentId) {
        createRelationship(RelationshipType.二维码, child, parentId);
    }

    private void createRelationship(RelationshipType type, User child, Long parentId) {
        UserRelationship relationship = relationshipRepository.findByChildUserId(child.getId());
        if (relationship == null){
            relationship = new UserRelationship();
            relationship.setRelationshipType(type);
            relationship.setParentId(parentId);
            relationship.setChildUserId(child.getId());
            relationshipRepository.save(relationship);
            checkProfitOrCreateProfit(parentId, child);
        }
    }

    private  void checkProfitOrCreateProfit(Long parentId, User child) {
        User parent = userService.findIsAgent(parentId);
        if(parent != null) {
            userProfitService.createProfitUser(child, parent, UserProfitType.direct);
            userProfitService.createProfitUserByGrandFather(child, parent,UserProfitType.indirect);
        } else {
            parent = userService.findById(parentId);
            if (parent != null) {
                try {
                    userProfitService.createProfitUserByGrandFather(child, parent,UserProfitType.direct);
                }catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
