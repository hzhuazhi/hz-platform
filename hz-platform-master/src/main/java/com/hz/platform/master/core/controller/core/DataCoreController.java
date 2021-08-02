package com.hz.platform.master.core.controller.core;

import com.alibaba.fastjson.JSON;
import com.alipay.api.internal.util.AlipaySignature;
import com.hz.platform.master.core.common.controller.BaseController;
import com.hz.platform.master.core.common.exception.ExceptionMethod;
import com.hz.platform.master.core.common.utils.MD5Util;
import com.hz.platform.master.core.common.utils.StringUtil;
import com.hz.platform.master.core.common.utils.constant.ServerConstant;
import com.hz.platform.master.core.model.alipay.AlipayNotifyModel;
import com.hz.platform.master.core.model.channel.ChannelModel;
import com.hz.platform.master.core.model.channeldata.ChannelDataModel;
import com.hz.platform.master.core.model.channelgeway.ChannelGewayModel;
import com.hz.platform.master.core.model.channelout.ChannelOutModel;
import com.hz.platform.master.core.model.datacore.DataCoreModel;
import com.hz.platform.master.core.model.datacoreout.DataCoreOutModel;
import com.hz.platform.master.core.model.geway.GewayModel;
import com.hz.platform.master.core.model.receivingaccountdata.ReceivingAccountDataModel;
import com.hz.platform.master.core.model.zfbapp.ZfbAppModel;
import com.hz.platform.master.core.protocol.request.RequestAlipay;
import com.hz.platform.master.core.protocol.request.notify.*;
import com.hz.platform.master.util.ComponentUtil;
import com.hz.platform.master.util.HodgepodgeMethod;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @Description 数据同步接收接口
 * @Author yoko
 * @Date 2020/3/24 20:54
 * @Version 1.0
 */
@RestController
@RequestMapping("/platform/data")
public class DataCoreController extends BaseController {

    private static Logger log = LoggerFactory.getLogger(DataCoreController.class);

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
     * @Description: 蜗牛
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8082/pay/data/wn
     * http://localhost:8082/pay/data/wn?total_amount=50000&out_trade_no=2020032421550000001&trade_status=SUCCESS&trade_no=trade_no_WN_1&extra_return_param=WN&trade_time=2020-03-25%2014:59:55&sign=f3895913683160a593342eb81fdca636
     */
    @RequestMapping(value = "/wn", method = {RequestMethod.GET})
    public String wn(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String sgid = ComponentUtil.redisIdService.getNewId();
        String cgid = "";
        String ip = StringUtil.getIpAddress(request);
        String data = "";
        RequestWn requestModel = new RequestWn();
        try{

            requestModel = (RequestWn) getRootBean(RequestWn.class, request);
            if (requestModel == null){
                return "no";
            }
            if (requestModel.out_trade_no == null){
                return "no";
            }
            if (requestModel.trade_no == null){
                return "no";
            }
            log.info("---------------蜗牛数据:out_trade_no:" + requestModel.out_trade_no + "trade_no:" + requestModel.trade_no);
            String total_amount = "";
            total_amount = StringUtil.getBigDecimalDivide(requestModel.total_amount, String.valueOf(100));

            // 查询此数据属于哪个订单
            ChannelDataModel channelDataModel = new ChannelDataModel();
            channelDataModel.setMyTradeNo(requestModel.out_trade_no);
            channelDataModel = (ChannelDataModel) ComponentUtil.channelDataService.findByObject(channelDataModel);
            if (channelDataModel == null){
                return "no";
            }

            // 查询渠道信息
            ChannelModel channelModel = (ChannelModel) ComponentUtil.channelService.findById(channelDataModel.getChannelId());
            if (channelModel == null){
                return "no";
            }

            // 查询通道信息
            GewayModel gewayModel = (GewayModel) ComponentUtil.gewayService.findById(channelDataModel.getGewayId());
            if (gewayModel == null){
                return "no";
            }
            String secretKey = gewayModel.getSecretKey();

            // 校验上游下发的数据
            String mySign = "out_trade_no=" + requestModel.out_trade_no + "&" + "total_amount=" + requestModel.total_amount + "&" + "trade_status=" + requestModel.trade_status + secretKey;
            mySign = MD5Util.encryption(mySign);
            if (!mySign.equals(requestModel.sign)){
                return "no";
            }

            // 根据渠道号主键ID以及通道ID查询关联关系
            ChannelGewayModel channelGewayModel = new ChannelGewayModel();
            channelGewayModel.setChannelId(channelDataModel.getChannelId());
            channelGewayModel.setGewayId(channelDataModel.getGewayId());
            channelGewayModel = (ChannelGewayModel) ComponentUtil.channelGewayService.findByObject(channelGewayModel);

            String serviceCharge = "";
            if (!StringUtils.isBlank(channelDataModel.getServiceCharge())){
                serviceCharge = channelDataModel.getServiceCharge();
            }else {
                serviceCharge = channelGewayModel.getServiceCharge();
            }
            serviceCharge = StringUtil.getMultiply(total_amount, serviceCharge);
            String actualMoney = StringUtil.getBigDecimalSubtractStr(total_amount, serviceCharge);
//            int deductRatio = channelGewayModel.getDeductRatio();
            int tradeStatus = 0;
            if (requestModel.trade_status.equals("SUCCESS")){
                tradeStatus = 1;
            }else {
                tradeStatus = 2;
            }
            // 组装上游数据
            DataCoreModel dataCoreModel = HodgepodgeMethod.assembleDataCore(requestModel, channelDataModel, channelGewayModel, total_amount, serviceCharge, actualMoney, tradeStatus);
            int num = ComponentUtil.dataCoreService.add(dataCoreModel);
            if (num > 0){
                return "SUCCESS";
            }else {
                return "no";
            }
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // #添加异常
            log.error(String.format("this DataCoreController.wn() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            e.printStackTrace();
            return JSON.toJSONString(map);
        }
    }





    /**
     * @Description: 接收阿里支付宝的数据
     * @param request
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8082/pay/data/notify
     */
    @RequestMapping(value = "/notify", method = {RequestMethod.POST})
//    public JsonResult<Object> notify(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestEncryptionJson requestData) throws Exception{
    public void notify(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String sgid = ComponentUtil.redisIdService.getNewId();
        String cgid = "";
        String ip = StringUtil.getIpAddress(request);
        RequestAlipay requestAlipay = new RequestAlipay();
        try{
            //获取支付宝POST过来反馈信息
            Map<String,String> params = new HashMap<String,String>();
            Map requestParams = request.getParameterMap();
            for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
                String name = (String) iter.next();
                String[] values = (String[]) requestParams.get(name);
                String valueStr = "";
                for (int i = 0; i < values.length; i++) {
                    valueStr = (i == values.length - 1) ? valueStr + values[i]
                            : valueStr + values[i] + ",";
                }
                //乱码解决，这段代码在出现乱码时使用。
                //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
                params.put(name, valueStr);
            }
            //切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
            //boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String publicKey, String charset, String sign_type)
            String resultData = JSON.toJSONString(params);// 阿里返回的数据
            log.info(String.format("the DataCoreController.notify() , the resultData=%s ", resultData));
            String ALIPAY_PUBLIC_KEY = "";
            ZfbAppModel zfbAppQuery = new ZfbAppModel();
            zfbAppQuery.setAppId(params.get("app_id"));
            ZfbAppModel zfbAppModel = (ZfbAppModel) ComponentUtil.zfbAppService.findByObject(zfbAppQuery);
            ALIPAY_PUBLIC_KEY = zfbAppModel.getAppPublicKey();
            boolean flag = AlipaySignature.rsaCheckV1(params, ALIPAY_PUBLIC_KEY, "UTF-8","RSA2");
            if (flag){
                AlipayNotifyModel alipayNotifyModel = HodgepodgeMethod.assembleAlipayNotify(params);
                if (alipayNotifyModel != null){
                    ComponentUtil.alipayService.addAlipayNotify(alipayNotifyModel);
                }
            }
            log.info(String.format("the DataCoreController.notify() , the flag=%s ", flag));
            // 返回数据给客户端
            response.getWriter().write("success");//直接将完整的表单html输出到页面
//            return JsonResult.successResult(null, cgid, sgid);
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            log.error(String.format("this DataCoreController.notify() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, request.getQueryString()));
            e.printStackTrace();
//            return JsonResult.failedResult(map.get("message"), map.get("code"), cgid, sgid);
        }
    }









    /**
     * @Description: 蛋糕-代收
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8092/platform/data/cakeIn
     * http://localhost:8092/platform/data/cakeIn
     *
     * {"total_amount":"100.00","pay_amount":"80.00","out_trade_no":"2020080317100600001","trade_status":1,"trade_no":"test_order_no_sl_1","trade_time":"2020-08-05 20:10:35"}
     * {"total_amount":"100.00","pay_amount":"200.00","out_trade_no":"2020080317080600001","trade_status":1,"trade_no":"test_order_no_dl_1","trade_time":"2020-08-05 20:10:35"}
     * {"total_amount":"200.00","pay_amount":"200.00","out_trade_no":"2020080317040000001","trade_status":1,"trade_no":"test_order_no_yz_1","trade_time":"2020-08-05 20:10:35"}
     */
//    @RequestMapping(value = "/cakeIn", method = {RequestMethod.GET})
//    @GetMapping(value = "cakeIn")
    @RequestMapping(value = "/cakeIn", method = {RequestMethod.POST})
    public String cakeIn(HttpServletRequest request, HttpServletResponse response,@RequestBody RequestFine requestModel) throws Exception{
        String ip = StringUtil.getIpAddress(request);
        String data = "";
//        RequestFine requestModel = new RequestFine();
        try{
//            request.getParameter("total_amount");
//            requestModel = (RequestFine) getRootBean(RequestFine.class, request);
            if (requestModel == null){
                return "no";
            }
            if (requestModel.trade_no == null){
                return "no";
            }
            if (requestModel.out_trade_no == null){
                return "no";
            }
            if (requestModel.total_amount == null){
                return "no";
            }


            log.info("---------------Fine:trade_no:" + requestModel.trade_no + "out_trade_no:" + requestModel.out_trade_no);
            String total_amount = requestModel.total_amount;
            String pay_amount = requestModel.pay_amount;
            // 查询此数据属于哪个订单
            ChannelDataModel channelDataModel = new ChannelDataModel();
            channelDataModel.setMyTradeNo(requestModel.out_trade_no);
            channelDataModel = (ChannelDataModel) ComponentUtil.channelDataService.findByObject(channelDataModel);
            if (channelDataModel == null){
                return "no";
            }

            // 查询渠道信息
            ChannelModel channelModel = (ChannelModel) ComponentUtil.channelService.findById(channelDataModel.getChannelId());
            if (channelModel == null){
                return "no";
            }

            // 查询通道信息
            GewayModel gewayModel = (GewayModel) ComponentUtil.gewayService.findById(channelDataModel.getGewayId());
            if (gewayModel == null){
                return "no";
            }
            String secretKey = gewayModel.getSecretKey();

//            // 校验上游下发的数据
//            String mySign = "memberid=" + requestModel.memberid + "&" + "orderid=" + requestModel.orderid + "&" + "amount=" + requestModel.amount
//                    + "&" + "transaction_id=" + requestModel.transaction_id + "&" + "datetime=" + requestModel.datetime + "&" + "returncode=" + requestModel.returncode;
//            mySign = MD5Util.encryption(mySign);
//            if (!mySign.equals(requestModel.sign)){
//                return "no";
//            }

            // 根据渠道号主键ID以及通道ID查询关联关系
            ChannelGewayModel channelGewayModel = new ChannelGewayModel();
            channelGewayModel.setChannelId(channelDataModel.getChannelId());
            channelGewayModel.setGewayId(channelDataModel.getGewayId());
            channelGewayModel = (ChannelGewayModel) ComponentUtil.channelGewayService.findByObject(channelGewayModel);

            String serviceCharge = "";
            String pay_serviceCharge = "";
            if (!StringUtils.isBlank(channelDataModel.getServiceCharge())){
                serviceCharge = channelDataModel.getServiceCharge();
            }else {
                serviceCharge = channelGewayModel.getServiceCharge();
            }
            pay_serviceCharge = serviceCharge;

            // 判断订单金额是否与实际支付金额一致：1初始化，2少了，3多了，4一致
            int moneyFitType = 0;// 金额是否与上报金额一致：1初始化，2少了，3多了，4一致
            // 金额相减
            String result = StringUtil.getBigDecimalSubtractByStr(pay_amount, total_amount);
            if (result.equals("0")){
                moneyFitType = 4;
            }else{
                boolean flag_money = StringUtil.getBigDecimalSubtract(total_amount, pay_amount);
                if (flag_money){
                    // 少了
                    moneyFitType = 2;
                }else {
                    // 多了
                    moneyFitType = 3;
                }
            }

            serviceCharge = StringUtil.getMultiply(total_amount, serviceCharge);
            String actualMoney = StringUtil.getBigDecimalSubtractStr(total_amount, serviceCharge);
            pay_serviceCharge = StringUtil.getMultiply(pay_amount, pay_serviceCharge);
            String payActualMoney = StringUtil.getBigDecimalSubtractStr(pay_amount, pay_serviceCharge);
//            int deductRatio = channelGewayModel.getDeductRatio();
            int tradeStatus = 1;
//            String resPrice = StringUtil.getBigDecimalSubtractStr(channelDataModel.getTotalAmount(), total_amount);
//            if (resPrice.equals("0")){
//                tradeStatus = 1;
//            }else {
//                tradeStatus = 2;
//            }
            // 更新状态
            ReceivingAccountDataModel receivingAccountDataModel = new ReceivingAccountDataModel();
            receivingAccountDataModel.setMyTradeNo(requestModel.out_trade_no);
            receivingAccountDataModel.setIsOk(2);
            ComponentUtil.receivingAccountDataService.update(receivingAccountDataModel);
            //组装上游数据
            DataCoreModel dataCoreModel = HodgepodgeMethod.assembleDataCoreFine(requestModel, channelDataModel, channelGewayModel, total_amount, serviceCharge, actualMoney, tradeStatus,
                    pay_amount, payActualMoney, moneyFitType, channelDataModel.getChannelGewayId(), channelDataModel.getProfitType());
            int num = ComponentUtil.dataCoreService.add(dataCoreModel);
            if (num > 0){
                return "ok";
            }else {
                return "no";
            }
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // #添加异常
            log.error(String.format("this DataCoreController.cakeIn() is error , the all data=%s!", data));
            e.printStackTrace();
            return JSON.toJSONString(map);
        }
    }



    /**
     * @Description: 蛋糕-代付
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8092/platform/data/cakeOut
     * http://localhost:8092/platform/data/cakeOut
     *
     * {"out_trade_no":"DF202010312034491","trade_status":1,"trade_no":"test_order_no_sl_1","fail_info":"","picture_ads":"https://fruit-file.oss-cn-hangzhou.aliyuncs.com/img/758a84a9f1d3418ba06ff3502f40fe1c.png","trade_time":"2020-10-31 20:10:35"}
     * {"out_trade_no":"DF202010312034491","trade_status":2,"trade_no":"test_order_no_sl_1","fail_info":"银行卡错误","picture_ads":"","trade_time":"2020-10-31 20:10:35"}
     *
     * {"out_trade_no":"DF202011011331131","trade_status":1,"trade_no":"test_order_no_sl_1","fail_info":"","picture_ads":"https://fruit-file.oss-cn-hangzhou.aliyuncs.com/img/758a84a9f1d3418ba06ff3502f40fe1c.png","trade_time":"2020-10-31 20:10:35"}
     * {"out_trade_no":"DF202011011331131","trade_status":2,"trade_no":"test_order_no_sl_1","fail_info":"银行卡错误","picture_ads":"","trade_time":"2020-10-31 20:10:35"}
     */
//    @RequestMapping(value = "/fine", method = {RequestMethod.GET})
//    @GetMapping(value = "fine")
    @RequestMapping(value = "/cakeOut", method = {RequestMethod.POST})
    public String cakeOut(HttpServletRequest request, HttpServletResponse response,@RequestBody RequestCakeOut requestModel) throws Exception{
        String ip = StringUtil.getIpAddress(request);
        String data = "";
//        RequestFine requestModel = new RequestFine();
        try{
//            request.getParameter("total_amount");
//            requestModel = (RequestFine) getRootBean(RequestFine.class, request);
            if (requestModel == null){
                return "no";
            }
            if (requestModel.trade_no == null){
                return "no";
            }
            if (requestModel.out_trade_no == null){
                return "no";
            }
            if (requestModel.trade_status == null || requestModel.trade_status == 0){
                return "no";
            }


            log.info("---------------cakeOut:trade_no:" + requestModel.trade_no + "out_trade_no:" + requestModel.out_trade_no);

            String total_amount = null;
            String pay_amount = null;
            // 查询此数据属于哪个订单
            ChannelOutModel channelOutModel = new ChannelOutModel();
            channelOutModel.setMyTradeNo(requestModel.out_trade_no);
            channelOutModel = (ChannelOutModel) ComponentUtil.channelOutService.findByObject(channelOutModel);
            if (channelOutModel == null){
                return "no";
            }else{
                total_amount = channelOutModel.getTotalAmount();
                pay_amount = channelOutModel.getTotalAmount();
            }

            // 查询渠道信息
            ChannelModel channelModel = (ChannelModel) ComponentUtil.channelService.findById(channelOutModel.getChannelId());
            if (channelModel == null){
                return "no";
            }

            // 查询通道信息
            GewayModel gewayModel = (GewayModel) ComponentUtil.gewayService.findById(channelOutModel.getGewayId());
            if (gewayModel == null){
                return "no";
            }
            String secretKey = gewayModel.getSecretKey();

//            // 校验上游下发的数据
//            String mySign = "memberid=" + requestModel.memberid + "&" + "orderid=" + requestModel.orderid + "&" + "amount=" + requestModel.amount
//                    + "&" + "transaction_id=" + requestModel.transaction_id + "&" + "datetime=" + requestModel.datetime + "&" + "returncode=" + requestModel.returncode;
//            mySign = MD5Util.encryption(mySign);
//            if (!mySign.equals(requestModel.sign)){
//                return "no";
//            }

            // 根据渠道号主键ID以及通道ID查询关联关系
            ChannelGewayModel channelGewayModel = new ChannelGewayModel();
            channelGewayModel.setChannelId(channelOutModel.getChannelId());
            channelGewayModel.setGewayId(channelOutModel.getGewayId());
            channelGewayModel = (ChannelGewayModel) ComponentUtil.channelGewayService.findByObject(channelGewayModel);


            String serviceCharge = "";
            String pay_serviceCharge = "";
            if (!StringUtils.isBlank(channelOutModel.getServiceCharge())){
                serviceCharge = channelOutModel.getServiceCharge();
            }else {
                serviceCharge = channelGewayModel.getServiceCharge();
            }
            pay_serviceCharge = serviceCharge;


            serviceCharge = StringUtil.getMultiply(total_amount, serviceCharge);
            if (channelGewayModel.getServiceChargeType() == 2){
                serviceCharge = StringUtil.getBigDecimalAdd(serviceCharge, channelGewayModel.getExtraServiceCharge());
            }
            String actualMoney = StringUtil.getBigDecimalAdd(total_amount, serviceCharge);


            pay_serviceCharge = StringUtil.getMultiply(pay_amount, pay_serviceCharge);
            if (channelGewayModel.getServiceChargeType() == 2){
                pay_serviceCharge = StringUtil.getBigDecimalAdd(pay_serviceCharge, channelGewayModel.getExtraServiceCharge());
            }
            String payActualMoney = StringUtil.getBigDecimalAdd(pay_amount, pay_serviceCharge);



            //组装上游数据
            DataCoreOutModel dataCoreOutModel = HodgepodgeMethod.assembleDataCoreOutByCake(requestModel, channelOutModel, channelGewayModel, requestModel.trade_status,
                    channelOutModel.getChannelGewayId(), channelOutModel.getProfitType(), total_amount, serviceCharge, actualMoney,
                    pay_amount, payActualMoney);
            int num = ComponentUtil.dataCoreOutService.add(dataCoreOutModel);
            if (num > 0){
                return "ok";
            }else {
                return "no";
            }
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // #添加异常
            log.error(String.format("this DataCoreController.cakeOut() is error , the all data=%s!", data));
            e.printStackTrace();
            return JSON.toJSONString(map);
        }
    }



    /**
     * @Description: 华富-代付
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8092/platform/data/outHf
     * http://localhost:8092/platform/data/outHf
     *
     * {"out_trade_no":"DF202010312034491","trade_status":1,"trade_no":"test_order_no_sl_1","fail_info":"","picture_ads":"https://fruit-file.oss-cn-hangzhou.aliyuncs.com/img/758a84a9f1d3418ba06ff3502f40fe1c.png","trade_time":"2020-10-31 20:10:35"}
     * {"out_trade_no":"DF202010312034491","trade_status":2,"trade_no":"test_order_no_sl_1","fail_info":"银行卡错误","picture_ads":"","trade_time":"2020-10-31 20:10:35"}
     *
     * {"out_trade_no":"DF202011011331131","trade_status":1,"trade_no":"test_order_no_sl_1","fail_info":"","picture_ads":"https://fruit-file.oss-cn-hangzhou.aliyuncs.com/img/758a84a9f1d3418ba06ff3502f40fe1c.png","trade_time":"2020-10-31 20:10:35"}
     * {"out_trade_no":"DF202011011331131","trade_status":2,"trade_no":"test_order_no_sl_1","fail_info":"银行卡错误","picture_ads":"","trade_time":"2020-10-31 20:10:35"}
     */
//    @RequestMapping(value = "/outHf", method = {RequestMethod.GET})
//    @GetMapping(value = "outHf")
    @RequestMapping(value = "/outHf", method = {RequestMethod.POST})
    public String outHf(HttpServletRequest request, HttpServletResponse response, RequestOutHf requestModel) throws Exception{
        String ip = StringUtil.getIpAddress(request);
        String data = "";
        log.info("outHf-allData:" + JSON.toJSONString(requestModel));
//        RequestFine requestModel = new RequestFine();
        try{
            if (requestModel == null){
                return "no";
            }
            if (requestModel.orderid == null){
                return "no";
            }
            if (requestModel.out_trade_id == null){
                return "no";
            }
            if (requestModel.status == null || requestModel.status == 0){
                return "no";
            }


            log.info("---------------cakeOut:orderid:" + requestModel.orderid + "out_trade_id:" + requestModel.out_trade_id);
            // 查询此数据属于哪个订单
            ChannelOutModel channelOutModel = new ChannelOutModel();
            channelOutModel.setMyTradeNo(requestModel.out_trade_id);
            channelOutModel = (ChannelOutModel) ComponentUtil.channelOutService.findByObject(channelOutModel);
            if (channelOutModel == null){
                return "no";
            }

            // 查询渠道信息
            ChannelModel channelModel = (ChannelModel) ComponentUtil.channelService.findById(channelOutModel.getChannelId());
            if (channelModel == null){
                return "no";
            }

            // 查询通道信息
            GewayModel gewayModel = (GewayModel) ComponentUtil.gewayService.findById(channelOutModel.getGewayId());
            if (gewayModel == null){
                return "no";
            }
            String secretKey = gewayModel.getSecretKey();

//            // 校验上游下发的数据
//            String mySign = "memberid=" + requestModel.memberid + "&" + "orderid=" + requestModel.orderid + "&" + "amount=" + requestModel.amount
//                    + "&" + "transaction_id=" + requestModel.transaction_id + "&" + "datetime=" + requestModel.datetime + "&" + "returncode=" + requestModel.returncode;
//            mySign = MD5Util.encryption(mySign);
//            if (!mySign.equals(requestModel.sign)){
//                return "no";
//            }

            // 根据渠道号主键ID以及通道ID查询关联关系
            ChannelGewayModel channelGewayModel = new ChannelGewayModel();
            channelGewayModel.setChannelId(channelOutModel.getChannelId());
            channelGewayModel.setGewayId(channelOutModel.getGewayId());
            channelGewayModel = (ChannelGewayModel) ComponentUtil.channelGewayService.findByObject(channelGewayModel);

            if (requestModel.status == 2){
                //组装上游数据
                DataCoreOutModel dataCoreOutModel = HodgepodgeMethod.assembleDataCoreOutByHf(requestModel, channelOutModel, channelGewayModel, 1,
                        channelOutModel.getChannelGewayId(), channelOutModel.getProfitType());
                int num = ComponentUtil.dataCoreOutService.add(dataCoreOutModel);
                if (num > 0){
                    return "success";
                }else {
                    return "no";
                }
            }else{
                return "no";
            }


        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // #添加异常
            log.error(String.format("this DataCoreController.cakeOut() is error , the all data=%s!", data));
            e.printStackTrace();
            return JSON.toJSONString(map);
        }
    }



    /**
     * @Description: 微派-代收
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8092/platform/data/wp
     * http://localhost:8092/platform/data/wp
     *
     * {"total_amount":"100.00","pay_amount":"80.00","out_trade_no":"2020080317100600001","trade_status":1,"trade_no":"test_order_no_sl_1","trade_time":"2020-08-05 20:10:35"}
     * {"total_amount":"100.00","pay_amount":"200.00","out_trade_no":"2020080317080600001","trade_status":1,"trade_no":"test_order_no_dl_1","trade_time":"2020-08-05 20:10:35"}
     * {"total_amount":"200.00","pay_amount":"200.00","out_trade_no":"2020080317040000001","trade_status":1,"trade_no":"test_order_no_yz_1","trade_time":"2020-08-05 20:10:35"}
     */
    @RequestMapping(value = "/wpIn", method = {RequestMethod.POST})
    public String wpIn(HttpServletRequest request, HttpServletResponse response, RequestWPay requestModel) throws Exception{
        String ip = StringUtil.getIpAddress(request);
        String data = "";
//        RequestFine requestModel = new RequestFine();
        try{
//            request.getParameter("total_amount");
//            requestModel = (RequestFine) getRootBean(RequestFine.class, request);
            if (requestModel == null){
                return "no";
            }else{
                log.info("wpIn().requestModel:" + JSON.toJSONString(requestModel));
            }
            if (requestModel.orderNo == null){
                return "no";
            }
            if (requestModel.status == null){
                return "no";
            }else {
                if (!StringUtils.isBlank(requestModel.status)){
                    if (!requestModel.status.equals("success")){
                        return "no";
                    }
                }
            }
            if (requestModel.cpparam == null){
                return "no";
            }
            if (requestModel.sign == null){
                return "no";
            }


            log.info("---------------WPay:trade_no:" + requestModel.orderNo + "out_trade_no:" + requestModel.cpparam);
            String total_amount = null;
            String pay_amount = requestModel.price;
            // 查询此数据属于哪个订单
            ChannelDataModel channelDataModel = new ChannelDataModel();
            channelDataModel.setMyTradeNo(requestModel.cpparam);
            channelDataModel = (ChannelDataModel) ComponentUtil.channelDataService.findByObject(channelDataModel);
            if (channelDataModel == null){
                return "no";
            }else {
                total_amount = channelDataModel.getTotalAmount();
            }

            // 查询渠道信息
            ChannelModel channelModel = (ChannelModel) ComponentUtil.channelService.findById(channelDataModel.getChannelId());
            if (channelModel == null){
                return "no";
            }

            // 查询通道信息
            GewayModel gewayModel = (GewayModel) ComponentUtil.gewayService.findById(channelDataModel.getGewayId());
            if (gewayModel == null){
                return "no";
            }
            String secretKey = gewayModel.getSecretKey();

            //MD5(cpparam=123456789&orderNo=10271944028870003&price=1.5&status=success&synType=wxpay&time=20170221200707CRCBwJce5ku88n8LSEbRWTg5dWAkJBiq).toUpperCase()
            // 校验上游下发的数据
            String mySign = "cpparam=" + requestModel.cpparam + "&" + "orderNo=" + requestModel.orderNo + "&" + "price=" + requestModel.price
                    + "&" + "status=" + requestModel.status + "&" + "synType=" + requestModel.synType + "&" + "time=" + requestModel.time + secretKey;
            mySign = MD5Util.encryption(mySign).toUpperCase();
            if (!mySign.equals(requestModel.sign)){
                return "no";
            }

            // 根据渠道号主键ID以及通道ID查询关联关系
            ChannelGewayModel channelGewayModel = new ChannelGewayModel();
            channelGewayModel.setChannelId(channelDataModel.getChannelId());
            channelGewayModel.setGewayId(channelDataModel.getGewayId());
            channelGewayModel = (ChannelGewayModel) ComponentUtil.channelGewayService.findByObject(channelGewayModel);

            String serviceCharge = "";
            String pay_serviceCharge = "";
            if (!StringUtils.isBlank(channelDataModel.getServiceCharge())){
                serviceCharge = channelDataModel.getServiceCharge();
            }else {
                serviceCharge = channelGewayModel.getServiceCharge();
            }
            pay_serviceCharge = serviceCharge;

            // 判断订单金额是否与实际支付金额一致：1初始化，2少了，3多了，4一致
            int moneyFitType = 4;// 金额是否与上报金额一致：1初始化，2少了，3多了，4一致
            // 金额相减
            String result = StringUtil.getBigDecimalSubtractByStr(pay_amount, total_amount);
            if (result.equals("0")){
                moneyFitType = 4;
            }else{
                boolean flag_money = StringUtil.getBigDecimalSubtract(total_amount, pay_amount);
                if (flag_money){
                    // 少了
                    moneyFitType = 2;
                }else {
                    // 多了
                    moneyFitType = 3;
                }
            }

            serviceCharge = StringUtil.getMultiply(total_amount, serviceCharge);
            String actualMoney = StringUtil.getBigDecimalSubtractStr(total_amount, serviceCharge);
            pay_serviceCharge = StringUtil.getMultiply(pay_amount, pay_serviceCharge);
            String payActualMoney = StringUtil.getBigDecimalSubtractStr(pay_amount, pay_serviceCharge);
//            int deductRatio = channelGewayModel.getDeductRatio();
            int tradeStatus = 1;
//            String resPrice = StringUtil.getBigDecimalSubtractStr(channelDataModel.getTotalAmount(), total_amount);
//            if (resPrice.equals("0")){
//                tradeStatus = 1;
//            }else {
//                tradeStatus = 2;
//            }
            // 更新状态
            ReceivingAccountDataModel receivingAccountDataModel = new ReceivingAccountDataModel();
            receivingAccountDataModel.setMyTradeNo(requestModel.cpparam);
            receivingAccountDataModel.setIsOk(2);
            ComponentUtil.receivingAccountDataService.update(receivingAccountDataModel);
            //组装上游数据
            DataCoreModel dataCoreModel = HodgepodgeMethod.assembleDataCoreWpay(requestModel, channelDataModel, channelGewayModel, total_amount, serviceCharge, actualMoney, tradeStatus,
                    pay_amount, payActualMoney, moneyFitType, channelDataModel.getChannelGewayId(), channelDataModel.getProfitType());
            int num = ComponentUtil.dataCoreService.add(dataCoreModel);
            if (num > 0){
                return "success";
            }else {
                return "no";
            }
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // #添加异常
            log.error(String.format("this DataCoreController.cakeIn() is error , the all data=%s!", data));
            e.printStackTrace();
            return JSON.toJSONString(map);
        }
    }




    /**
     * @Description: 狮子支付-代收
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8092/platform/data/szIn
     * http://localhost:8092/platform/data/szIn
     *
     * {"code":0,"msg":"操作成功","merchant_code":"商户号","order_id":"商户订单号","pay_no":"test_order_no_sl_1","amount":"500", "resp_code":"支付状态 00成功 其它失败  目前也只有成功会异步通知","sign":"sign1"}
     */
    @RequestMapping(value = "/szIn", method = {RequestMethod.POST})
    public String szIn(HttpServletRequest request, HttpServletResponse response,@RequestBody RequestSz requestModel) throws Exception{
        String ip = StringUtil.getIpAddress(request);
        String data = "";
//        RequestFine requestModel = new RequestFine();
        try{
//            request.getParameter("total_amount");
//            requestModel = (RequestFine) getRootBean(RequestFine.class, request);
            if (requestModel == null){
                return "no";
            }
            if (requestModel.order_id == null){
                return "no";
            }
            if (requestModel.pay_no == null){
                return "no";
            }
            if (requestModel.amount == null){
                return "no";
            }
            if (requestModel.code != 0){
                return "SUCCESS";// 只收取成功数据
            }


            log.info("---------------sz:order_id:" + requestModel.order_id + "pay_no:" + requestModel.pay_no);
            String total_amount = requestModel.amount;
            String pay_amount = requestModel.amount;
            // 查询此数据属于哪个订单
            ChannelDataModel channelDataModel = new ChannelDataModel();
            channelDataModel.setMyTradeNo(requestModel.order_id);
            channelDataModel = (ChannelDataModel) ComponentUtil.channelDataService.findByObject(channelDataModel);
            if (channelDataModel == null){
                return "no";
            }

            // 查询渠道信息
            ChannelModel channelModel = (ChannelModel) ComponentUtil.channelService.findById(channelDataModel.getChannelId());
            if (channelModel == null){
                return "no";
            }

            // 查询通道信息
            GewayModel gewayModel = (GewayModel) ComponentUtil.gewayService.findById(channelDataModel.getGewayId());
            if (gewayModel == null){
                return "no";
            }
            String secretKey = gewayModel.getSecretKey();

//            // 校验上游下发的数据
//            String mySign = "memberid=" + requestModel.memberid + "&" + "orderid=" + requestModel.orderid + "&" + "amount=" + requestModel.amount
//                    + "&" + "transaction_id=" + requestModel.transaction_id + "&" + "datetime=" + requestModel.datetime + "&" + "returncode=" + requestModel.returncode;
//            mySign = MD5Util.encryption(mySign);
//            if (!mySign.equals(requestModel.sign)){
//                return "no";
//            }

            // 根据渠道号主键ID以及通道ID查询关联关系
            ChannelGewayModel channelGewayModel = new ChannelGewayModel();
            channelGewayModel.setChannelId(channelDataModel.getChannelId());
            channelGewayModel.setGewayId(channelDataModel.getGewayId());
            channelGewayModel = (ChannelGewayModel) ComponentUtil.channelGewayService.findByObject(channelGewayModel);

            String serviceCharge = "";
            String pay_serviceCharge = "";
            if (!StringUtils.isBlank(channelDataModel.getServiceCharge())){
                serviceCharge = channelDataModel.getServiceCharge();
            }else {
                serviceCharge = channelGewayModel.getServiceCharge();
            }
            pay_serviceCharge = serviceCharge;

            // 判断订单金额是否与实际支付金额一致：1初始化，2少了，3多了，4一致
            int moneyFitType = 0;// 金额是否与上报金额一致：1初始化，2少了，3多了，4一致
            // 金额相减
            String result = StringUtil.getBigDecimalSubtractByStr(pay_amount, total_amount);
            if (result.equals("0")){
                moneyFitType = 4;
            }else{
                boolean flag_money = StringUtil.getBigDecimalSubtract(total_amount, pay_amount);
                if (flag_money){
                    // 少了
                    moneyFitType = 2;
                }else {
                    // 多了
                    moneyFitType = 3;
                }
            }

            serviceCharge = StringUtil.getMultiply(total_amount, serviceCharge);
            String actualMoney = StringUtil.getBigDecimalSubtractStr(total_amount, serviceCharge);
            pay_serviceCharge = StringUtil.getMultiply(pay_amount, pay_serviceCharge);
            String payActualMoney = StringUtil.getBigDecimalSubtractStr(pay_amount, pay_serviceCharge);
//            int deductRatio = channelGewayModel.getDeductRatio();
            int tradeStatus = 1;
//            String resPrice = StringUtil.getBigDecimalSubtractStr(channelDataModel.getTotalAmount(), total_amount);
//            if (resPrice.equals("0")){
//                tradeStatus = 1;
//            }else {
//                tradeStatus = 2;
//            }
            // 更新状态
            ReceivingAccountDataModel receivingAccountDataModel = new ReceivingAccountDataModel();
            receivingAccountDataModel.setMyTradeNo(requestModel.order_id);
            receivingAccountDataModel.setIsOk(2);
            ComponentUtil.receivingAccountDataService.update(receivingAccountDataModel);
            //组装上游数据
            DataCoreModel dataCoreModel = HodgepodgeMethod.assembleDataCoreSz(requestModel, channelDataModel, channelGewayModel, total_amount, serviceCharge, actualMoney, tradeStatus,
                    pay_amount, payActualMoney, moneyFitType, channelDataModel.getChannelGewayId(), channelDataModel.getProfitType());
            int num = ComponentUtil.dataCoreService.add(dataCoreModel);
            if (num > 0){
                return "SUCCESS";
            }else {
                return "no";
            }
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // #添加异常
            log.error(String.format("this DataCoreController.szIn() is error , the all data=%s!", data));
            e.printStackTrace();
            return JSON.toJSONString(map);
        }
    }






    /**
     * @Description: 木星支付-代收
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8092/platform/data/mxIn
     * http://localhost:8092/platform/data/mxIn
     *
     * {"code":"11","transactionid":"transactionid1","systemorderid":"systemorderid1","orderno":"orderno1","bankaccount":"bankaccount1","merno":"10095", "transamt":"500","timeend":"timeend1","banktype":"banktype1","productid":"productid1","signature":"signature1"}
     */
    @RequestMapping(value = "/mxIn", method = {RequestMethod.POST})
    public String mxIn(HttpServletRequest request, HttpServletResponse response, RequestMx requestModel) throws Exception{
        String ip = StringUtil.getIpAddress(request);
        String data = "";
//        RequestFine requestModel = new RequestFine();
        try{
//            request.getParameter("total_amount");
//            requestModel = (RequestFine) getRootBean(RequestFine.class, request);
            if (requestModel == null){
                return "no";
            }
            if (requestModel.orderno == null){
                return "no";
            }
            if (requestModel.systemorderid == null){
                return "no";
            }
            if (requestModel.transamt == null){
                return "no";
            }
            if (requestModel.code != 11){
                return "success";// 只收取成功数据
            }


            log.info("---------------mx:orderno:" + requestModel.orderno + "systemorderid:" + requestModel.systemorderid);
            String resStr = JSON.toJSONString(requestModel);
            log.info("mx-----------all-----data:" + resStr);
            String amount = StringUtil.getBigDecimalDivide(requestModel.transamt, "100.00");
            String total_amount = amount;
            String pay_amount = amount;
            // 查询此数据属于哪个订单
            ChannelDataModel channelDataModel = new ChannelDataModel();
            channelDataModel.setMyTradeNo(requestModel.orderno);
            channelDataModel = (ChannelDataModel) ComponentUtil.channelDataService.findByObject(channelDataModel);
            if (channelDataModel == null){
                return "no";
            }

            // 查询渠道信息
            ChannelModel channelModel = (ChannelModel) ComponentUtil.channelService.findById(channelDataModel.getChannelId());
            if (channelModel == null){
                return "no";
            }

            // 查询通道信息
            GewayModel gewayModel = (GewayModel) ComponentUtil.gewayService.findById(channelDataModel.getGewayId());
            if (gewayModel == null){
                return "no";
            }
            String secretKey = gewayModel.getSecretKey();

//            // 校验上游下发的数据
//            String mySign = "memberid=" + requestModel.memberid + "&" + "orderid=" + requestModel.orderid + "&" + "amount=" + requestModel.amount
//                    + "&" + "transaction_id=" + requestModel.transaction_id + "&" + "datetime=" + requestModel.datetime + "&" + "returncode=" + requestModel.returncode;
//            mySign = MD5Util.encryption(mySign);
//            if (!mySign.equals(requestModel.sign)){
//                return "no";
//            }

            // 根据渠道号主键ID以及通道ID查询关联关系
            ChannelGewayModel channelGewayModel = new ChannelGewayModel();
            channelGewayModel.setChannelId(channelDataModel.getChannelId());
            channelGewayModel.setGewayId(channelDataModel.getGewayId());
            channelGewayModel = (ChannelGewayModel) ComponentUtil.channelGewayService.findByObject(channelGewayModel);

            String serviceCharge = "";
            String pay_serviceCharge = "";
            if (!StringUtils.isBlank(channelDataModel.getServiceCharge())){
                serviceCharge = channelDataModel.getServiceCharge();
            }else {
                serviceCharge = channelGewayModel.getServiceCharge();
            }
            pay_serviceCharge = serviceCharge;

            // 判断订单金额是否与实际支付金额一致：1初始化，2少了，3多了，4一致
            int moneyFitType = 0;// 金额是否与上报金额一致：1初始化，2少了，3多了，4一致
            // 金额相减
            String result = StringUtil.getBigDecimalSubtractByStr(pay_amount, total_amount);
            if (result.equals("0")){
                moneyFitType = 4;
            }else{
                boolean flag_money = StringUtil.getBigDecimalSubtract(total_amount, pay_amount);
                if (flag_money){
                    // 少了
                    moneyFitType = 2;
                }else {
                    // 多了
                    moneyFitType = 3;
                }
            }

            serviceCharge = StringUtil.getMultiply(total_amount, serviceCharge);
            String actualMoney = StringUtil.getBigDecimalSubtractStr(total_amount, serviceCharge);
            pay_serviceCharge = StringUtil.getMultiply(pay_amount, pay_serviceCharge);
            String payActualMoney = StringUtil.getBigDecimalSubtractStr(pay_amount, pay_serviceCharge);
//            int deductRatio = channelGewayModel.getDeductRatio();
            int tradeStatus = 1;
//            String resPrice = StringUtil.getBigDecimalSubtractStr(channelDataModel.getTotalAmount(), total_amount);
//            if (resPrice.equals("0")){
//                tradeStatus = 1;
//            }else {
//                tradeStatus = 2;
//            }
            // 更新状态
            ReceivingAccountDataModel receivingAccountDataModel = new ReceivingAccountDataModel();
            receivingAccountDataModel.setMyTradeNo(requestModel.orderno);
            receivingAccountDataModel.setIsOk(2);
            ComponentUtil.receivingAccountDataService.update(receivingAccountDataModel);
            //组装上游数据
            DataCoreModel dataCoreModel = HodgepodgeMethod.assembleDataCoreMx(requestModel, channelDataModel, channelGewayModel, total_amount, serviceCharge, actualMoney, tradeStatus,
                    pay_amount, payActualMoney, moneyFitType, channelDataModel.getChannelGewayId(), channelDataModel.getProfitType());
            int num = ComponentUtil.dataCoreService.add(dataCoreModel);
            if (num > 0){
                return "success";
            }else {
                return "no";
            }
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // #添加异常
            log.error(String.format("this DataCoreController.mxIn() is error , the all data=%s!", data));
            e.printStackTrace();
            return JSON.toJSONString(map);
        }
    }




    /**
     * @Description: 大信誉支付-代收
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8092/platform/data/dxyIn
     * http://localhost:8092/platform/data/dxyIn
     *
     * {"memberid":"memberid1","orderid":"orderid1","amount":"amount1","transaction_id":"transaction_id1","datetime":"datetime1","returncode":"returncode1", "attach":"attach1","sign":"sign1"}
     */
    @RequestMapping(value = "/dxyIn", method = {RequestMethod.POST})
    public String dxyIn(HttpServletRequest request, HttpServletResponse response, RequestDxy requestModel) throws Exception{
        String ip = StringUtil.getIpAddress(request);
        String data = "";
//        RequestFine requestModel = new RequestFine();
        try{
//            request.getParameter("total_amount");
//            requestModel = (RequestFine) getRootBean(RequestFine.class, request);
            if (requestModel == null){
                return "no";
            }
            if (requestModel.orderid == null){
                return "no";
            }
            if (requestModel.transaction_id == null){
                return "no";
            }
            if (requestModel.amount == null){
                return "no";
            }
            if (StringUtils.isBlank(requestModel.returncode)){
                return "no";
            }else {
                if (!requestModel.returncode.equals("00")){
                    return "OK";// 只接受成功数据
                }
            }


            log.info("---------------dxy:orderid:" + requestModel.orderid + "transaction_id:" + requestModel.transaction_id);
            String resStr = JSON.toJSONString(requestModel);
            log.info("dxy-----------all-----data:" + resStr);
            String total_amount = requestModel.amount;
            String pay_amount = requestModel.amount;
            // 查询此数据属于哪个订单
            ChannelDataModel channelDataModel = new ChannelDataModel();
            channelDataModel.setMyTradeNo(requestModel.orderid);
            channelDataModel = (ChannelDataModel) ComponentUtil.channelDataService.findByObject(channelDataModel);
            if (channelDataModel == null){
                return "no";
            }

            // 查询渠道信息
            ChannelModel channelModel = (ChannelModel) ComponentUtil.channelService.findById(channelDataModel.getChannelId());
            if (channelModel == null){
                return "no";
            }

            // 查询通道信息
            GewayModel gewayModel = (GewayModel) ComponentUtil.gewayService.findById(channelDataModel.getGewayId());
            if (gewayModel == null){
                return "no";
            }
            String secretKey = gewayModel.getSecretKey();

//            // 校验上游下发的数据
//            String mySign = "memberid=" + requestModel.memberid + "&" + "orderid=" + requestModel.orderid + "&" + "amount=" + requestModel.amount
//                    + "&" + "transaction_id=" + requestModel.transaction_id + "&" + "datetime=" + requestModel.datetime + "&" + "returncode=" + requestModel.returncode;
//            mySign = MD5Util.encryption(mySign);
//            if (!mySign.equals(requestModel.sign)){
//                return "no";
//            }

            // 根据渠道号主键ID以及通道ID查询关联关系
            ChannelGewayModel channelGewayModel = new ChannelGewayModel();
            channelGewayModel.setChannelId(channelDataModel.getChannelId());
            channelGewayModel.setGewayId(channelDataModel.getGewayId());
            channelGewayModel = (ChannelGewayModel) ComponentUtil.channelGewayService.findByObject(channelGewayModel);

            String serviceCharge = "";
            String pay_serviceCharge = "";
            if (!StringUtils.isBlank(channelDataModel.getServiceCharge())){
                serviceCharge = channelDataModel.getServiceCharge();
            }else {
                serviceCharge = channelGewayModel.getServiceCharge();
            }
            pay_serviceCharge = serviceCharge;

            // 判断订单金额是否与实际支付金额一致：1初始化，2少了，3多了，4一致
            int moneyFitType = 0;// 金额是否与上报金额一致：1初始化，2少了，3多了，4一致
            // 金额相减
            String result = StringUtil.getBigDecimalSubtractByStr(pay_amount, total_amount);
            if (result.equals("0")){
                moneyFitType = 4;
            }else{
                boolean flag_money = StringUtil.getBigDecimalSubtract(total_amount, pay_amount);
                if (flag_money){
                    // 少了
                    moneyFitType = 2;
                }else {
                    // 多了
                    moneyFitType = 3;
                }
            }

            serviceCharge = StringUtil.getMultiply(total_amount, serviceCharge);
            String actualMoney = StringUtil.getBigDecimalSubtractStr(total_amount, serviceCharge);
            pay_serviceCharge = StringUtil.getMultiply(pay_amount, pay_serviceCharge);
            String payActualMoney = StringUtil.getBigDecimalSubtractStr(pay_amount, pay_serviceCharge);
//            int deductRatio = channelGewayModel.getDeductRatio();
            int tradeStatus = 1;
//            String resPrice = StringUtil.getBigDecimalSubtractStr(channelDataModel.getTotalAmount(), total_amount);
//            if (resPrice.equals("0")){
//                tradeStatus = 1;
//            }else {
//                tradeStatus = 2;
//            }
            // 更新状态
            ReceivingAccountDataModel receivingAccountDataModel = new ReceivingAccountDataModel();
            receivingAccountDataModel.setMyTradeNo(requestModel.orderid);
            receivingAccountDataModel.setIsOk(2);
            ComponentUtil.receivingAccountDataService.update(receivingAccountDataModel);
            //组装上游数据
            DataCoreModel dataCoreModel = HodgepodgeMethod.assembleDataCoreDxy(requestModel, channelDataModel, channelGewayModel, total_amount, serviceCharge, actualMoney, tradeStatus,
                    pay_amount, payActualMoney, moneyFitType, channelDataModel.getChannelGewayId(), channelDataModel.getProfitType());
            int num = ComponentUtil.dataCoreService.add(dataCoreModel);
            if (num > 0){
                return "OK";
            }else {
                return "no";
            }
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // #添加异常
            log.error(String.format("this DataCoreController.dxyIn() is error , the all data=%s!", data));
            e.printStackTrace();
            return JSON.toJSONString(map);
        }
    }




    /**
     * @Description: 金木星-代收
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8092/platform/data/jmxIn
     * http://localhost:8092/platform/data/jmxIn
     *
     * {"mch_id":"mch_id1","trade_no":"trade_no1","out_trade_no":"out_trade_no1","psg_trade_no":"psg_trade_no1","money":"500","notify_time":"2021-07-20 10:12:26","subject":"subject1","state":"2","sign":"sign1"}
     */
    @RequestMapping(value = "/jmxIn", method = {RequestMethod.POST})
    public String jmxIn(HttpServletRequest request, HttpServletResponse response,@RequestBody RequestJmx requestModel) throws Exception{
        String ip = StringUtil.getIpAddress(request);
        String data = "";
//        RequestFine requestModel = new RequestFine();
        try{
//            request.getParameter("total_amount");
//            requestModel = (RequestFine) getRootBean(RequestFine.class, request);
            if (requestModel == null){
                return "no";
            }
            if (requestModel.trade_no == null){
                return "no";
            }
            if (requestModel.out_trade_no == null){
                return "no";
            }
            if (requestModel.money == null){
                return "no";
            }
            if (requestModel.state == null){
                return "no";
            }
            if (!StringUtils.isBlank(requestModel.state)){
                if (!requestModel.state.equals("2")){
                    return "SUCCESS";// 只收取成功数据
                }
            }


            log.info("---------------jmx:trade_no:" + requestModel.trade_no + "out_trade_no:" + requestModel.out_trade_no);
            String total_amount = requestModel.money;
            String pay_amount = requestModel.money;
            // 查询此数据属于哪个订单
            ChannelDataModel channelDataModel = new ChannelDataModel();
            channelDataModel.setMyTradeNo(requestModel.out_trade_no);
            channelDataModel = (ChannelDataModel) ComponentUtil.channelDataService.findByObject(channelDataModel);
            if (channelDataModel == null){
                return "no";
            }

            // 查询渠道信息
            ChannelModel channelModel = (ChannelModel) ComponentUtil.channelService.findById(channelDataModel.getChannelId());
            if (channelModel == null){
                return "no";
            }

            // 查询通道信息
            GewayModel gewayModel = (GewayModel) ComponentUtil.gewayService.findById(channelDataModel.getGewayId());
            if (gewayModel == null){
                return "no";
            }
            String secretKey = gewayModel.getSecretKey();

//            // 校验上游下发的数据
//            String mySign = "memberid=" + requestModel.memberid + "&" + "orderid=" + requestModel.orderid + "&" + "amount=" + requestModel.amount
//                    + "&" + "transaction_id=" + requestModel.transaction_id + "&" + "datetime=" + requestModel.datetime + "&" + "returncode=" + requestModel.returncode;
//            mySign = MD5Util.encryption(mySign);
//            if (!mySign.equals(requestModel.sign)){
//                return "no";
//            }

            // 根据渠道号主键ID以及通道ID查询关联关系
            ChannelGewayModel channelGewayModel = new ChannelGewayModel();
            channelGewayModel.setChannelId(channelDataModel.getChannelId());
            channelGewayModel.setGewayId(channelDataModel.getGewayId());
            channelGewayModel = (ChannelGewayModel) ComponentUtil.channelGewayService.findByObject(channelGewayModel);

            String serviceCharge = "";
            String pay_serviceCharge = "";
            if (!StringUtils.isBlank(channelDataModel.getServiceCharge())){
                serviceCharge = channelDataModel.getServiceCharge();
            }else {
                serviceCharge = channelGewayModel.getServiceCharge();
            }
            pay_serviceCharge = serviceCharge;

            // 判断订单金额是否与实际支付金额一致：1初始化，2少了，3多了，4一致
            int moneyFitType = 0;// 金额是否与上报金额一致：1初始化，2少了，3多了，4一致
            // 金额相减
            String result = StringUtil.getBigDecimalSubtractByStr(pay_amount, total_amount);
            if (result.equals("0")){
                moneyFitType = 4;
            }else{
                boolean flag_money = StringUtil.getBigDecimalSubtract(total_amount, pay_amount);
                if (flag_money){
                    // 少了
                    moneyFitType = 2;
                }else {
                    // 多了
                    moneyFitType = 3;
                }
            }

            serviceCharge = StringUtil.getMultiply(total_amount, serviceCharge);
            String actualMoney = StringUtil.getBigDecimalSubtractStr(total_amount, serviceCharge);
            pay_serviceCharge = StringUtil.getMultiply(pay_amount, pay_serviceCharge);
            String payActualMoney = StringUtil.getBigDecimalSubtractStr(pay_amount, pay_serviceCharge);
//            int deductRatio = channelGewayModel.getDeductRatio();
            int tradeStatus = 1;
//            String resPrice = StringUtil.getBigDecimalSubtractStr(channelDataModel.getTotalAmount(), total_amount);
//            if (resPrice.equals("0")){
//                tradeStatus = 1;
//            }else {
//                tradeStatus = 2;
//            }
            // 更新状态
            ReceivingAccountDataModel receivingAccountDataModel = new ReceivingAccountDataModel();
            receivingAccountDataModel.setMyTradeNo(requestModel.out_trade_no);
            receivingAccountDataModel.setIsOk(2);
            ComponentUtil.receivingAccountDataService.update(receivingAccountDataModel);
            //组装上游数据
            DataCoreModel dataCoreModel = HodgepodgeMethod.assembleDataCoreJmx(requestModel, channelDataModel, channelGewayModel, total_amount, serviceCharge, actualMoney, tradeStatus,
                    pay_amount, payActualMoney, moneyFitType, channelDataModel.getChannelGewayId(), channelDataModel.getProfitType());
            int num = ComponentUtil.dataCoreService.add(dataCoreModel);
            if (num > 0){
                return "SUCCESS";
            }else {
                return "no";
            }
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // #添加异常
            log.error(String.format("this DataCoreController.jmxIn() is error , the all data=%s!", data));
            e.printStackTrace();
            return JSON.toJSONString(map);
        }
    }






    /**
     * @Description: 高防支付-代收
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8092/platform/data/gfIn
     * http://localhost:8092/platform/data/gfIn
     *
     * http://localhost:8092/platform/data/gfIn?total_amount=500.00&out_trade_no=out_trade_no1&trade_status=1&pay_amount=500.00&trade_no=trade_no1&extra_return_param=extra_return_param1&trade_time=trade_time1&sign=sign1
     */
    @RequestMapping(value = "/gfIn", method = {RequestMethod.GET})
    public String gfIn(HttpServletRequest request, HttpServletResponse response, RequestGf requestModel) throws Exception{
        String ip = StringUtil.getIpAddress(request);
        String data = "";
//        RequestFine requestModel = new RequestFine();
        try{
//            request.getParameter("total_amount");
//            requestModel = (RequestFine) getRootBean(RequestFine.class, request);
            if (requestModel == null){
                return "no";
            }
            if (requestModel.out_trade_no == null){
                return "no";
            }
            if (requestModel.trade_no == null){
                return "no";
            }
            if (requestModel.total_amount == null){
                return "no";
            }
            if (StringUtils.isBlank(requestModel.trade_status)){
                return "no";
            }else {
                if (!requestModel.trade_status.equals("1")){
                    return "OK";// 只接受成功数据
                }
            }


            log.info("---------------gf:out_trade_no:" + requestModel.out_trade_no + "trade_no:" + requestModel.trade_no);
            String resStr = JSON.toJSONString(requestModel);
            log.info("gf-----------all-----data:" + resStr);
            String total_amount = requestModel.total_amount;
            String pay_amount = requestModel.total_amount;
            // 查询此数据属于哪个订单
            ChannelDataModel channelDataModel = new ChannelDataModel();
            channelDataModel.setMyTradeNo(requestModel.out_trade_no);
            channelDataModel = (ChannelDataModel) ComponentUtil.channelDataService.findByObject(channelDataModel);
            if (channelDataModel == null){
                return "no";
            }

            // 查询渠道信息
            ChannelModel channelModel = (ChannelModel) ComponentUtil.channelService.findById(channelDataModel.getChannelId());
            if (channelModel == null){
                return "no";
            }

            // 查询通道信息
            GewayModel gewayModel = (GewayModel) ComponentUtil.gewayService.findById(channelDataModel.getGewayId());
            if (gewayModel == null){
                return "no";
            }
            String secretKey = gewayModel.getSecretKey();

//            // 校验上游下发的数据
//            String mySign = "memberid=" + requestModel.memberid + "&" + "orderid=" + requestModel.orderid + "&" + "amount=" + requestModel.amount
//                    + "&" + "transaction_id=" + requestModel.transaction_id + "&" + "datetime=" + requestModel.datetime + "&" + "returncode=" + requestModel.returncode;
//            mySign = MD5Util.encryption(mySign);
//            if (!mySign.equals(requestModel.sign)){
//                return "no";
//            }

            // 根据渠道号主键ID以及通道ID查询关联关系
            ChannelGewayModel channelGewayModel = new ChannelGewayModel();
            channelGewayModel.setChannelId(channelDataModel.getChannelId());
            channelGewayModel.setGewayId(channelDataModel.getGewayId());
            channelGewayModel = (ChannelGewayModel) ComponentUtil.channelGewayService.findByObject(channelGewayModel);

            String serviceCharge = "";
            String pay_serviceCharge = "";
            if (!StringUtils.isBlank(channelDataModel.getServiceCharge())){
                serviceCharge = channelDataModel.getServiceCharge();
            }else {
                serviceCharge = channelGewayModel.getServiceCharge();
            }
            pay_serviceCharge = serviceCharge;

            // 判断订单金额是否与实际支付金额一致：1初始化，2少了，3多了，4一致
            int moneyFitType = 0;// 金额是否与上报金额一致：1初始化，2少了，3多了，4一致
            // 金额相减
            String result = StringUtil.getBigDecimalSubtractByStr(pay_amount, total_amount);
            if (result.equals("0")){
                moneyFitType = 4;
            }else{
                boolean flag_money = StringUtil.getBigDecimalSubtract(total_amount, pay_amount);
                if (flag_money){
                    // 少了
                    moneyFitType = 2;
                }else {
                    // 多了
                    moneyFitType = 3;
                }
            }

            serviceCharge = StringUtil.getMultiply(total_amount, serviceCharge);
            String actualMoney = StringUtil.getBigDecimalSubtractStr(total_amount, serviceCharge);
            pay_serviceCharge = StringUtil.getMultiply(pay_amount, pay_serviceCharge);
            String payActualMoney = StringUtil.getBigDecimalSubtractStr(pay_amount, pay_serviceCharge);
//            int deductRatio = channelGewayModel.getDeductRatio();
            int tradeStatus = 1;
//            String resPrice = StringUtil.getBigDecimalSubtractStr(channelDataModel.getTotalAmount(), total_amount);
//            if (resPrice.equals("0")){
//                tradeStatus = 1;
//            }else {
//                tradeStatus = 2;
//            }
            // 更新状态
            ReceivingAccountDataModel receivingAccountDataModel = new ReceivingAccountDataModel();
            receivingAccountDataModel.setMyTradeNo(requestModel.out_trade_no);
            receivingAccountDataModel.setIsOk(2);
            ComponentUtil.receivingAccountDataService.update(receivingAccountDataModel);
            //组装上游数据
            DataCoreModel dataCoreModel = HodgepodgeMethod.assembleDataCoreGf(requestModel, channelDataModel, channelGewayModel, total_amount, serviceCharge, actualMoney, tradeStatus,
                    pay_amount, payActualMoney, moneyFitType, channelDataModel.getChannelGewayId(), channelDataModel.getProfitType());
            int num = ComponentUtil.dataCoreService.add(dataCoreModel);
            if (num > 0){
                return "OK";
            }else {
                return "no";
            }
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // #添加异常
            log.error(String.format("this DataCoreController.gfIn() is error , the all data=%s!", data));
            e.printStackTrace();
            return JSON.toJSONString(map);
        }
    }




    /**
     * @Description: 小肥龙支付-代收
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8092/platform/data/xflIn
     * http://localhost:8092/platform/data/xflIn
     *
     * {"payOrderId":"payOrderId1","mchId":"mchId1","appId":"appId1","productId":"productId1","mchOrderNo":"mchOrderNo1","amount":5000, "income":"5000","status":2,"channelOrderNo":"channelOrderNo1","param1":"param11","param2":"param21","paySuccTime":2021080119471111,"backType":1,"reqTime":"reqTime1","sign":"sign1"}
     */
    @RequestMapping(value = "/xflIn", method = {RequestMethod.POST})
    public String xflIn(HttpServletRequest request, HttpServletResponse response, RequestXfl requestModel) throws Exception{
        String ip = StringUtil.getIpAddress(request);
        String data = "";
//        RequestFine requestModel = new RequestFine();
        try{
//            request.getParameter("total_amount");
//            requestModel = (RequestFine) getRootBean(RequestFine.class, request);
            if (requestModel == null){
                return "no";
            }
            if (requestModel.mchOrderNo == null){
                return "no";
            }
            if (requestModel.payOrderId == null){
                return "no";
            }
            if (requestModel.amount == 0){
                return "no";
            }
            if (requestModel.status <= 1 || requestModel.status >= 4){
                return "success";// 只收取成功数据
            }


            log.info("---------------xfl:mchOrderNo:" + requestModel.mchOrderNo + "payOrderId:" + requestModel.payOrderId);
            String resStr = JSON.toJSONString(requestModel);
            log.info("xfl-----------all-----data:" + resStr);
            String amount = StringUtil.getBigDecimalDivide(String.valueOf(requestModel.amount), "100.00");
            String sy_pay_amount = StringUtil.getBigDecimalDivide(String.valueOf(requestModel.income), "100.00");
            String total_amount = amount;
            String pay_amount = sy_pay_amount;
            // 查询此数据属于哪个订单
            ChannelDataModel channelDataModel = new ChannelDataModel();
            channelDataModel.setMyTradeNo(requestModel.mchOrderNo);
            channelDataModel = (ChannelDataModel) ComponentUtil.channelDataService.findByObject(channelDataModel);
            if (channelDataModel == null){
                return "no";
            }

            // 查询渠道信息
            ChannelModel channelModel = (ChannelModel) ComponentUtil.channelService.findById(channelDataModel.getChannelId());
            if (channelModel == null){
                return "no";
            }

            // 查询通道信息
            GewayModel gewayModel = (GewayModel) ComponentUtil.gewayService.findById(channelDataModel.getGewayId());
            if (gewayModel == null){
                return "no";
            }
            String secretKey = gewayModel.getSecretKey();

//            // 校验上游下发的数据
//            String mySign = "memberid=" + requestModel.memberid + "&" + "orderid=" + requestModel.orderid + "&" + "amount=" + requestModel.amount
//                    + "&" + "transaction_id=" + requestModel.transaction_id + "&" + "datetime=" + requestModel.datetime + "&" + "returncode=" + requestModel.returncode;
//            mySign = MD5Util.encryption(mySign);
//            if (!mySign.equals(requestModel.sign)){
//                return "no";
//            }

            // 根据渠道号主键ID以及通道ID查询关联关系
            ChannelGewayModel channelGewayModel = new ChannelGewayModel();
            channelGewayModel.setChannelId(channelDataModel.getChannelId());
            channelGewayModel.setGewayId(channelDataModel.getGewayId());
            channelGewayModel = (ChannelGewayModel) ComponentUtil.channelGewayService.findByObject(channelGewayModel);

            String serviceCharge = "";
            String pay_serviceCharge = "";
            if (!StringUtils.isBlank(channelDataModel.getServiceCharge())){
                serviceCharge = channelDataModel.getServiceCharge();
            }else {
                serviceCharge = channelGewayModel.getServiceCharge();
            }
            pay_serviceCharge = serviceCharge;

            // 判断订单金额是否与实际支付金额一致：1初始化，2少了，3多了，4一致
            int moneyFitType = 0;// 金额是否与上报金额一致：1初始化，2少了，3多了，4一致
            // 金额相减
            String result = StringUtil.getBigDecimalSubtractByStr(pay_amount, total_amount);
            if (result.equals("0")){
                moneyFitType = 4;
            }else{
                boolean flag_money = StringUtil.getBigDecimalSubtract(total_amount, pay_amount);
                if (flag_money){
                    // 少了
                    moneyFitType = 2;
                }else {
                    // 多了
                    moneyFitType = 3;
                }
            }

            serviceCharge = StringUtil.getMultiply(total_amount, serviceCharge);
            String actualMoney = StringUtil.getBigDecimalSubtractStr(total_amount, serviceCharge);
            pay_serviceCharge = StringUtil.getMultiply(pay_amount, pay_serviceCharge);
            String payActualMoney = StringUtil.getBigDecimalSubtractStr(pay_amount, pay_serviceCharge);
//            int deductRatio = channelGewayModel.getDeductRatio();
            int tradeStatus = 1;
//            String resPrice = StringUtil.getBigDecimalSubtractStr(channelDataModel.getTotalAmount(), total_amount);
//            if (resPrice.equals("0")){
//                tradeStatus = 1;
//            }else {
//                tradeStatus = 2;
//            }
            // 更新状态
            ReceivingAccountDataModel receivingAccountDataModel = new ReceivingAccountDataModel();
            receivingAccountDataModel.setMyTradeNo(requestModel.mchOrderNo);
            receivingAccountDataModel.setIsOk(2);
            ComponentUtil.receivingAccountDataService.update(receivingAccountDataModel);
            //组装上游数据
            DataCoreModel dataCoreModel = HodgepodgeMethod.assembleDataCoreXfl(requestModel, channelDataModel, channelGewayModel, total_amount, serviceCharge, actualMoney, tradeStatus,
                    pay_amount, payActualMoney, moneyFitType, channelDataModel.getChannelGewayId(), channelDataModel.getProfitType());
            int num = ComponentUtil.dataCoreService.add(dataCoreModel);
            if (num > 0){
                return "success";
            }else {
                return "no";
            }
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // #添加异常
            log.error(String.format("this DataCoreController.mxIn() is error , the all data=%s!", data));
            e.printStackTrace();
            return JSON.toJSONString(map);
        }
    }



}
