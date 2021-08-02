package com.hz.platform.master.core.protocol.request.notify;

import java.io.Serializable;

/**
 * @ClassName:
 * @Description: 小肥龙支付
 * @Author: yoko
 * @Date: $
 * @Version: 1.0
 **/
public class RequestXfl implements Serializable {
    private static final long   serialVersionUID = 5233293392189L;

    public RequestXfl(){

    }
    public String payOrderId;// 支付中心生成的订单号
    public String mchId;// 支付中心分配的商户号
    public String appId;// 商户应用ID
    public String productId;//
    public String mchOrderNo;// 商户生成的订单号
    public int amount;// 支付金额,单位分
    public int income;// 入账金额,单位分
    public int status;//支付状态,-2:订单已关闭,0-订单生成,1-支付中,2-支付成功,3-业务处理完成,4-已退款（2和3都表示支付成功,3表示支付平台回调商户且返回成功后的状态）
    public String channelOrderNo;// 三方支付渠道订单号
    public String param1;// 支付中心回调时会原样返回
    public String param2;// 支付中心回调时会原样返回
    public long paySuccTime;// 精确到毫秒
    public int backType;// 通知类型，1-前台通知，2-后台通知
    public String reqTime;// 通知请求时间，yyyyMMddHHmmss格式
    public String sign;// 签名

    public String getPayOrderId() {
        return payOrderId;
    }

    public void setPayOrderId(String payOrderId) {
        this.payOrderId = payOrderId;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getMchOrderNo() {
        return mchOrderNo;
    }

    public void setMchOrderNo(String mchOrderNo) {
        this.mchOrderNo = mchOrderNo;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getIncome() {
        return income;
    }

    public void setIncome(int income) {
        this.income = income;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getChannelOrderNo() {
        return channelOrderNo;
    }

    public void setChannelOrderNo(String channelOrderNo) {
        this.channelOrderNo = channelOrderNo;
    }

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }

    public String getParam2() {
        return param2;
    }

    public void setParam2(String param2) {
        this.param2 = param2;
    }

    public long getPaySuccTime() {
        return paySuccTime;
    }

    public void setPaySuccTime(long paySuccTime) {
        this.paySuccTime = paySuccTime;
    }

    public int getBackType() {
        return backType;
    }

    public void setBackType(int backType) {
        this.backType = backType;
    }

    public String getReqTime() {
        return reqTime;
    }

    public void setReqTime(String reqTime) {
        this.reqTime = reqTime;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
