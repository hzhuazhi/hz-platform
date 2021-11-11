package com.hz.platform.master.core.protocol.request.notify;

import java.io.Serializable;

/**
 * @author yoko
 * @desc 回传协议：ZZ的同步数据协议
 * @create 2021-11-10 11:13
 **/
public class RequestZz implements Serializable {
    private static final long   serialVersionUID = 1259293532189L;

    public RequestZz(){

    }

    public String code;// 状态码，0000为成功，其他为失败。
    public String msg;// 返回信息
    public ZzData data;


    public class ZzData{
        public ZzData(){

        }

        public String orderNo;// 订单号
        public String trade_dateTime;// 支付时间。
        public String fee;// 请求金额。单位:分
        public String money;// 实际支付金额。单位:分
        public String trade_type;// 交易类型。 0：未知的交易类型，1000：微信H5，2000：支付宝H5，2001：支付宝红包。
        public String transactionNumber;// 交易流水号。

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getTrade_dateTime() {
            return trade_dateTime;
        }

        public void setTrade_dateTime(String trade_dateTime) {
            this.trade_dateTime = trade_dateTime;
        }

        public String getFee() {
            return fee;
        }

        public void setFee(String fee) {
            this.fee = fee;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getTrade_type() {
            return trade_type;
        }

        public void setTrade_type(String trade_type) {
            this.trade_type = trade_type;
        }

        public String getTransactionNumber() {
            return transactionNumber;
        }

        public void setTransactionNumber(String transactionNumber) {
            this.transactionNumber = transactionNumber;
        }
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ZzData getData() {
        return data;
    }

    public void setData(ZzData data) {
        this.data = data;
    }
}
