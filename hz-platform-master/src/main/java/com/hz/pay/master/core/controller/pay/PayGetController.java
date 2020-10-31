package com.hz.pay.master.core.controller.pay;

import com.alibaba.fastjson.JSON;
import com.hz.pay.master.core.common.alipay.Alipay;
import com.hz.pay.master.core.common.controller.BaseController;
import com.hz.pay.master.core.common.exception.ExceptionMethod;
import com.hz.pay.master.core.common.exception.ServiceException;
import com.hz.pay.master.core.common.utils.DateUtil;
import com.hz.pay.master.core.common.utils.HttpSendUtils;
import com.hz.pay.master.core.common.utils.MD5Util;
import com.hz.pay.master.core.common.utils.StringUtil;
import com.hz.pay.master.core.common.utils.constant.ServerConstant;
import com.hz.pay.master.core.model.alipay.AlipayH5Model;
import com.hz.pay.master.core.model.alipay.AlipayModel;
import com.hz.pay.master.core.model.bufpay.BufpayModel;
import com.hz.pay.master.core.model.channel.ChannelModel;
import com.hz.pay.master.core.model.channeldata.ChannelDataModel;
import com.hz.pay.master.core.model.channelgeway.ChannelGewayModel;
import com.hz.pay.master.core.model.geway.GewayModel;
import com.hz.pay.master.core.model.geway.GewaytradetypeModel;
import com.hz.pay.master.core.model.receivingaccount.ReceivingAccountModel;
import com.hz.pay.master.core.model.receivingaccountdata.ReceivingAccountDataModel;
import com.hz.pay.master.core.model.zfbapp.ZfbAppModel;
import com.hz.pay.master.core.protocol.request.pay.RequestPay;
import com.hz.pay.master.util.ComponentUtil;
import com.hz.pay.master.util.HodgepodgeMethod;
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
@RequestMapping("/pay/get")
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
    public String dataCore(HttpServletRequest request, HttpServletResponse response,RequestPay requestData) throws Exception{
        String sgid = ComponentUtil.redisIdService.getNewId();
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
                if (StringUtils.isBlank(requestData.trade_type)){
                    throw new ServiceException("0003", "请填写交易类型!");
                }
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

            // 获取渠道的信息
            ChannelModel channelModel = new ChannelModel();
            channelModel.setChannel(requestData.channel);
            channelModel = (ChannelModel) ComponentUtil.channelService.findByObject(channelModel);
            if (channelModel == null || channelModel.getId() <= 0){
                throw new ServiceException("0010", "请填写正确的商家号!");
            }

            // 根据交易类型查询通道
            GewaytradetypeModel gewaytradetypeQuery = new GewaytradetypeModel();
            gewaytradetypeQuery.setMyTradeType(requestData.trade_type);
            GewaytradetypeModel gewaytradetypeModel = (GewaytradetypeModel)ComponentUtil.gewaytradetypeService.findByObject(gewaytradetypeQuery);
            if (gewaytradetypeModel == null || gewaytradetypeModel.getId() ==  null || gewaytradetypeModel.getId() <= 0){
                throw new ServiceException("0011", "请填写正确的支付类型!");
            }


            // 查询通道信息
            GewayModel gewayModel = (GewayModel) ComponentUtil.gewayService.findById(gewaytradetypeModel.getGewayId());
            if (gewayModel == null || gewayModel.getId() <= 0){
                throw new ServiceException("0012", "请联系运营人员!");
            }

            // 根据渠道ID加通道ID查询渠道与通道的关联关系
            ChannelGewayModel channelGewayModel = new ChannelGewayModel();
            channelGewayModel.setChannelId(channelModel.getId());
            channelGewayModel.setGewayId(gewayModel.getId());
            channelGewayModel = (ChannelGewayModel) ComponentUtil.channelGewayService.findByObject(channelGewayModel);
            if (channelGewayModel == null || channelGewayModel.getId() <= 0){
                throw new ServiceException("0013", "请联系运营人员!");
            }


            // 校验sign签名
            String checkSign = "channel=" + requestData.channel + "&" + "trade_type=" + requestData.trade_type + "&" + "total_amount=" + requestData.total_amount
                    + "&" + "out_trade_no=" + requestData.out_trade_no + "&" + "notify_url=" + requestData.notify_url + "&" + "key=" + channelModel.getSecretKey();
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
}
