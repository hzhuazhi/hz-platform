package com.hz.platform.master.util;

import com.alibaba.fastjson.JSON;
import com.hz.platform.master.core.common.utils.DateUtil;
import com.hz.platform.master.core.common.utils.StringUtil;
import com.hz.platform.master.core.common.utils.constant.ServerConstant;
import com.hz.platform.master.core.model.agent.AgentModel;
import com.hz.platform.master.core.model.alipay.AlipayH5Model;
import com.hz.platform.master.core.model.alipay.AlipayModel;
import com.hz.platform.master.core.model.alipay.AlipayNotifyModel;
import com.hz.platform.master.core.model.bufpay.BufpayModel;
import com.hz.platform.master.core.model.channeldata.ChannelDataModel;
import com.hz.platform.master.core.model.channelgeway.ChannelGewayModel;
import com.hz.platform.master.core.model.datacore.DataCoreModel;
import com.hz.platform.master.core.model.geway.GewaytradetypeModel;
import com.hz.platform.master.core.model.receivingaccount.ReceivingAccountModel;
import com.hz.platform.master.core.model.receivingaccountdata.ReceivingAccountDataModel;
import com.hz.platform.master.core.model.region.RegionModel;
import com.hz.platform.master.core.model.task.TaskAlipayNotifyModel;
import com.hz.platform.master.core.model.zfbapp.ZfbAppModel;
import com.hz.platform.master.core.protocol.request.notify.RequestBufPay;
import com.hz.platform.master.core.protocol.request.notify.RequestFine;
import com.hz.platform.master.core.protocol.request.notify.RequestJt;
import com.hz.platform.master.core.protocol.request.notify.RequestWn;
import com.hz.platform.master.core.protocol.request.pay.RequestPay;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Description 公共方法类
 * @Author yoko
 * @Date 2020/1/7 20:25
 * @Version 1.0
 */
public class HodgepodgeMethod {
    private static Logger log = LoggerFactory.getLogger(HodgepodgeMethod.class);

    /**
     * @Description: 组装查询地域的查询条件
     * @param ip
     * @return RegionModel
     * @author yoko
     * @date 2019/12/18 18:41
     */
    public static RegionModel assembleRegionModel(String ip){
        RegionModel resBean = new RegionModel();
        resBean.setIp(ip);
        return resBean;
    }


    /**
     * @Description: 通过枚举循环获取通道的支付码
     * @param myCode - 我方的支付码
     * @return String - 通道的支付码
     * @author yoko
     * @date 2019/12/16 18:03
     */
    public static Map<String, String> getPayCode(String myCode, int type){
        Map<String, String> map = new HashMap<>();
        for (ServerConstant.PayTypeEnum enums : ServerConstant.PayTypeEnum.values()){

            if (enums.getMyCode().equals(myCode)){
                map.put("payCode", enums.getOtherCode());
                if (!StringUtils.isBlank(enums.getServiceCharge())){
                    map.put("serviceCharge", enums.getServiceCharge());
                }else {
                    map.put("serviceCharge", "");
                }
                break;
            }
        }
        return map;
    }

    /**
     * @Description: 组装通道支付类型的查询条件
     * @param trade_type - 支付类型
     * @return
     * @author yoko
     * @date 2020/4/7 14:24
    */
    public static GewaytradetypeModel assembleGewaytradetypeQuery(String trade_type){
        GewaytradetypeModel resBean = new GewaytradetypeModel();
        resBean.setMyTradeType(trade_type);
        return resBean;
    }



    /**
     * @Description: 组装请求蜗牛支付的数据
     * @param requestData - 请求的基础数据
     * @param payId - 通道给我方的商品号
     * @param trade_type - 支付类型
     * @param total_amount - 支付金额
     * @param out_trade_no - 订单号
     * @param notify_url - 数据同步地址
     * @param sign - 签名
     * @param extra_return_param - 透传参数
     * @return java.lang.String
     * @author yoko
     * @date 2020/3/24 17:34
     */
    public static String assembleWnData(RequestPay requestData, String payId, String trade_type, String total_amount, String out_trade_no,
                                        String notify_url, String sign, String extra_return_param){
        String str = "";
        Map<String, String> map = new HashMap<>();
        map.put("app_id", payId);
        map.put("trade_type", trade_type);
        map.put("total_amount", total_amount);
        map.put("out_trade_no", out_trade_no);
        map.put("notify_url", notify_url);
        map.put("interface_version", "V2.0");
        if (!StringUtils.isBlank(requestData.return_url)){
            map.put("return_url", requestData.return_url);
        }
        if (!StringUtils.isBlank(extra_return_param)){
            map.put("extra_return_param", extra_return_param);
        }
        if (!StringUtils.isBlank(requestData.client_ip)){
            map.put("client_ip", requestData.client_ip);
        }
        map.put("sign", sign);
        str = JSON.toJSONString(map);
        return str;

    }


    /**
     * @Description: 组装请求蜗牛支付的数据-get形式
     * @param requestData - 请求的基础数据
     * @param payId - 通道给我方的商品号
     * @param trade_type - 支付类型
     * @param total_amount - 支付金额
     * @param out_trade_no - 订单号
     * @param notify_url - 数据同步地址
     * @param sign - 签名
     * @param extra_return_param - 透传参数
     * @return java.lang.String
     * @author yoko
     * @date 2020/3/24 17:34
     */
    public static String assembleWnGetData(RequestPay requestData, String payId, String trade_type, String total_amount, String out_trade_no,
                                        String notify_url, String sign, String extra_return_param){
        StringBuffer sb = new StringBuffer();
        sb.append("app_id=" + payId);
        sb.append("&trade_type=" + trade_type);
        sb.append("&total_amount=" + total_amount);
        sb.append("&out_trade_no=" + out_trade_no);

        sb.append("&notify_url=" + notify_url);
        sb.append("&interface_version=" + "V2.0");
        if (!StringUtils.isBlank(requestData.return_url)){
            sb.append("&return_url=" + requestData.return_url);
        }
        if (!StringUtils.isBlank(extra_return_param)){
            sb.append("&extra_return_param=" + extra_return_param);
        }
        if (!StringUtils.isBlank(requestData.client_ip)){
            sb.append("&client_ip=" + requestData.client_ip);
        }
        sb.append("&sign=" + sign);
        return sb.toString();

    }

    /**
     * @Description: 组装请求API支付/九通支付的数据-post形式
     * @param requestData - 请求的基础数据
     * @param payId - 通道给我方的商品号
     * @param trade_type - 支付类型
     * @param total_amount - 支付金额
     * @param out_trade_no - 订单号
     * @param notify_url - 数据同步地址
     * @param sign - 签名
     * @param extra_return_param - 透传参数
     * @param nowTime - 当前时间
     * @return java.lang.String
     * @author yoko
     * @date 2020/3/24 17:34
     */
    public static Map<String, String> assembleApiData(RequestPay requestData, String payId, String trade_type, String total_amount, String out_trade_no,
                                           String notify_url, String sign, String extra_return_param, String nowTime){
        Map<String, String> map = new HashMap<>();
        map.put("pay_memberid", payId);
        map.put("pay_orderid", out_trade_no);
        map.put("pay_applydate", nowTime);
        map.put("pay_bankcode", trade_type);
        map.put("pay_notifyurl", notify_url);
        if (!StringUtils.isBlank(requestData.return_url)){
            map.put("pay_callbackurl", requestData.return_url);
        }
        map.put("pay_amount", requestData.total_amount);
        map.put("pay_md5sign", sign);
        map.put("pay_attach", extra_return_param);
        if (!StringUtils.isBlank(extra_return_param)){
            map.put("extra_return_param", extra_return_param);
        }
        map.put("pay_productname", "礼品");
        map.put("pay_format", "json");
        return map;

    }


    /**
     * @Description: 组装请求BufPay个人支付的数据-post形式
     * @param requestData - 请求的基础数据
     * @param trade_type - 支付类型
     * @param out_trade_no - 订单号
     * @param notify_url - 数据同步地址
     * @param sign - 签名
     * @return java.lang.String
     * @author yoko
     * @date 2020/3/24 17:34
     */
    public static Map<String, String> assembleBufPayData(RequestPay requestData, String trade_type, String out_trade_no,
                                                      String notify_url, String sign, long channelId){
        Map<String, String> map = new HashMap<>();
        map.put("name", "礼品");
        map.put("pay_type", trade_type);
        map.put("price", requestData.total_amount);
        map.put("order_id", out_trade_no);
        map.put("order_uid", String.valueOf(channelId));
        map.put("notify_url", notify_url);
        if (!StringUtils.isBlank(requestData.return_url)){
            map.put("return_url", requestData.return_url);
        }
        map.put("feedback_url", "feedbackData");
        map.put("sign", sign);
        return map;

    }


    /**
     * @Description: 组装渠道数据
     * @param requestData - 请求的基础数据
     * @param myTradeNo - 我方订单号
     * @param channelId - 渠道主键ID
     * @param gewayId - 通道主键ID
     * @param channelGewayId - 渠道与通道的关联关系的ID：对应表tb_hz_channel_geway的主键ID
     * @param profitType - 收益类型：1普通收益类型，2多人分配收益类型
     * @param nowTime - 现在时间
     * @param serviceCharge - 手续费
     * @param sendFlag - false表示请求失败，true表示请求成功
     * @return ChannelDataModel
     * @author yoko
     * @date 2020/3/24 21:41
     */
    public static ChannelDataModel assembleChannelData(RequestPay requestData, String myTradeNo, long channelId, long gewayId,
                                                       long channelGewayId, int profitType, String nowTime, String my_notify_url, String serviceCharge, boolean sendFlag){
        ChannelDataModel resBean = new ChannelDataModel();
        resBean.setMyTradeNo(myTradeNo);
        resBean.setChannelId(channelId);
        resBean.setGewayId(gewayId);
        resBean.setChannel(requestData.channel);
        resBean.setChannelGewayId(channelGewayId);
        resBean.setProfitType(profitType);
        resBean.setTradeType(requestData.trade_type);
        resBean.setTotalAmount(requestData.total_amount);
        resBean.setServiceCharge(serviceCharge);
        String actualMoney = StringUtil.getMultiply(requestData.total_amount, serviceCharge);
        actualMoney = StringUtil.getBigDecimalSubtractStr(requestData.total_amount , actualMoney);
        resBean.setActualMoney(actualMoney);
        resBean.setOutTradeNo(requestData.out_trade_no);
        resBean.setNotifyUrl(requestData.notify_url);
        resBean.setMyNotifyUrl(my_notify_url);
        if (!StringUtils.isBlank(requestData.interface_ver)){
            resBean.setInterfaceVer(requestData.interface_ver);
        }
        if (!StringUtils.isBlank(requestData.return_url)){
            resBean.setReturnUrl(requestData.return_url);
        }
        if (!StringUtils.isBlank(requestData.extra_return_param)){
            resBean.setExtraReturnParam(requestData.extra_return_param);
        }
        if (!StringUtils.isBlank(requestData.client_ip)){
            resBean.setClientIp(requestData.client_ip);
        }
        resBean.setSign(requestData.sign);
        resBean.setSubTime(nowTime);
        if (!StringUtils.isBlank(requestData.product_name)){
            resBean.setProductName(requestData.product_name);
        }
        if (!StringUtils.isBlank(requestData.product_code)){
            resBean.setProductCode(requestData.product_code);
        }
        if (sendFlag){
            resBean.setSendOk(1);
        }else {
            resBean.setSendOk(2);
        }
        resBean.setCurday(DateUtil.getDayNumber(new Date()));
        resBean.setCurhour(DateUtil.getHour(new Date()));
        resBean.setCurminute(DateUtil.getCurminute(new Date()));
        return resBean;

    }



    /**
     * @Description: 组装上游同步的数据-蜗牛
     * @param requestModel - 上游同步的基本数据
     * @param channelDataModel - 渠道请求的基本数据
     * @param channelGewayModel - 渠道与通道关联关系
     * @param total_amount - 总金额
     * @param serviceCharge - 手续费
     * @param actualMoney - 实际金额
     * @param tradeStatus - 订单状态：1成功，2失败，3其它
     * @return DataCoreModel
     * @author yoko
     * @date 2020/3/25 14:08
     */
    public static DataCoreModel assembleDataCore(RequestWn requestModel, ChannelDataModel channelDataModel, ChannelGewayModel channelGewayModel,
                                                 String total_amount, String serviceCharge, String actualMoney, int tradeStatus){
        DataCoreModel resBean = new DataCoreModel();
        resBean.setMyTradeNo(requestModel.getOut_trade_no());
        resBean.setTradeNo(requestModel.trade_no);
        resBean.setOutTradeNo(channelDataModel.getOutTradeNo());
        resBean.setTotalAmount(total_amount);
        resBean.setServiceCharge(serviceCharge);
        resBean.setActualMoney(actualMoney);
        resBean.setTradeStatus(tradeStatus);
        resBean.setExtraReturnParam(requestModel.extra_return_param);
        resBean.setTradeTime(requestModel.trade_time);
        resBean.setSign(requestModel.sign);
        resBean.setChannelId(channelDataModel.getChannelId());
        resBean.setGewayId(channelDataModel.getGewayId());
        resBean.setNotifyUrl(channelDataModel.getNotifyUrl());
//        resBean.setNotifySuc();
        if (!StringUtils.isBlank(channelDataModel.getExtraReturnParam())){
            resBean.setXyExtraReturnParam(channelDataModel.getExtraReturnParam());
        }
        resBean.setDeductRatio(channelGewayModel.getDeductRatio());
        resBean.setCurday(DateUtil.getDayNumber(new Date()));
        resBean.setCurhour(DateUtil.getHour(new Date()));
        resBean.setCurminute(DateUtil.getCurminute(new Date()));
        return resBean;

    }


    /**
     * @Description: 组装上游同步的数据-九通
     * @param requestModel - 上游同步的基本数据
     * @param channelDataModel - 渠道请求的基本数据
     * @param channelGewayModel - 渠道与通道关联关系
     * @param total_amount - 总金额
     * @param serviceCharge - 手续费
     * @param actualMoney - 实际金额
     * @param tradeStatus - 订单状态：1成功，2失败，3其它
     * @return DataCoreModel
     * @author yoko
     * @date 2020/3/25 14:08
     */
    public static DataCoreModel assembleDataCoreJt(RequestJt requestModel, ChannelDataModel channelDataModel, ChannelGewayModel channelGewayModel,
                                                   String total_amount, String serviceCharge, String actualMoney, int tradeStatus){
        DataCoreModel resBean = new DataCoreModel();
        resBean.setMyTradeNo(requestModel.orderid);
        resBean.setTradeNo(requestModel.transaction_id);
        resBean.setOutTradeNo(channelDataModel.getOutTradeNo());
        resBean.setTotalAmount(total_amount);
        resBean.setServiceCharge(serviceCharge);
        resBean.setActualMoney(actualMoney);
        resBean.setTradeStatus(tradeStatus);
        resBean.setExtraReturnParam(requestModel.attach);
        resBean.setTradeTime(requestModel.datetime);
        resBean.setSign(requestModel.sign);
        resBean.setChannelId(channelDataModel.getChannelId());
        resBean.setGewayId(channelDataModel.getGewayId());
        resBean.setNotifyUrl(channelDataModel.getNotifyUrl());
//        resBean.setNotifySuc();
        if (!StringUtils.isBlank(channelDataModel.getExtraReturnParam())){
            resBean.setXyExtraReturnParam(channelDataModel.getExtraReturnParam());
        }
        resBean.setDeductRatio(channelGewayModel.getDeductRatio());
        resBean.setCurday(DateUtil.getDayNumber(new Date()));
        resBean.setCurhour(DateUtil.getHour(new Date()));
        resBean.setCurminute(DateUtil.getCurminute(new Date()));
        return resBean;

    }


    /**
     * @Description: 根据比例获取随机获取支付宝账号
     * @param zfbAppList - 支付宝账号数据
     * @return ZfbAppModel
     * @author yoko
     * @date 2020/4/9 17:23
    */
    public static ZfbAppModel ratioZfbAppMethod(List<ZfbAppModel> zfbAppList){
        ZfbAppModel resBean = new ZfbAppModel();
        int start = 0;
        int end = 0;
        for (int i = 0; i < zfbAppList.size(); i++){
            start = end;
            end += zfbAppList.get(i).getRatio();
            zfbAppList.get(i).setStart(start);
            zfbAppList.get(i).setEnd(end);
        }
        int random = new Random().nextInt(end);
        for (int i = 0; i < zfbAppList.size(); i++){
            if (random >= zfbAppList.get(i).getStart() && random < zfbAppList.get(i).getEnd()){
                resBean = zfbAppList.get(i);
                break;
            }
        }
        return resBean;
    }



    /**
     * @Description: 组装阿里支付的订单访问数据-H5
     * @param requestAlipay - 阿里支付订单的基本信息
     * @param outTradeNo - 交易订单号
     * @return AlipayModel
     * @author yoko
     * @date 2019/12/19 20:36
     */
    public static AlipayH5Model assembleH5AlipayData(RequestPay requestAlipay, String outTradeNo){
        AlipayH5Model resBean = new AlipayH5Model();
        if (requestAlipay != null){
            resBean.body = "费用缴纳";
            resBean.subject = "500费用";
            resBean.out_trade_no = outTradeNo;
            resBean.timeout_express = "30m";
            resBean.total_amount = requestAlipay.total_amount;
            if (!StringUtils.isBlank(requestAlipay.product_code)){
                resBean.product_code = requestAlipay.product_code;
            }else {
                resBean.product_code = "500_HY";
            }
        }
        return resBean;
    }


    /**
     * @Description: 组装阿里支付请求的纪录数据-H5
     * @param alipayH5Model - 阿里支付的数据-H5
     * @param aliOrder - 阿里支付宝请求之后返回的订单串
     * @return
     * @author yoko
     * @date 2019/12/26 14:42
     */
    public static AlipayModel assembleH5AlipayModel(AlipayH5Model alipayH5Model, long channelId, String aliOrder){
        AlipayModel resBean = new AlipayModel();
        resBean.memberId = channelId;
        resBean.body = alipayH5Model.body;
        resBean.subject = alipayH5Model.subject;
        resBean.outTradeNo = alipayH5Model.out_trade_no;
        resBean.timeoutExpress = alipayH5Model.timeout_express;
        resBean.totalAmount = alipayH5Model.total_amount;
        resBean.productCode = alipayH5Model.product_code;
        resBean.dataType = ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO;
        if (!StringUtils.isBlank(aliOrder)){
            resBean.setAliOrder(aliOrder);
        }
        resBean.setCurday(DateUtil.getDayNumber(new Date()));
        resBean.setCurhour(DateUtil.getHour(new Date()));
        resBean.setCurminute(DateUtil.getCurminute(new Date()));
        return resBean;
    }


    /**
     * @Description: 组装阿里云支付宝的订单回调结果数据
     * @param map - 阿里云支付宝返回的订单结果数据
     * @return AlipayNotifyModel
     * @author yoko
     * @date 2019/12/27 15:54
     */
    public static AlipayNotifyModel assembleAlipayNotify(Map<String, String> map) throws Exception{
        if (map == null){
            return null;
        }
        AlipayNotifyModel resBean = new AlipayNotifyModel();
        if (map.containsKey("app_id")){
            if (!StringUtils.isBlank(map.get("app_id"))){
                resBean.appId = map.get("app_id");
            }
        }
        if (map.containsKey("auth_app_id")){
            if (!StringUtils.isBlank(map.get("auth_app_id"))){
                resBean.authAppId = map.get("auth_app_id");
            }
        }
        if (map.containsKey("body")){
            if (!StringUtils.isBlank(map.get("body"))){
                resBean.body = map.get("body");
            }
        }
        if (map.containsKey("buyer_id")){
            if (!StringUtils.isBlank(map.get("buyer_id"))){
                resBean.buyerId = map.get("buyer_id");
            }
        }
        if (map.containsKey("buyer_logon_id")){
            if (!StringUtils.isBlank(map.get("buyer_logon_id"))){
                resBean.buyerLogonId = map.get("buyer_logon_id");
            }
        }
        if (map.containsKey("buyer_pay_amount")){
            if (!StringUtils.isBlank(map.get("buyer_pay_amount"))){
                resBean.buyerPayAmount = map.get("buyer_pay_amount");
            }
        }
        if (map.containsKey("charset")){
            if (!StringUtils.isBlank(map.get("charset"))){
                resBean.dataCharset = map.get("charset");
            }
        }
        if (map.containsKey("fund_bill_list")){
            if (!StringUtils.isBlank(map.get("fund_bill_list"))){
                resBean.fundBillList = map.get("fund_bill_list");
            }
        }
        if (map.containsKey("gmt_create")){
            if (!StringUtils.isBlank(map.get("gmt_create"))){
                resBean.gmtCreate = map.get("gmt_create");
            }
        }
        if (map.containsKey("gmt_payment")){
            if (!StringUtils.isBlank(map.get("gmt_payment"))){
                resBean.gmtPayment = map.get("gmt_payment");
            }
        }
        if (map.containsKey("invoice_amount")){
            if (!StringUtils.isBlank(map.get("invoice_amount"))){
                resBean.invoiceAmount = map.get("invoice_amount");
            }
        }
        if (map.containsKey("notify_id")){
            if (!StringUtils.isBlank(map.get("notify_id"))){
                resBean.notifyId = map.get("notify_id");
            }
        }
        if (map.containsKey("notify_time")){
            if (!StringUtils.isBlank(map.get("notify_time"))){
                resBean.notifyTime = map.get("notify_time");
            }
        }
        if (map.containsKey("notify_type")){
            if (!StringUtils.isBlank(map.get("notify_type"))){
                resBean.notifyType = map.get("notify_type");
            }
        }
        if (map.containsKey("out_trade_no")){
            if (!StringUtils.isBlank(map.get("out_trade_no"))){
                resBean.outTradeNo = map.get("out_trade_no");
            }
        }
        if (map.containsKey("point_amount")){
            if (!StringUtils.isBlank(map.get("point_amount"))){
                resBean.pointAmount = map.get("point_amount");
            }
        }
        if (map.containsKey("receipt_amount")){
            if (!StringUtils.isBlank(map.get("receipt_amount"))){
                resBean.receiptAmount = map.get("receipt_amount");
            }
        }
        if (map.containsKey("seller_email")){
            if (!StringUtils.isBlank(map.get("seller_email"))){
                resBean.sellerEmail = map.get("seller_email");
            }
        }
        if (map.containsKey("seller_id")){
            if (!StringUtils.isBlank(map.get("seller_id"))){
                resBean.sellerId = map.get("seller_id");
            }
        }
        if (map.containsKey("subject")){
            if (!StringUtils.isBlank(map.get("subject"))){
                resBean.subject = map.get("subject");
            }
        }
        if (map.containsKey("total_amount")){
            if (!StringUtils.isBlank(map.get("total_amount"))){
                resBean.totalAmount = map.get("total_amount");
            }
        }
        if (map.containsKey("trade_no")){
            if (!StringUtils.isBlank(map.get("trade_no"))){
                resBean.tradeNo = map.get("trade_no");
            }
        }
        if (map.containsKey("trade_status")){
            if (!StringUtils.isBlank(map.get("trade_status"))){
                resBean.tradeStatus = map.get("trade_status");
            }
        }
        if (map.containsKey("version")){
            if (!StringUtils.isBlank(map.get("version"))){
                resBean.dataVersion = map.get("version");
            }
        }
        resBean.setCurday(DateUtil.getDayNumber(new Date()));
        resBean.setCurhour(DateUtil.getHour(new Date()));
        resBean.setCurminute(DateUtil.getCurminute(new Date()));
        return resBean;
    }



    /**
     * @Description: 组装上游同步的数据-阿里支付宝数据
     * @param taskAlipayNotifyModel - 上游同步的基本数据 - 支付宝数据
     * @param channelDataModel - 渠道请求的基本数据
     * @param channelGewayModel - 渠道与通道关联关系
     * @param total_amount - 总金额
     * @param serviceCharge - 手续费
     * @param actualMoney - 实际金额
     * @param tradeStatus - 订单状态：1成功，2失败，3其它
     * @return DataCoreModel
     * @author yoko
     * @date 2020/3/25 14:08
     */
    public static DataCoreModel assembleDataCoreAlipay(TaskAlipayNotifyModel taskAlipayNotifyModel, ChannelDataModel channelDataModel, ChannelGewayModel channelGewayModel,
                                                       String total_amount, String serviceCharge, String actualMoney, int tradeStatus){
        DataCoreModel resBean = new DataCoreModel();
        resBean.setMyTradeNo(taskAlipayNotifyModel.getOutTradeNo());
        resBean.setTradeNo(taskAlipayNotifyModel.getTradeNo());
        resBean.setOutTradeNo(channelDataModel.getOutTradeNo());
        resBean.setTotalAmount(total_amount);
        resBean.setServiceCharge(serviceCharge);
        resBean.setActualMoney(actualMoney);
        resBean.setTradeStatus(tradeStatus);
        resBean.setExtraReturnParam(taskAlipayNotifyModel.getBody());
        resBean.setTradeTime(taskAlipayNotifyModel.getGmtPayment());
//        resBean.setSign(requestModel.sign);
        resBean.setChannelId(channelDataModel.getChannelId());
        resBean.setGewayId(channelDataModel.getGewayId());
        resBean.setNotifyUrl(channelDataModel.getNotifyUrl());
//        resBean.setNotifySuc();
        if (!StringUtils.isBlank(channelDataModel.getExtraReturnParam())){
            resBean.setXyExtraReturnParam(channelDataModel.getExtraReturnParam());
        }
        resBean.setDeductRatio(channelGewayModel.getDeductRatio());
        resBean.setCurday(DateUtil.getDayNumber(new Date()));
        resBean.setCurhour(DateUtil.getHour(new Date()));
        resBean.setCurminute(DateUtil.getCurminute(new Date()));
        return resBean;

    }



    /**
     * @Description: 组装上游同步的数据-BufPay
     * @param requestModel - 上游同步的基本数据
     * @param channelDataModel - 渠道请求的基本数据
     * @param channelGewayModel - 渠道与通道关联关系
     * @param total_amount - 总金额
     * @param serviceCharge - 手续费
     * @param actualMoney - 实际金额
     * @param tradeStatus - 订单状态：1成功，2失败，3其它
     * @return DataCoreModel
     * @author yoko
     * @date 2020/3/25 14:08
     */
    public static DataCoreModel assembleDataCoreBufPay(RequestBufPay requestModel, ChannelDataModel channelDataModel, ChannelGewayModel channelGewayModel,
                                                       String total_amount, String serviceCharge, String actualMoney, int tradeStatus) throws Exception{
        DataCoreModel resBean = new DataCoreModel();
        resBean.setMyTradeNo(requestModel.order_id);
        resBean.setTradeNo(requestModel.aoid);
        resBean.setOutTradeNo(channelDataModel.getOutTradeNo());
        resBean.setTotalAmount(total_amount);
        resBean.setServiceCharge(serviceCharge);
        resBean.setActualMoney(actualMoney);
        resBean.setTradeStatus(tradeStatus);
        String priceInfo = "";// 订单金额是否与实际金额相等
        if (tradeStatus == 2){
            priceInfo = "订单金额是否与实际金额不匹配";
        }
        resBean.setExtraReturnParam(requestModel.pay_price + priceInfo);// 用户实际支付的金额
        resBean.setTradeTime(DateUtil.getNowPlusTime());
        resBean.setSign(requestModel.sign);
        resBean.setChannelId(channelDataModel.getChannelId());
        resBean.setGewayId(channelDataModel.getGewayId());
        resBean.setNotifyUrl(channelDataModel.getNotifyUrl());
//        resBean.setNotifySuc();
        if (!StringUtils.isBlank(channelDataModel.getExtraReturnParam())){
            resBean.setXyExtraReturnParam(channelDataModel.getExtraReturnParam());
        }
        resBean.setDeductRatio(channelGewayModel.getDeductRatio());
        resBean.setCurday(DateUtil.getDayNumber(new Date()));
        resBean.setCurhour(DateUtil.getHour(new Date()));
        resBean.setCurminute(DateUtil.getCurminute(new Date()));
        return resBean;

    }



    /**
     * @Description: 根据比例获取随机获取bufPay的数据
     * @param bufpayList - 支付宝账号数据
     * @return ZfbAppModel
     * @author yoko
     * @date 2020/4/9 17:23
     */
    public static BufpayModel ratioBufPayMethod(List<BufpayModel> bufpayList){
        BufpayModel resBean = new BufpayModel();
        int start = 0;
        int end = 0;
        for (int i = 0; i < bufpayList.size(); i++){
            start = end;
            end += bufpayList.get(i).getRatio();
            bufpayList.get(i).setStart(start);
            bufpayList.get(i).setEnd(end);
        }
        int random = new Random().nextInt(end);
        for (int i = 0; i < bufpayList.size(); i++){
            if (random >= bufpayList.get(i).getStart() && random < bufpayList.get(i).getEnd()){
                resBean = bufpayList.get(i);
                break;
            }
        }

        return resBean;
    }


    /**
     * @Description: 组装查询收款账号的数据纪录
     * @param bufPayId - bufPay的账号ID
     * @param receivingAccountId - 收款账号的ID
     * @param type - 组装数据类型：1日，2月，3年
     * @return ReceivingAccountModel
     * @author yoko
     * @date 2020/4/20 18:44
     */
    public static ReceivingAccountDataModel assembleReceivingAccountDataQuery(Long bufPayId, Long receivingAccountId, int type){
        ReceivingAccountDataModel resBean = new ReceivingAccountDataModel();
        if (bufPayId != null && bufPayId != 0){
            resBean.setBufPayId(bufPayId);
        }
        if (receivingAccountId != null && receivingAccountId != 0){
            resBean.setReceivingAccountId(receivingAccountId);
        }
        if (type == 1){
            resBean.setCurday(DateUtil.getDayNumber(new Date()));
        } else if (type == 2){
            resBean.setCurdayStart(Integer.parseInt(DateUtil.getFirstDayOfThisMonth()));
            resBean.setCurdayEnd(Integer.parseInt(DateUtil.getMaxDayOfThisMonth()));
        }else if (type == 3){
            resBean.setCurdayStart(20200101);
            resBean.setCurdayEnd(20201231);
        }
        resBean.setIsOk(2);
        return resBean;
    }

    /**
     * @Description: 组装查询收款账号的查询条件
     * @param bufPayId - bufPay的账号ID
     * @param payCode - 支付类型：支付宝OR微信
     * @return
     * @author yoko
     * @date 2020/4/20 19:32
    */
    public static ReceivingAccountModel assembleReceivingAccountQuery(long bufPayId, String payCode){
        ReceivingAccountModel resBean = new ReceivingAccountModel();
        resBean.setBufPayId(bufPayId);
        if (payCode.equals("alipay")){
            resBean.setAccountType(1);
        }else if (payCode.equals("wechat")){
            resBean.setAccountType(2);
        }
        return resBean;
    }



    /**
     * @Description: 组装收款账号的数据纪录的数据
     * @param requestData - 请求的基本数据
     * @param myTradeNo - 我方订单号
     * @param channelId - 渠道号的ID
     * @param gewayId - 通道ID
     * @param bufPayId - bufPay的账号ID
     * @param receivingAccountId - 收款账号的ID
     * @param serviceCharge - 手续费
     * @return ReceivingAccountDataModel
     * @author yoko
     * @date 2020/4/21 11:38
     */
    public static ReceivingAccountDataModel assembleReceivingAccountData(RequestPay requestData, String myTradeNo, long channelId, long gewayId, long bufPayId, long receivingAccountId, String serviceCharge) {
        ReceivingAccountDataModel resBean = new ReceivingAccountDataModel();
        resBean.setMyTradeNo(myTradeNo);
        resBean.setChannelId(channelId);
        resBean.setGewayId(gewayId);
        resBean.setBufPayId(bufPayId);
        resBean.setReceivingAccountId(receivingAccountId);
        resBean.setTotalAmount(requestData.total_amount);
        resBean.setServiceCharge(serviceCharge);
        String actualMoney = StringUtil.getMultiply(requestData.total_amount, serviceCharge);
        actualMoney = StringUtil.getBigDecimalSubtractStr(requestData.total_amount, actualMoney);
        resBean.setActualMoney(actualMoney);
        resBean.setOutTradeNo(requestData.out_trade_no);
        resBean.setCurday(DateUtil.getDayNumber(new Date()));
        resBean.setCurhour(DateUtil.getHour(new Date()));
        resBean.setCurminute(DateUtil.getCurminute(new Date()));
        return resBean;
    }

    /**
     * @Description: 计算替换订单价格的下标位
     * @param str
     * @return
     * @author yoko
     * @date 2020/4/21 14:05
    */
    public static Map<String, Integer> getStrIndexofByMoney(String str) throws Exception{
        int start = 0;
        int end = 0;
        int jl = 0;
        if (str.indexOf("<span id=\"copy_price\">") > -1){
            start = str.indexOf("<span id=\"copy_price\">");
        }
        if (start > -1){
            end = str.indexOf("</span>", start);
        }
        String sb = "<span id=\"copy_price\">";
        jl = sb.length();
        Map<String, Integer> map = new HashMap<>();
        if (start != 0 && end !=0 && jl != 0){
            map.put("start", start);
            map.put("end", end);
            map.put("jl", jl);
            return map;
        }else {
            return null;
        }
    }

    /**
     * @Description: 计算替换二维码地址的下标位
     * @return
     * @author yoko
     * @date 2020/4/21 14:05
    */
    public static Map<String, Integer> getStrIndexofByQrCode(String str) throws Exception{
        int start = 0;
        int end = 0;
        int jl = 0;
        if (str.indexOf("<img id=\"qr\" style=\"width:150px; border-radius: 3px;\" src=\"") > -1){
            start = str.indexOf("<img id=\"qr\" style=\"width:150px; border-radius: 3px;\" src=\"");
        }
        if (start > -1){
            end = str.indexOf("\"/>", start);
        }
        String sb = "<img id=\"qr\" style=\"width:150px; border-radius: 3px;\" src=\"";
        jl = sb.length();
        Map<String, Integer> map = new HashMap<>();
        if (start != 0 && end !=0 && jl != 0){
            map.put("start", start);
            map.put("end", end);
            map.put("jl", jl);
            return map;
        }else {
            return null;
        }
    }


    /**
     * @Description: 组装上游同步的数据-美好-fine
     * @param requestModel - 上游同步的基本数据
     * @param channelDataModel - 渠道请求的基本数据
     * @param channelGewayModel - 渠道与通道关联关系
     * @param total_amount - 总金额
     * @param serviceCharge - 手续费
     * @param actualMoney - 实际金额
     * @param tradeStatus - 订单状态：1成功，2失败，3其它
     * @param payAmount - 实际支付金额：用户实际支付的金额
     * @param payActualMoney - 实际支付金额扣手续费后的金额
     * @param moneyFitType - 订单金额是否与实际支付金额一致：1初始化，2少了，3多了，4一致
     * @return DataCoreModel
     * @author yoko
     * @date 2020/3/25 14:08
     */
    public static DataCoreModel assembleDataCoreFine(RequestFine requestModel, ChannelDataModel channelDataModel, ChannelGewayModel channelGewayModel,
                                                     String total_amount, String serviceCharge, String actualMoney, int tradeStatus, String payAmount,
                                                     String payActualMoney, int moneyFitType, long channelGewayId, int profitType) throws Exception{
        DataCoreModel resBean = new DataCoreModel();
        resBean.setMyTradeNo(requestModel.out_trade_no);
        resBean.setTradeNo(requestModel.trade_no);
        resBean.setOutTradeNo(channelDataModel.getOutTradeNo());
        resBean.setTotalAmount(total_amount);
        resBean.setServiceCharge(serviceCharge);
        resBean.setActualMoney(actualMoney);
        resBean.setPayAmount(payAmount);
        resBean.setPayActualMoney(payActualMoney);
        resBean.setTradeStatus(tradeStatus);
        String priceInfo = "";// 订单金额是否与实际金额相等
        if (tradeStatus == 2){
            priceInfo = "订单金额是否与实际金额不匹配";
        }
        resBean.setExtraReturnParam(requestModel.total_amount + priceInfo);// 用户实际支付的金额
        resBean.setTradeTime(DateUtil.getNowPlusTime());
//        resBean.setSign(requestModel.sign);
        resBean.setChannelId(channelDataModel.getChannelId());
        resBean.setGewayId(channelDataModel.getGewayId());
        resBean.setNotifyUrl(channelDataModel.getNotifyUrl());
//        resBean.setNotifySuc();
        if (!StringUtils.isBlank(channelDataModel.getExtraReturnParam())){
            resBean.setXyExtraReturnParam(channelDataModel.getExtraReturnParam());
        }
        resBean.setDeductRatio(channelGewayModel.getDeductRatio());

        resBean.setChannelGewayId(channelGewayId);
        resBean.setProfitType(profitType);

        resBean.setMoneyFitType(moneyFitType);
        resBean.setCurday(DateUtil.getDayNumber(new Date()));
        resBean.setCurhour(DateUtil.getHour(new Date()));
        resBean.setCurminute(DateUtil.getCurminute(new Date()));
        return resBean;

    }

    /**
     * @Description: 组装更新代理的金额
     * @param id
     * @param money
     * @return AgentModel
     * @author yoko
     * @date 2020/9/17 22:38
     */
    public static AgentModel assembleAgentUpdate(long id, String money){
        AgentModel resBean = new AgentModel();
        resBean.setId(id);
        BigDecimal bd = new BigDecimal(money);
        resBean.setMoney(bd);
        return resBean;
    }


    public static void main(String [] args){
        String str = "<h1 style=\"margin: 20px 0px;\">请手动输入 ￥<span id=\"copy_price\">0.01</span></h1>";
        int start = 0;
        int end = 0;
        int jl = 0;
        if (str.indexOf("<span id=\"copy_price\">") > -1){
            start = str.indexOf("<span id=\"copy_price\">");
        }
        if (start > -1){
            end = str.indexOf("</span>", start);
        }
        String sb = "<span id=\"copy_price\">";
        jl = sb.length();
        StringBuilder str1 = new StringBuilder(str);
        str1.replace(start + jl, end, "1");
        System.out.println(start);
        System.out.println(end);
        System.out.println(str1);
    }






}
