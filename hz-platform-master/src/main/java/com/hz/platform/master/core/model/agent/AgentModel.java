package com.hz.platform.master.core.model.agent;

import com.hz.platform.master.core.protocol.page.BasePage;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Description 代理的实体属性Bean
 * @Author yoko
 * @Date 2020/5/5 9:01
 * @Version 1.0
 */
public class AgentModel extends BasePage implements Serializable {
    private static final long   serialVersionUID = 1233223301146L;

    /**
     * 自增主键ID
     */
    private Long id;

    /**
     * 代理名称
     */
    private String agentName;

    /**
     * 联系人
     */
    private String contacts;

    /**
     * 联系人电话
     */
    private String phoneNum;

    /**
     * 结算/支付方式：1对公，2对私
     */
    private Integer payType;

    /**
     * 总账
     */
    private String totalMoney;

    /**
     * 余额
     */
    private String balance;

    /**
     * 收益提成
     */
    private String royalty;

    private Long roleId; //所属角色ID

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 是否有效：0有效，1无效/删除
     */
    private Integer yn;

    /**
     * 金额
     */
    private BigDecimal money;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public String getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getRoyalty() {
        return royalty;
    }

    public void setRoyalty(String royalty) {
        this.royalty = royalty;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getYn() {
        return yn;
    }

    public void setYn(Integer yn) {
        this.yn = yn;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }
}
