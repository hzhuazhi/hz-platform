package com.hz.pay.master.core.model.zfbapp;

import com.hz.pay.master.core.protocol.page.BasePage;

import java.io.Serializable;

/**
 * @Description 支付宝账号的实体属性Bean
 * @Author yoko
 * @Date 2020/4/9 16:55
 * @Version 1.0
 */
public class ZfbAppModel extends BasePage implements Serializable {
    private static final long   serialVersionUID = 1233223101145L;

    /**
     * 自增主键ID
     */
    private Long id;

    /**
     * APP名称
     */
    private String appName;

    /**
     *app的ID
     */
    private String appId;

    /**
     * APP的私有秘钥
     */
    private String appPrivateKey;

    /**
     * APP的公钥
     */
    private String appPublicKey;

    /**
     * 应用的类型：1H5,2APP，3笔转账到账户
     */
    private Integer appType;

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
     * 比例
     */
    private Integer ratio;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否启用：0初始化属于暂停状态，1表示暂停使用，2正常状态
     */
    private Integer isEnable;

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

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppPrivateKey() {
        return appPrivateKey;
    }

    public void setAppPrivateKey(String appPrivateKey) {
        this.appPrivateKey = appPrivateKey;
    }

    public String getAppPublicKey() {
        return appPublicKey;
    }

    public void setAppPublicKey(String appPublicKey) {
        this.appPublicKey = appPublicKey;
    }

    public Integer getAppType() {
        return appType;
    }

    public void setAppType(Integer appType) {
        this.appType = appType;
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

    public Integer getRatio() {
        return ratio;
    }

    public void setRatio(Integer ratio) {
        this.ratio = ratio;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(Integer isEnable) {
        this.isEnable = isEnable;
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
