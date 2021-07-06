package com.hz.platform.master.core.protocol.request.notify;

import java.io.Serializable;

/**
 * @ClassName: RequestSz
 * @Description: 狮子支付-同步数据返回的实体属性Bean
 * @Author: yoko
 * @Date: $
 * @Version: 1.0
 **/
public class RequestSz implements Serializable {
    private static final long   serialVersionUID = 5233293332189L;

    public int code;// 0成功其它失败
    public String msg;// 信息
    public String merchant_code;// 商铺号
    public String order_id;// 商户订单号
    public String pay_no;// 平台订单号
    public String amount;// 付款金额
    public String resp_code;// 支付状态 00成功 其它失败
    public String sign;// 签名 详见签名

    public RequestSz(){

    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMerchant_code() {
        return merchant_code;
    }

    public void setMerchant_code(String merchant_code) {
        this.merchant_code = merchant_code;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getPay_no() {
        return pay_no;
    }

    public void setPay_no(String pay_no) {
        this.pay_no = pay_no;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getResp_code() {
        return resp_code;
    }

    public void setResp_code(String resp_code) {
        this.resp_code = resp_code;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
