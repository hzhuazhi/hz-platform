package com.hz.platform.master.core.model.channelgeway;

import com.hz.platform.master.core.protocol.page.BasePage;

import java.io.Serializable;

/**
 * @Description 渠道与通道的关联关系的实体Bean
 * @Author yoko
 * @Date 2020/3/24 15:55
 * @Version 1.0
 */
public class ChannelGewayModel extends BasePage implements Serializable {
    private static final long   serialVersionUID = 1233223301146L;

    /**
     * 自增主键ID
     */
    private Long id;

    /**
     * 渠道的主键ID
     */
    private Long channelId;

    /**
     *通道的主键ID
     */
    private Long gewayId;

    /**
     * 关联关系的名称
     */
    private String linkName;

    /**
     * 手续费
     */
    private String serviceCharge;

    /**
     * 通道手续费
     */
    private String gewayServiceCharge;

    /**
     * 手续费类型：1固定值的手续费，2除了固定的手续费每单额外要收取手续费（额外手续费）
     */
    private Integer serviceChargeType;

    /**
     * 额外手续费值：每单还要收取额外的手续费；当字段service_charge_type等于2时，则要用到此字段
     */
    private String extraServiceCharge;

    /**
     * 扣量比例
     */
    private Integer deductRatio;

    /**
     * 收益类型：1普通收益类型，2多人分配收益类型
     */
    private Integer profitType;

    /**
     * 通道代码定性类型：1初始化/无任何属性，2代收，3代付
     */
    private Integer gewayCodeType;

    /**
     * 每日成功金额是否到达上限：1初始化/未到达上限，2已到达上限
     */
    private Integer dayLimit;

    /**
     * 每日上限的成功金额
     */
    private String dayMoney;

    /**
     * 筛选比例
     */
    private Integer ratio;


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
     * 筛选比例：开始
     */
    private int startRatio;

    /**
     * 筛选比例结束
     */
    private int endRatio;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public Long getGewayId() {
        return gewayId;
    }

    public void setGewayId(Long gewayId) {
        this.gewayId = gewayId;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
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

    public String getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(String serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public Integer getDeductRatio() {
        return deductRatio;
    }

    public void setDeductRatio(Integer deductRatio) {
        this.deductRatio = deductRatio;
    }

    public Integer getProfitType() {
        return profitType;
    }

    public void setProfitType(Integer profitType) {
        this.profitType = profitType;
    }

    public Integer getGewayCodeType() {
        return gewayCodeType;
    }

    public void setGewayCodeType(Integer gewayCodeType) {
        this.gewayCodeType = gewayCodeType;
    }

    public Integer getDayLimit() {
        return dayLimit;
    }

    public void setDayLimit(Integer dayLimit) {
        this.dayLimit = dayLimit;
    }

    public String getDayMoney() {
        return dayMoney;
    }

    public void setDayMoney(String dayMoney) {
        this.dayMoney = dayMoney;
    }

    public Integer getRatio() {
        return ratio;
    }

    public void setRatio(Integer ratio) {
        this.ratio = ratio;
    }

    public int getStartRatio() {
        return startRatio;
    }

    public void setStartRatio(int startRatio) {
        this.startRatio = startRatio;
    }

    public int getEndRatio() {
        return endRatio;
    }

    public void setEndRatio(int endRatio) {
        this.endRatio = endRatio;
    }

    public String getGewayServiceCharge() {
        return gewayServiceCharge;
    }

    public void setGewayServiceCharge(String gewayServiceCharge) {
        this.gewayServiceCharge = gewayServiceCharge;
    }

    public Integer getServiceChargeType() {
        return serviceChargeType;
    }

    public void setServiceChargeType(Integer serviceChargeType) {
        this.serviceChargeType = serviceChargeType;
    }

    public String getExtraServiceCharge() {
        return extraServiceCharge;
    }

    public void setExtraServiceCharge(String extraServiceCharge) {
        this.extraServiceCharge = extraServiceCharge;
    }


}
