package com.hz.platform.master.core.protocol.request.notify;

import java.io.Serializable;

/**
 * @ClassName:
 * @Description: 大信誉支付-同步数据返回的实体属性Bean
 * @Author: yoko
 * @Date: $
 * @Version: 1.0
 **/
public class RequestDxy implements Serializable {
    private static final long   serialVersionUID = 5633293332189L;

    public RequestDxy(){

    }

    public String memberid;// 平台分配商户号
    public String orderid;// 平台订单号
    public String amount;// 订单金额 单位：元
    public String transaction_id;// 交易流水号
    public String datetime;// 交易成功时间  时间格式：2018-12-28 18:18:18
    public String returncode;// “00” 为成功
    public String attach;// 商户附加数据返回
    public String sign;// 签名

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
