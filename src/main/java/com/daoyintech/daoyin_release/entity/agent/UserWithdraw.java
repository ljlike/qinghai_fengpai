package com.daoyintech.daoyin_release.entity.agent;

import com.daoyintech.daoyin_release.entity.common.AbstractEntity;
import com.daoyintech.daoyin_release.enums.agent.WithdrawStatus;
import com.daoyintech.daoyin_release.enums.agent.WithdrawType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "user_withdraw")
@Data
@EqualsAndHashCode(callSuper = true)
public class UserWithdraw extends AbstractEntity {

	@Column
	private Long userId;

	@Column
	private Double integral; //提现积分

	@Column
	private Double money;

	@Column
	@Enumerated(EnumType.ORDINAL)
	private WithdrawStatus status = WithdrawStatus.提现失败;

	@Column
	private String content;//失败原因

	@Column
	private String tradeNo;		//提现单号

	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date successTime;

	@Column
	private String bankCardNum;

	@Column
	private BigDecimal feeMoney;


	@Column
	@Enumerated(EnumType.STRING)
	private WithdrawType type = WithdrawType.零钱;
}
