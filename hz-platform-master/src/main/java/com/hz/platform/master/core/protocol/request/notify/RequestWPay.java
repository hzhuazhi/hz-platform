package com.hz.platform.master.core.protocol.request.notify;

import java.io.Serializable;

/**
 * @Description 回传协议：微派的同步数据协议
 * @Author yoko
 * @Date 2021/5/26 17:05
 * @Version 1.0
 */
public class RequestWPay implements Serializable {
    private static final long   serialVersionUID = 1233293335189L;
    public String orderNo;// 微派的订单号
    public String synType;// 支付类型:wiipay：短信 prepaid：充值卡、点卡upmp：银联 alipay：支付宝wxpay：微信 qqpay：QQ钱包jdpay：京东 transfer：企业代付
    public String status;// 状态：success为成功，其他为失败
    public String price;// 价格:单位元
    public String time;// 时间:例如 20150729171606
    public String cpparam;// 透传参数:请求时的透传参数(out_trade_no)，原样返回
    public String sign;// 签名

    public RequestWPay(){

    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getSynType() {
        return synType;
    }

    public void setSynType(String synType) {
        this.synType = synType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCpparam() {
        return cpparam;
    }

    public void setCpparam(String cpparam) {
        this.cpparam = cpparam;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
