package com.hz.pay.master.core.model.geway;


import com.hz.pay.master.core.protocol.page.BasePage;

import java.io.Serializable;

/**
 * @Description 通道的支付类型的实体属性Bean
 * @Author yoko
 * @Date 2020/3/31 15:48
 * @Version 1.0
 */
public class GewaytradetypeModel extends BasePage implements Serializable {
    private static final long   serialVersionUID = 1233223301146L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 通道的主键ID
     */
    private Long gewayId;

    /**
     * 我方支付类型名称
     */
    private String myName;

    /**
     * 我方支付类型编码
     */
    private String myTradeType;

    /**
     * 我方手续费
     */
    private String myServiceCharge;

    /**
     * 通道/对方支付类型名称
     */
    private String outName;

    /**
     * 通道/对方支付类型编码
     */
    private String outTradeType;

    /**
     * 通道/对方手续费
     */
    private String serviceCharge;

    /**
     * 规定金额/限定金额
     */
    private String limitMoney;

    /**
     * 每日限量金额
     */
    private String limitDay;

    /**
     * 每月限量金额
     */
    private String limitMonth;

    /**
     * 备注
     */
    private String remark;

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
     * 通道名称
     */
    private String gewayName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGewayId() {
        return gewayId;
    }

    public void setGewayId(Long gewayId) {
        this.gewayId = gewayId;
    }

    public String getMyName() {
        return myName;
    }

    public void setMyName(String myName) {
        this.myName = myName;
    }

    public String getMyTradeType() {
        return myTradeType;
    }

    public void setMyTradeType(String myTradeType) {
        this.myTradeType = myTradeType;
    }

    public String getMyServiceCharge() {
        return myServiceCharge;
    }

    public void setMyServiceCharge(String myServiceCharge) {
        this.myServiceCharge = myServiceCharge;
    }

    public String getOutName() {
        return outName;
    }

    public void setOutName(String outName) {
        this.outName = outName;
    }

    public String getOutTradeType() {
        return outTradeType;
    }

    public void setOutTradeType(String outTradeType) {
        this.outTradeType = outTradeType;
    }

    public String getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(String serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public String getLimitMoney() {
        return limitMoney;
    }

    public void setLimitMoney(String limitMoney) {
        this.limitMoney = limitMoney;
    }

    public String getLimitDay() {
        return limitDay;
    }

    public void setLimitDay(String limitDay) {
        this.limitDay = limitDay;
    }

    public String getLimitMonth() {
        return limitMonth;
    }

    public void setLimitMonth(String limitMonth) {
        this.limitMonth = limitMonth;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getGewayName() {
        return gewayName;
    }

    public void setGewayName(String gewayName) {
        this.gewayName = gewayName;
    }
}
