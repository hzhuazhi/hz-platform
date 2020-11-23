package com.hz.platform.master.core.protocol.request.notify;

import java.io.Serializable;

/**
 * @Description 华富-代付
 * @Author yoko
 * @Date 2020/11/21 21:57
 * @Version 1.0
 */
public class RequestOutHf implements Serializable {
    private static final long   serialVersionUID = 1233293332190L;
    public String uid;// 商户id
    public String balance;// 余额
    public String orderid;// 平台订单号
    public String out_trade_id;// 商户单号
    public Integer status;// 代付状态,1为等待付款,2为已完成,3为处理中,4为已驳回,5为待核对
    public String amount;// 提交金额
    public String money;// 实付金额
    public String poundage;// 手续费
    public String bankuser;// 收款姓名
    public String bankname;// 收款银行
    public String bankno;// 收款账号
    public String mobile;// 手机号
    public String attach;// 备注信息


    public RequestOutHf(){

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getOut_trade_id() {
        return out_trade_id;
    }

    public void setOut_trade_id(String out_trade_id) {
        this.out_trade_id = out_trade_id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getPoundage() {
        return poundage;
    }

    public void setPoundage(String poundage) {
        this.poundage = poundage;
    }

    public String getBankuser() {
        return bankuser;
    }

    public void setBankuser(String bankuser) {
        this.bankuser = bankuser;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getBankno() {
        return bankno;
    }

    public void setBankno(String bankno) {
        this.bankno = bankno;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }
}
