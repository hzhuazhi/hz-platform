package com.hz.platform.master.core.protocol.request.whitelist;

import java.io.Serializable;

/**
 * @author yoko
 * @desc 众邦白名单的协议请求
 * @create 2022-07-05 15:05
 **/
public class RequestWhitelist implements Serializable {
    private static final long   serialVersionUID = 17833283332519L;
    public RequestWhitelist(){

    }

    /**
     * 渠道号
     */
    public String channel;

    /**
     * 白名单流水序号
     */
    public String serialNo;

    /**
     * 用户的白名单流水序号（渠道的）
     */
    public String userSerialNo;

    /**
     * 用户姓名
     */
    public String userName;

    /**
     * 身份证号
     */
    public String cardNum;

    /**
     * 身份证正面照片URL
     */
    public String cardImgFront;

    /**
     * 身份证反面照片URL
     */
    public String cardImgBack;

    /**
     * 手机号
     */
    public String phoneNum;

    /**
     * 银行卡号
     */
    public String bankCard;

    /**
     * 同步的接口地址:我方的同步地址
     */
    public String notifyUrl;

    /**
     * 签名值
     */
    public String sign;


    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getUserSerialNo() {
        return userSerialNo;
    }

    public void setUserSerialNo(String userSerialNo) {
        this.userSerialNo = userSerialNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getCardImgFront() {
        return cardImgFront;
    }

    public void setCardImgFront(String cardImgFront) {
        this.cardImgFront = cardImgFront;
    }

    public String getCardImgBack() {
        return cardImgBack;
    }

    public void setCardImgBack(String cardImgBack) {
        this.cardImgBack = cardImgBack;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }


    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
