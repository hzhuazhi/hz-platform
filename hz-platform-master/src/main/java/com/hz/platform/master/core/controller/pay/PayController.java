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
import com.hz.platform.master.core.model.geway.GewayModel;
import com.hz.platform.master.core.model.geway.GewaytradetypeModel;
import com.hz.platform.master.core.model.receivingaccount.ReceivingAccountModel;
import com.hz.platform.master.core.model.receivingaccountdata.ReceivingAccountDataModel;
import com.hz.platform.master.core.model.zfbapp.ZfbAppModel;
import com.hz.platform.master.core.protocol.request.pay.RequestPay;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * @Description: 首页-开始浏览广告
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



}
