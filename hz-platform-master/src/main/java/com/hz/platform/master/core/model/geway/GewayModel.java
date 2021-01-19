package com.hz.platform.master.core.model.geway;

import com.hz.platform.master.core.protocol.page.BasePage;

import java.io.Serializable;

/**
 * @Description 通道的实体属性Bean
 * @Author yoko
 * @Date 2020/3/24 15:37
 * @Version 1.0
 */
public class GewayModel extends BasePage implements Serializable {
    private static final long   serialVersionUID = 1233223301146L;

    /**
     * 自增主键ID
     */
    private Long id;

    /**
     *登录账号
     */
    private String accountNum;

    /**
     * 账号密码
     */
    private String passWd;

    /**
     * 通道名称
     */
    private String gewayName;

    /**
     *接口地址
     */
    private String interfaceAds;

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
     * 商铺号（商家号）
     */
    private String payId;

    /**
     * 总账
     */
    private String totalMoney;

    /**
     * 保底金额：预付款通道，如果余额少于保底金额，就不出码
     */
    private String leastMoney;

    /**
     * 余额
     */
    private String balance;

    /**
     * 秘钥key
     */
    private String secretKey;

    /**
     * 回传标识
     */
    private String identify;

    /**
     * 同步的接口地址:我方的同步地址
     */
    private String notifyUrl;

    /**
     * 通道类型：1普通通道，2预付款通道
     */
    private Integer gewayType;

    /**
     * 所属角色ID
     */
    private Long roleId;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGewayName() {
        return gewayName;
    }

    public void setGewayName(String gewayName) {
        this.gewayName = gewayName;
    }

    public String getInterfaceAds() {
        return interfaceAds;
    }

    public void setInterfaceAds(String interfaceAds) {
        this.interfaceAds = interfaceAds;
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

    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
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

    public String getIdentify() {
        return identify;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
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

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getAccountNum() {
        return accountNum;
    }

    public void setAccountNum(String accountNum) {
        this.accountNum = accountNum;
    }

    public String getPassWd() {
        return passWd;
    }

    public void setPassWd(String passWd) {
        this.passWd = passWd;
    }

    public String getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getLeastMoney() {
        return leastMoney;
    }

    public void setLeastMoney(String leastMoney) {
        this.leastMoney = leastMoney;
    }

    public Integer getGewayType() {
        return gewayType;
    }

    public void setGewayType(Integer gewayType) {
        this.gewayType = gewayType;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Integer getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(Integer isEnable) {
        this.isEnable = isEnable;
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
}
