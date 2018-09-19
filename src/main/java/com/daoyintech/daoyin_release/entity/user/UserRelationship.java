package com.daoyintech.daoyin_release.entity.user;

import com.daoyintech.daoyin_release.entity.common.AbstractEntity;
import com.daoyintech.daoyin_release.enums.user.RelationshipType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.*;

@Entity
@Table(name = "user_relationships")
@Data
@EqualsAndHashCode(callSuper = true)
public class UserRelationship extends AbstractEntity {

    @Column
    private Long parentId;

    @Column
    private String childOpenId;

    @Column
    private Long childUserId;

    @Column
    private String relationshipLink;

    @Column
    @Enumerated(EnumType.STRING)
    private RelationshipType relationshipType = RelationshipType.二维码;

}
