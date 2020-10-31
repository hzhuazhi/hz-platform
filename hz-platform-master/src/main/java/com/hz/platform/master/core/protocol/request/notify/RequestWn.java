package com.hz.platform.master.core.protocol.request.notify;

import com.hz.platform.master.core.protocol.base.BaseRequest;

import java.io.Serializable;

/**
 * @Description TODO
 * @Author yoko
 * @Date 2020/3/25 10:32
 * @Version 1.0
 */
public class RequestWn extends BaseRequest implements Serializable {
    private static final long   serialVersionUID = 1233293332183L;
    public String total_amount;// 参数名称：商家订单金额，订单总金额，单位为分
    public String out_trade_no;// 参数名称：商家订单号
    public String trade_status;// 参数名：订单状态取值为“SUCCESS”，代表订单交易成功
    public String trade_no;// 商铺号
    public String extra_return_param;// 参数名称：回传参数商户如果支付请求是传递了该参数，则通知商户支付成功时会回传该参数
    public String trade_time;// 参数名：平台订单时间格式：yyyy-MM-dd HH:mm:ss
    public String sign;// 签名


    public RequestWn(){

    }

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

    @Override
    public String getSign() {
        return sign;
    }

    @Override
    public void setSign(String sign) {
        this.sign = sign;
    }
}
