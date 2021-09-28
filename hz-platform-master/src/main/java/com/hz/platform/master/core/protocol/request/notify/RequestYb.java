package com.hz.platform.master.core.protocol.request.notify;

import java.io.Serializable;

/**
 * @ClassName:
 * @Description: 易宝支付接口-同步数据返回的实体属性Bean
 * @Author: yoko
 * @Date: $
 * @Version: 1.0
 **/
public class RequestYb implements Serializable {
    private static final long   serialVersionUID = 125933293332169L;

    public RequestYb(){

    }

    public String merId;// 商户号
    public String orderId;// 商户单号
    public String sysOrderId;// 我方系统单号
    public String desc;// 系统描述
    public String orderAmt;// 订单金额
    public int status;// 订单状态：1为支付成功
    public String nonceStr;// 随机字符串
    public String sign;// 验签字段
    public String attch;// 附加信息


    public String getMerId() {
        return merId;
    }

    public void setMerId(String merId) {
        this.merId = merId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getSysOrderId() {
        return sysOrderId;
    }

    public void setSysOrderId(String sysOrderId) {
        this.sysOrderId = sysOrderId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getOrderAmt() {
        return orderAmt;
    }

    public void setOrderAmt(String orderAmt) {
        this.orderAmt = orderAmt;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getAttch() {
        return attch;
    }

    public void setAttch(String attch) {
        this.attch = attch;
    }
}
