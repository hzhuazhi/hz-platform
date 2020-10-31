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
     * 扣量比例
     */
    private Integer deductRatio;

    /**
     * 收益类型：1普通收益类型，2多人分配收益类型
     */
    private Integer profitType;


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
}
