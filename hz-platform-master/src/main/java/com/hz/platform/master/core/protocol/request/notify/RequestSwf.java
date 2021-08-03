package com.hz.platform.master.core.protocol.request.notify;

import java.io.Serializable;

/**
 * @ClassName:
 * @Description: 思维付互联网综合服务平台支付接口-同步数据返回的实体属性Bean
 * @Author: yoko
 * @Date: $
 * @Version: 1.0
 **/
public class RequestSwf implements Serializable {
    private static final long   serialVersionUID = 5933293332189L;

    public RequestSwf(){

    }

    public String state;// 返回状态     0失败 1成功
    public String msg;// 返回消息
    public String ordernum;// 商户订单号
    public String outordernum;// 三方订单号
    public String amount;// 支付金额单位分    1元=100分
    public String respTxnTime;// 支付完成时间
    public String paytype;// 用户支付方式Wx微信支付 WxH5微信H5支付 pcWxDm微信动态码支付 Alipay支付宝支付
    public String attach;// 订制客户原样返回
    public String listid;// 订单明细id
    public String sign;// 签名：以上参数加Key签名

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getOrdernum() {
        return ordernum;
    }

    public void setOrdernum(String ordernum) {
        this.ordernum = ordernum;
    }

    public String getOutordernum() {
        return outordernum;
    }

    public void setOutordernum(String outordernum) {
        this.outordernum = outordernum;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getRespTxnTime() {
        return respTxnTime;
    }

    public void setRespTxnTime(String respTxnTime) {
        this.respTxnTime = respTxnTime;
    }

    public String getPaytype() {
        return paytype;
    }

    public void setPaytype(String paytype) {
        this.paytype = paytype;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getListid() {
        return listid;
    }

    public void setListid(String listid) {
        this.listid = listid;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
