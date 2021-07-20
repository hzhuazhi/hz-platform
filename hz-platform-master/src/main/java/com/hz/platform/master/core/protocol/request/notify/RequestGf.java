package com.hz.platform.master.core.protocol.request.notify;

import java.io.Serializable;

/**
 * @ClassName:
 * @Description: 回传协议：高防支付的同步数据协议
 * @Author: yoko
 * @Date: $
 * @Version: 1.0
 **/
public class RequestGf implements Serializable {
    private static final long   serialVersionUID = 1253493332189L;

    public RequestGf(){

    }

    public String total_amount;
    public String out_trade_no;
    public String trade_status;
    public String pay_amount;
    public String trade_no;
    public String extra_return_param;
    public String trade_time;
    public String sign;

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getTrade_status() {
        return trade_status;
    }

    public void setTrade_status(String trade_status) {
        this.trade_status = trade_status;
    }

    public String getPay_amount() {
        return pay_amount;
    }

    public void setPay_amount(String pay_amount) {
        this.pay_amount = pay_amount;
    }

    public String getTrade_no() {
        return trade_no;
    }

    public void setTrade_no(String trade_no) {
        this.trade_no = trade_no;
    }

    public String getExtra_return_param() {
        return extra_return_param;
    }

    public void setExtra_return_param(String extra_return_param) {
        this.extra_return_param = extra_return_param;
    }

    public String getTrade_time() {
        return trade_time;
    }

    public void setTrade_time(String trade_time) {
        this.trade_time = trade_time;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
