package com.hz.pay.master.core.model.agent;

import com.hz.pay.master.core.protocol.page.BasePage;

import java.io.Serializable;

/**
 * @Description 代理与渠道的关联关系的实体属性Bean
 * @Author yoko
 * @Date 2020/9/17 21:00
 * @Version 1.0
 */
public class AgentChannelGewayModel extends BasePage implements Serializable {
    private static final long   serialVersionUID = 1233223302146L;

    /**
     * 自增主键ID
     */
    private Long id;

    /**
     * 代理的的主键ID
     */
    private Long agentId;

    /**
     * 渠道与通道的关联关系的ID：对应表tb_hz_channel_geway的主键ID
     */
    private Long channelGewayId;

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

    public Long getChannelGewayId() {
        return channelGewayId;
    }

    public void setChannelGewayId(Long channelGewayId) {
        this.channelGewayId = channelGewayId;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
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
}
