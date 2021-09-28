package com.hz.platform.master.core.controller.pay;

import com.alibaba.fastjson.JSON;
import com.hz.platform.master.core.common.alipay.Alipay;
import com.hz.platform.master.core.common.controller.BaseController;
import com.hz.platform.master.core.common.exception.ExceptionMethod;
import com.hz.platform.master.core.common.exception.ServiceException;
import com.hz.platform.master.core.common.utils.*;
import com.hz.platform.master.core.common.utils.DateUtil;
import com.hz.platform.master.core.common.utils.HttpSendUtils;
import com.hz.platform.master.core.common.utils.MD5Util;
import com.hz.platform.master.core.common.utils.StringUtil;
import com.hz.platform.master.core.common.utils.constant.ServerConstant;
import com.hz.platform.master.core.model.alipay.AlipayH5Model;
import com.hz.platform.master.core.model.alipay.AlipayModel;
import com.hz.platform.master.core.model.bufpay.BufpayModel;
import com.hz.platform.master.core.model.channel.ChannelModel;
import com.hz.platform.master.core.model.channeldata.ChannelDataModel;
import com.hz.platform.master.core.model.channelgeway.ChannelGewayModel;
import com.hz.platform.master.core.model.datacore.DataCoreModel;
import com.hz.platform.master.core.model.geway.GewayModel;
import com.hz.platform.master.core.model.geway.GewaytradetypeModel;
import com.hz.platform.master.core.model.receivingaccount.ReceivingAccountModel;
import com.hz.platform.master.core.model.receivingaccountdata.ReceivingAccountDataModel;
import com.hz.platform.master.core.model.strategy.StrategyData;
import com.hz.platform.master.core.model.strategy.StrategyModel;
import com.hz.platform.master.core.model.zfbapp.ZfbAppModel;
import com.hz.platform.master.core.protocol.request.pay.RequestPay;
import com.hz.platform.master.core.protocol.response.pay.ResponseDataCore;
import com.hz.platform.master.util.ComponentUtil;
import com.hz.platform.master.util.HodgepodgeMethod;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.*;

/**
 * @Description TODO
 * @Author yoko
 * @Date 2020/3/23 17:22
 * @Version 1.0
 */
@RestController
@RequestMapping("/platform/action")
public class PayController extends BaseController {

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
     * @Description: 出码
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8092/platform/action/dataCore
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
    @RequestMapping(value = "/dataCore", method = {RequestMethod.POST})
    public String dataCore(HttpServletRequest request, HttpServletResponse response,@RequestBody RequestPay requestData) throws Exception{
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
            log.info("---------all-data-json:" + JSON.toJSONString(requestData));


            // 策略数据：出码开关
            StrategyModel strategyQrCodeSwitchQuery = HodgepodgeMethod.assembleStrategyQuery(ServerConstant.StrategyEnum.QR_CODE_SWITCH.getStgType());
            StrategyModel strategyQrCodeSwitchModel = ComponentUtil.strategyService.getStrategyModel(strategyQrCodeSwitchQuery, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);
            HodgepodgeMethod.checkStrategyByQrCodeSwitch(strategyQrCodeSwitchModel);



            String sign_trade_type = "";
            String trade_type = "";
            if (!StringUtils.isBlank(requestData.trade_type)){
                trade_type = requestData.trade_type;
                sign_trade_type = requestData.trade_type;
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

                // 策略数据：微派通道筛选
                StrategyModel strategyWpayQuery = HodgepodgeMethod.assembleStrategyQuery(ServerConstant.StrategyEnum.W_PAY.getStgType());
                StrategyModel strategyWpayModel = ComponentUtil.strategyService.getStrategyModel(strategyWpayQuery, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);
                if (strategyWpayModel != null){
                    if (!StringUtils.isBlank(strategyWpayModel.getStgValue())){
                        if(trade_type.equals(strategyWpayModel.getStgValue())){
                            // 说明是微派系列的，从多个通道中筛选

                            // 剔除权重值为0的，并且成功金额超出范围的
                            List<StrategyData> stgList = JSON.parseArray(strategyWpayModel.getStgBigValue(), StrategyData.class);
                            List<StrategyData> stgDataList = new ArrayList<>();// 真正可以放量的集合
                            for (StrategyData stg : stgList){
                                if (stg.getStgValueTwo() != 0){
                                    // 表示有权重
                                    // 判断通道当日是否超出当日跑量限制
                                    DataCoreModel dataCoreQuery = HodgepodgeMethod.assembleDataCoreQueryByGeway(stg.getStgKey());
                                    String gewayMoney = ComponentUtil.dataCoreService.getSumMoney(dataCoreQuery);
                                    if (gewayMoney.equals("0.00")){
                                        // 当天还没有成功的
                                        stgDataList.add(stg);
                                    }else {
                                        // 当天已有成功的：判断是否超过设定的上限
                                        String limitMoney = stg.getStgValueFour();
                                        boolean flag_gewayMoney = StringUtil.getBigDecimalSubtract(limitMoney, gewayMoney);
                                        if (flag_gewayMoney){
                                            // 没有超过限额
                                            stgDataList.add(stg);
                                        }

                                    }
                                }
                            }

                            if (stgDataList != null && stgDataList.size() > 0){
                                // 通过权重，筛选一个微派系列的通道
                                trade_type = HodgepodgeMethod.ratioGewayByWpay(stgDataList);
                                requestData.trade_type = trade_type;
                            }else {
                                throw new ServiceException("S001", "请您稍后再试!");
                            }

                        }
                    }
                }


                // 查询具体通道
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
                ChannelGewayModel channelGewayQuery = HodgepodgeMethod.assembleChannelGewayQuery(0, channelModel.getId(), 0, 2, 1);
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

            if (gewayModel.getGewayType() == 2){
                // 通道属于预付款类型， 需要对通道金额进行check
                boolean flag_geway = HodgepodgeMethod.checkGewayMoney(gewayModel, requestData.total_amount);
                if (!flag_geway){
                    throw new ServiceException("0015", "请您稍后再试,谢谢!");
                }
            }

            // check校验请求的订单金额是否属于通道金额范围内
            HodgepodgeMethod.checkGewayMoneyRange(gewayModel.getMoneyType(), gewayModel.getMoneyRange(), requestData.total_amount);




            // 校验sign签名
            String checkSign = "";
            if (!StringUtils.isBlank(sign_trade_type)){
                checkSign = "channel=" + requestData.channel + "&" + "trade_type=" + sign_trade_type + "&" + "total_amount=" + requestData.total_amount
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
            if (gewayModel.getContacts().equals("WN")){
                // 蜗牛支付
                // 组装数据
                total_amount = String.valueOf(Integer.parseInt(requestData.total_amount) * 100);
                String mySign = "app_id=" + gewayModel.getPayId() + "&" + "notify_url=" + my_notify_url + "&" + "out_trade_no=" + sgid + "&" + "total_amount=" + total_amount
                        + "&" + "trade_type=" + payCode + gewayModel.getSecretKey();
                mySign = MD5Util.encryption(mySign);
//                String sendData = HodgepodgeMethod.assembleWnData(requestData, gewayModel.getPayId(), payCode, requestData.total_amount, sgid, notify_url, mySign, gewayModel.getIdentify());
                String sendData = HodgepodgeMethod.assembleWnGetData(requestData, gewayModel.getPayId(), payCode, total_amount, sgid, my_notify_url, mySign, gewayModel.getIdentify());
//                String resData = HttpSendUtils.sendPostAppJson(gewayModel.getInterfaceAds(), sendData);
                resData = HttpSendUtils.sendGet(gewayModel.getInterfaceAds() + "?" + sendData, null, null);
                //<html><head><title>Object moved</title></head><body>
                //<h2>Object moved to <a href="https://www.chs589.com/desk/wechatpay.html?serialNumber=123298971010609152&amp;payUrlOverTime=1585048082612">here</a>.</h2>
                //</body></html>

                //error:999 调用支付渠道失败
                log.info("--------------resData:" + resData);
            }else if (gewayModel.getContacts().equals("JT")){
                // 九通
                //ay_amount=pay_amount&pay_applydate=pay_applydate&pay_bankcode=pay_bankcode&pay_callbackurl=pay_callbackurl&pay_memberid=pay_memberid&pay_notifyurl=pay_notifyurl&pay_orderid=pay_orderid&key=key
                String mySign = "pay_amount=" + requestData.total_amount + "&" + "pay_applydate=" + nowTime + "&" + "pay_bankcode=" + payCode + "&" + "pay_callbackurl=" + requestData.return_url
                        + "&" + "pay_memberid=" + gewayModel.getPayId() + "&" + "pay_notifyurl=" + my_notify_url + "&" + "pay_orderid=" + sgid + "&" + "key=" + gewayModel.getSecretKey();
                log.info("----------------mySign:" + mySign);
                mySign = MD5Util.encryption(mySign).toUpperCase();
                Map<String, String> sendData = HodgepodgeMethod.assembleApiData(requestData, gewayModel.getPayId(), payCode, total_amount, sgid, my_notify_url, mySign, gewayModel.getIdentify(), nowTime);
                resData = HttpSendUtils.doPostForm(gewayModel.getInterfaceAds(), sendData);
                //{"status":"error","msg":"签名验证失败","data":{"extra_return_param":"API","pay_amount":"100","pay_applydate":"2020-03-24 20:21:58","pay_attach":"API","pay_bankcode":"932","pay_callbackurl":"http://www.baidu.com/return_url","pay_md5sign":"D3DF2E3461D5395A505F964B4205750F","pay_memberid":"200377749","pay_notifyurl":"http://www.baidu.com","pay_orderid":"2020032420215800001","pay_productname":"礼品"}}
                log.info("--------------resData:" + resData);
            }else if(gewayModel.getContacts().equals("LSB")){
                // 绿色宝
                String mySign = "pay_amount=" + requestData.total_amount + "&" + "pay_applydate=" + nowTime + "&" + "pay_bankcode=" + payCode + "&" + "pay_callbackurl=" + requestData.return_url
                        + "&" + "pay_memberid=" + gewayModel.getPayId() + "&" + "pay_notifyurl=" + my_notify_url + "&" + "pay_orderid=" + sgid + "&" + "key=" + gewayModel.getSecretKey();
                log.info("--------lsb--------mySign:" + mySign);
                mySign = MD5Util.encryption(mySign).toUpperCase();
                Map<String, String> sendData = HodgepodgeMethod.assembleApiData(requestData, gewayModel.getPayId(), payCode, total_amount, sgid, my_notify_url, mySign, gewayModel.getIdentify(), nowTime);
                resData = HttpSendUtils.doPostForm(gewayModel.getInterfaceAds(), sendData);
                //{"status":"error","msg":"签名验证失败","data":{"extra_return_param":"API","pay_amount":"100","pay_applydate":"2020-03-24 20:21:58","pay_attach":"API","pay_bankcode":"932","pay_callbackurl":"http://www.baidu.com/return_url","pay_md5sign":"D3DF2E3461D5395A505F964B4205750F","pay_memberid":"200377749","pay_notifyurl":"http://www.baidu.com","pay_orderid":"2020032420215800001","pay_productname":"礼品"}}
                log.info("---------lsb-----resData:" + resData);
            }else if (gewayModel.getContacts().equals("MY_ZFB_H5")){
                // 我方_支付宝_H5
                List<ZfbAppModel> zfbAppList = ComponentUtil.zfbAppService.findAll();
                if (zfbAppList == null){
                    throw new ServiceException("0014", "请联系运营人员!");
                }
                ZfbAppModel zfbAppModel = HodgepodgeMethod.ratioZfbAppMethod(zfbAppList);// 随机获取一个支付宝账号
                // 调用阿里云支付宝生成订单-h5
                AlipayH5Model alipayModel = HodgepodgeMethod.assembleH5AlipayData(requestData, sgid);
                String alipayData = JSON.toJSONString(alipayModel);
                String aliOrder = Alipay.createH5AlipaySend(zfbAppModel, alipayData, requestData.return_url, my_notify_url);
                // 添加请求阿里支付的纪录-H5
                AlipayModel addAlipayModel = HodgepodgeMethod.assembleH5AlipayModel(alipayModel, channelModel.getId(), aliOrder);
                ComponentUtil.alipayService.add(addAlipayModel);

                resData = aliOrder;

            }else if(gewayModel.getContacts().equals("JH")){
                // 聚鸿支付
                String mySign = "pay_amount=" + requestData.total_amount + "&" + "pay_applydate=" + nowTime + "&" + "pay_bankcode=" + payCode + "&" + "pay_callbackurl=" + requestData.return_url
                        + "&" + "pay_memberid=" + gewayModel.getPayId() + "&" + "pay_notifyurl=" + my_notify_url + "&" + "pay_orderid=" + sgid + "&" + "key=" + gewayModel.getSecretKey();
                log.info("--------jh--------mySign:" + mySign);
                mySign = MD5Util.encryption(mySign).toUpperCase();
                Map<String, String> sendData = HodgepodgeMethod.assembleApiData(requestData, gewayModel.getPayId(), payCode, total_amount, sgid, my_notify_url, mySign, gewayModel.getIdentify(), nowTime);
                resData = HttpSendUtils.doPostForm(gewayModel.getInterfaceAds(), sendData);
                //{"status":"error","msg":"签名验证失败","data":{"extra_return_param":"API","pay_amount":"100","pay_applydate":"2020-03-24 20:21:58","pay_attach":"API","pay_bankcode":"932","pay_callbackurl":"http://www.baidu.com/return_url","pay_md5sign":"D3DF2E3461D5395A505F964B4205750F","pay_memberid":"200377749","pay_notifyurl":"http://www.baidu.com","pay_orderid":"2020032420215800001","pay_productname":"礼品"}}
                log.info("---------jh-----resData:" + resData);
            }else if(gewayModel.getContacts().equals("BUF")){
                // BufPay个人支付

                int countNum = 0;
                // 获取bufPay的账号列表
                List<BufpayModel> bufpayList = ComponentUtil.bufpayService.findAll();
                // 根据权重筛选一个bufPay账号
                BufpayModel bufpayModel = new BufpayModel();
                // bufPay下面的收款账号
                ReceivingAccountModel receivingAccountModel = new ReceivingAccountModel();
                while (1 == 1){
                    boolean flag = true;
                    bufpayModel = HodgepodgeMethod.ratioBufPayMethod(bufpayList);
                    // 获取金额日上限 && check金额日上限
                    if (flag){
                        if (!StringUtils.isBlank(bufpayModel.getLimitDayMoney()) && !bufpayModel.getLimitDayMoney().equals("-1")){
                            ReceivingAccountDataModel bufPayMoneyQuery = HodgepodgeMethod.assembleReceivingAccountDataQuery(bufpayModel.getId(), null, 1);
                            String bufPayMoney = ComponentUtil.receivingAccountDataService.getTotalMoney(bufPayMoneyQuery);
                            flag = StringUtil.getBigDecimalSubtract(bufpayModel.getLimitDayMoney(), bufPayMoney);
                        }
                    }

                    // 获取金额月上限 && check金额月上限
                    if (flag){
                        if (!StringUtils.isBlank(bufpayModel.getLimitMonthMoney()) && !bufpayModel.getLimitMonthMoney().equals("-1")){
                            ReceivingAccountDataModel bufPayMoneyQuery = HodgepodgeMethod.assembleReceivingAccountDataQuery(bufpayModel.getId(), null, 2);
                            String bufPayMoney = ComponentUtil.receivingAccountDataService.getTotalMoney(bufPayMoneyQuery);
                            flag = StringUtil.getBigDecimalSubtract(bufpayModel.getLimitMonthMoney(), bufPayMoney);
                        }
                    }

                    // 获取金额年上限 && check金额年上限
                    if (flag){
                        if (!StringUtils.isBlank(bufpayModel.getLimitYearMoney()) && !bufpayModel.getLimitYearMoney().equals("-1")){
                            ReceivingAccountDataModel bufPayMoneyQuery = HodgepodgeMethod.assembleReceivingAccountDataQuery(bufpayModel.getId(), null, 3);
                            String bufPayMoney = ComponentUtil.receivingAccountDataService.getTotalMoney(bufPayMoneyQuery);
                            flag = StringUtil.getBigDecimalSubtract(bufpayModel.getLimitYearMoney(), bufPayMoney);
                        }
                    }


                    // 获取次数日上限 && check次数日上限
                    if (flag){
                        if (bufpayModel.getLimitDayNum() > 0 ){
                            ReceivingAccountDataModel bufPayNumQuery = HodgepodgeMethod.assembleReceivingAccountDataQuery(bufpayModel.getId(), null, 1);
                            int bufPayNum = ComponentUtil.receivingAccountDataService.getTotalNum(bufPayNumQuery);
                            if (bufPayNum > bufpayModel.getLimitDayNum()){
                                flag = false;
                            }else {
                                flag = true;
                            }
                        }
                    }

                    // 获取次数月上限 && check次数月上限
                    if (flag){
                        if (bufpayModel.getLimitMonthNum() > 0 ){
                            ReceivingAccountDataModel bufPayNumQuery = HodgepodgeMethod.assembleReceivingAccountDataQuery(bufpayModel.getId(), null, 2);
                            int bufPayNum = ComponentUtil.receivingAccountDataService.getTotalNum(bufPayNumQuery);
                            if (bufPayNum > bufpayModel.getLimitMonthNum()){
                                flag = false;
                            }else {
                                flag = true;
                            }
                        }
                    }

                    // 获取次数年上限 && check次数年上限
                    if (flag){
                        if (bufpayModel.getLimitYearNum() > 0 ){
                            ReceivingAccountDataModel bufPayNumQuery = HodgepodgeMethod.assembleReceivingAccountDataQuery(bufpayModel.getId(), null, 3);
                            int bufPayNum = ComponentUtil.receivingAccountDataService.getTotalNum(bufPayNumQuery);
                            if (bufPayNum > bufpayModel.getLimitYearNum()){
                                flag = false;
                            }else {
                                flag = true;
                            }
                        }
                    }


                    // 根据bufPay的ID查询收款账号
                    if (flag){
                        ReceivingAccountModel receivingAccountQuery = HodgepodgeMethod.assembleReceivingAccountQuery(bufpayModel.getId(), payCode);
                        receivingAccountModel = (ReceivingAccountModel) ComponentUtil.receivingAccountService.findByObject(receivingAccountQuery);
                    }

                    // 获取金额日上限 && check金额日上限
                    if (flag){
                        if (!StringUtils.isBlank(receivingAccountModel.getLimitDayMoney()) && !receivingAccountModel.getLimitDayMoney().equals("-1")){
                            ReceivingAccountDataModel accountMoneyQuery = HodgepodgeMethod.assembleReceivingAccountDataQuery(null, receivingAccountModel.getId(), 1);
                            String accountMoney = ComponentUtil.receivingAccountDataService.getTotalMoney(accountMoneyQuery);
                            flag = StringUtil.getBigDecimalSubtract(receivingAccountModel.getLimitDayMoney(), accountMoney);
                        }
                    }

                    // 获取金额月上限 && check金额月上限
                    if (flag){
                        if (!StringUtils.isBlank(receivingAccountModel.getLimitMonthMoney()) && !receivingAccountModel.getLimitMonthMoney().equals("-1")){
                            ReceivingAccountDataModel accountMoneyQuery = HodgepodgeMethod.assembleReceivingAccountDataQuery(null, receivingAccountModel.getId(), 2);
                            String accountMoney = ComponentUtil.receivingAccountDataService.getTotalMoney(accountMoneyQuery);
                            flag = StringUtil.getBigDecimalSubtract(receivingAccountModel.getLimitMonthMoney(), accountMoney);
                        }
                    }

                    // 获取金额年上限 && check金额年上限
                    if (flag){
                        if (!StringUtils.isBlank(receivingAccountModel.getLimitYearMoney()) && !receivingAccountModel.getLimitYearMoney().equals("-1")){
                            ReceivingAccountDataModel accountMoneyQuery = HodgepodgeMethod.assembleReceivingAccountDataQuery(null, receivingAccountModel.getId(), 3);
                            String accountMoney = ComponentUtil.receivingAccountDataService.getTotalMoney(accountMoneyQuery);
                            flag = StringUtil.getBigDecimalSubtract(receivingAccountModel.getLimitYearMoney(), accountMoney);
                        }
                    }


                    // 获取次数日上限 && check次数日上限
                    if (flag){
                        if (receivingAccountModel.getLimitDayNum() > 0 ){
                            ReceivingAccountDataModel accountMoneyQuery = HodgepodgeMethod.assembleReceivingAccountDataQuery(null, receivingAccountModel.getId(), 1);
                            int accountNum = ComponentUtil.receivingAccountDataService.getTotalNum(accountMoneyQuery);
                            if (accountNum > receivingAccountModel.getLimitDayNum()){
                                flag = false;
                            }else {
                                flag = true;
                            }
                        }
                    }

                    // 获取次数月上限 && check次数月上限
                    if (flag){
                        if (receivingAccountModel.getLimitMonthNum() > 0 ){
                            ReceivingAccountDataModel accountMoneyQuery = HodgepodgeMethod.assembleReceivingAccountDataQuery(null, receivingAccountModel.getId(), 2);
                            int accountNum = ComponentUtil.receivingAccountDataService.getTotalNum(accountMoneyQuery);
                            if (accountNum > receivingAccountModel.getLimitMonthNum()){
                                flag = false;
                            }else {
                                flag = true;
                            }
                        }
                    }

                    // 获取次数年上限 && check次数年上限
                    if (flag){
                        if (receivingAccountModel.getLimitYearNum() > 0 ){
                            ReceivingAccountDataModel accountMoneyQuery = HodgepodgeMethod.assembleReceivingAccountDataQuery(null, receivingAccountModel.getId(), 3);
                            int accountNum = ComponentUtil.receivingAccountDataService.getTotalNum(accountMoneyQuery);
                            if (accountNum > receivingAccountModel.getLimitYearNum()){
                                flag = false;
                            }else {
                                flag = true;
                            }
                        }
                    }
                    countNum ++;
                    if (flag || countNum > 3){
                        break;
                    }
                }

                if (bufpayModel == null || bufpayModel.getId() == 0 || receivingAccountModel == null || receivingAccountModel.getId() == 0){
                    throw new ServiceException("0015", "请联系运营人员!");
                }

                String mySign = "礼品" + payCode + requestData.total_amount + sgid + channelModel.getId() + my_notify_url + requestData.return_url + "feedbackData" + bufpayModel.getSecretKey();
                log.info("--------buf--------mySign:" + mySign);
                mySign = MD5Util.encryption(mySign);
                Map<String, String> sendData = HodgepodgeMethod.assembleBufPayData(requestData, payCode, sgid, my_notify_url, mySign, channelModel.getId());
                resData = HttpSendUtils.doPostForm(gewayModel.getInterfaceAds() + bufpayModel.getPayId(), sendData);
                //{"status":"error","msg":"签名验证失败","data":{"extra_return_param":"API","pay_amount":"100","pay_applydate":"2020-03-24 20:21:58","pay_attach":"API","pay_bankcode":"932","pay_callbackurl":"http://www.baidu.com/return_url","pay_md5sign":"D3DF2E3461D5395A505F964B4205750F","pay_memberid":"200377749","pay_notifyurl":"http://www.baidu.com","pay_orderid":"2020032420215800001","pay_productname":"礼品"}}
                log.info("---------buf-----resData:" + resData);
                // 替换数据
                resData = resData.replace("bufpay.com 独立开发者个人即时到账收款接口，支持支付宝支付和微信支付，免签约即时到账，只需个人微信支付宝账号即可收款，即开即用，超稳定不漏单，支持多微信支付宝账户并行收款" , "高防支付");
                resData = resData.replace("扫码支付 - bufpay.com 独立开发者个人即时到账收款", "扫码支付 - 高防");
                // 替换金额
                Map<String, Integer> moneyMap = HodgepodgeMethod.getStrIndexofByMoney(resData);
                if (moneyMap != null){
                    StringBuilder str1 = new StringBuilder(resData);
                    str1.replace(moneyMap.get("start") + moneyMap.get("jl"), moneyMap.get("end"), requestData.total_amount);
                    Map<String, Integer> qrMap = HodgepodgeMethod.getStrIndexofByQrCode(str1.toString());
                    if (qrMap != null){
                        StringBuilder str2 = new StringBuilder(str1.toString());
                        str2.replace(qrMap.get("start") + qrMap.get("jl"), qrMap.get("end"), receivingAccountModel.getQrCode());
                        ReceivingAccountDataModel receivingAccountDataModel = HodgepodgeMethod.assembleReceivingAccountData(requestData, sgid, channelModel.getId(), gewayModel.getId(), bufpayModel.getId(), receivingAccountModel.getId(), serviceCharge);
                        ComponentUtil.receivingAccountDataService.add(receivingAccountDataModel);
                        resData = str2.toString();
                    }
                }else {
                    throw new ServiceException("0016", "请联系运营人员!");
                }
            }else if (gewayModel.getContacts().equals("FINE")){
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
                        //{"order":{"invalidTime":"2020-06-02 16:19:29","orderMoney":"1111","orderNo":"202006021606380000001","qrCode":"dd_qr_code3"},"sign":"","stime":1591085369310}
                        // {"resultCode":"0","message":"success","data":{"jsonData":"eyJvcmRlciI6eyJpbnZhbGlkVGltZSI6IjIwMjAtMDYtMDggMjE6NTA6MzIiLCJvcmRlck1vbmV5IjoiMC4wMSIsIm9yZGVyTm8iOiIyMDIwMDYwODIxMzg0NDAwMDAwMDEiLCJxckNvZGUiOiJkZF9xcl9jb2RlMyIsInFyQ29kZVVybCI6InFyQ29kZS51cmwlM0ZvcmRlck5vJTNEMjAyMDA2MDgyMTM4NDQwMDAwMDAxJTI2cmV0dXJuVXJsJTNEIn0sInNpZ24iOiIiLCJzdGltZSI6MTU5MTYyMzYzODAwM30="},"sgid":"202006082138440000001","cgid":""}
                        Map<String, Object> mapData = new HashMap<>();
//                JSONObject base64_jsonData =  JSON.parseObject(resMap.get("data").toString());
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
                        //{"order":{"invalidTime":"2020-06-02 16:19:29","orderMoney":"1111","orderNo":"202006021606380000001","qrCode":"dd_qr_code3"},"sign":"","stime":1591085369310}
                        // {"resultCode":"0","message":"success","data":{"jsonData":"eyJvcmRlciI6eyJpbnZhbGlkVGltZSI6IjIwMjAtMDYtMDggMjE6NTA6MzIiLCJvcmRlck1vbmV5IjoiMC4wMSIsIm9yZGVyTm8iOiIyMDIwMDYwODIxMzg0NDAwMDAwMDEiLCJxckNvZGUiOiJkZF9xcl9jb2RlMyIsInFyQ29kZVVybCI6InFyQ29kZS51cmwlM0ZvcmRlck5vJTNEMjAyMDA2MDgyMTM4NDQwMDAwMDAxJTI2cmV0dXJuVXJsJTNEIn0sInNpZ24iOiIiLCJzdGltZSI6MTU5MTYyMzYzODAwM30="},"sgid":"202006082138440000001","cgid":""}
                        Map<String, Object> mapData = new HashMap<>();
//                JSONObject base64_jsonData =  JSON.parseObject(resMap.get("data").toString());
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
            }else if (gewayModel.getContacts().equals("MX")){
                // 木星支付

                String [] checkMoneyArr = requestData.total_amount.split("\\.");
                if (!checkMoneyArr[1].equals("00")){
                    throw new ServiceException("A0001", "请按照规定填写整数金额!");
                }

                Map<String ,Object> sendDataMap = new HashMap<>();


                sendDataMap.put("pay_memberid", gewayModel.getPayId());
                sendDataMap.put("pay_orderid", sgid);
                String pay_amount = StringUtil.getMultiply(requestData.total_amount, "100.00");// 对方是以分为单位的
                String [] pay_amountArr = pay_amount.split("\\.");
                sendDataMap.put("pay_amount", pay_amountArr[0]);
                sendDataMap.put("pay_callbackurl", gewayModel.getNotifyUrl());
                sendDataMap.put("pay_turnyurl", requestData.return_url);
                sendDataMap.put("pay_productname", "zs");
                sendDataMap.put("pay_tradetype", payCode);



                String mySign = ASCIISort.getKeySign(sendDataMap, gewayModel.getSecretKey(), 2);
                log.info("--------xm--------mySign:" + mySign);
                sendDataMap.put("signature", mySign);
                String parameter = JSON.toJSONString(sendDataMap);

                String szData = HttpSendUtils.doPostForm(gewayModel.getInterfaceAds(), sendDataMap);
                log.info("------xm---------szData:" + szData);
                Map<String, Object> resMap = new HashMap<>();
                if (!StringUtils.isBlank(szData)) {
                    resMap = JSON.parseObject(szData, Map.class);
                    if (Integer.parseInt(resMap.get("code").toString()) == 11) {
                        qrCodeUrl = (String) resMap.get("codeUrl");
                        resData = "ok";
                    }
                }
                log.info("--------------resData:" + resData);
            }else if (gewayModel.getContacts().equals("DXY")){
                // 大信誉支付

                Map<String ,Object> sendDataMap = new HashMap<>();


                sendDataMap.put("pay_memberid", gewayModel.getPayId());
                sendDataMap.put("pay_orderid", sgid);
                sendDataMap.put("pay_applydate", DateUtil.getNowPlusTime());
                sendDataMap.put("pay_bankcode", payCode);
                sendDataMap.put("pay_notifyurl", gewayModel.getNotifyUrl());
                sendDataMap.put("pay_callbackurl", requestData.return_url);
                sendDataMap.put("pay_amount", requestData.total_amount);




                String mySign = ASCIISort.getKeySign(sendDataMap, gewayModel.getSecretKey(), 2);
                log.info("--------dxy--------mySign:" + mySign);
                sendDataMap.put("pay_type", "json");
                sendDataMap.put("pay_md5sign", mySign);
                String parameter = JSON.toJSONString(sendDataMap);

                String szData = HttpSendUtils.doPostForm(gewayModel.getInterfaceAds(), sendDataMap);
                log.info("------dxy---------szData:" + szData);
                Map<String, Object> resMap = new HashMap<>();
                if (!StringUtils.isBlank(szData)) {
                    resMap = JSON.parseObject(szData, Map.class);
                    if (resMap.get("data") != null) {
                        if (!StringUtils.isBlank(resMap.get("data").toString())){
                            qrCodeUrl = (String) resMap.get("data");
                            resData = "ok";
                        }

                    }
                }
                log.info("--------------resData:" + resData);
            }else if (gewayModel.getContacts().equals("JMX")){
                // 金木星支付
                Map<String ,Object> sendDataMap = new HashMap<>();
                sendDataMap.put("mch_id", gewayModel.getPayId());
                sendDataMap.put("pass_code", payCode);
                sendDataMap.put("subject", "钻石");
                sendDataMap.put("out_trade_no", sgid);
                sendDataMap.put("money", requestData.total_amount);
                sendDataMap.put("client_ip", "192.168.0.1");
                sendDataMap.put("notify_url", gewayModel.getNotifyUrl());
                sendDataMap.put("return_url", requestData.return_url);
                sendDataMap.put("timestamp", DateUtil.getNowPlusTime());


                String mySign = ASCIISort.getKeySign(sendDataMap, gewayModel.getSecretKey(), 2);
                log.info("--------buf--------mySign:" + mySign);
                sendDataMap.put("sign", mySign);
                String parameter = JSON.toJSONString(sendDataMap);

                String resJmxData = HttpSendUtils.sentPost(gewayModel.getInterfaceAds(), parameter);
                log.info("--------resJmxData:" + resJmxData);
                Map<String, Object> resMap = new HashMap<>();
                if (!StringUtils.isBlank(resJmxData)) {
                    resMap = JSON.parseObject(resJmxData, Map.class);
                    if (Integer.parseInt(resMap.get("code").toString()) == 0){
                        if (resMap.get("data") != null) {
                            if (!StringUtils.isBlank(resMap.get("data").toString())){
                                Map<String, Object> resDataMap = new HashMap<>();
                                resDataMap = JSON.parseObject(resMap.get("data").toString(), Map.class);
                                qrCodeUrl = (String) resDataMap.get("pay_url");
                                resData = "ok";
                            }

                        }
                    }
                }
                log.info("--------------resData:" + resData);
            }else if (gewayModel.getContacts().equals("GF")){
                // 高防支付
                Map<String ,Object> sendDataMap = new HashMap<>();
                sendDataMap.put("channel", gewayModel.getPayId());
                sendDataMap.put("trade_type", payCode);
                sendDataMap.put("total_amount", requestData.total_amount);
                sendDataMap.put("out_trade_no", sgid);
                sendDataMap.put("notify_url", gewayModel.getNotifyUrl());
                sendDataMap.put("interface_ver", "4.0");
                sendDataMap.put("return_url", requestData.return_url);
                sendDataMap.put("noredirect", "1");

                String mySign = "channel=" + sendDataMap.get("channel") + "&" + "trade_type=" + sendDataMap.get("trade_type") + "&" + "total_amount=" + sendDataMap.get("total_amount")
                        + "&" + "out_trade_no=" + sendDataMap.get("out_trade_no") + "&" + "notify_url=" + sendDataMap.get("notify_url") + "&" + "key=" + gewayModel.getSecretKey();
                mySign = MD5Util.encryption(mySign);

                sendDataMap.put("sign", mySign);
                String parameter = JSON.toJSONString(sendDataMap);

                String resGfData = HttpUtil.doPostJson(gewayModel.getInterfaceAds(), parameter);
                log.info("--------resGfData:" + resGfData);
                if (!StringUtils.isBlank(resGfData) && resGfData.indexOf("code") <= -1) {
                    byte[] decoded = Base64.getDecoder().decode(resGfData);
                    String decodeStr = new String(decoded);
                    qrCodeUrl = decodeStr;
                    resData = "ok";
                }
                log.info("--------------resData:" + resData);
            }else if (gewayModel.getContacts().equals("XFL")){
                // 小肥龙支付
                String [] checkMoneyArr = requestData.total_amount.split("\\.");
                if (!checkMoneyArr[1].equals("00")){
                    throw new ServiceException("A0001", "请按照规定填写整数金额!");
                }


                Map<String ,Object> sendDataMap = new HashMap<>();

                sendDataMap.put("mchId", gewayModel.getPayId());
                sendDataMap.put("appId", "8df9fea55dd5459babe85c97b31ff591");
                sendDataMap.put("productId", payCode);// 8029
                sendDataMap.put("mchOrderNo", sgid);
                String pay_amount = StringUtil.getMultiply(requestData.total_amount, "100.00");// 对方是以分为单位的
                String [] pay_amountArr = pay_amount.split("\\.");
                sendDataMap.put("amount", pay_amountArr[0]);

                sendDataMap.put("currency", "cny");
                sendDataMap.put("clientIp", "192.168.0.1");
                sendDataMap.put("device", "ios10.3.1");
                sendDataMap.put("notifyUrl", gewayModel.getNotifyUrl());
                sendDataMap.put("returnUrl", requestData.return_url);
                sendDataMap.put("subject", "打赏");
                sendDataMap.put("body", "钻石");
                sendDataMap.put("reqTime", DateUtil.getNowPlusTimeMill());
                sendDataMap.put("version", "1.0");



                String mySign = ASCIISort.getKeySign(sendDataMap, gewayModel.getSecretKey(), 2);
                sendDataMap.put("sign", mySign);


                String resXflData = HttpSendUtils.doPostForm(gewayModel.getInterfaceAds(), sendDataMap);
                log.info("--------resXflData:" + resXflData);

                Map<String, Object> resXflMap = new HashMap<>();
                if (!StringUtils.isBlank(resXflData)) {
                    resXflMap = JSON.parseObject(resXflData, Map.class);
                    if (resXflMap.get("retCode").equals("0")) {
                        qrCodeUrl = (String) resXflMap.get("payJumpUrl");
                        resData = "ok";
                    }
                }


                log.info("--------------resXflData:" + resXflData);
            }else if (gewayModel.getContacts().equals("SWF")){
                // 思维付支付
                String [] checkMoneyArr = requestData.total_amount.split("\\.");
                if (!checkMoneyArr[1].equals("00")){
                    throw new ServiceException("A0001", "请按照规定填写整数金额!");
                }


                Map<String ,Object> sendDataMap = new HashMap<>();

                sendDataMap.put("memberid", gewayModel.getPayId());
                sendDataMap.put("backurl", gewayModel.getNotifyUrl());
                sendDataMap.put("webbackurl", requestData.return_url);
                sendDataMap.put("clienturl", requestData.return_url);
                sendDataMap.put("type", payCode);// 303
                sendDataMap.put("body", "钻石");
                String pay_amount = StringUtil.getMultiply(requestData.total_amount, "100.00");// 对方是以分为单位的
                String [] pay_amountArr = pay_amount.split("\\.");
                sendDataMap.put("amount", pay_amountArr[0]);
                sendDataMap.put("ordernum", sgid);
                sendDataMap.put("openid", "openid");
                sendDataMap.put("appid", "appid");
                sendDataMap.put("key", gewayModel.getSecretKey());
                sendDataMap.put("code", "code");





                String mySign = ASCIISort.getSignNoKey(sendDataMap, 1);
                mySign = StringUtil.mergeCodeBase64(mySign);
                log.info("--------swf--------mySign:" + mySign);
                sendDataMap.put("sign", mySign);

                sendDataMap.remove("key");


                String resSwfData = HttpSendUtils.doPostForm(gewayModel.getInterfaceAds(), sendDataMap);
                log.info("--------resSwfData:" + resSwfData);

                Map<String, Object> resSwfMap = new HashMap<>();
                if (!StringUtils.isBlank(resSwfData)) {
                    resSwfMap = JSON.parseObject(resSwfData, Map.class);
                    boolean containsKey = resSwfMap.containsKey("state");
                    if (containsKey) {
                        if (resSwfMap.get("state").equals("1")) {
                            qrCodeUrl = (String) resSwfMap.get("url");
                            resData = "ok";
                        }
                    }
                }


                log.info("--------------resSwfData:" + resSwfData);
            }else if (gewayModel.getContacts().equals("HBT")){
                // 花呗通支付

                Map<String ,Object> sendDataMap = new HashMap<>();

                sendDataMap.put("p1_merchantno", gewayModel.getPayId());
                sendDataMap.put("p2_amount", requestData.total_amount);
                sendDataMap.put("p3_orderno", sgid);
                sendDataMap.put("p4_paytype", payCode);
                sendDataMap.put("p5_reqtime", DateUtil.getNowLongTime());
                sendDataMap.put("p6_goodsname", "钻石");
                sendDataMap.put("p8_returnurl", requestData.return_url);
                sendDataMap.put("p9_callbackurl", gewayModel.getNotifyUrl());


                String mySign = ASCIISort.getKeySign(sendDataMap, gewayModel.getSecretKey(), 2);
                log.info("--------hbt--------mySign:" + mySign);
                sendDataMap.put("sign", mySign);
                String parameter = JSON.toJSONString(sendDataMap);

                String resHbtData = HttpSendUtils.doPostForm(gewayModel.getInterfaceAds(), sendDataMap);
                log.info("--------resHbtData:" + resHbtData);

                Map<String, Object> resHbtMap = new HashMap<>();
                if (!StringUtils.isBlank(resHbtData)) {
                    resHbtMap = JSON.parseObject(resHbtData, Map.class);
                    boolean containsKey = resHbtMap.containsKey("rspcode");
                    if (containsKey) {
                        if (resHbtMap.get("rspcode").equals("A0")) {
                            qrCodeUrl = (String) resHbtMap.get("data");
                            resData = "ok";
                        }
                    }
                }

            }else if (gewayModel.getContacts().equals("YB")){
                // 易宝支付

                Map<String ,Object> sendDataMap = new HashMap<>();

                sendDataMap.put("merId", gewayModel.getPayId());
                sendDataMap.put("orderId", sgid);
                sendDataMap.put("orderAmt", requestData.total_amount);
                sendDataMap.put("channel", payCode);//通道列表请查看商户后台，或联系商务
                //商品名称，utf-8编码
                sendDataMap.put("desc","TOP-UP");
                sendDataMap.put("ip", "192.168.0.1");//支付用户IP地址,用户支付时设备的IP地址
                sendDataMap.put("notifyUrl", gewayModel.getNotifyUrl());//异步通知地址,异步接收支付结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数。
                sendDataMap.put("returnUrl", requestData.return_url);//同步通知地址,支付成功后跳转到的地址，不参与签名。
                sendDataMap.put("nonceStr", String.valueOf(System.currentTimeMillis()));//随机字符串


                String mySign = ASCIISort.getKeySign(sendDataMap, gewayModel.getSecretKey(), 2);
                log.info("--------yb--------mySign:" + mySign);
                sendDataMap.put("sign", mySign);
                String parameter = JSON.toJSONString(sendDataMap);

                String resYbData = HttpSendUtils.doPostForm(gewayModel.getInterfaceAds(), sendDataMap);
                log.info("--------resYbData:" + resYbData);

                Map<String, Object> resYbMap = new HashMap<>();
                if (!StringUtils.isBlank(resYbData)) {
                    resYbMap = JSON.parseObject(resYbData, Map.class);
                    boolean containsKey = resYbMap.containsKey("code");
                    if (containsKey) {
                        if (Integer.parseInt(resYbMap.get("code").toString()) == 1) {
                            if (resYbMap.get("data") != null) {
                                if (!StringUtils.isBlank(resYbMap.get("data").toString())){
                                    Map<String, Object> resYbDataMap = new HashMap<>();
                                    resYbDataMap = JSON.parseObject(resYbMap.get("data").toString(), Map.class);
                                    qrCodeUrl = (String) resYbDataMap.get("payurl");
                                    resData = "ok";
                                }
                            }
                        }
                    }
                }

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
            ChannelDataModel channelDataModel = HodgepodgeMethod.assembleChannelData(requestData, sgid, channelModel.getId(), gewayModel.getId(), gewayModel.getGewayName(),channelGewayModel.getId(), channelGewayModel.getProfitType(), nowTime, my_notify_url, serviceCharge, sendFlag);
            ComponentUtil.channelDataService.add(channelDataModel);
            if (!StringUtils.isBlank(qrCodeUrl)){
//                qrCodeUrl = "http://www.baidu.com";
                qrCodeUrl = URLDecoder.decode(qrCodeUrl, "UTF-8" );
                if (!StringUtils.isBlank(requestData.noredirect)){
                    if(!requestData.noredirect.equals("138")){
                        // 不等于138则直接返回支付地址
                        resData = StringUtil.mergeCodeBase64(qrCodeUrl);
                    }else{
                        // 等于138，返回json数据
                        Map<String, String> payMap = new HashMap<>();
                        payMap.put("code", "0");
                        payMap.put("trade_no", sgid);
                        payMap.put("pay_url", StringUtil.mergeCodeBase64(qrCodeUrl));
                        payMap.put("timestamp", String.valueOf(System.currentTimeMillis()));
                        return JSON.toJSONString(payMap);
                    }
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

//
//    /**
//     * @Description: 首页-开始浏览广告
//     * @param response
//     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
//     * @author yoko
//     * @date 2019/11/25 22:58
//     * local:http://localhost:8082/pay/action/dataCore
//     * 请求的属性类:RequestAppeal
//     * http://localhost:8082/pay/action/dataCore?channel=channel_1&trade_type=trade_type_1&total_amount=total_amount_1&out_trade_no=out_trade_no_1&notify_url=notify_url_1&interface_ver=interface_ver_1&extra_return_param=extra_return_param_1&client_ip=client_ip_1&sign=sign_1&sub_time=sub_time_1&product_name=product_name_1&product_code=product_code_1
//     * 必填字段:{"channel":"channel_1","trade_type":"trade_type_1","total_amount":"total_amount_1","out_trade_no":"out_trade_no_1","notify_url":"notify_url_1","interface_ver":"interface_ver_1","extra_return_param":"extra_return_param_1","client_ip":"client_ip_1","sign":"sign_1","sub_time":"sub_time_1","product_name":"product_name_1","product_code":"product_code_1"}
//     * 客户端加密字段:token+ctime+秘钥=sign
//     * 返回加密字段:stime+秘钥=sign
//     *
//     * {
//     *     "resultCode": "0",
//     *     "message": "success",
//     *     "data": {
//     *         "jsonData": "eyJzaWduIjoiN2UyZDhlODA4NWRhNzE5YzAzNWU2NTNiNGZmZmUzYjYiLCJzdGltZSI6MTU4MzMwNTc0NTQzOSwid2giOnsiYWRBZHMiOiJodHRwOi8vd3d3LnFpZGlhbi5jb20iLCJwcm9maXQiOiIwLjMyIn19"
//     *     },
//     *     "sgid": "202003041509030000001",
//     *     "cgid": ""
//     * }
//     */
//    @RequestMapping(value = "/dataCore", method = {RequestMethod.POST, RequestMethod.GET})
//    public JsonResult<Object> dataCore(HttpServletRequest request, HttpServletResponse response, RequestPay requestData, @RequestParam String channel) throws Exception{
//        String sgid = ComponentUtil.redisIdService.getNewId();
//        String cgid = "";
//        String ip = StringUtil.getIpAddress(request);
//        String data = "";
//        RequestPay requestModel = new RequestPay();
//        try{
//            if (request.getMethod().equals("POST")){
//                requestModel = requestData;
//            }else if (request.getMethod().equals("GET")){
//                requestModel = (RequestPay) getRootBean(RequestPay.class, request);
//            }else {
//                throw new Exception();
//            }
//
////            // 解密
////            data = StringUtil.decoderBase64(requestData.jsonData);
////            requestModel  = JSON.parseObject(data, RequestAdvert.class);
//            // #零时数据
////            ComponentUtil.redisService.set(requestModel.token, "3");
//            // check校验数据
//            long stime = System.currentTimeMillis();
//            String sign = SignUtil.getSgin(stime, secretKeySign); // stime+秘钥=sign
//            String strData = "";
//            // 数据加密
//            String encryptionData = StringUtil.mergeCodeBase64(strData);
//            ResponseEncryptionJson resultDataModel = new ResponseEncryptionJson();
//            resultDataModel.jsonData = encryptionData;
//
//            // #添加流水
//            // 返回数据给客户端
//            return JsonResult.successResult(resultDataModel, cgid, sgid);
//        }catch (Exception e){
//            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
//            // #添加异常
//            log.error(String.format("this AdvertController.watch() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
//            e.printStackTrace();
//            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
//        }
//    }

    public static void main(String [] args) throws Exception{
        String fineData = "{\"resultCode\":\"0\",\"message\":\"success\",\"data\":{\"jsonData\":\"eyJvcmRlciI6eyJpbnZhbGlkVGltZSI6IjIwMjAtMDYtMDggMjE6NTA6MzIiLCJvcmRlck1vbmV5IjoiMC4wMSIsIm9yZGVyTm8iOiIyMDIwMDYwODIxMzg0NDAwMDAwMDEiLCJxckNvZGUiOiJkZF9xcl9jb2RlMyIsInFyQ29kZVVybCI6InFyQ29kZS51cmwlM0ZvcmRlck5vJTNEMjAyMDA2MDgyMTM4NDQwMDAwMDAxJTI2cmV0dXJuVXJsJTNEIn0sInNpZ24iOiIiLCJzdGltZSI6MTU5MTYyMzYzODAwM30=\"},\"sgid\":\"202006082138440000001\",\"cgid\":\"\"}";
        Map<String, Object> resMap = new HashMap<>();
        Map<String, Object> dataMap = new HashMap<>();
        String qrCodeUrl = "";
        if (!StringUtils.isBlank(fineData)) {
            resMap = JSON.parseObject(fineData, Map.class);
            if (resMap.get("resultCode").equals("0")) {
                //{"order":{"invalidTime":"2020-06-02 16:19:29","orderMoney":"1111","orderNo":"202006021606380000001","qrCode":"dd_qr_code3"},"sign":"","stime":1591085369310}
                // {"resultCode":"0","message":"success","data":{"jsonData":"eyJvcmRlciI6eyJpbnZhbGlkVGltZSI6IjIwMjAtMDYtMDggMjE6NTA6MzIiLCJvcmRlck1vbmV5IjoiMC4wMSIsIm9yZGVyTm8iOiIyMDIwMDYwODIxMzg0NDAwMDAwMDEiLCJxckNvZGUiOiJkZF9xcl9jb2RlMyIsInFyQ29kZVVybCI6InFyQ29kZS51cmwlM0ZvcmRlck5vJTNEMjAyMDA2MDgyMTM4NDQwMDAwMDAxJTI2cmV0dXJuVXJsJTNEIn0sInNpZ24iOiIiLCJzdGltZSI6MTU5MTYyMzYzODAwM30="},"sgid":"202006082138440000001","cgid":""}
                Map<String, Object> mapData = new HashMap<>();
//                JSONObject base64_jsonData =  JSON.parseObject(resMap.get("data").toString());
                mapData =  JSON.parseObject(resMap.get("data").toString(), Map.class);
                String jsonData = StringUtil.decoderBase64(mapData.get("jsonData").toString());
                Map<String, Object> orderMap = new HashMap<>();
                dataMap = JSON.parseObject(jsonData, Map.class);
                orderMap = (Map<String, Object>) dataMap.get("order");
                qrCodeUrl = (String) orderMap.get("qrCodeUrl");
                System.out.println(qrCodeUrl);
            }
        }
    }




    /**
     * @Description: 查询网关
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8092/platform/action/queryDataCore
     * 请求的属性类:RequestPay
     * 必填字段:{"channel":"channel_2","out_trade_no":"out_trade_no_1","sign":"c414e730be7d93bec15408b83dd69281"}
     * 客户端加密字段:token+ctime+秘钥=sign
     * 返回加密字段:stime+秘钥=sign
     *
     * {
     *     "resultCode": "0",
     *     "message": "success",
     *     "data": {
     *         "trade_no": "trade2021012003",
     *         "out_trade_no": "out2021012003",
     *         "trade_status": 1,
     *         "send_status": 1,
     *         "notify_url": "http://www.baidu.com",
     *         "trade_time": "2021-01-20 19:23:40"
     *     }
     * }
     *
     */
    @RequestMapping(value = "/queryDataCore", method = {RequestMethod.POST})
    public JsonResult<Object> queryDataCore(HttpServletRequest request, HttpServletResponse response,@RequestBody RequestPay requestData) throws Exception{
        String ip = StringUtil.getIpAddress(request);
        String data = "";
        RequestPay requestModel = new RequestPay();
        try{
            if (StringUtils.isBlank(requestData.channel)){
                throw new ServiceException("0001", "请填写商铺号!");
            }
            if (StringUtils.isBlank(requestData.out_trade_no)){
                throw new ServiceException("0002", "请填写商家订单号!");
            }
            if (StringUtils.isBlank(requestData.sign)){
                throw new ServiceException("0003", "请填写签名!");
            }


            // 获取渠道的信息
            ChannelModel channelModel = new ChannelModel();
            channelModel.setChannel(requestData.channel);
            channelModel = (ChannelModel) ComponentUtil.channelService.findByObject(channelModel);
            if (channelModel == null || channelModel.getId() <= 0){
                throw new ServiceException("0004", "请填写正确的商铺号!");
            }

            // 校验sign签名
            String checkSign = "channel=" + requestData.channel + "&" + "out_trade_no=" + requestData.out_trade_no + "&" + "key=" + channelModel.getSecretKey();

            checkSign = MD5Util.encryption(checkSign);
            if (!requestData.sign.equals(checkSign)){
                throw new ServiceException("0005", "签名错误!");
            }

            DataCoreModel dataCoreQuery = new DataCoreModel();
            dataCoreQuery.setChannelId(channelModel.getId());
            dataCoreQuery.setOutTradeNo(requestData.out_trade_no);
            DataCoreModel dataCoreModel = (DataCoreModel)ComponentUtil.dataCoreService.findByObject(dataCoreQuery);

            ResponseDataCore responseDataCore = HodgepodgeMethod.assembleResponseDataCoreResult(dataCoreModel, requestData.out_trade_no);

            // 返回数据给客户端
            return JsonResult.successResult(responseDataCore);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // #添加异常
            log.error(String.format("this PayController.queryDataCore() is error , the data=%s!", data));
            e.printStackTrace();
            return JsonResult.failedResult(map.get("message"), map.get("code"));
        }
    }



}
