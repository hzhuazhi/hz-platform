package com.hz.platform.master.core.model.datacore;

import com.hz.platform.master.core.protocol.page.BasePage;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 上游数据的实体Bean
 * @Author yoko
 * @Date 2020/3/25 11:38
 * @Version 1.0
 */
public class DataCoreModel extends BasePage implements Serializable {
    private static final long   serialVersionUID = 1203223301142L;

    public DataCoreModel(){

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
     * 平台订单号
     */
    private String tradeNo;

    /**
     * 参数名称：商家订单号
     */
    private String outTradeNo;

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
     * 订单状态：1成功，2失败，3其它
     */
    private Integer tradeStatus;

    /**
     * 参数名称：回传参数商户如果支付请求是传递了该参数，则通知商户支付成功时会回传该参数
     */
    private String extraReturnParam;

    /**
     * 参数名：平台订单时间格式：yyyy-MM-dd HH:mm:ss
     */
    private String tradeTime;

    /**
     * 参数名称：平台返回签名数据该参数用于验签
     */
    private String sign;

    /**
     * 渠道的主键ID
     */
    private Long channelId;

    /**
     * 通道的主键ID
     */
    private Long gewayId;

    /**
     * 异步通知地址
     */
    private String notifyUrl;

    /**
     * 返回的成功标识
     */
    private String notifySuc;

    /**
     * 下游透传参数
     */
    private String xyExtraReturnParam;

    /**
     * 扣量比例
     */
    private Integer deductRatio;

    /**
     * 渠道与通道的关联关系的ID：对应表tb_hz_channel_geway的主键ID
     */
    private Long channelGewayId;

    /**
     * 收益类型：1普通收益类型，2多人分配收益类型
     */
    private Integer profitType;

    /**
     * 值以work形式填充计算,以后数据多起来则这些字段值填充由worker来跑数据：0初始化，1填充完毕，2无需下发数据
     */
    private Integer workType;

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


    /**
     * 实际支付金额：用户实际支付的金额
     */
    private String payAmount;

    /**
     * 实际支付金额扣手续费后的金额：字段pay_amount扣除手续费后的金额
     */
    private String payActualMoney;

    /**
     * 是否是补单：1初始化不是补单，2是补单
     */
    private Integer replenishType;

    /**
     * 订单金额是否与实际支付金额一致：1初始化，2少了，3多了，4一致
     */
    private Integer moneyFitType;

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

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
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

    public Integer getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(Integer tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public String getExtraReturnParam() {
        return extraReturnParam;
    }

    public void setExtraReturnParam(String extraReturnParam) {
        this.extraReturnParam = extraReturnParam;
    }

    public String getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(String tradeTime) {
        this.tradeTime = tradeTime;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
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

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getNotifySuc() {
        return notifySuc;
    }

    public void setNotifySuc(String notifySuc) {
        this.notifySuc = notifySuc;
    }

    public String getXyExtraReturnParam() {
        return xyExtraReturnParam;
    }

    public void setXyExtraReturnParam(String xyExtraReturnParam) {
        this.xyExtraReturnParam = xyExtraReturnParam;
    }

    public Integer getDeductRatio() {
        return deductRatio;
    }

    public void setDeductRatio(Integer deductRatio) {
        this.deductRatio = deductRatio;
    }

    public Integer getWorkType() {
        return workType;
    }

    public void setWorkType(Integer workType) {
        this.workType = workType;
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

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(String payAmount) {
        this.payAmount = payAmount;
    }

    public String getPayActualMoney() {
        return payActualMoney;
    }

    public void setPayActualMoney(String payActualMoney) {
        this.payActualMoney = payActualMoney;
    }

    public Integer getReplenishType() {
        return replenishType;
    }

    public void setReplenishType(Integer replenishType) {
        this.replenishType = replenishType;
    }

    public Integer getMoneyFitType() {
        return moneyFitType;
    }

    public void setMoneyFitType(Integer moneyFitType) {
        this.moneyFitType = moneyFitType;
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
}
