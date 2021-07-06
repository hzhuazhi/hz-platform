package com.hz.platform.master.core.protocol.request.notify;

import java.io.Serializable;

/**
 * @ClassName:
 * @Description: 回传协议：木星的同步数据协议
 * @Author: yoko
 * @Date: $
 * @Version: 1.0
 **/
public class RequestMx implements Serializable {
    private static final long   serialVersionUID = 1253293332189L;

    public RequestMx(){

    }

    public int code;// 代码, 成功: 11
    public String transactionid;// 流水ID  时有时无
    public String systemorderid;// 系统订单ID
    public String orderno;// 商户订单ID
    public String bankaccount;// 银行账号
    public String merno;// 商户号
    public String transamt;// 交易金额
    public String timeend;// 结束时间
    public String banktype;// 类型,固定值为PF
    public String productid;// 交易类型ID, 详细请查看交易类型表
    public String signature;// 签名

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getTransactionid() {
        return transactionid;
    }

    public void setTransactionid(String transactionid) {
        this.transactionid = transactionid;
    }

    public String getSystemorderid() {
        return systemorderid;
    }

    public void setSystemorderid(String systemorderid) {
        this.systemorderid = systemorderid;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public String getBankaccount() {
        return bankaccount;
    }

    public void setBankaccount(String bankaccount) {
        this.bankaccount = bankaccount;
    }

    public String getMerno() {
        return merno;
    }

    public void setMerno(String merno) {
        this.merno = merno;
    }

    public String getTransamt() {
        return transamt;
    }

    public void setTransamt(String transamt) {
        this.transamt = transamt;
    }

    public String getTimeend() {
        return timeend;
    }

    public void setTimeend(String timeend) {
        this.timeend = timeend;
    }

    public String getBanktype() {
        return banktype;
    }

    public void setBanktype(String banktype) {
        this.banktype = banktype;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
