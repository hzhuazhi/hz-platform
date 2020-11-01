package com.hz.platform.master.core.model.task.base;

import java.io.Serializable;
import java.util.List;

/**
 * @Description task的抓取规则的实体Bean
 * @Author yoko
 * @Date 2019/12/6 21:01
 * @Version 1.0
 */
public class StatusModel implements Serializable {
    private static final long   serialVersionUID = 1233223301140L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 状态集合
     */
    private List<Integer> statusList;

    /**
     * 值以work形式填充计算,以后数据多起来则这些字段值填充由worker来跑数据：0初始化，1填充完毕，2无需下发数据
     */
    private Integer workType;

    /**
     * 金额
     */
    private String money;

    /**
     * 运行次数
     */
    private Integer runNum;

    /**
     * 运行计算状态：0初始化，1锁定，2计算失败，3计算成功
     */
    private Integer runStatus;

    /**
     * 运行计算状态：0初始化，1锁定，2计算失败，3计算成功
     * 当做条件
     */
    private Integer runStatusWhere;

    /**
     * 发送次数
     */
    private Integer sendNum;

    /**
     * 发送状态：0初始化，1锁定，2计算失败，3计算成功
     */
    private Integer sendStatus;

    /**
     * 查询多少条数据
     */
    private Integer limitNum;

    /**
     * 日期
     */
    private Integer curday;
    private Integer curdayStart;
    private Integer curdayEnd;


    /**
     * 订单状态：1初始化，2超时/失败，3成功
     */
    private Integer orderStatus;

    /**
     * 原因：task跑时，可能的一些失败原因的存储
     */
    private String info;

    /**
     * 订单状态：1成功，2失败，3其它
     */
    private Integer tradeStatus;

    /**
     * 大于
     */
    private Integer greaterThan;

    /**
     * 小于
     */
    private Integer lessThan;





    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRunNum() {
        return runNum;
    }

    public void setRunNum(Integer runNum) {
        this.runNum = runNum;
    }

    public Integer getRunStatus() {
        return runStatus;
    }

    public void setRunStatus(Integer runStatus) {
        this.runStatus = runStatus;
    }

    public Integer getLimitNum() {
        return limitNum;
    }

    public void setLimitNum(Integer limitNum) {
        this.limitNum = limitNum;
    }

    public Integer getCurday() {
        return curday;
    }

    public void setCurday(Integer curday) {
        this.curday = curday;
    }

    public Integer getCurdayStart() {
        return curdayStart;
    }

    public void setCurdayStart(Integer curdayStart) {
        this.curdayStart = curdayStart;
    }

    public Integer getCurdayEnd() {
        return curdayEnd;
    }

    public void setCurdayEnd(Integer curdayEnd) {
        this.curdayEnd = curdayEnd;
    }

    public List<Integer> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<Integer> statusList) {
        this.statusList = statusList;
    }

    public Integer getRunStatusWhere() {
        return runStatusWhere;
    }

    public void setRunStatusWhere(Integer runStatusWhere) {
        this.runStatusWhere = runStatusWhere;
    }

    public Integer getWorkType() {
        return workType;
    }

    public void setWorkType(Integer workType) {
        this.workType = workType;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public Integer getSendNum() {
        return sendNum;
    }

    public void setSendNum(Integer sendNum) {
        this.sendNum = sendNum;
    }

    public Integer getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(Integer sendStatus) {
        this.sendStatus = sendStatus;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Integer getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(Integer tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public Integer getGreaterThan() {
        return greaterThan;
    }

    public void setGreaterThan(Integer greaterThan) {
        this.greaterThan = greaterThan;
    }

    public Integer getLessThan() {
        return lessThan;
    }

    public void setLessThan(Integer lessThan) {
        this.lessThan = lessThan;
    }
}
