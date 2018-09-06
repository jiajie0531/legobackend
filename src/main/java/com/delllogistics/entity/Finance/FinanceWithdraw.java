package com.delllogistics.entity.Finance;

import com.delllogistics.entity.BaseModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

/**
 *  提现管理
 * Created by calvin  2018/3/7
 */
@Entity
@Getter
@Setter
@SQLDelete(sql = "update finance_withdraw set is_deleted=1,update_time=now() where id=? and version_=?")
public class FinanceWithdraw extends BaseModel {






}
