package com.hz.platform.master.core.protocol.request.pay;

import com.hz.platform.master.core.protocol.base.BaseRequest;

import java.io.Serializable;

/**
 * @Description TODO
 * @Author yoko
 * @Date 2020/10/31 16:52
 * @Version 1.0
 */
public class RequestPayOut extends BaseRequest implements Serializable {
    private static final long   serialVersionUID = 1233293332190L;
    public String channel;// 商铺号
    public String trade_type;// 交易类型
    public String total_amount;// 参数名称：商家订单金额，订单总金额，单位为分
    public String out_trade_no;// 参数名称：商家订单号
    public String bank_name;// 银行名称
    public String bank_card;// 银行卡卡号
    public String account_name;// 开户人
    public String notify_url;// 异步通知地址
    public String interface_ver;// 接口版本
    public String return_url;// 参数名称：页面跳转同步通知地址支付成功后，通过页面跳转的方式跳转到商家网站
    public String extra_return_param;// 参数名称：回传参数商户如果支付请求是传递了该参数，则通知商户支付成功时会回传该参数
    public String client_ip;// 客户端IP地址
    public String sign;// 签名
    public String sub_time;// 提交时间
    public String product_name;// 商品名称
    public String product_code;// 商品码
    public String noredirect;// 默认没值或者没传表示走302跳转，有值则走200；描述（302跳转，由平台自动重定向到扫码的地址；200则返回一个网址，此网址就是扫码的页面地址）。

    public RequestPayOut(){

    }

    @Override
    public String getChannel() {
        return channel;
    }

    @Override
    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getBank_card() {
        return bank_card;
    }

    public void setBank_card(String bank_card) {
        this.bank_card = bank_card;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getInterface_ver() {
        return interface_ver;
    }

    public void setInterface_ver(String interface_ver) {
        this.interface_ver = interface_ver;
    }

    public String getReturn_url() {
        return return_url;
    }

    public void setReturn_url(String return_url) {
        this.return_url = return_url;
    }

    public String getExtra_return_param() {
        return extra_return_param;
    }

    public void setExtra_return_param(String extra_return_param) {
        this.extra_return_param = extra_return_param;
    }

    public String getClient_ip() {
        return client_ip;
    }

    public void setClient_ip(String client_ip) {
        this.client_ip = client_ip;
    }

    @Override
    public String getSign() {
        return sign;
    }

    @Override
    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSub_time() {
        return sub_time;
    }

    public void setSub_time(String sub_time) {
        this.sub_time = sub_time;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getNoredirect() {
        return noredirect;
    }

    public void setNoredirect(String noredirect) {
        this.noredirect = noredirect;
    }
}
