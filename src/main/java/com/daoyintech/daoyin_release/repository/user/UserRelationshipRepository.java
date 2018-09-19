package com.daoyintech.daoyin_release.repository.user;

import com.daoyintech.daoyin_release.entity.user.UserRelationship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRelationshipRepository extends JpaRepository<UserRelationship, Long> {

    UserRelationship findByChildUserId(Long userId);

    UserRelationship findByChildUserIdAndParentId(Long childId, Long parentId);

}
