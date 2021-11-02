package com.hz.platform.master.core.protocol.request.notify;

import java.io.Serializable;

/**
 * @author yoko
 * @desc 回传协议：东风的同步数据协议
 * @create 2021-11-02 11:25
 **/
public class RequestDfzf implements Serializable {
    private static final long   serialVersionUID = 1253299532189L;

    public RequestDfzf(){

    }

    public String amount;// 支付金额/元(请用 string 类型接收，验签)
    public String bankCode;// 支付类型(同上)
    public String outOrderNo;// 服务订单号
    public String extend;// 扩展参数将会无变化返回
    public String responseMsg;// 返回消息
    public String goodsName;// 商品名称
    public String mchId;// 商户号
    public String responseState;// 订单状态(1：成功，0：失败)
    public String orderNo;// 商户生成的订单编号
    public String payTime;// 提交时间，格式：yyyyMMddHHmmss
    public String signType;// 默认是 MD5
    public String sign;// 签名

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getOutOrderNo() {
        return outOrderNo;
    }

    public void setOutOrderNo(String outOrderNo) {
        this.outOrderNo = outOrderNo;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public String getResponseMsg() {
        return responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getResponseState() {
        return responseState;
    }

    public void setResponseState(String responseState) {
        this.responseState = responseState;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
