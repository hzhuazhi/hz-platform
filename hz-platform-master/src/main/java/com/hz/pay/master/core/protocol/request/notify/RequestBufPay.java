package com.hz.pay.master.core.protocol.request.notify;

import com.hz.pay.master.core.protocol.base.BaseRequest;

import java.io.Serializable;

/**
 * @Description BufPay同步数据
 * @Author yoko
 * @Date 2020/4/15 15:18
 * @Version 1.0
 */
public class RequestBufPay extends BaseRequest implements Serializable {
    private static final long   serialVersionUID = 1233298332189L;
    public String aoid;// 交易流水号 bufpay平台订单唯一标识
    public String order_id;// 订单号 你传入的 order_id 参数
    public String order_uid;// 你传入的 order_uid 参数
    public String price;// 订单价格
    public String pay_price;// 用户支付的金额
    public String sign;// 签名


    public RequestBufPay(){

    }


    public String getAoid() {
        return aoid;
    }

    public void setAoid(String aoid) {
        this.aoid = aoid;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_uid() {
        return order_uid;
    }

    public void setOrder_uid(String order_uid) {
        this.order_uid = order_uid;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPay_price() {
        return pay_price;
    }

    public void setPay_price(String pay_price) {
        this.pay_price = pay_price;
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
