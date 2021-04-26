package com.hz.platform.master.core.protocol.response.pay;


import java.io.Serializable;

/**
 * @Description 查询网关返回的数据
 * @Author yoko
 * @Date 2021/4/26 17:17
 * @Version 1.0
 */
public class ResponseDataCore implements Serializable {
    private static final long   serialVersionUID = 2233023131150L;

    public String trade_no;// 平台订单号
    public String out_trade_no;// 渠道订单号
    public Integer trade_status;// 交易状态：1成功，其它失败
    public Integer send_status;// 同步状态：1成功，其它失败
    public String notify_url;// 同步地址
    public String trade_time;// 交易成功时间
    public ResponseDataCore(){

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

    public Integer getTrade_status() {
        return trade_status;
    }

    public void setTrade_status(Integer trade_status) {
        this.trade_status = trade_status;
    }

    public Integer getSend_status() {
        return send_status;
    }

    public void setSend_status(Integer send_status) {
        this.send_status = send_status;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getTrade_time() {
        return trade_time;
    }

    public void setTrade_time(String trade_time) {
        this.trade_time = trade_time;
    }
}
