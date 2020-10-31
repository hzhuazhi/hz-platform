package com.hz.pay.master.core.protocol.request.notify;

import com.hz.pay.master.core.protocol.base.BaseRequest;

import java.io.Serializable;

/**
 * @Description TODO
 * @Author yoko
 * @Date 2020/3/25 15:15
 * @Version 1.0
 */
public class RequestJt extends BaseRequest implements Serializable {
    private static final long   serialVersionUID = 1233293332189L;
    public String memberid;// 商户编号
    public String orderid;// 订单号
    public String amount;// 订单金额
    public String transaction_id;// 交易流水号
    public String datetime;// 交易时间
    public String returncode;// 交易状态
    public String attach;// 商户附加数据返回
    public String sign;// 签名


    public RequestJt(){

    }

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

    @Override
    public String getSign() {
        return sign;
    }

    @Override
    public void setSign(String sign) {
        this.sign = sign;
    }
}
