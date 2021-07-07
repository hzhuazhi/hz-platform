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
     * 账号
     */
    private String accountNum;

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
     * 锁定金额
     */
    private String lockMoney;

    /**
     * 秘钥key
     */
    private String secretKey;

    /**
     * 是否需要谷歌验证：1不需要，2需要
     */
    private Integer isGoogle;

    /**
     * 谷歌唯一码
     */
    private String googleKey;

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
     * 提现类型：1默认在支付平台操作，2发送下发数据到蛋糕平台
     */
    private Integer withdrawType;

    /**
     * 渠道类型：1代收，2大包，3代付
     */
    private Integer channelType;

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

    /**
     * 余额加
     */
    private String addBalance;

    /**
     * 余额减
     */
    private String subtractBalance;

    /**
     * 要进行更改的金额
     */
    private String orderMoney;


    /**
     * 手续费具体金额
     */
    private String serviceChargeMoney;


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

    public String getAddBalance() {
        return addBalance;
    }

    public void setAddBalance(String addBalance) {
        this.addBalance = addBalance;
    }

    public String getSubtractBalance() {
        return subtractBalance;
    }

    public void setSubtractBalance(String subtractBalance) {
        this.subtractBalance = subtractBalance;
    }

    public String getOrderMoney() {
        return orderMoney;
    }

    public void setOrderMoney(String orderMoney) {
        this.orderMoney = orderMoney;
    }

    public String getLockMoney() {
        return lockMoney;
    }

    public void setLockMoney(String lockMoney) {
        this.lockMoney = lockMoney;
    }

    public String getAccountNum() {
        return accountNum;
    }

    public void setAccountNum(String accountNum) {
        this.accountNum = accountNum;
    }

    public Integer getIsGoogle() {
        return isGoogle;
    }

    public void setIsGoogle(Integer isGoogle) {
        this.isGoogle = isGoogle;
    }

    public String getGoogleKey() {
        return googleKey;
    }

    public void setGoogleKey(String googleKey) {
        this.googleKey = googleKey;
    }

    public Integer getWithdrawType() {
        return withdrawType;
    }

    public void setWithdrawType(Integer withdrawType) {
        this.withdrawType = withdrawType;
    }

    public Integer getChannelType() {
        return channelType;
    }

    public void setChannelType(Integer channelType) {
        this.channelType = channelType;
    }

    public Integer getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(Integer isEnable) {
        this.isEnable = isEnable;
    }

    public String getServiceChargeMoney() {
        return serviceChargeMoney;
    }

    public void setServiceChargeMoney(String serviceChargeMoney) {
        this.serviceChargeMoney = serviceChargeMoney;
    }
}
