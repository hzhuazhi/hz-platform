package com.hz.platform.master.core.protocol.request.notify;

import java.io.Serializable;

/**
 * @Description 回传协议：美好的同步数据协议
 * @Author yoko
 * @Date 2020/6/9 15:42
 * @Version 1.0
 */
public class RequestFine implements Serializable {
    private static final long   serialVersionUID = 1233293332189L;
    public String total_amount;// 订单金额
    public String pay_amount;// 实际支付金额
    public String out_trade_no;// 参数名称：商家订单号
    public Integer trade_status;// 订单状态：1表示成功
    public String trade_no;// 平台订单号
    public String trade_time;// 交易时间
    public RequestFine(){

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

    public Integer getTrade_status() {
        return trade_status;
    }

    public void setTrade_status(Integer trade_status) {
        this.trade_status = trade_status;
    }

    public String getTrade_no() {
        return trade_no;
    }

    public void setTrade_no(String trade_no) {
        this.trade_no = trade_no;
    }

    public String getTrade_time() {
        return trade_time;
    }

    public void setTrade_time(String trade_time) {
        this.trade_time = trade_time;
    }

    public String getPay_amount() {
        return pay_amount;
    }

    public void setPay_amount(String pay_amount) {
        this.pay_amount = pay_amount;
    }
}
