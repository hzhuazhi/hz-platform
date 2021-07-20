package com.hz.platform.master.core.protocol.request.notify;

import java.io.Serializable;

/**
 * @ClassName:
 * @Description: 回传协议：金木星的同步数据协议
 * @Author: yoko
 * @Date: $
 * @Version: 1.0
 **/
public class RequestJmx implements Serializable {
    private static final long   serialVersionUID = 1253293332489L;

    public RequestJmx(){

    }

    public String mch_id;// 商户号
    public String trade_no;// 平台订单号
    public String out_trade_no;// 商户订单号
    public String psg_trade_no;// 原始订单号：建议保存，可作为异常凭证
    public String money;// 订单金额，单位为元，精确到小数点后两位
    public String notify_time;// 发送请求的时间，格式"yyyy-MM-dd HH:mm:ss"
    public String subject;// 标题
    public String state;// 状态 订单状态：0=失败，1=待支付，2=支付成功，3=冲正
    public String sign;// 签名（详见签名规则）

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getTrade_no() {
        return trade_no;
    }

    public void setTrade_no(String trade_no) {
        this.trade_no = trade_no;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getPsg_trade_no() {
        return psg_trade_no;
    }

    public void setPsg_trade_no(String psg_trade_no) {
        this.psg_trade_no = psg_trade_no;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getNotify_time() {
        return notify_time;
    }

    public void setNotify_time(String notify_time) {
        this.notify_time = notify_time;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
