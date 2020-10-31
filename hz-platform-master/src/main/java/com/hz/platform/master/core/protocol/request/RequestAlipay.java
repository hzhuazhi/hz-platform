package com.hz.platform.master.core.protocol.request;




import com.hz.platform.master.core.protocol.base.BaseRequest;

import java.io.Serializable;

/**
 * @Description TODO
 * @Author yoko
 * @Date 2019/12/19 20:06
 * @Version 1.0
 */
public class RequestAlipay extends BaseRequest implements Serializable {
    private static final long   serialVersionUID = 1233223332241L;

    public Long did;
    public String body;
    public String subject;
    public String outTradeNo;
    public String timeoutExpress;
    public String totalAmount;
    public String productCode;
    public String notifyUrl;
    public String returnUrl;
    public String adNum;

    public RequestAlipay(){

    }

    public Long getDid() {
        return did;
    }

    public void setDid(Long did) {
        this.did = did;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getTimeoutExpress() {
        return timeoutExpress;
    }

    public void setTimeoutExpress(String timeoutExpress) {
        this.timeoutExpress = timeoutExpress;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getAdNum() {
        return adNum;
    }

    public void setAdNum(String adNum) {
        this.adNum = adNum;
    }
}
