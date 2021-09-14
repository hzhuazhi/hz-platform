package com.hz.platform.master.core.protocol.request.notify;

import java.io.Serializable;

/**
 * @ClassName:
 * @Description: 花呗通支付接口-同步数据返回的实体属性Bean
 * @Author: yoko
 * @Date: $
 * @Version: 1.0
 **/
public class RequestHbt implements Serializable {
    private static final long   serialVersionUID = 5933293332169L;

    public RequestHbt(){

    }

    public String p1_merchantno;// 商户号: 请访问商户后台来获取您的商户号。
    public String p2_amount;// 支付金额: 以元为单位，精确到小数点后 2 位。如 15.00 及 15 都是合法的参数值。
    public String p3_orderno;// 订单号: 唯一标识您的支付平台的一笔订单。
    public String p4_status;// 订单状态:1: 未支付2: 已支付3: 支付失败
    public String p5_producttype;// 支付产品类型编码: 请参阅支付产品类型编码表。
    public String p6_requesttime;// 支付发起时间: 14 字节长的数字串，格式布局如 yyyyMMddHHmmss. (如 20060102150405 表示 2006年1月2日下午3点04分05秒).
    public String p7_goodsname;// 商品名称。
    public String p8_tradetime;// 交易时间:14 字节长的数字串，格式布局如 yyyyMMddHHmmss. (如 20060102150405 表示 2006年1月2日下午3点04分05秒).
    public String p9_porderno;// 平台交易流水号: 花呗通支付平台为您的订单生成的全局唯一的交易流水号。
    public String sign;// MD5 签名: HEX 大写, 32 字节。

    public String getP1_merchantno() {
        return p1_merchantno;
    }

    public void setP1_merchantno(String p1_merchantno) {
        this.p1_merchantno = p1_merchantno;
    }

    public String getP2_amount() {
        return p2_amount;
    }

    public void setP2_amount(String p2_amount) {
        this.p2_amount = p2_amount;
    }

    public String getP3_orderno() {
        return p3_orderno;
    }

    public void setP3_orderno(String p3_orderno) {
        this.p3_orderno = p3_orderno;
    }

    public String getP4_status() {
        return p4_status;
    }

    public void setP4_status(String p4_status) {
        this.p4_status = p4_status;
    }

    public String getP5_producttype() {
        return p5_producttype;
    }

    public void setP5_producttype(String p5_producttype) {
        this.p5_producttype = p5_producttype;
    }

    public String getP6_requesttime() {
        return p6_requesttime;
    }

    public void setP6_requesttime(String p6_requesttime) {
        this.p6_requesttime = p6_requesttime;
    }

    public String getP7_goodsname() {
        return p7_goodsname;
    }

    public void setP7_goodsname(String p7_goodsname) {
        this.p7_goodsname = p7_goodsname;
    }

    public String getP8_tradetime() {
        return p8_tradetime;
    }

    public void setP8_tradetime(String p8_tradetime) {
        this.p8_tradetime = p8_tradetime;
    }

    public String getP9_porderno() {
        return p9_porderno;
    }

    public void setP9_porderno(String p9_porderno) {
        this.p9_porderno = p9_porderno;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
