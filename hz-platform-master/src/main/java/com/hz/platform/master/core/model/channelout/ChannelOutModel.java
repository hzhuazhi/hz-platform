package com.hz.platform.master.core.model.channelout;

import com.hz.platform.master.core.protocol.page.BasePage;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 渠道数据代付订单的实体属性Bean
 * @Author yoko
 * @Date 2020/10/31 15:19
 * @Version 1.0
 */
public class ChannelOutModel extends BasePage implements Serializable {
    private static final long   serialVersionUID = 1203223301141L;

    public ChannelOutModel(){

    }
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 我方订单号
     */
    private String myTradeNo;

    /**
     * 渠道的主键ID
     */
    private Long channelId;

    /**
     * 通道的主键ID
     */
    private Long gewayId;

    /**
     * 渠道号/商铺号
     */
    private String channel;

    /**
     * 交易类型
     */
    private String tradeType;

    /**
     * 参数名称：商家订单金额，订单总金额
     */
    private String totalAmount;

    /**
     * 手续费
     */
    private String serviceCharge;

    /**
     * 实际金额
     */
    private String actualMoney;

    /**
     * 订单状态：1初始化，2超时，3质疑，4成功
     */
    private Integer orderStatus;

    /**
     * 参数名称：商家订单号
     */
    private String outTradeNo;

    /**
     * 银行名称
     */
    private String bankName;

    /**
     * 银行卡号
     */
    private String bankCard;

    /**
     * 开户名
     */
    private String accountName;

    /**
     * 银行卡转账图片凭证
     */
    private String pictureAds;

    /**
     * 失败缘由
     */
    private String failInfo;

    /**
     * 渠道的异步通知地址
     */
    private String notifyUrl;

    /**
     * 我方的同步地址
     */
    private String myNotifyUrl;

    /**
     * 接口版本
     */
    private String interfaceVer;

    /**
     * 参数名称：页面跳转同步通知地址；支付成功后，通过页面跳转的方式跳转到商家网站
     */
    private String returnUrl;

    /**
     * 参数名称：回传参数商户如果支付请求是传递了该参数，则通知商户支付成功时会回传该参数
     */
    private String extraReturnParam;

    /**
     * 客户端IP地址
     */
    private String clientIp;

    /**
     * 参数名称：平台返回签名数据该参数用于验签
     */
    private String sign;

    /**
     * 提交时间
     */
    private String subTime;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品码
     */
    private String productCode;

    /**
     * 渠道与通道的关联关系的ID：对应表tb_hz_channel_geway的主键ID
     */
    private Long channelGewayId;

    /**
     * 收益类型：1普通收益类型，2多人分配收益类型
     */
    private Integer profitType;

    /**
     * 请求是否成功：1成功，2失败
     */
    private Integer sendOk;

    /**
     * 审核状态：1初始化，2审核收款失败，3系统自动审核，4审核收款成功
     */
    private Integer checkStatus;

    /**
     * 审核失败缘由，审核失败的原因
     */
    private String checkInfo;

    /**
     * 创建日期：存的日期格式20160530
     */
    private Integer curday;

    /**
     * 创建所属小时：24小时制
     */
    private Integer curhour;

    /**
     * 创建所属分钟：60分钟制
     */
    private Integer curminute;

    /**
     *运行计算次数
     */
    private Integer runNum;

    /**
     * 运行计算状态：：0初始化，1锁定，2计算失败，3计算成功
     */
    private Integer runStatus;


    /**
     *发送次数
     */
    private Integer sendNum;

    /**
     * 发送状态：0初始化，1锁定，2计算失败，3计算成功
     */
    private Integer sendStatus;

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

    private Integer curdayStart;
    private Integer curdayEnd;

    /**
     * did的集合
     */
    private List<Long> idList;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMyTradeNo() {
        return myTradeNo;
    }

    public void setMyTradeNo(String myTradeNo) {
        this.myTradeNo = myTradeNo;
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

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(String serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public String getActualMoney() {
        return actualMoney;
    }

    public void setActualMoney(String actualMoney) {
        this.actualMoney = actualMoney;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getPictureAds() {
        return pictureAds;
    }

    public void setPictureAds(String pictureAds) {
        this.pictureAds = pictureAds;
    }

    public String getFailInfo() {
        return failInfo;
    }

    public void setFailInfo(String failInfo) {
        this.failInfo = failInfo;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getMyNotifyUrl() {
        return myNotifyUrl;
    }

    public void setMyNotifyUrl(String myNotifyUrl) {
        this.myNotifyUrl = myNotifyUrl;
    }

    public String getInterfaceVer() {
        return interfaceVer;
    }

    public void setInterfaceVer(String interfaceVer) {
        this.interfaceVer = interfaceVer;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getExtraReturnParam() {
        return extraReturnParam;
    }

    public void setExtraReturnParam(String extraReturnParam) {
        this.extraReturnParam = extraReturnParam;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSubTime() {
        return subTime;
    }

    public void setSubTime(String subTime) {
        this.subTime = subTime;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Long getChannelGewayId() {
        return channelGewayId;
    }

    public void setChannelGewayId(Long channelGewayId) {
        this.channelGewayId = channelGewayId;
    }

    public Integer getProfitType() {
        return profitType;
    }

    public void setProfitType(Integer profitType) {
        this.profitType = profitType;
    }

    public Integer getSendOk() {
        return sendOk;
    }

    public void setSendOk(Integer sendOk) {
        this.sendOk = sendOk;
    }

    public Integer getCurday() {
        return curday;
    }

    public void setCurday(Integer curday) {
        this.curday = curday;
    }

    public Integer getCurhour() {
        return curhour;
    }

    public void setCurhour(Integer curhour) {
        this.curhour = curhour;
    }

    public Integer getCurminute() {
        return curminute;
    }

    public void setCurminute(Integer curminute) {
        this.curminute = curminute;
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

    public List<Long> getIdList() {
        return idList;
    }

    public void setIdList(List<Long> idList) {
        this.idList = idList;
    }

    public Integer getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(Integer checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getCheckInfo() {
        return checkInfo;
    }

    public void setCheckInfo(String checkInfo) {
        this.checkInfo = checkInfo;
    }
}
