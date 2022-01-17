package com.hz.platform.master.core.model.statistics;


import com.hz.platform.master.core.protocol.page.BasePage;

import java.io.Serializable;

/**
 * @author yoko
 * @desc 统计代理的每日记录的实体属性Bean
 * @create 2021-12-30 16:35
 **/
public class StatisticsAgentModel extends BasePage implements Serializable {
    private static final long   serialVersionUID = 12931439301145L;

    public StatisticsAgentModel(){

    }

    /**
     * 自增主键ID
     */
    private Long id;

    /**
     * 归属代理ID
     */
    private Long agentId;

    /**
     * 总账
     */
    private String totalMoney;

    /**
     * 余额
     */
    private String balance;

    /**
     * 锁定金额
     */
    private String lockMoney;

    /**
     * 跑量金额
     */
    private String totalAmount;

    /**
     * 收益
     */
    private String profit;

    /**
     * 提现中的金额
     */
    private String withdrawIngMoney;

    /**
     * 提现成功金额
     */
    private String withdrawSucMoney;

    /**
     * 提现失败金额：驳回金额
     */
    private String withdrawFailMoney;

    /**
     * 加金额
     */
    private String addMoney;

    /**
     * 减金额
     */
    private String reduceMoney;

    /**
     * 创建日期：存的日期格式20160530
     */
    private Integer curday;

    /**
     * 创建所属小时：24小时制
     */
    private Integer curhour;

    /**
     * 创建所属分钟：60分钟制
     */
    private Integer curminute;


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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
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

    public String getLockMoney() {
        return lockMoney;
    }

    public void setLockMoney(String lockMoney) {
        this.lockMoney = lockMoney;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getProfit() {
        return profit;
    }

    public void setProfit(String profit) {
        this.profit = profit;
    }

    public String getWithdrawIngMoney() {
        return withdrawIngMoney;
    }

    public void setWithdrawIngMoney(String withdrawIngMoney) {
        this.withdrawIngMoney = withdrawIngMoney;
    }

    public String getWithdrawSucMoney() {
        return withdrawSucMoney;
    }

    public void setWithdrawSucMoney(String withdrawSucMoney) {
        this.withdrawSucMoney = withdrawSucMoney;
    }

    public String getWithdrawFailMoney() {
        return withdrawFailMoney;
    }

    public void setWithdrawFailMoney(String withdrawFailMoney) {
        this.withdrawFailMoney = withdrawFailMoney;
    }

    public String getAddMoney() {
        return addMoney;
    }

    public void setAddMoney(String addMoney) {
        this.addMoney = addMoney;
    }

    public String getReduceMoney() {
        return reduceMoney;
    }

    public void setReduceMoney(String reduceMoney) {
        this.reduceMoney = reduceMoney;
    }

    public Integer getCurday() {
        return curday;
    }

    public void setCurday(Integer curday) {
        this.curday = curday;
    }

    public Integer getCurhour() {
        return curhour;
    }

    public void setCurhour(Integer curhour) {
        this.curhour = curhour;
    }

    public Integer getCurminute() {
        return curminute;
    }

    public void setCurminute(Integer curminute) {
        this.curminute = curminute;
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
}
