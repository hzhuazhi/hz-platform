package com.hz.platform.master.core.controller.order;

import com.alibaba.fastjson.JSON;
import com.hz.platform.master.core.common.alipay.Alipay;
import com.hz.platform.master.core.common.controller.BaseController;
import com.hz.platform.master.core.common.exception.ExceptionMethod;
import com.hz.platform.master.core.common.exception.ServiceException;
import com.hz.platform.master.core.common.utils.*;
import com.hz.platform.master.core.common.utils.constant.CacheKey;
import com.hz.platform.master.core.common.utils.constant.CachedKeyUtils;
import com.hz.platform.master.core.common.utils.constant.ServerConstant;
import com.hz.platform.master.core.controller.pay.PayController;
import com.hz.platform.master.core.model.alipay.AlipayH5Model;
import com.hz.platform.master.core.model.alipay.AlipayModel;
import com.hz.platform.master.core.model.bufpay.BufpayModel;
import com.hz.platform.master.core.model.channel.ChannelModel;
import com.hz.platform.master.core.model.channelbalancededuct.ChannelBalanceDeductModel;
import com.hz.platform.master.core.model.channeldata.ChannelDataModel;
import com.hz.platform.master.core.model.channelgeway.ChannelGewayModel;
import com.hz.platform.master.core.model.channelout.ChannelOutModel;
import com.hz.platform.master.core.model.datacoreout.DataCoreOutModel;
import com.hz.platform.master.core.model.geway.GewayModel;
import com.hz.platform.master.core.model.geway.GewaytradetypeModel;
import com.hz.platform.master.core.model.receivingaccount.ReceivingAccountModel;
import com.hz.platform.master.core.model.receivingaccountdata.ReceivingAccountDataModel;
import com.hz.platform.master.core.model.strategy.StrategyModel;
import com.hz.platform.master.core.model.zfbapp.ZfbAppModel;
import com.hz.platform.master.core.protocol.request.pay.RequestPay;
import com.hz.platform.master.core.protocol.request.pay.RequestPayOut;
import com.hz.platform.master.core.protocol.response.pay.ResponseDataCore;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 代付订单的Controller层
 * @Author yoko
 * @Date 2020/10/31 15:16
 * @Version 1.0
 */
@RestController
@RequestMapping("/platform/out")
public class OrderOutController extends BaseController {

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
     * @Description: 代付订单
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8092/platform/out/order
     * 请求的属性类:RequestAppeal
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
    @RequestMapping(value = "/order", method = {RequestMethod.POST})
    public JsonResult<Object> order(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestPayOut requestData) throws Exception{
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
                if (StringUtils.isBlank(requestData.bank_card)){
                    throw new ServiceException("0010", "请填写转账的银行开户人!");
                }
                if (StringUtils.isBlank(requestData.sign)){
                    throw new ServiceException("0011", "请填写签名!");
                }
            }

            log.info("-----order-----out----all-data-json:" + JSON.toJSONString(requestData));

            // 策略数据：出码开关
            StrategyModel strategyQrCodeSwitchQuery = HodgepodgeMethod.assembleStrategyQuery(ServerConstant.StrategyEnum.OUT_QR_CODE_SWITCH.getStgType());
            StrategyModel strategyQrCodeSwitchModel = ComponentUtil.strategyService.getStrategyModel(strategyQrCodeSwitchQuery, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);
            HodgepodgeMethod.checkStrategyByQrCodeSwitch(strategyQrCodeSwitchModel);

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

            // 校验是否是白名单IP
            if (!StringUtils.isBlank(channelModel.getWhiteListIp())){
                HodgepodgeMethod.checkWhiteListIp(channelModel.getWhiteListIp(), ip);
            }

            // check 查询订单是否有重复的
            ChannelOutModel channelOutQuery = HodgepodgeMethod.assembleChannelOutQueryByOutTradeNo(channelModel.getId(), requestData.out_trade_no);
            ChannelOutModel channelOutModel_query = (ChannelOutModel) ComponentUtil.channelOutService.findByObject(channelOutQuery);
            if(channelOutModel_query != null && channelOutModel_query.getId() != null){
                throw new ServiceException("OT011", "订单号:" + requestData.out_trade_no + ",重复单号,请您换一个订单号!");
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
                log.info("");
                GewaytradetypeModel gewaytradetypeQuery = new GewaytradetypeModel();
                gewaytradetypeQuery.setMyTradeType(requestData.trade_type);
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

            // check校验请求的订单金额是否属于通道金额范围内
            HodgepodgeMethod.checkGewayMoneyRange(gewayModel.getMoneyType(), gewayModel.getMoneyRange(), requestData.total_amount);

            // 校验sign签名
            String checkSign = "";
            if (!StringUtils.isBlank(requestData.trade_type)){
                checkSign = "channel=" + requestData.channel + "&" + "trade_type=" + requestData.trade_type + "&" + "total_amount=" + requestData.total_amount
                        + "&" + "out_trade_no=" + requestData.out_trade_no + "&" + "bank_name=" + requestData.bank_name
                        + "&" + "bank_card=" + requestData.bank_card + "&" + "account_name=" + requestData.account_name
                        + "&" + "key=" + channelModel.getSecretKey();
            }else {
                checkSign = "channel=" + requestData.channel + "&" + "total_amount=" + requestData.total_amount
                        + "&" + "out_trade_no=" + requestData.out_trade_no + "&" + "bank_name=" + requestData.bank_name
                        + "&" + "bank_card=" + requestData.bank_card + "&" + "account_name=" + requestData.account_name
                        + "&" + "key=" + channelModel.getSecretKey();
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
                if (!StringUtils.isBlank(gewaytradetypeModel.getServiceCharge())){
                    serviceCharge = gewaytradetypeModel.getServiceCharge();
                }
            }


            // check 订单+手续费是否小于余额
            String totalMoney = "";
            String resMoney = StringUtil.getMultiply(requestData.total_amount, serviceCharge);
            String money = StringUtil.getBigDecimalAdd(resMoney, requestData.total_amount);
            if (!StringUtils.isBlank(extraServiceCharge)){
                totalMoney = StringUtil.getBigDecimalAdd(money, extraServiceCharge);
            }

            double d_balance = Double.parseDouble(channelModel.getBalance());
            double d_orderMoney = Double.parseDouble(totalMoney);
            if (d_balance < d_orderMoney){
                throw new ServiceException("00188", "您的余额不足小于订单金额!" + "余额：" + d_balance + " 订单加手续费需要：" + totalMoney);
            }


            String my_notify_url = gewayModel.getNotifyUrl();
            String notify_url = requestData.notify_url;
            String total_amount = "";
            String nowTime = DateUtil.getNowPlusTime();
            String resData = "";
            String qrCodeUrl = "";


            // start
            // 组装扣减渠道余额
            ChannelModel updateBalance = HodgepodgeMethod.assembleChannelBalance(channelModel.getId(), requestData.total_amount, serviceCharge, extraServiceCharge);

            // 组装添加渠道扣减余额的流水
            ChannelBalanceDeductModel channelBalanceDeductModel = HodgepodgeMethod.assembleChannelBalanceDeduct(0, channelModel.getId(), sgid, 2, updateBalance.getOrderMoney(),
                    0, null, null, 2);

            // 组装渠道请求的代付订单信息
            ChannelOutModel channelOutModel = HodgepodgeMethod.assembleChannelOutData(requestData, sgid, channelModel.getId(), gewayModel.getId(),
                    channelGewayModel.getId(), channelGewayModel.getProfitType(), nowTime, my_notify_url, serviceCharge, updateBalance.getOrderMoney(),
                    updateBalance.getServiceChargeMoney(), sendFlag);

            // 插入代付订单
            int num_add = ComponentUtil.channelOutService.add(channelOutModel);
            if (num_add > 0){
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
                    log.info("--------order--out--dg--resData:" + fineData);
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
                }else if (gewayModel.getContacts().equals("CKSD")){
                    // 蛋糕杉德
                    Map<String ,Object> sendDataMap = new HashMap<>();
                    sendDataMap.put("money", requestData.total_amount);
                    sendDataMap.put("payType", payCode);
                    sendDataMap.put("outTradeNo", sgid);
                    sendDataMap.put("secretKey", channelModel.getSecretKey());
                    sendDataMap.put("notifyUrl", my_notify_url);
                    sendDataMap.put("inBankCard", requestData.bank_card);
                    sendDataMap.put("inBankName", requestData.bank_name);
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
                    log.info("--------order--out--sd--resData:" + fineData);
                }else if (gewayModel.getContacts().equals("CKJF")){
                    // 蛋糕金服
                    Map<String ,Object> sendDataMap = new HashMap<>();
                    sendDataMap.put("money", requestData.total_amount);
                    sendDataMap.put("payType", payCode);
                    sendDataMap.put("outTradeNo", sgid);
                    sendDataMap.put("secretKey", channelModel.getSecretKey());
                    sendDataMap.put("notifyUrl", my_notify_url);
                    sendDataMap.put("inBankCard", requestData.bank_card);
                    sendDataMap.put("inBankName", requestData.bank_name);
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
                    log.info("--------order--out--jf--resData:" + fineData);
                }else if (gewayModel.getContacts().equals("CKHC")){
                    // 蛋糕汇潮
                    Map<String ,Object> sendDataMap = new HashMap<>();
                    sendDataMap.put("money", requestData.total_amount);
                    sendDataMap.put("payType", payCode);
                    sendDataMap.put("outTradeNo", sgid);
                    sendDataMap.put("secretKey", channelModel.getSecretKey());
                    sendDataMap.put("notifyUrl", my_notify_url);
                    sendDataMap.put("inBankCard", requestData.bank_card);
                    sendDataMap.put("inBankName", requestData.bank_name);
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
                    log.info("--------order--out--hc--resData:" + fineData);
                }




                boolean flag = false;// 是否执行成功
                if (sendFlag){
                    ChannelOutModel upChannelOutModel = HodgepodgeMethod.assembleUpChannelOutStatus(channelOutModel.getMyTradeNo(), 1);
                    int count = 0;
                    while (1==1){
                        // 锁住要执行的渠道ID
                        String lockKey = CachedKeyUtils.getCacheKey(CacheKey.LOCK_TASK_WORK_TYPE_CHANNEL, channelModel.getId());
                        boolean flagLock = ComponentUtil.redisIdService.lock(lockKey);
                        if (flagLock){
                            try {
                                // 正式处理逻辑
                                flag = ComponentUtil.channelOutService.handleChannelOutMoney(updateBalance, channelBalanceDeductModel, upChannelOutModel);
                                if (flag){
                                    // 解锁
                                    ComponentUtil.redisIdService.delLock(lockKey);
                                    break;
                                }
                            }catch (Exception e){
                                // 解锁
                                ComponentUtil.redisIdService.delLock(lockKey);
                                log.error(String.format("this handleChannelOutMoney is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
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
                }

                if (!flag){
                    return JsonResult.failedResult("拉单失败,请稍后再试", "5001");
                }






            }else {
                return JsonResult.failedResult("拉单失败,请稍后再试", "5001");
            }
            // end








            // 返回数据给客户端
            Map<String, String> resMap = new HashMap<>();
            resMap.put("trade_no", sgid);
            resMap.put("out_trade_no", requestData.out_trade_no);
            resMap.put("total_amount", requestData.total_amount);
            return JsonResult.successResult(resMap);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // #添加异常
            log.error(String.format("this OrderOutController.order() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"));
        }
    }


    /**
     * @Description: 查询网关-代付
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8092/platform/out/orderStatus
     * 请求的属性类:ProtocolInOrder
     * 必填字段:{"channel":"10102","out_trade_no":"202109271728320866","sign":"30ac3a8fd96640508c4ce1022d3a9089"}
     *
     * {
     *     "code": "0",
     *     "message": "success",
     *     "data": {
     *         "trade_no": "SDDF202109271728320716",
     *         "out_trade_no": "202109271728320866",
     *         "trade_status": 1,
     *         "send_status": 2,
     *         "notify_url": null,
     *         "trade_time": "2021-09-27 17:28:32"
     *     }
     * }
     *
     *
     */
    @RequestMapping(value = "/orderStatus", method = {RequestMethod.POST})
    public JsonResult<Object> orderStatus(HttpServletRequest request, HttpServletResponse response,@RequestBody RequestPayOut requestData) throws Exception{
        String ip = StringUtil.getIpAddress(request);
        String data = "";
        try{

            if (requestData == null){
                throw new ServiceException("40001", "协议数据不能为空!");
            }

            // check商铺号
            if (StringUtils.isBlank(requestData.channel)){
                throw new ServiceException("40002", "商家号不能为空!");
            }

            // check商家订单号
            if (StringUtils.isBlank(requestData.out_trade_no)){
                throw new ServiceException("40003", "订单号不能为空!");
            }

            // check签名
            if (StringUtils.isBlank(requestData.sign)){
                throw new ServiceException("40004", "秘钥不能为空!");
            }

            // check校验是否频繁查询
            HodgepodgeMethod.checkFrequentlyByQueryOutOrder(requestData.out_trade_no);


            // 获取渠道的信息
            ChannelModel channelModel = new ChannelModel();
            channelModel.setChannel(requestData.channel);
            channelModel = (ChannelModel) ComponentUtil.channelService.findByObject(channelModel);
            if (channelModel == null || channelModel.getId() <= 0){
                throw new ServiceException("0010", "请填写正确的商家号!");
            }

            // 校验是否是白名单IP
            if (!StringUtils.isBlank(channelModel.getWhiteListIp())){
                HodgepodgeMethod.checkWhiteListIp(channelModel.getWhiteListIp(), ip);
            }


            // 校验sign签名
            HodgepodgeMethod.checkSignByOutOrderStatus(requestData, channelModel.getSecretKey());

            // 查询订单状态
            ChannelOutModel channelOutQuery = HodgepodgeMethod.assembleChannelOutQuery(0,null,requestData.out_trade_no,
                    channelModel.getId());
            ChannelOutModel channelOutModel = (ChannelOutModel)ComponentUtil.channelOutService.findByObject(channelOutQuery);

            // 组装要返回渠道的数据
            ResponseDataCore responseDataCore = HodgepodgeMethod.assembleResponseChannelOutResult(channelOutModel, requestData.out_trade_no);

            // redis 保存订单号，check频繁操作查询相同订单
            HodgepodgeMethod.saveFrequentlyByQueryOutOrder(requestData.out_trade_no);

            // 返回数据给客户端
            return JsonResult.successResult(responseDataCore);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // #添加异常
            log.error(String.format("this OrderOutController.orderStatus() is error , the data=%s!", data));
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"));
        }
    }








    public static void main(String [] args){
        String url = "https://gopay.huafuwg.com/pay/Apply/new";
        String sign = "";

//        String data = "addtime=1586576409&list=[{\"out_trade_id\":\"2020041111400901\",\"amount\":\"1.00\",\"bankcode\":\"unionpay\",\"addtime\":1586576409,\"method\":\"web\",\"bankname\":\"中国银行\",\"bankuser\":\"张三\",\"bankno\":\"10000000000\",\"bankpro\":\"北京市\",\"bankcity\":\"北京市\",\"bankarea\":\"朝阳区支行\",\"mobile\":\"13800138000\",\"attach\":\"备注\"},{\"out_trade_id\":\"2020041111400902\",\"amount\":\"2.00\",\"bankcode\":\"unionpay\",\"addtime\":1586576409,\"method\":\"web\",\"bankname\":\"中国银行\",\"bankuser\":\"李四\",\"bankno\":\"20000000000\",\"bankpro\":\"北京市\",\"bankcity\":\"北京市\",\"bankarea\":\"朝阳区支行\",\"mobile\":\"13800138009\",\"attach\":\"备注\"}]¬ifyurl=http://www.baidu.com/notify&uid=10002&key=****"


        Map<String, Object> infoMap = new HashMap<>();
        infoMap.put("out_trade_id", System.currentTimeMillis());
        infoMap.put("amount", "100.00");
        infoMap.put("bankcode", "unionpay");
        infoMap.put("bankuser", "测试_收款户名");
        infoMap.put("bankname", "测试_银行名称");
        infoMap.put("bankno", "测试_收款账号");

        Map<String ,Object> sendDataMap = new HashMap<>();
        sendDataMap.put("uid", "13138");
        sendDataMap.put("addtime", System.currentTimeMillis());
        sendDataMap.put("list", infoMap);
        sendDataMap.put("notifyurl", "http://www.baidu.com/notifyurl");
        sendDataMap.put("sign", sign);

        String sendData = JSON.toJSONString(sendDataMap);
        String fineData = HttpSendUtils.sendPostAppJson(url, sendData);
    }
}
