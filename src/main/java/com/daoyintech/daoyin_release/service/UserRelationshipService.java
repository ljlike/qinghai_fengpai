package com.daoyintech.daoyin_release.service;

import com.daoyintech.daoyin_release.entity.user.User;

public interface UserRelationshipService {


    //todo 砍价用户合伙人关系及分润建立
    void createBargainRelationship(User child, Long parentId);

    //todo 二维码用户合伙人关系及分润建立
    void createRelationship(User child, Long parentId);
}
