package com.hz.platform.master.util;

import com.alibaba.fastjson.JSON;
import com.hz.platform.master.core.common.exception.ServiceException;
import com.hz.platform.master.core.common.utils.BeanUtils;
import com.hz.platform.master.core.common.utils.DateUtil;
import com.hz.platform.master.core.common.utils.MD5Util;
import com.hz.platform.master.core.common.utils.StringUtil;
import com.hz.platform.master.core.common.utils.constant.CacheKey;
import com.hz.platform.master.core.common.utils.constant.CachedKeyUtils;
import com.hz.platform.master.core.common.utils.constant.ServerConstant;
import com.hz.platform.master.core.model.agent.AgentChannelGewayModel;
import com.hz.platform.master.core.model.agent.AgentModel;
import com.hz.platform.master.core.model.agent.AgentProfitModel;
import com.hz.platform.master.core.model.alipay.AlipayH5Model;
import com.hz.platform.master.core.model.alipay.AlipayModel;
import com.hz.platform.master.core.model.alipay.AlipayNotifyModel;
import com.hz.platform.master.core.model.bufpay.BufpayModel;
import com.hz.platform.master.core.model.channel.ChannelModel;
import com.hz.platform.master.core.model.channel.ChannelProfitModel;
import com.hz.platform.master.core.model.channelbalancededuct.ChannelBalanceDeductModel;
import com.hz.platform.master.core.model.channelchange.ChannelChangeModel;
import com.hz.platform.master.core.model.channeldata.ChannelDataModel;
import com.hz.platform.master.core.model.channelgeway.ChannelGewayModel;
import com.hz.platform.master.core.model.channelout.ChannelOutModel;
import com.hz.platform.master.core.model.channelwithdraw.ChannelWithdrawModel;
import com.hz.platform.master.core.model.datacore.DataCoreModel;
import com.hz.platform.master.core.model.datacoreout.DataCoreOutModel;
import com.hz.platform.master.core.model.geway.GewayModel;
import com.hz.platform.master.core.model.geway.GewayProfitModel;
import com.hz.platform.master.core.model.geway.GewaytradetypeModel;
import com.hz.platform.master.core.model.preparerecharge.PrepareRechargeModel;
import com.hz.platform.master.core.model.receivingaccount.ReceivingAccountModel;
import com.hz.platform.master.core.model.receivingaccountdata.ReceivingAccountDataModel;
import com.hz.platform.master.core.model.region.RegionModel;
import com.hz.platform.master.core.model.strategy.StrategyData;
import com.hz.platform.master.core.model.strategy.StrategyModel;
import com.hz.platform.master.core.model.task.TaskAlipayNotifyModel;
import com.hz.platform.master.core.model.withdraw.WithdrawModel;
import com.hz.platform.master.core.model.zfbapp.ZfbAppModel;
import com.hz.platform.master.core.protocol.request.notify.*;
import com.hz.platform.master.core.protocol.request.pay.RequestPay;
import com.hz.platform.master.core.protocol.request.pay.RequestPayOut;
import com.hz.platform.master.core.protocol.response.channel.ResponseChannel;
import com.hz.platform.master.core.protocol.response.pay.ResponseDataCore;
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
     * @param gewayName - 通道名称
     * @param channelGewayId - 渠道与通道的关联关系的ID：对应表tb_hz_channel_geway的主键ID
     * @param profitType - 收益类型：1普通收益类型，2多人分配收益类型
     * @param nowTime - 现在时间
     * @param serviceCharge - 手续费
     * @param sendFlag - false表示请求失败，true表示请求成功
     * @return ChannelDataModel
     * @author yoko
     * @date 2020/3/24 21:41
     */
    public static ChannelDataModel assembleChannelData(RequestPay requestData, String myTradeNo, long channelId, long gewayId, String gewayName,
                                                       long channelGewayId, int profitType, String nowTime, String my_notify_url, String serviceCharge, boolean sendFlag){
        ChannelDataModel resBean = new ChannelDataModel();
        resBean.setMyTradeNo(myTradeNo);
        resBean.setChannelId(channelId);
        resBean.setGewayId(gewayId);
        if (!StringUtils.isBlank(gewayName)){
            resBean.setGewayName(gewayName);
        }
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
     * @Description: 组装上游同步的数据-微派
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
    public static DataCoreModel assembleDataCoreWpay(RequestWPay requestModel, ChannelDataModel channelDataModel, ChannelGewayModel channelGewayModel,
                                                     String total_amount, String serviceCharge, String actualMoney, int tradeStatus, String payAmount,
                                                     String payActualMoney, int moneyFitType, long channelGewayId, int profitType) throws Exception{
        DataCoreModel resBean = new DataCoreModel();
        resBean.setMyTradeNo(requestModel.cpparam);
        resBean.setTradeNo(requestModel.orderNo);
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
        resBean.setExtraReturnParam(requestModel.price + priceInfo);// 用户实际支付的金额
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


    /**
     * @Description: 校验余额是否大于订单金额
     * @param balance - 余额
     * @param orderMoney - 订单金额
     * @return void
     * @author yoko
     * @date 2020/10/31 19:29
     */
    public static void checkMoney(String balance, String orderMoney) throws Exception{
        if (orderMoney.indexOf("-") > -1){
            throw new ServiceException("1001", "金额填写有误!");
        }
        if (StringUtils.isBlank(balance)){
            throw new ServiceException("1002", "您的余额不足,请稍后在重试!");
        }
        double d_balance = Double.parseDouble(balance);
        double d_orderMoney = Double.parseDouble(orderMoney);
        if (d_balance < d_orderMoney){
            throw new ServiceException("1003", "您的余额不足,请稍后在重试!");
        }
    }


    /**
     * @Description: 组装扣减渠道余额
     * @param id - 主键ID
     * @param orderMoney - 订单金额
     * @param serviceCharge - 手续费
     * @param extraServiceCharge - 额外手续费
     * @return com.hz.platform.master.core.model.channel.ChannelModel
     * @author yoko
     * @date 2020/10/31 19:40
     */
    public static ChannelModel assembleChannelBalance(long id, String orderMoney, String serviceCharge, String extraServiceCharge){
        ChannelModel resBean = new ChannelModel();
        resBean.setId(id);
        // 计算要扣的金额
        String resMoney = StringUtil.getMultiply(orderMoney, serviceCharge);
        String money = StringUtil.getBigDecimalAdd(resMoney, orderMoney);
        if (!StringUtils.isBlank(extraServiceCharge)){
            money = StringUtil.getBigDecimalAdd(money, extraServiceCharge);
        }
        resBean.setSubtractBalance("1");
        resBean.setOrderMoney(money);

        String serviceChargeMoney = "";
        serviceChargeMoney = StringUtil.getMultiply(orderMoney, serviceCharge);
        if (!StringUtils.isBlank(extraServiceCharge)){
            serviceChargeMoney = StringUtil.getBigDecimalAdd(serviceChargeMoney, extraServiceCharge);
        }
        resBean.setServiceChargeMoney(serviceChargeMoney);
        return resBean;
    }


    /**
     * @Description: 组装添加流水或查询流水的方法
     * @param id - 主键ID
     * @param channelId - 渠道ID
     * @param orderNo - 订单号
     * @param orderType - 订单类型：1代收，2代付
     * @param money - 订单金额
     * @param orderStatus - 订单状态：1初始化，2超时/失败，3有质疑，4成功，5表示订单超时且操作状态属于初始化的
     * @param delayTime - 延迟运行时间：当订单属于超时状态：则系统时间需要大于此时间才能进行逻辑操作
     * @param lockTime - 锁定时间
     * @param type - 操作类型：1查询，2添加数据
     * @return com.hz.cake.master.core.model.merchant.MerchantBalanceDeductModel
     * @author yoko
     * @date 2020/10/30 20:21
     */
    public static ChannelBalanceDeductModel assembleChannelBalanceDeduct(long id, long channelId, String orderNo, int orderType, String money, int orderStatus,
                                                                         String delayTime, String lockTime, int type){
        ChannelBalanceDeductModel resBean = new ChannelBalanceDeductModel();
        if (id > 0){
            resBean.setId(id);
        }
        if (channelId > 0){
            resBean.setChannelId(channelId);
        }
        if (!StringUtils.isBlank(orderNo)){
            resBean.setOrderNo(orderNo);
        }
        if (orderType > 0){
            resBean.setOrderType(orderType);
        }
        if (!StringUtils.isBlank(money)){
            resBean.setMoney(money);
        }
        if (orderStatus > 0){
            resBean.setOrderStatus(orderStatus);
        }
        if (!StringUtils.isBlank(delayTime)){
            resBean.setDelayTime(delayTime);
        }else {
            String delayTimeStr = DateUtil.addDateMinute(1440);
            resBean.setDelayTime(delayTimeStr);
        }
        if (!StringUtils.isBlank(lockTime)){
            resBean.setLockTime(lockTime);
        }
        if (type > 0){
            if (type == 2){
                resBean.setCurday(DateUtil.getDayNumber(new Date()));
                resBean.setCurhour(DateUtil.getHour(new Date()));
                resBean.setCurminute(DateUtil.getCurminute(new Date()));
            }
        }
        return resBean;
    }


    /**
     * @Description: 组装渠道代付订单数据
     * @param requestData - 请求的基础数据
     * @param myTradeNo - 我方订单号
     * @param channelId - 渠道主键ID
     * @param gewayId - 通道主键ID
     * @param channelGewayId - 渠道与通道的关联关系的ID：对应表tb_hz_channel_geway的主键ID
     * @param profitType - 收益类型：1普通收益类型，2多人分配收益类型
     * @param nowTime - 现在时间
     * @param serviceCharge - 手续费
     * @param serviceChargeMoney - 手续费具体金额
     * @param sendFlag - false表示请求失败，true表示请求成功
     * @return ChannelDataModel
     * @author yoko
     * @date 2020/3/24 21:41
     */
    public static ChannelOutModel assembleChannelOutData(RequestPayOut requestData, String myTradeNo, long channelId, long gewayId,
                                                         long channelGewayId, int profitType, String nowTime, String my_notify_url, String serviceCharge, String actualMoney,
                                                         String serviceChargeMoney, boolean sendFlag){
        ChannelOutModel resBean = new ChannelOutModel();
        resBean.setMyTradeNo(myTradeNo);
        resBean.setChannelId(channelId);
        resBean.setGewayId(gewayId);
        resBean.setChannel(requestData.channel);
        resBean.setChannelGewayId(channelGewayId);
        resBean.setProfitType(profitType);
        resBean.setTradeType(requestData.trade_type);
        resBean.setTotalAmount(requestData.total_amount);
        resBean.setServiceCharge(serviceCharge);
        if (!StringUtils.isBlank(actualMoney)){
            resBean.setActualMoney(actualMoney);
        }
        resBean.setServiceChargeMoney(serviceChargeMoney);
        resBean.setOutTradeNo(requestData.out_trade_no);
        resBean.setBankName(requestData.bank_name);
        resBean.setBankCard(requestData.bank_card);
        resBean.setAccountName(requestData.account_name);
        if (!StringUtils.isBlank(requestData.bank_subbranch)){
            resBean.setBankSubbranch(requestData.bank_subbranch);
        }
        if (!StringUtils.isBlank(requestData.bank_province)){
            resBean.setBankProvince(requestData.bank_province);
        }
        if (!StringUtils.isBlank(requestData.bank_city)){
            resBean.setBankCity(requestData.bank_city);
        }
        if (!StringUtils.isBlank(requestData.notify_url)){
            resBean.setNotifyUrl(requestData.notify_url);
        }
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
     * @Description: 组装上游同步的数据-蛋糕-代付
     * @param requestModel - 上游同步的基本数据
     * @param channelOutModel - 渠道请求的基本数据-代付
     * @param channelGewayModel - 渠道与通道关联关系
     * @param tradeStatus - 订单状态：1成功，2失败，3其它
     * @return DataCoreModel
     * @author yoko
     * @date 2020/3/25 14:08
     */
    public static DataCoreOutModel assembleDataCoreOutByCake(RequestCakeOut requestModel, ChannelOutModel channelOutModel, ChannelGewayModel channelGewayModel,
                                                             int tradeStatus, long channelGewayId, int profitType,
                                                             String total_amount, String serviceCharge, String actualMoney,
                                                             String payAmount, String payActualMoney) throws Exception{
        DataCoreOutModel resBean = new DataCoreOutModel();
        resBean.setMyTradeNo(requestModel.out_trade_no);
        resBean.setTradeNo(requestModel.trade_no);
        resBean.setOutTradeNo(channelOutModel.getOutTradeNo());

        resBean.setTotalAmount(total_amount);
        resBean.setServiceCharge(serviceCharge);
        resBean.setActualMoney(actualMoney);
        resBean.setPayAmount(payAmount);
        resBean.setPayActualMoney(payActualMoney);


        resBean.setTradeStatus(tradeStatus);
        if (!StringUtils.isBlank(requestModel.picture_ads)){
            resBean.setPictureAds(requestModel.picture_ads);
        }
        if (!StringUtils.isBlank(requestModel.fail_info)){
            resBean.setFailInfo(requestModel.fail_info);
        }

        resBean.setTradeTime(DateUtil.getNowPlusTime());
//        resBean.setSign(requestModel.sign);
        resBean.setChannelId(channelOutModel.getChannelId());
        resBean.setGewayId(channelOutModel.getGewayId());
        resBean.setNotifyUrl(channelOutModel.getNotifyUrl());
//        resBean.setNotifySuc();
        if (!StringUtils.isBlank(channelOutModel.getExtraReturnParam())){
            resBean.setXyExtraReturnParam(channelOutModel.getExtraReturnParam());
        }
        resBean.setDeductRatio(channelGewayModel.getDeductRatio());

        resBean.setChannelGewayId(channelGewayId);
        resBean.setProfitType(profitType);

        resBean.setCurday(DateUtil.getDayNumber(new Date()));
        resBean.setCurhour(DateUtil.getHour(new Date()));
        resBean.setCurminute(DateUtil.getCurminute(new Date()));
        return resBean;

    }


    /**
     * @Description: 组装
     * @param orderNo - 订单号
     * @param orderStatus - 订单状态
     * @param pictureAds - 转账凭证
     * @param failInfo - 失败缘由
     * @return com.hz.platform.master.core.model.channelout.ChannelOutModel
     * @author yoko
     * @date 2020/11/1 16:53
     */
    public static ChannelOutModel assembleChannelOutUpdate(String orderNo, int orderStatus, String pictureAds, String failInfo){
        ChannelOutModel resBean = new ChannelOutModel();
        resBean.setMyTradeNo(orderNo);
        resBean.setOrderStatus(orderStatus);
        if (!StringUtils.isBlank(pictureAds)){
            resBean.setPictureAds(pictureAds);
        }
        if (!StringUtils.isBlank(failInfo)){
            resBean.setFailInfo(failInfo);
        }
        return resBean;
    }


    /**
     * @Description: 组装渠道余额加扣款流水的金额
     * @param id - 主键ID
     * @param orderMoney - 订单金额
     * @return com.hz.platform.master.core.model.channel.ChannelModel
     * @author yoko
     * @date 2020/10/31 19:40
     */
    public static ChannelModel assembleChannelBalanceAdd(long id, String orderMoney){
        ChannelModel resBean = new ChannelModel();
        resBean.setId(id);
        resBean.setAddBalance("1");
        resBean.setOrderMoney(orderMoney);
        return resBean;
    }


    /**
     * @Description: 组装渠道余额核减金额
     * @param id - 主键ID
     * @param orderMoney - 订单金额
     * @return com.hz.platform.master.core.model.channel.ChannelModel
     * @author yoko
     * @date 2020/10/31 19:40
     */
    public static ChannelModel assembleChannelBalanceSubtract(long id, String orderMoney){
        ChannelModel resBean = new ChannelModel();
        resBean.setId(id);
        resBean.setSubtractBalance("1");
        resBean.setOrderMoney(orderMoney);
        return resBean;
    }

    /**
     * @Description: 组装跟新渠道的锁定金额
     * @param id - 主键ID
     * @param lockMoney - 锁定金额
     * @return com.hz.platform.master.core.model.channel.ChannelModel
     * @author yoko
     * @date 2020/11/3 11:49
     */
    public static ChannelModel assembleLockMoneyUpdate(long id, String lockMoney){
        ChannelModel resBean = new ChannelModel();
        resBean.setId(id);
        if (!StringUtils.isBlank(lockMoney)){
            resBean.setLockMoney(lockMoney);
        }
        return resBean;
    }


    /**
     * @Description: 组装添加蛋糕的提现记录的方法
     * @param withdrawModel - 平台提现记录
     * @param channelType - 渠道类型：0初始化，1代收，2大包，3代付
     * @param secretKey - 商户秘钥
     * @return
     * @author yoko
     * @date 2020/11/19 14:02
    */
    public static ChannelWithdrawModel assembleChannelWithdraw(WithdrawModel withdrawModel, int channelType, String secretKey) throws Exception{
        ChannelWithdrawModel resBean = new ChannelWithdrawModel();
//        resBean.setChannelId(withdrawModel.getId());
        String orderNo = "QD" + withdrawModel.getId() + DateUtil.getNowPlusTimeMill();
        resBean.setOrderNo(orderNo);
        resBean.setOutTradeNo(withdrawModel.getOrderNo());
        resBean.setMoney(withdrawModel.getMoney());
        resBean.setWithdrawServiceCharge(withdrawModel.getServiceCharge());
        if (channelType > 0){
            resBean.setChannelType(channelType);
        }
        if (!StringUtils.isBlank(secretKey)){
            resBean.setSecretKey(secretKey);
        }
        resBean.setInBankCard(withdrawModel.getBankCard());
        resBean.setInBankName(withdrawModel.getBankName());
        resBean.setInAccountName(withdrawModel.getAccountName());
        resBean.setDataType(1);
        resBean.setCurday(DateUtil.getDayNumber(new Date()));
        resBean.setCurhour(DateUtil.getHour(new Date()));
        resBean.setCurminute(DateUtil.getCurminute(new Date()));
        return resBean;
    }



    /**
     * @Description: 组装上游同步的数据-华富-代付
     * @param requestModel - 上游同步的基本数据
     * @param channelOutModel - 渠道请求的基本数据-代付
     * @param channelGewayModel - 渠道与通道关联关系
     * @param tradeStatus - 订单状态：1成功，2失败，3其它
     * @return DataCoreModel
     * @author yoko
     * @date 2020/3/25 14:08
     */
    public static DataCoreOutModel assembleDataCoreOutByHf(RequestOutHf requestModel, ChannelOutModel channelOutModel, ChannelGewayModel channelGewayModel,
                                                             int tradeStatus, long channelGewayId, int profitType) throws Exception{
        DataCoreOutModel resBean = new DataCoreOutModel();
        resBean.setMyTradeNo(requestModel.out_trade_id);
        resBean.setTradeNo(requestModel.orderid);
        resBean.setOutTradeNo(channelOutModel.getOutTradeNo());

        resBean.setTradeStatus(tradeStatus);

        resBean.setTradeTime(DateUtil.getNowPlusTime());
//        resBean.setSign(requestModel.sign);
        resBean.setChannelId(channelOutModel.getChannelId());
        resBean.setGewayId(channelOutModel.getGewayId());
        resBean.setNotifyUrl(channelOutModel.getNotifyUrl());
//        resBean.setNotifySuc();
        if (!StringUtils.isBlank(channelOutModel.getExtraReturnParam())){
            resBean.setXyExtraReturnParam(channelOutModel.getExtraReturnParam());
        }
        resBean.setDeductRatio(channelGewayModel.getDeductRatio());

        resBean.setChannelGewayId(channelGewayId);
        resBean.setProfitType(profitType);

        resBean.setCurday(DateUtil.getDayNumber(new Date()));
        resBean.setCurhour(DateUtil.getHour(new Date()));
        resBean.setCurminute(DateUtil.getCurminute(new Date()));
        return resBean;

    }


    /**
     * @Description: 组装查询渠道与通道的关联关系的查询方法
     * @param id - 主键ID
     * @param channelId - 渠道主键ID
     * @param gewayId - 通道ID
     * @param gewayCodeType - 通道代码定性类型：1初始化/无任何属性，2代收，3代付
     * @param dayLimit - 每日成功金额是否到达上限：1初始化/未到达上限，2已到达上限
     * @return com.hz.platform.master.core.model.channelgeway.ChannelGewayModel
     * @author yoko
     * @date 2020/11/25 16:53
     */
    public static ChannelGewayModel assembleChannelGewayQuery(long id, long channelId, long gewayId, int gewayCodeType, int dayLimit){
        ChannelGewayModel resBean = new ChannelGewayModel();
        if (id > 0){
            resBean.setId(id);
        }
        if (channelId > 0){
            resBean.setChannelId(channelId);
        }
        if (gewayId > 0){
            resBean.setGewayId(gewayId);
        }
        if (gewayCodeType > 0){
            resBean.setGewayCodeType(gewayCodeType);
        }
        if (dayLimit > 0){
            resBean.setDayLimit(dayLimit);
        }
        return resBean;
    }


    /**
     * @Description: 通过比例筛选出一条关联关系
     * @param channelGewayList - 渠道与通道的关联关系
     * @return
     * @author yoko
     * @date 2020/11/25 17:08
    */
    public static ChannelGewayModel ratioChannelGeway(List<ChannelGewayModel> channelGewayList){
        int start = 0;
        int end = 0;
        for (int i = 0; i < channelGewayList.size(); i++){
            start = end;
            end += channelGewayList.get(i).getRatio();
            channelGewayList.get(i).setStartRatio(start);
            channelGewayList.get(i).setEndRatio(end);
        }
        int random = new Random().nextInt(end);
        for (ChannelGewayModel channelGewayModel : channelGewayList){
            log.info("id:"+ channelGewayModel.getId() + ",ratio:" + channelGewayModel.getRatio() + ",start:" + channelGewayModel.getStartRatio() + ",end:" + channelGewayModel.getEndRatio());
            if (random >= channelGewayModel.getStartRatio() && random < channelGewayModel.getEndRatio()){
                return channelGewayModel;
            }
        }
        return null;

    }


    /**
     * @Description: 组装查询渠道的查询方法
     * @param id - 主键ID
     * @param accountNum - 账号
     * @param channel - 商品编号
     * @param isGoogle - 是否需要谷歌验证：1不需要，2需要
     * @param googleKey - 谷歌唯一码
     * @param isSynchro - 是否需要数据同步:1需要同步，2不需要同步
     * @param withdrawType - 提现类型：1默认在支付平台操作，2发送下发数据到水果平台
     * @param channelType - 渠道类型：1代收，2大包，3代付
     * @param isEnable - 是否启用：0初始化属于暂停状态，1表示暂停使用，2正常状态
     * @return com.hz.platform.master.core.model.channel.ChannelModel
     * @author yoko
     * @date 2020/12/1 15:41
     */
    public static ChannelModel assembleChannelQuery(long id, String accountNum, String channel, int isGoogle, String googleKey, int isSynchro,
                                                    int withdrawType, int channelType, int isEnable){
        ChannelModel resBean = new ChannelModel();
        if (id > 0){
            resBean.setId(id);
        }
        if (!StringUtils.isBlank(accountNum)){
            resBean.setAccountNum(accountNum);
        }
        if (!StringUtils.isBlank(channel)){
            resBean.setChannel(channel);
        }
        if (isGoogle > 0){
            resBean.setIsGoogle(isGoogle);
        }
        if (!StringUtils.isBlank(googleKey)){
            resBean.setGoogleKey(googleKey);
        }
        if (isSynchro > 0){
            resBean.setIsSynchro(isSynchro);
        }
        if (withdrawType > 0){
            resBean.setWithdrawType(withdrawType);
        }
        if (channelType > 0){
            resBean.setChannelType(channelType);
        }
        if (isEnable > 0){
            resBean.setIsEnable(isEnable);
        }
        return resBean;

    }



    /**
     * @Description: 组装通道余额核减金额
     * @param id - 主键ID
     * @param orderMoney - 订单金额
     * @return com.hz.platform.master.core.model.channel.ChannelModel
     * @author yoko
     * @date 2020/10/31 19:40
     */
    public static GewayModel assembleGewayBalanceSubtract(long id, String orderMoney){
        GewayModel resBean = new GewayModel();
        resBean.setId(id);
        resBean.setSubtractBalance("1");
        resBean.setOrderMoney(orderMoney);
        return resBean;
    }

    /**
     * @Description: 组装通道余额加金额
     * @param id - 主键ID
     * @param orderMoney - 订单金额
     * @return com.hz.platform.master.core.model.channel.ChannelModel
     * @author yoko
     * @date 2020/10/31 19:40
     */
    public static GewayModel assembleGewayBalanceAdd(long id, String orderMoney){
        GewayModel resBean = new GewayModel();
        resBean.setId(id);
        resBean.setAddBalance("1");
        resBean.setOrderMoney(orderMoney);
        return resBean;
    }


    /**
     * @Description: check预付款通道是否有足够的金额进行跑量
     * @param gewayModel - 通道信息
     * @param orderMoney - 订单金额
     * @return boolean
     * @author yoko
     * @date 2021/1/19 17:05
     */
    public static boolean checkGewayMoney(GewayModel gewayModel, String orderMoney){
        // check余额是否为空
        if (StringUtils.isBlank(gewayModel.getBalance())){
            return false;
        }
        // check最低保证金是否为空
        if (StringUtils.isBlank(gewayModel.getLeastMoney())){
            return false;
        }
        // check余额是否小于保证金
        boolean flag_least = StringUtil.getBigDecimalSubtract(gewayModel.getBalance(), gewayModel.getLeastMoney());
        if (!flag_least){
            return false;
        }
        // check余额是否小于订单金额
        boolean flag_order = StringUtil.getBigDecimalSubtract(gewayModel.getBalance(), orderMoney);
        if (!flag_order){
            return false;
        }
        return true;
    }


    /**
     * @Description: 组装代理收益数据集合
     * @param agentChannelGewayList - 代理分成的关联关系
     * @param dataCoreModel - 成功的订单信息
     * @return java.util.List<com.hz.platform.master.core.model.agent.AgentProfitModel>
     * @author yoko
     * @date 2021/1/20 15:19
     */
    public static List<AgentProfitModel> assembleAgentProfitList(List<AgentChannelGewayModel> agentChannelGewayList, DataCoreModel dataCoreModel){
        List<AgentProfitModel> resList = new ArrayList<>();
        for (AgentChannelGewayModel ag_data : agentChannelGewayList){
            String profit = StringUtil.getMultiply(dataCoreModel.getPayAmount(), ag_data.getServiceCharge());
            AgentProfitModel agentProfitModel = BeanUtils.copy(dataCoreModel, AgentProfitModel.class);
            agentProfitModel.setId(null);
            agentProfitModel.setAgentId(ag_data.getAgentId());
            agentProfitModel.setProfitRatio(ag_data.getServiceCharge());
            agentProfitModel.setProfit(profit);
            agentProfitModel.setProfitType(1);
            agentProfitModel.setRunNum(null);
            agentProfitModel.setRunStatus(null);
            resList.add(agentProfitModel);
        }
        return resList;
    }



    /**
     * @Description: 组装通道收益数据集合
     * @param dataCoreModel - 成功的订单信息
     * @param orderType - 订单类型：1代收，2代付
     * @param gewayType - 通道类型：1普通通道，2预付款通道
     * @return java.util.List<com.hz.platform.master.core.model.agent.AgentProfitModel>
     * @author yoko
     * @date 2021/1/20 15:19
     */
    public static GewayProfitModel assembleGewayProfit(DataCoreModel dataCoreModel, int orderType, int gewayType){
        GewayProfitModel resBean = BeanUtils.copy(dataCoreModel, GewayProfitModel.class);
        resBean.setOrderType(orderType);
        resBean.setGewayType(gewayType);
        String profit = StringUtil.getMultiply(dataCoreModel.getPayAmount(), dataCoreModel.getServiceCharge());
        resBean.setMoney(dataCoreModel.getTotalAmount());
        resBean.setProfit(profit);
        resBean.setId(null);
        resBean.setProfitRatio(dataCoreModel.getServiceCharge());
        resBean.setProfit(profit);
        resBean.setRunNum(null);
        resBean.setRunStatus(null);
        return resBean;
    }


    /**
     * @Description: 组装通道收益数据集合-根据代付订单
     * @param dataCoreOutModel - 成功的代付订单信息
     * @param orderType - 订单类型：1代收，2代付
     * @param gewayType - 通道类型：1普通通道，2预付款通道
     * @return java.util.List<com.hz.platform.master.core.model.agent.AgentProfitModel>
     * @author yoko
     * @date 2021/1/20 15:19
     */
    public static GewayProfitModel assembleGewayProfitByOutOrder(DataCoreOutModel dataCoreOutModel, int orderType, int gewayType){

        GewayProfitModel resBean = BeanUtils.copy(dataCoreOutModel, GewayProfitModel.class);
        resBean.setOrderType(orderType);
        resBean.setGewayType(gewayType);
        String profit = StringUtil.getMultiply(dataCoreOutModel.getPayAmount(), dataCoreOutModel.getServiceCharge());
        resBean.setMoney(dataCoreOutModel.getTotalAmount());
        resBean.setProfit(profit);
        resBean.setId(null);
        resBean.setProfitRatio(dataCoreOutModel.getServiceCharge());
        resBean.setProfit(profit);
        resBean.setRunNum(null);
        resBean.setRunStatus(null);
//        段峰
        return resBean;
    }


    /**
     * @Description: 组装渠道收益数据集合
     * @param dataCoreModel - 成功的订单信息
     * @return java.util.List<com.hz.platform.master.core.model.agent.AgentProfitModel>
     * @author yoko
     * @date 2021/1/20 15:19
     */
    public static ChannelProfitModel assembleChannelProfit(DataCoreModel dataCoreModel){
        ChannelProfitModel resBean = BeanUtils.copy(dataCoreModel, ChannelProfitModel.class);
        String profit = StringUtil.getMultiply(dataCoreModel.getPayAmount(), dataCoreModel.getServiceCharge());
        resBean.setMoney(dataCoreModel.getActualMoney());
        resBean.setProfit(profit);
        resBean.setId(null);
        resBean.setProfitRatio(dataCoreModel.getServiceCharge());
        resBean.setProfit(profit);
        resBean.setRunNum(null);
        resBean.setRunStatus(null);
        return resBean;
    }


    /**
     * @Description: 组装更新渠道总额以及金额的方法
     * @param channelId - 渠道ID
     * @param orderMoney - 订单金额
     * @return com.hz.platform.master.core.model.channel.ChannelModel
     * @author yoko
     * @date 2021/1/21 16:45
     */
    public static ChannelModel assembleUpdateChannelMoney(long channelId, String orderMoney){
        ChannelModel resBean = new ChannelModel();
        resBean.setId(channelId);
        resBean.setOrderMoney(orderMoney);
        return resBean;
    }


    /**
     * @Description: 组装代理查询条件的方法
     * @param id - 代理主键ID
     * @param withdrawType - 提现类型：1默认在支付平台操作，2发送下发数据到蛋糕平台
     * @return com.hz.platform.master.core.model.agent.AgentModel
     * @author yoko
     * @date 2021/1/21 18:57
     */
    public static AgentModel assembleAgentQuery(long id, int withdrawType){
        AgentModel resBean = new AgentModel();
        resBean.setId(id);
        if (withdrawType > 0){
            resBean.setWithdrawType(withdrawType);
        }
        return resBean;
    }


    /**
     * @Description: 组装查询网关的数据
     * @param dataCoreModel - 成功数据
     * @param out_trade_no - 渠道订单号
     * @return com.hz.platform.master.core.protocol.response.pay.ResponseDataCore
     * @author yoko
     * @date 2021/4/26 17:34
     */
    public static ResponseDataCore assembleResponseDataCoreResult(DataCoreModel dataCoreModel, String out_trade_no){
        ResponseDataCore resBean = new ResponseDataCore();
        if (dataCoreModel != null && dataCoreModel.getId() != null && dataCoreModel.getId() > 0){
            resBean.setTrade_no(dataCoreModel.getTradeNo());
            resBean.setOut_trade_no(dataCoreModel.getOutTradeNo());
            resBean.setTrade_status(dataCoreModel.getTradeStatus());
            if (dataCoreModel.getSendStatus() == 3){
                resBean.setSend_status(1);
            }else {
                resBean.setSend_status(2);
            }
            if (!StringUtils.isBlank(dataCoreModel.getNotifyUrl())){
                resBean.setNotify_url(dataCoreModel.getNotifyUrl());
            }
            resBean.setTrade_time(dataCoreModel.getCreateTime());
        }else {
            resBean.setOut_trade_no(out_trade_no);
            resBean.setTrade_status(2);
        }
        return resBean;
    }



    /**
     * @Description: 组装代理收益数据集合 - 代付
     * @param agentChannelGewayList - 代理分成的关联关系
     * @param dataCoreOutModel - 成功的代付订单信息
     * @return java.util.List<com.hz.platform.master.core.model.agent.AgentProfitModel>
     * @author yoko
     * @date 2021/1/20 15:19
     */
    public static List<AgentProfitModel> assembleAgentProfitByOrderOutList(List<AgentChannelGewayModel> agentChannelGewayList, DataCoreOutModel dataCoreOutModel){
        List<AgentProfitModel> resList = new ArrayList<>();
        for (AgentChannelGewayModel ag_data : agentChannelGewayList){
            if (ag_data.getServiceChargeType() == 1){
                String profit = StringUtil.getMultiply(dataCoreOutModel.getPayAmount(), ag_data.getServiceCharge());
                AgentProfitModel agentProfitModel = BeanUtils.copy(dataCoreOutModel, AgentProfitModel.class);
                agentProfitModel.setId(null);
                agentProfitModel.setAgentId(ag_data.getAgentId());
                agentProfitModel.setProfitRatio(ag_data.getServiceCharge());
                agentProfitModel.setProfit(profit);
                agentProfitModel.setProfitType(1);
                agentProfitModel.setRunStatus(null);
                agentProfitModel.setRunNum(null);
                resList.add(agentProfitModel);
            }else if (ag_data.getServiceChargeType() == 2){
                // 固定分成
                String profit = StringUtil.getMultiply(dataCoreOutModel.getPayAmount(), ag_data.getServiceCharge());
                AgentProfitModel agentProfitModel = BeanUtils.copy(dataCoreOutModel, AgentProfitModel.class);
                agentProfitModel.setId(null);
                agentProfitModel.setAgentId(ag_data.getAgentId());
                agentProfitModel.setProfitRatio(ag_data.getServiceCharge());
                agentProfitModel.setProfitType(1);
                agentProfitModel.setProfit(profit);
                agentProfitModel.setRunNum(null);
                agentProfitModel.setRunStatus(null);
                resList.add(agentProfitModel);

                // 额外分成
                AgentProfitModel agentProfitModel1 = BeanUtils.copy(dataCoreOutModel, AgentProfitModel.class);
                agentProfitModel1.setId(null);
                agentProfitModel1.setAgentId(ag_data.getAgentId());
                agentProfitModel1.setProfitRatio(ag_data.getServiceCharge());
                agentProfitModel1.setTotalAmount(null);
                agentProfitModel1.setServiceCharge(null);
                agentProfitModel1.setActualMoney(null);
                agentProfitModel1.setPayAmount(null);
                agentProfitModel1.setPayActualMoney(null);
                agentProfitModel1.setProfitRatio(null);
                agentProfitModel1.setProfitType(2);
                agentProfitModel1.setProfit(ag_data.getExtraServiceCharge());
                agentProfitModel1.setRunNum(null);
                agentProfitModel1.setRunStatus(null);
                resList.add(agentProfitModel1);

            }

        }
        return resList;
    }




    /**
     * @Description: 组装上游同步的数据-狮子支付-sz
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
    public static DataCoreModel assembleDataCoreSz(RequestSz requestModel, ChannelDataModel channelDataModel, ChannelGewayModel channelGewayModel,
                                                   String total_amount, String serviceCharge, String actualMoney, int tradeStatus, String payAmount,
                                                   String payActualMoney, int moneyFitType, long channelGewayId, int profitType) throws Exception{
        DataCoreModel resBean = new DataCoreModel();
        resBean.setMyTradeNo(requestModel.order_id);
        resBean.setTradeNo(requestModel.pay_no);
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
        resBean.setExtraReturnParam(requestModel.amount + priceInfo);// 用户实际支付的金额
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
     * @Description: 组装上游同步的数据-木星支付-mx
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
    public static DataCoreModel assembleDataCoreMx(RequestMx requestModel, ChannelDataModel channelDataModel, ChannelGewayModel channelGewayModel,
                                                   String total_amount, String serviceCharge, String actualMoney, int tradeStatus, String payAmount,
                                                   String payActualMoney, int moneyFitType, long channelGewayId, int profitType) throws Exception{
        DataCoreModel resBean = new DataCoreModel();
        resBean.setMyTradeNo(requestModel.orderno);
        resBean.setTradeNo(requestModel.systemorderid);
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
        resBean.setExtraReturnParam(payAmount + priceInfo);// 用户实际支付的金额
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
     * @Description: 组装上游同步的数据-大信誉支付-mx
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
    public static DataCoreModel assembleDataCoreDxy(RequestDxy requestModel, ChannelDataModel channelDataModel, ChannelGewayModel channelGewayModel,
                                                   String total_amount, String serviceCharge, String actualMoney, int tradeStatus, String payAmount,
                                                   String payActualMoney, int moneyFitType, long channelGewayId, int profitType) throws Exception{
        DataCoreModel resBean = new DataCoreModel();
        resBean.setMyTradeNo(requestModel.orderid);
        resBean.setTradeNo(requestModel.transaction_id);
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
        resBean.setExtraReturnParam(payAmount + priceInfo);// 用户实际支付的金额
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
     * @Description: 校验请求拉单是否是在白名单内的IP进行拉单的
     * @param whiteListIp - 白名单IP：多个以英文逗号分割
     * @param ip - 访问的IP
     * @return void
     * @author yoko
     * @date 2020/10/31 19:29
     */
    public static void checkWhiteListIp(String whiteListIp, String ip) throws Exception{
        if (!StringUtils.isBlank(whiteListIp)){
            if (whiteListIp.indexOf(ip) <= -1){
                throw new ServiceException("1008", ip + "不是白名单IP拉单!");
            }

        }

    }


    /**
     * @Description: 组装查询策略数据条件的方法
     * @return com.pf.play.rule.core.model.strategy.StrategyModel
     * @author yoko
     * @date 2020/5/19 17:12
     */
    public static StrategyModel assembleStrategyQuery(int stgType){
        StrategyModel resBean = new StrategyModel();
        resBean.setStgType(stgType);
        return resBean;
    }



    /**
     * @Description: 校验策略类型数据:出码开关-判断此时是否属于正常出码
     * @return void
     * @author yoko
     * @date 2019/12/2 14:35
     */
    public static void checkStrategyByQrCodeSwitch(StrategyModel strategyModel) throws Exception{
        if (strategyModel == null){
            throw new ServiceException("S00001", "错误,请重试!");
        }
        if (strategyModel.getStgNumValue() == 1){
            throw new ServiceException("S00002", "目前出码处于关闭状态!");
        }
        if (strategyModel.getStgNumValue() == 2){
            if (StringUtils.isBlank(strategyModel.getStgValue())){
                throw new ServiceException("S00003", "错误,请重试!");
            }else{
                String[] str = strategyModel.getStgValue().split("-");
                boolean flag = DateUtil.isBelong(str[0], str[1]);
                if (!flag){
                    throw new ServiceException("S00004", "目前不在出码时间范围内!");
                }
            }
        }
    }


    /**
     * @Description: check校验请求的订单金额是否属于通道金额范围内
     * @param moneyType - 支持金额类型：1固定的，2单一范围，3多个范围
     * @param moneyRange - 支持金额:money_type=1则50多个则以英文逗号风格，money_type=2则100-1000；money_type=3则100-1000,200-2000多个以英文逗号分割
     * @return void
     * @Author: yoko
     * @Date 2021/7/13 10:11
     */
    public static void checkGewayMoneyRange(int moneyType, String moneyRange, String total_amount) throws Exception{
        if (!StringUtils.isBlank(moneyRange)){
            if (moneyType == 1){
                String [] strArr = moneyRange.split(",");
                for (String str : strArr){
                    String resStr = StringUtil.getBigDecimalSubtractStr(total_amount, str);
                    if (resStr.equals("0")){
                        return;
                    }
                }
            }else if (moneyType >= 2){
                String [] strArr = moneyRange.split(",");
                for (String str : strArr){
                    String [] rule = str.split("-");
                    double start = Double.parseDouble(rule[0]);
                    double end = Double.parseDouble(rule[1]);
                    double money = Double.parseDouble(total_amount);
                    if (money >= start && money <= end){
                        return;
                    }
                }
            }
            throw new ServiceException("M00001", "请按照规定输入金额!");
        }
    }







    /**
     * @Description: 组装上游同步的数据-金木星支付-jmx
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
    public static DataCoreModel assembleDataCoreJmx(RequestJmx requestModel, ChannelDataModel channelDataModel, ChannelGewayModel channelGewayModel,
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
        resBean.setExtraReturnParam(requestModel.money + priceInfo);// 用户实际支付的金额
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
     * @Description: 组装上游同步的数据-高防支付-gf
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
    public static DataCoreModel assembleDataCoreGf(RequestGf requestModel, ChannelDataModel channelDataModel, ChannelGewayModel channelGewayModel,
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
        resBean.setExtraReturnParam(payAmount + priceInfo);// 用户实际支付的金额
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
     * @Description: 组装查询当日代收成功金额
     * @param gewayId - 通道ID
     * @return
     * @Author: yoko
     * @Date 2021/7/26 14:32
    */
    public static DataCoreModel assembleDataCoreQueryByGeway(long gewayId){
        DataCoreModel resBean = new DataCoreModel();
        resBean.setGewayId(gewayId);
        resBean.setTradeStatus(1);
        resBean.setCurday(DateUtil.getDayNumber(new Date()));
        return resBean;
    }




    /**
     * @Description: 通过比例筛选出一条通道
     * @param strategyList - 微派集合通道
     * @return
     * @author yoko
     * @date 2020/11/25 17:08
     */
    public static String ratioGewayByWpay(List<StrategyData> strategyList){
        int start = 0;
        int end = 0;
        for (int i = 0; i < strategyList.size(); i++){
            start = end;
            end += strategyList.get(i).getStgValueTwo();
            strategyList.get(i).setStartRatio(start);
            strategyList.get(i).setEndRatio(end);
        }
        int random = new Random().nextInt(end);
        for (StrategyData strategyData : strategyList){
            log.info("id:"+ strategyData.getStgKey() + ",ratio:" + strategyData.getStgValueTwo() + ",start:" + strategyData.getStartRatio() + ",end:" + strategyData.getEndRatio());
            if (random >= strategyData.getStartRatio() && random < strategyData.getEndRatio()){
                return strategyData.getStgValue();
            }
        }
        return null;

    }




    /**
     * @Description: 组装上游同步的数据-小肥龙支付-xfl
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
    public static DataCoreModel assembleDataCoreXfl(RequestXfl requestModel, ChannelDataModel channelDataModel, ChannelGewayModel channelGewayModel,
                                                   String total_amount, String serviceCharge, String actualMoney, int tradeStatus, String payAmount,
                                                   String payActualMoney, int moneyFitType, long channelGewayId, int profitType) throws Exception{
        DataCoreModel resBean = new DataCoreModel();
        resBean.setMyTradeNo(requestModel.mchOrderNo);
        resBean.setTradeNo(requestModel.payOrderId);
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
        resBean.setExtraReturnParam(payAmount + priceInfo);// 用户实际支付的金额
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
     * @Description: 组装上游同步的数据-思维付支付-swf
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
    public static DataCoreModel assembleDataCoreSwf(RequestSwf requestModel, ChannelDataModel channelDataModel, ChannelGewayModel channelGewayModel,
                                                    String total_amount, String serviceCharge, String actualMoney, int tradeStatus, String payAmount,
                                                    String payActualMoney, int moneyFitType, long channelGewayId, int profitType) throws Exception{
        DataCoreModel resBean = new DataCoreModel();
        resBean.setMyTradeNo(requestModel.ordernum);
        resBean.setTradeNo(requestModel.outordernum);
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
        resBean.setExtraReturnParam(payAmount + priceInfo);// 用户实际支付的金额
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
     * @Description: 组装上游同步的数据-花呗通支付-hbt
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
    public static DataCoreModel assembleDataCoreHbt(RequestHbt requestModel, ChannelDataModel channelDataModel, ChannelGewayModel channelGewayModel,
                                                    String total_amount, String serviceCharge, String actualMoney, int tradeStatus, String payAmount,
                                                    String payActualMoney, int moneyFitType, long channelGewayId, int profitType) throws Exception{
        DataCoreModel resBean = new DataCoreModel();
        resBean.setMyTradeNo(requestModel.p3_orderno);
        resBean.setTradeNo(requestModel.p9_porderno);
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
        resBean.setExtraReturnParam(payAmount + priceInfo);// 用户实际支付的金额
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
     * @Description: 组装上游同步的数据-易宝龙支付-xfl
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
    public static DataCoreModel assembleDataCoreYb(RequestYb requestModel, ChannelDataModel channelDataModel, ChannelGewayModel channelGewayModel,
                                                    String total_amount, String serviceCharge, String actualMoney, int tradeStatus, String payAmount,
                                                    String payActualMoney, int moneyFitType, long channelGewayId, int profitType) throws Exception{
        DataCoreModel resBean = new DataCoreModel();
        resBean.setMyTradeNo(requestModel.orderId);
        resBean.setTradeNo(requestModel.sysOrderId);
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
        resBean.setExtraReturnParam(payAmount + priceInfo);// 用户实际支付的金额
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
     * @Description: check是否频繁查询订单状态
     * <p>
     *     纪录查询代付订单号，要限制用户每次查询的时间间隔要间隔5秒。
     *     如果存在值，表示在5秒内频繁查询返回false，
     *     如果不存在值，表示5秒内此订单号没有出现频繁查询返回true
     * </p>
     * @param out_trade_no - 商家订单号
     * @return
     * @author yoko
     * @date 2020/5/21 15:38
     */
    public static void checkFrequentlyByQueryOutOrder(String out_trade_no) throws Exception{
        String strKeyCache = CachedKeyUtils.getCacheKey(CacheKey.OUT_TRADE_NO_BY_OUT_ORDER, out_trade_no);
        String strCache = (String) ComponentUtil.redisService.get(strKeyCache);
        if (!StringUtils.isBlank(strCache)) {
            throw new ServiceException("40005", "频繁操作,请稍后重试!");
        }
    }


    /**
     * @Description: check校验签名-代付查询订单状态
     * @param requestPayOut - 请求的数据
     * @param secretKey - 渠道的秘钥
     * @return
     * @Author: yoko
     * @Date 2021/8/6 20:23
     */
    public static void checkSignByOutOrderStatus(RequestPayOut requestPayOut, String secretKey) throws Exception{
        String checkSign = "";
        checkSign = "channel=" + requestPayOut.channel + "&" + "out_trade_no=" + requestPayOut.out_trade_no + "&" + "key=" + secretKey;
        checkSign = MD5Util.encryption(checkSign);
        if (!requestPayOut.sign.equals(checkSign)){
            throw new ServiceException("40005", "签名错误,请重试!");
        }
    }

    /**
     * @Description: 组装查询代付的查询方法
     * @param id - 主键ID
     * @param myTradeNo - 我方订单号
     * @param outTradeNo - 商家订单号
     * @param channelId - 渠道主键
     * @return com.hz.platform.master.core.model.ChannelOutModel
     * @Author: yoko
     * @Date 2021/9/28 14:09
     */
    public static ChannelOutModel assembleChannelOutQuery(long id, String myTradeNo, String outTradeNo, long channelId){
        ChannelOutModel resBean = new ChannelOutModel();
        if (id > 0){
            resBean.setId(id);
        }
        if (!StringUtils.isBlank(myTradeNo)){
            resBean.setMyTradeNo(myTradeNo);
        }
        if (!StringUtils.isBlank(outTradeNo)){
            resBean.setOutTradeNo(outTradeNo);
        }
        if (channelId > 0){
            resBean.setChannelId(channelId);
        }
        return resBean;
    }


    /**
     * @Description: redis：代付在查询订单状态时，保存被查询的订单号
     * <p>
     *     针对过于频繁操作同一个订单号
     * </p>
     * @param out_trade_no - 商家订单号
     * @return void
     * @author yoko
     * @date 2020/10/10 15:44
     */
    public static void saveFrequentlyByQueryOutOrder(String out_trade_no){
        String strKeyCache = CachedKeyUtils.getCacheKey(CacheKey.OUT_TRADE_NO_BY_OUT_ORDER, out_trade_no);
        ComponentUtil.redisService.set(strKeyCache, out_trade_no, 5L);
    }




    /**
     * @Description: 组装查询代付网关的数据
     * @param channelOutModel - 代付数据
     * @param out_trade_no - 渠道订单号
     * @return com.hz.platform.master.core.protocol.response.pay.ResponseDataCore
     * @author yoko
     * @date 2021/4/26 17:34
     */
    public static ResponseDataCore assembleResponseChannelOutResult(ChannelOutModel channelOutModel, String out_trade_no){
        ResponseDataCore resBean = new ResponseDataCore();
        if (channelOutModel != null && channelOutModel.getId() != null && channelOutModel.getId() > 0){
            resBean.setTrade_no(channelOutModel.getMyTradeNo());
            resBean.setOut_trade_no(channelOutModel.getOutTradeNo());
            int tradeStatus = 0;
            if (channelOutModel.getOrderStatus() == 4){
                // 成功
                tradeStatus = 1;
            }else if (channelOutModel.getOrderStatus() == 1){
                // 初始化
                tradeStatus = 2;
            }else {
                // 失败/超时
                tradeStatus = 3;
            }

            resBean.setTrade_status(tradeStatus);

            if (channelOutModel.getSendStatus() == 3){
                resBean.setSend_status(1);
            }else {
                resBean.setSend_status(2);
            }
            if (!StringUtils.isBlank(channelOutModel.getNotifyUrl())){
                resBean.setNotify_url(channelOutModel.getNotifyUrl());
            }
            resBean.setTrade_time(channelOutModel.getCreateTime());
        }else {
            resBean.setOut_trade_no(out_trade_no);
            resBean.setTrade_status(4);
        }
        return resBean;
    }



    /**
     * @Description: 组装上游同步的数据-芒果支付-mg
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
    public static DataCoreModel assembleDataCoreMg(RequestMg requestModel, ChannelDataModel channelDataModel, ChannelGewayModel channelGewayModel,
                                                   String total_amount, String serviceCharge, String actualMoney, int tradeStatus, String payAmount,
                                                   String payActualMoney, int moneyFitType, long channelGewayId, int profitType) throws Exception{
        DataCoreModel resBean = new DataCoreModel();
        resBean.setMyTradeNo(requestModel.orderid);
        resBean.setTradeNo(requestModel.transaction_id);
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
        resBean.setExtraReturnParam(payAmount + priceInfo);// 用户实际支付的金额
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
     * @Description: 组装上游同步的数据-东风支付-dfzf
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
    public static DataCoreModel assembleDataCoreDfzf(RequestDfzf requestModel, ChannelDataModel channelDataModel, ChannelGewayModel channelGewayModel,
                                                   String total_amount, String serviceCharge, String actualMoney, int tradeStatus, String payAmount,
                                                   String payActualMoney, int moneyFitType, long channelGewayId, int profitType) throws Exception{
        DataCoreModel resBean = new DataCoreModel();
        resBean.setMyTradeNo(requestModel.orderNo);
        resBean.setTradeNo(requestModel.outOrderNo);
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
        resBean.setExtraReturnParam(payAmount + priceInfo);// 用户实际支付的金额
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
     * @Description: 组装上游同步的数据-ZZ支付-zz
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
    public static DataCoreModel assembleDataCoreZz(RequestZz requestModel, ChannelDataModel channelDataModel, ChannelGewayModel channelGewayModel,
                                                     String total_amount, String serviceCharge, String actualMoney, int tradeStatus, String payAmount,
                                                     String payActualMoney, int moneyFitType, long channelGewayId, int profitType) throws Exception{
        DataCoreModel resBean = new DataCoreModel();
        resBean.setMyTradeNo(requestModel.Data.orderNo);
        resBean.setTradeNo(requestModel.Data.transactionNumber);
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
        resBean.setExtraReturnParam(payAmount + priceInfo);// 用户实际支付的金额
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
    * @Description: 通过代付预备充值信息组装渠道金额变更的数据
    * @param prepareRechargeModel
    * @return: ChannelChangeModel
    * @author: yoko
    * @date: 2021/11/18 15:32
    * @version 1.0.0
    */
    public static ChannelChangeModel assembleChannelChangeAdd(PrepareRechargeModel prepareRechargeModel){
        ChannelChangeModel resBean = new ChannelChangeModel();
        if (prepareRechargeModel != null && prepareRechargeModel.getId() != null && prepareRechargeModel.getId() > 0){
            if (!StringUtils.isBlank(prepareRechargeModel.getAlias())){
                resBean.setAlias(prepareRechargeModel.getAlias());
            }
            if (prepareRechargeModel.getChannelId() != null && prepareRechargeModel.getChannelId() > 0){
                resBean.setChannelId(prepareRechargeModel.getChannelId());
            }else {
                return null;
            }
            if (!StringUtils.isBlank(prepareRechargeModel.getMyTradeNo())){
                resBean.setMyTradeNo(prepareRechargeModel.getMyTradeNo());
            }
            if (!StringUtils.isBlank(prepareRechargeModel.getMoney())){
                resBean.setMoney(prepareRechargeModel.getMoney());
            }else {
                return null;
            }
            if (prepareRechargeModel.getChangeType() != null && prepareRechargeModel.getChangeType() > 0){
                resBean.setChangeType(prepareRechargeModel.getChangeType());
            }else {
                return null;
            }
            if (prepareRechargeModel.getIsShow() != null && prepareRechargeModel.getIsShow() > 0){
                resBean.setIsShow(prepareRechargeModel.getIsShow());
            }else {
                return null;
            }
            if (!StringUtils.isBlank(prepareRechargeModel.getDataExplain())){
                resBean.setDataExplain(prepareRechargeModel.getDataExplain());
            }
            if (!StringUtils.isBlank(prepareRechargeModel.getRemark())){
                resBean.setRemark(prepareRechargeModel.getRemark());
            }

            resBean.setCurday(DateUtil.getDayNumber(new Date()));
            resBean.setCurhour(DateUtil.getHour(new Date()));
            resBean.setCurminute(DateUtil.getCurminute(new Date()));
            return resBean;
        }else {
            return null;
        }

    }


    /**
     * @Description: 组装根据渠道ID，渠道订单号查询代付订单信息
     * @param channelId - 渠道住建ID
     * @param outTradeNo - 渠道订单号
     * @return: com.hz.platform.master.core.model.channelout.ChannelOutModel
     * @author: yoko
     * @date: 2021/11/19 0:59
     * @version 1.0.0
     */
    public static ChannelOutModel assembleChannelOutQueryByOutTradeNo(long channelId, String outTradeNo){
        ChannelOutModel resBean = new ChannelOutModel();
        resBean.setChannelId(channelId);
        resBean.setOutTradeNo(outTradeNo);
        return resBean;
    }


    /**
    * @Description: 组装更新代付订单请求状态
    * @param orderNo - 我方订单号
     * @param sendOk - 请求是否成功：1成功，2失败
    * @return:
    * @author: yoko
    * @date: 2021/11/25 2:47
    * @version 1.0.0
    */
    public static ChannelOutModel assembleUpChannelOutStatus(String orderNo, int sendOk){
        ChannelOutModel resBean = new ChannelOutModel();
        resBean.setMyTradeNo(orderNo);
        resBean.setSendOk(sendOk);
        return resBean;
    }




    /**
     * @Description: check是否频繁查询渠道余额
     * <p>
     *     纪录查询余额，要限制用户每次查询的时间间隔要间隔5秒。
     *     如果存在值，表示在5秒内频繁查询返回false，
     *     如果不存在值，表示5秒内此渠道没有出现频繁查询返回true
     * </p>
     * @param channel - 商家渠道号
     * @return
     * @author yoko
     * @date 2020/5/21 15:38
     */
    public static void checkFrequentlyByQueryBalance(String channel) throws Exception{
        String strKeyCache = CachedKeyUtils.getCacheKey(CacheKey.CHANNEL_INFO, channel);
        String strCache = (String) ComponentUtil.redisService.get(strKeyCache);
        if (!StringUtils.isBlank(strCache)) {
            throw new ServiceException("50005", "频繁操作,请稍后重试!");
        }
    }


    /**
     * @Description: redis：渠道在查询余额时，保存被查询的渠道号
     * <p>
     *     针对过于频繁操作查询余额
     * </p>
     * @param channel - 商家渠道号
     * @return void
     * @author yoko
     * @date 2020/10/10 15:44
     */
    public static void saveFrequentlyByQueryBalance(String channel){
        String strKeyCache = CachedKeyUtils.getCacheKey(CacheKey.CHANNEL_INFO, channel);
        ComponentUtil.redisService.set(strKeyCache, channel, 5L);
    }


    /**
     * @Description: check校验签名-查询渠道余额
     * @param requestPayOut - 请求的数据
     * @param secretKey - 渠道的秘钥
     * @return
     * @Author: yoko
     * @Date 2021/8/6 20:23
     */
    public static void checkSignByChannel(RequestPayOut requestPayOut, String secretKey) throws Exception{
        String checkSign = "";
        checkSign = "channel=" + requestPayOut.channel + "&" + "ctime=" + requestPayOut.ctime + "&" + "key=" + secretKey;
        checkSign = MD5Util.encryption(checkSign);
        if (!requestPayOut.sign.equals(checkSign)){
            throw new ServiceException("50005", "签名错误,请重试!");
        }
    }


    /**
     * @Description: check校验请求时间
     * @param ctime - 请求时间
     * @return
     * @Author: yoko
     * @Date 2021/8/6 20:23
     */
    public static void checkCtime(long ctime) throws Exception{
        int dff_num = DateUtil.dateSubtractBySystemTimeLong(ctime);
        if (dff_num < 0 || dff_num > 2){
            throw new ServiceException("50006", "请求失效,请重试!");
        }
    }



    /**
     * @Description: 组装查询渠道余额的数据
     * @param channelModel - 渠道数据
     * @return com.hz.platform.master.core.protocol.response.pay.ResponseDataCore
     * @author yoko
     * @date 2021/4/26 17:34
     */
    public static ResponseChannel assembleResponseChannelResult(ChannelModel channelModel){
        ResponseChannel resBean = new ResponseChannel();
        if(!StringUtils.isBlank(channelModel.getBalance())){
            resBean.channel_balance = channelModel.getBalance();
        }else {
            resBean.channel_balance = "0.0000";
        }
        if (!StringUtils.isBlank(channelModel.getLockMoney())){
            resBean.channel_lock_money = channelModel.getLockMoney();
        }else {
            resBean.channel_lock_money = "0.0000";
        }
        return resBean;
    }



    public static void main(String [] args) throws Exception{
//        String str = "<h1 style=\"margin: 20px 0px;\">请手动输入 ￥<span id=\"copy_price\">0.01</span></h1>";
//        int start = 0;
//        int end = 0;
//        int jl = 0;
//        if (str.indexOf("<span id=\"copy_price\">") > -1){
//            start = str.indexOf("<span id=\"copy_price\">");
//        }
//        if (start > -1){
//            end = str.indexOf("</span>", start);
//        }
//        String sb = "<span id=\"copy_price\">";
//        jl = sb.length();
//        StringBuilder str1 = new StringBuilder(str);
//        str1.replace(start + jl, end, "1");
//        System.out.println(start);
//        System.out.println(end);
//        System.out.println(str1);
//
//        int moneyType = 3;
////        String moneyRange = "50,100,200";
////        String moneyRange = "100-1000";
//        String moneyRange = "100-1000,2000-3000,5000-6000";
//        String total_amount = "6000.01";
//        checkGewayMoneyRange(moneyType, moneyRange, total_amount);

        String a = "6#1";
        String [] a_arr = a.split("#");
        log.info("a_arr.size:" + a_arr.length);


//        String sb ="SUCCESS,OK";
        String sb ="OK";
        if (sb.contains("OK")){
            log.info("res:" + "hah");
        }

    }






}
