package com.hz.pay.master.core.model.receivingaccount;

import com.hz.pay.master.core.protocol.page.BasePage;

import java.io.Serializable;

/**
 * @Description 收款账号的实体属性Bean
 * @Author yoko
 * @Date 2020/4/20 15:00
 * @Version 1.0
 */
public class ReceivingAccountModel extends BasePage implements Serializable {
    private static final long   serialVersionUID = 1233123101140L;

    /**
     * 自增主键ID
     */
    private Long id;

    /**
     * 归属的bufPay的主键ID:对应tb_hz_buf_pay表的主键
     */
    private Long bufPayId;

    /**
     * 收款账号名称
     */
    private String accountName;

    /**
     *账号二维码地址
     */
    private String qrCode;

    /**
     * 账号类型：1支付宝账号，2微信账号
     */
    private Integer accountType;

    /**
     * 二维码更地址的更新时间
     */
    private String qrTime;

    /**
     * 是否属于固码：1不是固码，2是固码
     */
    private Integer gmType;

    /**
     * 规定金额/限定金额
     */
    private String limitMoney;

    /**
     * 每日限量金额
     */
    private String limitDayMoney;

    /**
     * 每月限量金额
     */
    private String limitMonthMoney;

    /**
     * 每年
     */
    private String limitYearMoney;

    /**
     * 每日次数
     */
    private Integer limitDayNum;

    /**
     * 每月次数
     */
    private Integer limitMonthNum;

    /**
     * 每年次数
     */
    private Integer limitYearNum;

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

    public Long getBufPayId() {
        return bufPayId;
    }

    public void setBufPayId(Long bufPayId) {
        this.bufPayId = bufPayId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public Integer getAccountType() {
        return accountType;
    }

    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }

    public String getQrTime() {
        return qrTime;
    }

    public void setQrTime(String qrTime) {
        this.qrTime = qrTime;
    }

    public Integer getGmType() {
        return gmType;
    }

    public void setGmType(Integer gmType) {
        this.gmType = gmType;
    }

    public String getLimitMoney() {
        return limitMoney;
    }

    public void setLimitMoney(String limitMoney) {
        this.limitMoney = limitMoney;
    }

    public String getLimitDayMoney() {
        return limitDayMoney;
    }

    public void setLimitDayMoney(String limitDayMoney) {
        this.limitDayMoney = limitDayMoney;
    }

    public String getLimitMonthMoney() {
        return limitMonthMoney;
    }

    public void setLimitMonthMoney(String limitMonthMoney) {
        this.limitMonthMoney = limitMonthMoney;
    }

    public String getLimitYearMoney() {
        return limitYearMoney;
    }

    public void setLimitYearMoney(String limitYearMoney) {
        this.limitYearMoney = limitYearMoney;
    }

    public Integer getLimitDayNum() {
        return limitDayNum;
    }

    public void setLimitDayNum(Integer limitDayNum) {
        this.limitDayNum = limitDayNum;
    }

    public Integer getLimitMonthNum() {
        return limitMonthNum;
    }

    public void setLimitMonthNum(Integer limitMonthNum) {
        this.limitMonthNum = limitMonthNum;
    }

    public Integer getLimitYearNum() {
        return limitYearNum;
    }

    public void setLimitYearNum(Integer limitYearNum) {
        this.limitYearNum = limitYearNum;
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
