package com.hz.platform.master.core.controller.pay;

import com.alibaba.fastjson.JSON;
import com.hz.platform.master.core.common.controller.BaseController;
import com.hz.platform.master.core.common.exception.ExceptionMethod;
import com.hz.platform.master.core.common.exception.ServiceException;
import com.hz.platform.master.core.common.utils.*;
import com.hz.platform.master.core.common.utils.constant.CacheKey;
import com.hz.platform.master.core.common.utils.constant.CachedKeyUtils;
import com.hz.platform.master.core.common.utils.constant.ServerConstant;
import com.hz.platform.master.core.model.channel.ChannelModel;
import com.hz.platform.master.core.model.channelbalancededuct.ChannelBalanceDeductModel;
import com.hz.platform.master.core.model.channeldata.ChannelDataModel;
import com.hz.platform.master.core.model.channelgeway.ChannelGewayModel;
import com.hz.platform.master.core.model.channelout.ChannelOutModel;
import com.hz.platform.master.core.model.geway.GewayModel;
import com.hz.platform.master.core.model.geway.GewaytradetypeModel;
import com.hz.platform.master.core.protocol.request.pay.RequestPay;
import com.hz.platform.master.core.protocol.request.pay.RequestPayOut;
import com.hz.platform.master.util.ComponentUtil;
import com.hz.platform.master.util.HodgepodgeMethod;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description TODO
 * @Author yoko
 * @Date 2020/6/8 22:35
 * @Version 1.0
 */
@RestController
@RequestMapping("/platform/get")
public class PayGetController extends BaseController {

    private static Logger log = LoggerFactory.getLogger(PayController.class);

    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    /**
     * 15分钟.
     */
    public long FIFTEEN_MIN = 900;

    /**
     * 30分钟.
     */
    public long THIRTY_MIN = 30;

    @Value("${secret.key.token}")
    private String secretKeyToken;

    @Value("${secret.key.sign}")
    private String secretKeySign;



    /**
     * @Description: 首页-开始浏览广告
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8082/pay/get/dataCore
     * test:http://localhost:8085/pay/get/dataCore?channel=channel_7&trade_type=70001&total_amount=0.01&out_trade_no=out_trade_no_1&notify_url=http://www.baidu.com/sb&interface_ver=V5.0&extra_return_param=extra_return_param_1&client_ip=192.168.0.1&sign=db4d24010fcfaafef7b4fd72159cd7eb&sub_time=2020-03-24 17:52:13&product_name=product_name_1&product_code=product_code_1&return_url=http://www.qidian.com
     * 请求的属性类:RequestAppeal
     * 必填字段:{"agtVer":1,"clientVer":1,"clientType":1,"ctime":201911071802959,"cctime":201911071802959,"sign":"abcdefg","token":"111111"}
     * 客户端加密字段:token+ctime+秘钥=sign
     * 返回加密字段:stime+秘钥=sign
     *
     * {
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJzaWduIjoiN2UyZDhlODA4NWRhNzE5YzAzNWU2NTNiNGZmZmUzYjYiLCJzdGltZSI6MTU4MzMwNTc0NTQzOSwid2giOnsiYWRBZHMiOiJodHRwOi8vd3d3LnFpZGlhbi5jb20iLCJwcm9maXQiOiIwLjMyIn19"
     *     },
     *     "sgid": "202003041509030000001",
     *     "cgid": ""
     * }
     *
     * 九通：
     * {"channel":"channel_2","trade_type":"2032","total_amount":"10","out_trade_no":"out_trade_no_1","notify_url":"http://www.baidu.com/sb","interface_ver":"V5.0","extra_return_param":"extra_return_param_1","client_ip":"192.168.0.1","sign":"c414e730be7d93bec15408b83dd69281","sub_time":"2020-03-24 17:52:13","product_name":"product_name_1","product_code":"product_code_1","return_url":"http://www.baidu.com/return_url"}
     */
    @RequestMapping(value = "/dataCore", method = {RequestMethod.GET})
    public String dataCore(HttpServletRequest request, HttpServletResponse response, RequestPay requestData) throws Exception{
        String sgid = "PDS" + ComponentUtil.redisIdService.getNewId();
        String cgid = "";
        String ip = StringUtil.getIpAddress(request);
        String data = "";
        RequestPay requestModel = new RequestPay();
        try{
            if (requestData == null){
                throw new ServiceException("0001", "请填写参数值!");
            }else {
                if (StringUtils.isBlank(requestData.channel)){
                    throw new ServiceException("0002", "请填写商铺号!");
                }
//                if (StringUtils.isBlank(requestData.trade_type)){
//                    throw new ServiceException("0003", "请填写交易类型!");
//                }
                log.info("");
                if (StringUtils.isBlank(requestData.total_amount)){
                    throw new ServiceException("0004", "请填写订单金额!");
                }
                if (StringUtils.isBlank(requestData.out_trade_no)){
                    throw new ServiceException("0005", "请填写商家订单号!");
                }
                if (StringUtils.isBlank(requestData.notify_url)){
                    throw new ServiceException("0006", "请填写异步通知地址!");
                }
                if (StringUtils.isBlank(requestData.interface_ver)){
                    throw new ServiceException("0007", "请填写接口版本!");
                }
                if (StringUtils.isBlank(requestData.return_url)){
                    throw new ServiceException("0008", "请填写跳转页!");
                }
                if (StringUtils.isBlank(requestData.sign)){
                    throw new ServiceException("0009", "请填写签名!");
                }
            }

            String trade_type = "";
            if (!StringUtils.isBlank(requestData.trade_type)){
                trade_type = requestData.trade_type;
            }

            // 获取渠道的信息
            ChannelModel channelModel = new ChannelModel();
            channelModel.setChannel(requestData.channel);
            channelModel = (ChannelModel) ComponentUtil.channelService.findByObject(channelModel);
            if (channelModel == null || channelModel.getId() <= 0){
                throw new ServiceException("0010", "请填写正确的商家号!");
            }

            // 渠道与通道的关联关系
            ChannelGewayModel channelGewayModel = null;

            // 通道信息
            GewayModel gewayModel = null;

            // 根据交易类型查询通道
            GewaytradetypeModel gewaytradetypeModel = null;
            if (!StringUtils.isBlank(trade_type)){
                GewaytradetypeModel gewaytradetypeQuery = new GewaytradetypeModel();
                gewaytradetypeQuery.setMyTradeType(requestData.trade_type);
                gewaytradetypeModel = (GewaytradetypeModel)ComponentUtil.gewaytradetypeService.findByObject(gewaytradetypeQuery);
                if (gewaytradetypeModel == null || gewaytradetypeModel.getId() ==  null || gewaytradetypeModel.getId() <= 0){
                    log.info("");
                    throw new ServiceException("0011", "请填写正确的支付类型!");
                }

                // 查询通道信息
                gewayModel = (GewayModel) ComponentUtil.gewayService.findById(gewaytradetypeModel.getGewayId());
                if (gewayModel == null || gewayModel.getId() <= 0){
                    throw new ServiceException("0012", "请联系运营人员!");
                }

                // 获取渠道与通道的关联关系
                ChannelGewayModel channelGewayQuery = new ChannelGewayModel();
                channelGewayQuery.setChannelId(channelModel.getId());
                channelGewayQuery.setGewayId(gewayModel.getId());
                channelGewayModel = (ChannelGewayModel) ComponentUtil.channelGewayService.findByObject(channelGewayQuery);
                if (channelGewayModel == null || channelGewayModel.getId() <= 0){
                    throw new ServiceException("0013", "请联系运营人员!");
                }

            }else{
                // 查询此渠道下代收的通道集合
                ChannelGewayModel channelGewayQuery = HodgepodgeMethod.assembleChannelGewayQuery(0, channelModel.getId(), 0, 2, 1);
                List<ChannelGewayModel> channelGewayList = ComponentUtil.channelGewayService.findByCondition(channelGewayQuery);
                if (channelGewayList == null || channelGewayList.size() <= 0){
                    throw new ServiceException("1001", "请联系运营人员!");
                }
                log.info("");
                // 从集合中按照比例筛选一个
                channelGewayModel = HodgepodgeMethod.ratioChannelGeway(channelGewayList);
                if (channelGewayModel == null || channelGewayModel.getId() <= 0){
                    throw new ServiceException("0013", "请联系运营人员!");
                }

                GewaytradetypeModel gewaytradetypeQuery = new GewaytradetypeModel();
                gewaytradetypeQuery.setGewayId(channelGewayModel.getGewayId());
                gewaytradetypeModel = (GewaytradetypeModel)ComponentUtil.gewaytradetypeService.findByObject(gewaytradetypeQuery);
                if (gewaytradetypeModel == null || gewaytradetypeModel.getId() ==  null || gewaytradetypeModel.getId() <= 0){
                    throw new ServiceException("0011", "请填写正确的支付类型!");
                }

                // 查询通道信息
                gewayModel = (GewayModel) ComponentUtil.gewayService.findById(gewaytradetypeModel.getGewayId());
                if (gewayModel == null || gewayModel.getId() <= 0){
                    throw new ServiceException("0012", "请联系运营人员!");
                }
            }

            if (gewayModel.getGewayType() == 2){
                // 通道属于预付款类型， 需要对通道金额进行check
                boolean flag_geway = HodgepodgeMethod.checkGewayMoney(gewayModel, requestData.total_amount);
                if (!flag_geway){
                    throw new ServiceException("0015", "请您稍后再试,谢谢!");
                }
            }


            // 校验sign签名
            String checkSign = "";
            if (!StringUtils.isBlank(requestData.trade_type)){
                checkSign = "channel=" + requestData.channel + "&" + "trade_type=" + requestData.trade_type + "&" + "total_amount=" + requestData.total_amount
                        + "&" + "out_trade_no=" + requestData.out_trade_no + "&" + "notify_url=" + requestData.notify_url + "&" + "key=" + channelModel.getSecretKey();
            }else {
                checkSign = "channel=" + requestData.channel + "&" + "total_amount=" + requestData.total_amount
                        + "&" + "out_trade_no=" + requestData.out_trade_no + "&" + "notify_url=" + requestData.notify_url + "&" + "key=" + channelModel.getSecretKey();
                requestData.trade_type = gewaytradetypeModel.getMyTradeType();
            }

            checkSign = MD5Util.encryption(checkSign);
            if (!requestData.sign.equals(checkSign)){
                throw new ServiceException("0014", "签名错误!");
            }



            boolean sendFlag = false;// 请求结果：false表示请求失败，true表示请求成功

            // 计算手续费
            String serviceCharge = "";
            // 使用上游手续费
//            Map<String, String> map = HodgepodgeMethod.getPayCode(requestData.trade_type);
            String payCode = gewaytradetypeModel.getOutTradeType();
            if(!StringUtils.isBlank(channelGewayModel.getServiceCharge())){
                // 使用统一手续费
                serviceCharge = channelGewayModel.getServiceCharge();
            }else{
                // 硬编码手续费
                if (!StringUtils.isBlank(gewaytradetypeModel.getServiceCharge())){
                    serviceCharge = gewaytradetypeModel.getServiceCharge();
                }
            }


            String my_notify_url = gewayModel.getNotifyUrl();
            String notify_url = requestData.notify_url;
            String total_amount = "";
            String nowTime = DateUtil.getNowPlusTime();
            String resData = "";
            String qrCodeUrl = "";
            //组装拼接
            if (gewayModel.getContacts().equals("FINE")){
                // 美好
                Map<String ,Object> sendDataMap = new HashMap<>();
                sendDataMap.put("money", requestData.total_amount);
                sendDataMap.put("payType", payCode);
                sendDataMap.put("outTradeNo", sgid);
                sendDataMap.put("notifyUrl", my_notify_url);
                sendDataMap.put("returnUrl", requestData.return_url);
                String parameter = JSON.toJSONString(sendDataMap);
                parameter = StringUtil.mergeCodeBase64(parameter);
                Map<String, String> sendMap = new HashMap<>();
                sendMap.put("jsonData", parameter);
                String sendData = JSON.toJSONString(sendMap);
                String fineData = HttpSendUtils.sendPostAppJson(gewayModel.getInterfaceAds(), sendData);
                Map<String, Object> resMap = new HashMap<>();
                Map<String, Object> dataMap = new HashMap<>();
                if (!StringUtils.isBlank(fineData)) {
                    resMap = JSON.parseObject(fineData, Map.class);
                    if (resMap.get("resultCode").equals("0")) {
                        Map<String, Object> mapData = new HashMap<>();
                        mapData =  JSON.parseObject(resMap.get("data").toString(), Map.class);
                        String jsonData = StringUtil.decoderBase64(mapData.get("jsonData").toString());
                        Map<String, Object> orderMap = new HashMap<>();
                        dataMap = JSON.parseObject(jsonData, Map.class);
                        orderMap = (Map<String, Object>) dataMap.get("order");
                        qrCodeUrl = (String) orderMap.get("qrCodeUrl");
                        resData = "ok";
                    }
                }
                log.info("--------------resData:" + resData);
            }else if (gewayModel.getContacts().equals("FR")){
                // 美好
                Map<String ,Object> sendDataMap = new HashMap<>();
                sendDataMap.put("money", requestData.total_amount);
                sendDataMap.put("payType", payCode);
                sendDataMap.put("outTradeNo", sgid);
                sendDataMap.put("secretKey", channelModel.getSecretKey());
                sendDataMap.put("notifyUrl", my_notify_url);
                sendDataMap.put("returnUrl", requestData.return_url);
                String parameter = JSON.toJSONString(sendDataMap);
                parameter = StringUtil.mergeCodeBase64(parameter);
                Map<String, String> sendMap = new HashMap<>();
                sendMap.put("jsonData", parameter);
                String sendData = JSON.toJSONString(sendMap);
                String fineData = HttpSendUtils.sendPostAppJson(gewayModel.getInterfaceAds(), sendData);
                Map<String, Object> resMap = new HashMap<>();
                Map<String, Object> dataMap = new HashMap<>();
                if (!StringUtils.isBlank(fineData)) {
                    resMap = JSON.parseObject(fineData, Map.class);
                    if (resMap.get("resultCode").equals("0")) {
                        Map<String, Object> mapData = new HashMap<>();
                        mapData =  JSON.parseObject(resMap.get("data").toString(), Map.class);
                        String jsonData = StringUtil.decoderBase64(mapData.get("jsonData").toString());
                        Map<String, Object> orderMap = new HashMap<>();
                        dataMap = JSON.parseObject(jsonData, Map.class);
                        orderMap = (Map<String, Object>) dataMap.get("order");
                        qrCodeUrl = (String) orderMap.get("qrCodeUrl");
                        resData = "ok";
                    }
                }
                log.info("--------------resData:" + resData);
            }else if(gewayModel.getContacts().equals("WP")){
                String api_url = gewayModel.getInterfaceAds();
                String api_key = gewayModel.getSecretKey();
                String callback_url = requestData.return_url;
                String out_trade_no = sgid;
                String total_fee = requestData.total_amount;
                String res = WPayHelper.getPayUrl(api_url, api_key, callback_url, my_notify_url, out_trade_no, total_fee);
                if (!StringUtils.isBlank(res)){
                    qrCodeUrl = res;
                    resData = "ok";
                }
            }else if (gewayModel.getContacts().equals("SZ")){
                // 狮子支付
                Map<String ,Object> sendDataMap = new HashMap<>();


                sendDataMap.put("merchant_code", gewayModel.getPayId());
                sendDataMap.put("order_id", sgid);
                sendDataMap.put("merchant_time", DateUtil.getNowPlusTime());
                sendDataMap.put("paytype", payCode);
                sendDataMap.put("subject", "钻石");
                sendDataMap.put("body", "打赏");
                sendDataMap.put("amount", requestData.total_amount);
                sendDataMap.put("returnUrl", requestData.return_url);
                sendDataMap.put("notify_url", gewayModel.getNotifyUrl());


                String mySign = "merchant_code=" + sendDataMap.get("merchant_code") + "&" + "order_id=" + sendDataMap.get("order_id") + "&" + "merchant_time=" + sendDataMap.get("merchant_time") + "&" + "paytype=" + sendDataMap.get("paytype")
                        + "&" + "subject=" + sendDataMap.get("subject") + "&" + "body=" + sendDataMap.get("body") + "&" + "amount=" + sendDataMap.get("amount") + "&" + "returnUrl=" + sendDataMap.get("returnUrl") + "&" + "notify_url=" + sendDataMap.get("notify_url")
                        + gewayModel.getSecretKey();
                log.info("--------sz--------data:" + mySign);
//                mySign = MD5Util.encryption(mySign).toLowerCase();
                mySign = ASCIISort.getSign(sendDataMap, gewayModel.getSecretKey(), 1);
                log.info("--------sz--------mySign:" + mySign);
                sendDataMap.put("sign", mySign);
                String parameter = JSON.toJSONString(sendDataMap);

                String szData = HttpSendUtils.sendPostAppJson(gewayModel.getInterfaceAds(), parameter);
                Map<String, Object> resMap = new HashMap<>();
                if (!StringUtils.isBlank(szData)) {
                    resMap = JSON.parseObject(szData, Map.class);
                    if (resMap.get("code").equals("0")) {
                        qrCodeUrl = (String) resMap.get("pay_url");
                        resData = "ok";
                    }
                }
                log.info("--------------resData:" + resData);
            }
            if (StringUtils.isBlank(resData)){
                sendFlag = false;
            }else {
                if (resData.indexOf("error") > -1 || resData.indexOf("ERROR") > -1){
                    sendFlag = false;
                }else {
                    sendFlag = true;
                }
            }
            ChannelDataModel channelDataModel = HodgepodgeMethod.assembleChannelData(requestData, sgid, channelModel.getId(), gewayModel.getId(), channelGewayModel.getId(), channelGewayModel.getProfitType(), nowTime, my_notify_url, serviceCharge, sendFlag);
            ComponentUtil.channelDataService.add(channelDataModel);
            if (!StringUtils.isBlank(qrCodeUrl)){
//                qrCodeUrl = "http://www.baidu.com";
                qrCodeUrl = URLDecoder.decode(qrCodeUrl, "UTF-8" );
                if (!StringUtils.isBlank(requestData.noredirect)){
                    resData = StringUtil.mergeCodeBase64(qrCodeUrl);
                }else {
                    response.sendRedirect(qrCodeUrl);
                    return null;
                }
            }
            // 返回数据给客户端
            return resData;
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // #添加异常
            log.error(String.format("this PayController.dataCore() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            e.printStackTrace();
            return JSON.toJSONString(map);
        }
    }






    /**
     * @Description: 代付订单-get
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8092/platform/get/outOrder
     * http://localhost:8092/platform/get/outOrder?channel=channel_3&total_amount=500.00&sign=bb2beddaf8aaccbe134616236e1dc429&bank_name=%E4%B8%AD%E5%9B%BD%E9%93%B6%E8%A1%8C&bank_card=%E9%93%B6%E8%A1%8C%E5%8D%A1%E5%8D%A1%E5%8F%B7&account_name=%E5%BC%80%E6%88%B7%E4%BA%BA&bank_subbranch=%E9%93%B6%E8%A1%8C%E6%94%AF%E8%A1%8C&bank_province=%E9%93%B6%E8%A1%8C%E5%BC%80%E6%88%B7%E7%9C%81&bank_city=%E9%93%B6%E8%A1%8C%E5%BC%80%E6%88%B7%E5%9F%8E%E5%B8%82&trade_type=200001&out_trade_no=df_out_trade_no_1&notify_url=http://www.baidu.com/sb&interface_ver=V5.0&extra_return_param=extra_return_param_1&client_ip=192.168.0.1&product_name=product_name_1&product_code=product_code_1&return_url=http://www.baidu.com/return_url
     * 请求的属性类:RequestAppeal
     * 必填字段:channel=channel_3&total_amount=500.00&sign=bb2beddaf8aaccbe134616236e1dc429&bank_name=中国银行&bank_card=银行卡卡号&account_name=开户人&bank_subbranch=银行支行&bank_province=银行开户省&bank_city=银行开户城市&trade_type=200001&out_trade_no=df_out_trade_no_1&notify_url=http://www.baidu.com/sb&interface_ver=V5.0&extra_return_param=extra_return_param_1&client_ip=192.168.0.1&product_name=product_name_1&product_code=product_code_1&return_url=http://www.baidu.com/return_url
     * 必填字段:{"channel":"channel_2","bank_name":"bank_name1","bank_card":"bank_card1","account_name":"account_name1","trade_type":"200001","total_amount":"500.00","out_trade_no":"df_out_trade_no_1","notify_url":"http://www.baidu.com/sb","interface_ver":"V5.0","extra_return_param":"extra_return_param_1","client_ip":"192.168.0.1","sign":"c414e730be7d93bec15408b83dd69281","sub_time":"2020-03-24 17:52:13","product_name":"product_name_1","product_code":"product_code_1","return_url":"http://www.baidu.com/return_url"}
     *
     * {"channel":"channel_3","bank_name":"中国银行","bank_card":"银行卡卡号","account_name":"开户人","trade_type":"200001","total_amount":"500.00","out_trade_no":"df_out_trade_no_1","notify_url":"http://www.baidu.com/sb","interface_ver":"V5.0","extra_return_param":"extra_return_param_1","client_ip":"192.168.0.1","sign":"bb2beddaf8aaccbe134616236e1dc429","sub_time":"2020-03-24 17:52:13","product_name":"product_name_1","product_code":"product_code_1","return_url":"http://www.baidu.com/return_url"}
     * {"channel":"channel_3","bank_name":"中国银行","bank_card":"银行卡卡号","account_name":"开户人","bank_subbranch":"银行支行","bank_province":"银行开户省","bank_city":"银行开户城市","trade_type":"200001","total_amount":"500.00","out_trade_no":"df_out_trade_no_1","notify_url":"http://www.baidu.com/sb","interface_ver":"V5.0","extra_return_param":"extra_return_param_1","client_ip":"192.168.0.1","sign":"bb2beddaf8aaccbe134616236e1dc429","sub_time":"2020-03-24 17:52:13","product_name":"product_name_1","product_code":"product_code_1","return_url":"http://www.baidu.com/return_url"}
     * 客户端加密字段:token+ctime+秘钥=sign
     * 返回加密字段:stime+秘钥=sign
     *
     * {
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "jsonData": "eyJzaWduIjoiN2UyZDhlODA4NWRhNzE5YzAzNWU2NTNiNGZmZmUzYjYiLCJzdGltZSI6MTU4MzMwNTc0NTQzOSwid2giOnsiYWRBZHMiOiJodHRwOi8vd3d3LnFpZGlhbi5jb20iLCJwcm9maXQiOiIwLjMyIn19"
     *     },
     *     "sgid": "202003041509030000001",
     *     "cgid": ""
     * }
     *
     * 九通：
     * {"channel":"channel_2","trade_type":"2032","total_amount":"10","out_trade_no":"out_trade_no_1","notify_url":"http://www.baidu.com/sb","interface_ver":"V5.0","extra_return_param":"extra_return_param_1","client_ip":"192.168.0.1","sign":"c414e730be7d93bec15408b83dd69281","sub_time":"2020-03-24 17:52:13","product_name":"product_name_1","product_code":"product_code_1","return_url":"http://www.baidu.com/return_url"}
     */
    @RequestMapping(value = "/outOrder", method = {RequestMethod.GET})
    public JsonResult<Object> order(HttpServletRequest request, HttpServletResponse response, RequestPayOut requestData) throws Exception{
        String sgid = "PDF" + ComponentUtil.redisIdService.getNewId();
        String cgid = "";
        String ip = StringUtil.getIpAddress(request);
        String data = "";
        RequestPay requestModel = new RequestPay();
        try{
            if (requestData == null){
                throw new ServiceException("0001", "请填写参数值!");
            }else {
                if (StringUtils.isBlank(requestData.channel)){
                    throw new ServiceException("0002", "请填写商铺号!");
                }
//                if (StringUtils.isBlank(requestData.trade_type)){
//                    throw new ServiceException("0003", "请填写交易类型!");
//                }
                if (StringUtils.isBlank(requestData.total_amount)){
                    throw new ServiceException("0004", "请填写订单金额!");
                }
                if (StringUtils.isBlank(requestData.out_trade_no)){
                    throw new ServiceException("0005", "请填写商家订单号!");
                }
                if (StringUtils.isBlank(requestData.notify_url)){
                    throw new ServiceException("0006", "请填写异步通知地址!");
                }
                if (StringUtils.isBlank(requestData.interface_ver)){
                    throw new ServiceException("0007", "请填写接口版本!");
                }
                if (StringUtils.isBlank(requestData.bank_name)){
                    throw new ServiceException("0008", "请填写转账的银行名称!");
                }
                if (StringUtils.isBlank(requestData.bank_card)){
                    throw new ServiceException("0009", "请填写转账的银行卡卡号!");
                }
                log.info("");
                if (StringUtils.isBlank(requestData.bank_card)){
                    throw new ServiceException("0010", "请填写转账的银行开户人!");
                }
                if (StringUtils.isBlank(requestData.sign)){
                    throw new ServiceException("0011", "请填写签名!");
                }
            }

            String trade_type = "";
            if (!StringUtils.isBlank(requestData.trade_type)){
                trade_type = requestData.trade_type;
            }

            // 获取渠道的信息
            ChannelModel channelModel = new ChannelModel();
            channelModel.setChannel(requestData.channel);
            channelModel = (ChannelModel) ComponentUtil.channelService.findByObject(channelModel);
            if (channelModel == null || channelModel.getId() <= 0){
                throw new ServiceException("0012", "请填写正确的商家号!");
            }

            // 判断余额是否大于订单金额
            HodgepodgeMethod.checkMoney(channelModel.getBalance(), requestData.total_amount);

            // 渠道与通道的关联关系
            ChannelGewayModel channelGewayModel = null;

            // 通道信息
            GewayModel gewayModel = null;

            // 根据交易类型查询通道
            GewaytradetypeModel gewaytradetypeModel = null;
            if (!StringUtils.isBlank(trade_type)){
                GewaytradetypeModel gewaytradetypeQuery = new GewaytradetypeModel();
                gewaytradetypeQuery.setMyTradeType(requestData.trade_type);
                log.info("");
                gewaytradetypeModel = (GewaytradetypeModel)ComponentUtil.gewaytradetypeService.findByObject(gewaytradetypeQuery);
                if (gewaytradetypeModel == null || gewaytradetypeModel.getId() ==  null || gewaytradetypeModel.getId() <= 0){
                    throw new ServiceException("0011", "请填写正确的支付类型!");
                }

                // 查询通道信息
                gewayModel = (GewayModel) ComponentUtil.gewayService.findById(gewaytradetypeModel.getGewayId());
                if (gewayModel == null || gewayModel.getId() <= 0){
                    throw new ServiceException("0012", "请联系运营人员!");
                }

                // 获取渠道与通道的关联关系
                ChannelGewayModel channelGewayQuery = new ChannelGewayModel();
                channelGewayQuery.setChannelId(channelModel.getId());
                channelGewayQuery.setGewayId(gewayModel.getId());
                channelGewayModel = (ChannelGewayModel) ComponentUtil.channelGewayService.findByObject(channelGewayQuery);
                if (channelGewayModel == null || channelGewayModel.getId() <= 0){
                    throw new ServiceException("0013", "请联系运营人员!");
                }

            }else{
                // 查询此渠道下代收的通道集合
                ChannelGewayModel channelGewayQuery = HodgepodgeMethod.assembleChannelGewayQuery(0, channelModel.getId(), 0, 3, 1);
                List<ChannelGewayModel> channelGewayList = ComponentUtil.channelGewayService.findByCondition(channelGewayQuery);
                if (channelGewayList == null || channelGewayList.size() <= 0){
                    throw new ServiceException("1001", "请联系运营人员!");
                }
                // 从集合中按照比例筛选一个
                log.info("");
                channelGewayModel = HodgepodgeMethod.ratioChannelGeway(channelGewayList);
                if (channelGewayModel == null || channelGewayModel.getId() <= 0){
                    throw new ServiceException("0013", "请联系运营人员!");
                }

                GewaytradetypeModel gewaytradetypeQuery = new GewaytradetypeModel();
                gewaytradetypeQuery.setGewayId(channelGewayModel.getGewayId());
                gewaytradetypeModel = (GewaytradetypeModel)ComponentUtil.gewaytradetypeService.findByObject(gewaytradetypeQuery);
                if (gewaytradetypeModel == null || gewaytradetypeModel.getId() ==  null || gewaytradetypeModel.getId() <= 0){
                    throw new ServiceException("0011", "请填写正确的支付类型!");
                }

                // 查询通道信息
                gewayModel = (GewayModel) ComponentUtil.gewayService.findById(gewaytradetypeModel.getGewayId());
                if (gewayModel == null || gewayModel.getId() <= 0){
                    throw new ServiceException("0012", "请联系运营人员!");
                }
            }

            // 校验sign签名
            String checkSign = "";
            if (!StringUtils.isBlank(requestData.trade_type)){
                checkSign = "channel=" + requestData.channel + "&" + "trade_type=" + requestData.trade_type + "&" + "total_amount=" + requestData.total_amount
                        + "&" + "out_trade_no=" + requestData.out_trade_no + "&" + "bank_name=" + requestData.bank_name + "&"
                        + "&" + "bank_card=" + requestData.bank_card + "&" + "account_name=" + requestData.account_name
                        + "key=" + channelModel.getSecretKey();
            }else {
                checkSign = "channel=" + requestData.channel + "&" + "total_amount=" + requestData.total_amount
                        + "&" + "out_trade_no=" + requestData.out_trade_no + "&" + "bank_name=" + requestData.bank_name + "&"
                        + "&" + "bank_card=" + requestData.bank_card + "&" + "account_name=" + requestData.account_name
                        + "key=" + channelModel.getSecretKey();
                requestData.trade_type = gewaytradetypeModel.getMyTradeType();
            }
            checkSign = MD5Util.encryption(checkSign);
            if (!requestData.sign.equals(checkSign)){
                throw new ServiceException("0016", "签名错误!");
            }


            boolean sendFlag = false;// 请求结果：false表示请求失败，true表示请求成功

            // 计算手续费
            String serviceCharge = "";
            String extraServiceCharge = "";// 额外手续费
            // 使用上游手续费
//            Map<String, String> map = HodgepodgeMethod.getPayCode(requestData.trade_type);
            String payCode = gewaytradetypeModel.getOutTradeType();
            if(!StringUtils.isBlank(channelGewayModel.getServiceCharge())){
                // 使用统一手续费
                serviceCharge = channelGewayModel.getServiceCharge();
                if (channelGewayModel.getServiceChargeType() == 2){
                    if (!StringUtils.isBlank(channelGewayModel.getExtraServiceCharge())){
                        extraServiceCharge = channelGewayModel.getExtraServiceCharge();
                    }
                }
            }else{
                // 硬编码手续费
                log.info("1");
                if (!StringUtils.isBlank(gewaytradetypeModel.getServiceCharge())){
                    serviceCharge = gewaytradetypeModel.getServiceCharge();
                }
            }


            String my_notify_url = gewayModel.getNotifyUrl();
            String notify_url = requestData.notify_url;
            String total_amount = "";
            String nowTime = DateUtil.getNowPlusTime();
            String resData = "";
            String qrCodeUrl = "";
            //组装拼接
            if (gewayModel.getContacts().equals("CK")){
                // 蛋糕
                Map<String ,Object> sendDataMap = new HashMap<>();
                sendDataMap.put("money", requestData.total_amount);
                sendDataMap.put("payType", payCode);
                sendDataMap.put("outTradeNo", sgid);
                sendDataMap.put("secretKey", channelModel.getSecretKey());
                sendDataMap.put("notifyUrl", my_notify_url);
                sendDataMap.put("inBankCard", requestData.bank_card);
                sendDataMap.put("inBankName", requestData.bank_name);
                log.info("");
                sendDataMap.put("inAccountName", requestData.account_name);
                sendDataMap.put("inBankSubbranch", requestData.bank_subbranch);
                sendDataMap.put("inBankProvince", requestData.bank_province);
                sendDataMap.put("inBankCity", requestData.bank_city);
                String parameter = JSON.toJSONString(sendDataMap);
                parameter = StringUtil.mergeCodeBase64(parameter);
                Map<String, String> sendMap = new HashMap<>();
                sendMap.put("jsonData", parameter);
                String sendData = JSON.toJSONString(sendMap);
                String fineData = HttpSendUtils.sendPostAppJson(gewayModel.getInterfaceAds(), sendData);
                Map<String, Object> resMap = new HashMap<>();
                Map<String, Object> dataMap = new HashMap<>();
                if (!StringUtils.isBlank(fineData)) {
                    resMap = JSON.parseObject(fineData, Map.class);
                    if (resMap.get("resultCode").equals("0")) {
                        sendFlag = true;
                    }
                }
                log.info("--------------resData:" + resData);
            }else if(gewayModel.getContacts().equals("HF")){
                Map<String ,Object> sendDataMap = new HashMap<>();
                sendDataMap.put("uid", gewayModel.getPayId());
                sendDataMap.put("addtime", String.valueOf(System.currentTimeMillis()));
                sendDataMap.put("out_trade_id", sgid);
                sendDataMap.put("amount", requestData.total_amount);
                sendDataMap.put("bankcode", "unionpay");
                sendDataMap.put("bankuser", requestData.account_name);
                sendDataMap.put("bankname", requestData.bank_name);
                sendDataMap.put("bankno", requestData.bank_card);
                sendDataMap.put("notifyurl", my_notify_url);
                log.info("");
                String sb = ASCIISort.getSign(sendDataMap, gewayModel.getSecretKey(), 2);
                sendDataMap.put("sign", sb);
                String sendData = JSON.toJSONString(sendDataMap);
                String resultData = HttpSendUtils.sendPostAppJson(gewayModel.getInterfaceAds(), sendData);
                Map<String, Object> resMap = new HashMap<>();
                if (!StringUtils.isBlank(resultData)) {
                    resMap = JSON.parseObject(resultData, Map.class);
                    if (Integer.parseInt(resMap.get("code").toString()) == 1) {
                        sendFlag = true;
                    }
                }
            }

            boolean flag = false;// 是否执行成功
            if (sendFlag){
                int count = 0;
                while (1==1){
                    // 锁住要执行的渠道ID
                    String lockKey = CachedKeyUtils.getCacheKey(CacheKey.LOCK_TASK_WORK_TYPE_CHANNEL, channelModel.getId());
                    boolean flagLock = ComponentUtil.redisIdService.lock(lockKey);
                    if (flagLock){
                        // 组装扣减渠道余额
                        ChannelModel updateBalance = HodgepodgeMethod.assembleChannelBalance(channelModel.getId(), requestData.total_amount, serviceCharge, extraServiceCharge);
                        // 组装添加渠道扣减余额的流水
                        ChannelBalanceDeductModel channelBalanceDeductModel = HodgepodgeMethod.assembleChannelBalanceDeduct(0, channelModel.getId(), sgid, 2, updateBalance.getOrderMoney(),
                                0, null, null, 2);
                        // 组装渠道请求的代付订单信息
                        ChannelOutModel channelOutModel = HodgepodgeMethod.assembleChannelOutData(requestData, sgid, channelModel.getId(), gewayModel.getId(),
                                channelGewayModel.getId(), channelGewayModel.getProfitType(), nowTime, my_notify_url, serviceCharge, updateBalance.getOrderMoney(), updateBalance.getServiceChargeMoney(), sendFlag);
                        try {
                            // 正式处理逻辑
                            flag = ComponentUtil.channelOutService.handleChannelOut(updateBalance, channelBalanceDeductModel, channelOutModel);
                            if (flag){
                                // 解锁
                                ComponentUtil.redisIdService.delLock(lockKey);
                                break;
                            }
                        }catch (Exception e){
                            log.info("");
                            // 解锁
                            ComponentUtil.redisIdService.delLock(lockKey);
                            log.error(String.format("this handleChannelOut is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
                            e.printStackTrace();
                            break;
                        }

                    }else {
                        count ++;
                    }
                    if (count >= 10){
                        // 解锁
                        ComponentUtil.redisIdService.delLock(lockKey);
                        break;
                    }
                }
            }else {
                // 请求蛋糕平台失败：纪录请求的纪录，并且请求状态是失败状态
                ChannelOutModel channelOutModel = HodgepodgeMethod.assembleChannelOutData(requestData, sgid, channelModel.getId(), gewayModel.getId(),
                        channelGewayModel.getId(), channelGewayModel.getProfitType(), nowTime, my_notify_url, serviceCharge, null,null, sendFlag);
                ComponentUtil.channelOutService.add(channelOutModel);
            }

            if (!flag){
                return JsonResult.failedResult("拉单失败,请稍后再试", "5001");
            }


            // 返回数据给客户端
            Map<String, String> resMap = new HashMap<>();
            resMap.put("trade_no", sgid);
            resMap.put("out_trade_no", requestData.out_trade_no);
            resMap.put("total_amount", requestData.total_amount);
            return JsonResult.successResult(resMap);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // #添加异常
            log.error(String.format("this PayGetController.order() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"));
        }
    }



}
