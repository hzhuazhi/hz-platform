package com.hz.platform.master.core.protocol.request.notify;

import java.io.Serializable;

/**
 * @ClassName:
 * @Description: 回传协议：芒果的同步数据协议
 * @Author: yoko
 * @Date: $
 * @Version: 1.0
 **/
public class RequestMg implements Serializable {
    private static final long   serialVersionUID = 1253293532189L;

    public RequestMg(){

    }

    public String memberid;// 商户编号:平台分配商户号
    public String orderid;// 订单号:上送订单号唯一, 字符长度20
    public String amount;// 订单金额
    public String transaction_id;
    public String datetime;// 交易时间
    public String returncode;// 交易状态-00表示成功，其它表示失败
    public String attach;// 扩展返回商户附加数据返回
    public String sign;// MD5签名


    public String getMemberid() {
        return memberid;
    }

    public void setMemberid(String memberid) {
        this.memberid = memberid;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getReturncode() {
        return returncode;
    }

    public void setReturncode(String returncode) {
        this.returncode = returncode;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
