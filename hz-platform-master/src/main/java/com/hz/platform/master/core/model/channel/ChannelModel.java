package com.hz.platform.master.core.model.channel;

import com.hz.platform.master.core.protocol.page.BasePage;

import java.io.Serializable;

/**
 * @Description 渠道的实体属性Bean
 * @Author yoko
 * @Date 2020/3/2 17:24
 * @Version 1.0
 */
public class ChannelModel extends BasePage implements Serializable {
    private static final long   serialVersionUID = 1233223301145L;

    /**
     * 自增主键ID
     */
    private Long id;

    /**
     * 渠道名称
     */
    private String channelName;

    /**
     *渠道号（商铺号）
     */
    private String channel;

    /**
     * 公司名称
     */
    private String companyName;

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
     * 秘钥key
     */
    private String secretKey;

    /**
     * 同步的接口地址
     */
    private String lowerUrl;

    /**
     * 返回的成功标识
     */
    private String lowerSuc;

    /**
     * 是否需要数据同步:1需要同步，2不需要同步
     */
    private Integer isSynchro;

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

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getLowerUrl() {
        return lowerUrl;
    }

    public void setLowerUrl(String lowerUrl) {
        this.lowerUrl = lowerUrl;
    }

    public String getLowerSuc() {
        return lowerSuc;
    }

    public void setLowerSuc(String lowerSuc) {
        this.lowerSuc = lowerSuc;
    }

    public Integer getIsSynchro() {
        return isSynchro;
    }

    public void setIsSynchro(Integer isSynchro) {
        this.isSynchro = isSynchro;
    }
}
