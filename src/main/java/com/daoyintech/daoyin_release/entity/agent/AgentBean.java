package com.daoyintech.daoyin_release.entity.agent;

import com.daoyintech.daoyin_release.entity.common.AbstractEntity;
import com.daoyintech.daoyin_release.enums.agent.ApplyStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "agents")
@Data
@ToString
@EqualsAndHashCode(callSuper = true,of = {})
public class AgentBean extends AbstractEntity {

	@Column
	private Long userId;

	@Column
	private String name;

	@Column
	private String mobile;

	@Column
	private String idNum;

	@Column
	private String positiveImage;

	@Column
	private String backImage;

	@Transient
	private String positiveImageMediaId;

	@Transient
	private String backImageMediaId;

	@Column
	private String bankCardNum;

	@Column
	private Long openBank;

	@Column
	private String email;

	@Column
	private String holdCardName;

	@Column
	@Enumerated(EnumType.STRING)
	private ApplyStatus isSuccess = ApplyStatus.未提交; //0、待审核 1、失败 2、通过 3、准合伙人 4、失效

	@Column
	private String content;//拒绝理由

	@Column
	private Double performanceMoney = 0.0;

	@Column
	private Integer teamCount = 0;

}
