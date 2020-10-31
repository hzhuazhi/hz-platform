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
import com.hz.platform.master.core.model.datacore.DataCoreModel;
import com.hz.platform.master.core.model.geway.GewayModel;
import com.hz.platform.master.core.model.receivingaccountdata.ReceivingAccountDataModel;
import com.hz.platform.master.core.model.zfbapp.ZfbAppModel;
import com.hz.platform.master.core.protocol.request.RequestAlipay;
import com.hz.platform.master.core.protocol.request.notify.RequestBufPay;
import com.hz.platform.master.core.protocol.request.notify.RequestFine;
import com.hz.platform.master.core.protocol.request.notify.RequestJt;
import com.hz.platform.master.core.protocol.request.notify.RequestWn;
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
@RequestMapping("/pay/data")
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
     * @Description: 九通支付
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8082/pay/data/jt
     * http://localhost:8082/pay/data/jt
     * memberid=200377749
     * orderid=2020032510200900001
     * amount=188
     * transaction_id=transaction_id_jt_1
     * datetime=2020-03-25 15:40:27
     * returncode=00
     * attach=JT
     * sign=0688fa7bcb789ae764a801c11a2c21df
     */
    @RequestMapping(value = "/jt", method = {RequestMethod.POST})
    public String jt(HttpServletRequest request, HttpServletResponse response, RequestJt requestModel) throws Exception{
        String sgid = ComponentUtil.redisIdService.getNewId();
        String cgid = "";
        String ip = StringUtil.getIpAddress(request);
        String data = "";
        try{

            if (requestModel == null){
                return "no";
            }
            if (requestModel.memberid == null){
                return "no";
            }
            if (requestModel.orderid == null){
                return "no";
            }
            if (requestModel.amount == null){
                return "no";
            }
            if (requestModel.transaction_id == null){
                return "no";
            }
            if (requestModel.returncode == null){
                return "no";
            }
            if (requestModel.sign == null){
                return "no";
            }
            log.info("---------------九通数据:memberid:" + requestModel.memberid + "transaction_id:" + requestModel.transaction_id);
            String total_amount = requestModel.amount;

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
            if (!StringUtils.isBlank(channelDataModel.getServiceCharge())){
                serviceCharge = channelDataModel.getServiceCharge();
            }else {
                serviceCharge = channelGewayModel.getServiceCharge();
            }
            serviceCharge = StringUtil.getMultiply(total_amount, serviceCharge);
            String actualMoney = StringUtil.getBigDecimalSubtractStr(total_amount, serviceCharge);
//            int deductRatio = channelGewayModel.getDeductRatio();
            int tradeStatus = 0;
            if (requestModel.returncode.equals("00")){
                tradeStatus = 1;
            }else {
                tradeStatus = 2;
            }
            // 组装上游数据
            DataCoreModel dataCoreModel = HodgepodgeMethod.assembleDataCoreJt(requestModel, channelDataModel, channelGewayModel, total_amount, serviceCharge, actualMoney, tradeStatus);
            int num = ComponentUtil.dataCoreService.add(dataCoreModel);
            if (num > 0){
                return "OK";
            }else {
                return "no";
            }
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // #添加异常
            log.error(String.format("this DataCoreController.jt() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            e.printStackTrace();
            return JSON.toJSONString(map);
        }
    }



    /**
     * @Description: 绿色宝支付
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8082/pay/data/lsb
     * http://localhost:8082/pay/data/lsb
     * memberid=200377749
     * orderid=2020032510200900001
     * amount=188
     * transaction_id=transaction_id_jt_1
     * datetime=2020-03-25 15:40:27
     * returncode=00
     * attach=JT
     * sign=0688fa7bcb789ae764a801c11a2c21df
     */
    @RequestMapping(value = "/lsb", method = {RequestMethod.POST})
    public String lsb(HttpServletRequest request, HttpServletResponse response, RequestJt requestModel) throws Exception{
        String sgid = ComponentUtil.redisIdService.getNewId();
        String cgid = "";
        String ip = StringUtil.getIpAddress(request);
        String data = "";
        try{

            if (requestModel == null){
                return "no";
            }
            if (requestModel.memberid == null){
                return "no";
            }
            if (requestModel.orderid == null){
                return "no";
            }
            if (requestModel.amount == null){
                return "no";
            }
            if (requestModel.transaction_id == null){
                return "no";
            }
            if (requestModel.returncode == null){
                return "no";
            }
            if (requestModel.sign == null){
                return "no";
            }
            log.info("---------------绿色宝:memberid:" + requestModel.memberid + "transaction_id:" + requestModel.transaction_id);
            String total_amount = requestModel.amount;

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
            if (!StringUtils.isBlank(channelDataModel.getServiceCharge())){
                serviceCharge = channelDataModel.getServiceCharge();
            }else {
                serviceCharge = channelGewayModel.getServiceCharge();
            }
            serviceCharge = StringUtil.getMultiply(total_amount, serviceCharge);
            String actualMoney = StringUtil.getBigDecimalSubtractStr(total_amount, serviceCharge);
//            int deductRatio = channelGewayModel.getDeductRatio();
            int tradeStatus = 0;
            if (requestModel.returncode.equals("00")){
                tradeStatus = 1;
            }else {
                tradeStatus = 2;
            }
            // 组装上游数据
            DataCoreModel dataCoreModel = HodgepodgeMethod.assembleDataCoreJt(requestModel, channelDataModel, channelGewayModel, total_amount, serviceCharge, actualMoney, tradeStatus);
            int num = ComponentUtil.dataCoreService.add(dataCoreModel);
            if (num > 0){
                return "OK";
            }else {
                return "no";
            }
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // #添加异常
            log.error(String.format("this DataCoreController.lsb() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
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
     * @Description: 绿色宝支付
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8082/pay/data/jh
     * http://localhost:8082/pay/data/jh
     * memberid=200377749
     * orderid=2020032510200900001
     * amount=188
     * transaction_id=transaction_id_jt_1
     * datetime=2020-03-25 15:40:27
     * returncode=00
     * attach=JT
     * sign=0688fa7bcb789ae764a801c11a2c21df
     */
    @RequestMapping(value = "/jh", method = {RequestMethod.POST})
    public String jh(HttpServletRequest request, HttpServletResponse response, RequestJt requestModel) throws Exception{
        String sgid = ComponentUtil.redisIdService.getNewId();
        String cgid = "";
        String ip = StringUtil.getIpAddress(request);
        String data = "";
        try{

            if (requestModel == null){
                return "no";
            }
            if (requestModel.memberid == null){
                return "no";
            }
            if (requestModel.orderid == null){
                return "no";
            }
            if (requestModel.amount == null){
                return "no";
            }
            if (requestModel.transaction_id == null){
                return "no";
            }
            if (requestModel.returncode == null){
                return "no";
            }
            if (requestModel.sign == null){
                return "no";
            }
            log.info("---------------聚鸿:memberid:" + requestModel.memberid + "transaction_id:" + requestModel.transaction_id);
            String total_amount = requestModel.amount;

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
            if (!StringUtils.isBlank(channelDataModel.getServiceCharge())){
                serviceCharge = channelDataModel.getServiceCharge();
            }else {
                serviceCharge = channelGewayModel.getServiceCharge();
            }
            serviceCharge = StringUtil.getMultiply(total_amount, serviceCharge);
            String actualMoney = StringUtil.getBigDecimalSubtractStr(total_amount, serviceCharge);
//            int deductRatio = channelGewayModel.getDeductRatio();
            int tradeStatus = 0;
            if (requestModel.returncode.equals("00")){
                tradeStatus = 1;
            }else {
                tradeStatus = 2;
            }
            // 组装上游数据
            DataCoreModel dataCoreModel = HodgepodgeMethod.assembleDataCoreJt(requestModel, channelDataModel, channelGewayModel, total_amount, serviceCharge, actualMoney, tradeStatus);
            int num = ComponentUtil.dataCoreService.add(dataCoreModel);
            if (num > 0){
                return "OK";
            }else {
                return "no";
            }
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // #添加异常
            log.error(String.format("this DataCoreController.jh() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            e.printStackTrace();
            return JSON.toJSONString(map);
        }
    }



    /**
     * @Description: BufPay个人支付
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8082/pay/data/buf
     * http://localhost:8082/pay/data/jh
     * memberid=200377749
     * orderid=2020032510200900001
     * amount=188
     * transaction_id=transaction_id_jt_1
     * datetime=2020-03-25 15:40:27
     * returncode=00
     * attach=JT
     * sign=0688fa7bcb789ae764a801c11a2c21df
     */
    @RequestMapping(value = "/buf", method = {RequestMethod.POST})
    public String buf(HttpServletRequest request, HttpServletResponse response, RequestBufPay requestModel) throws Exception{
        String sgid = ComponentUtil.redisIdService.getNewId();
        String cgid = "";
        String ip = StringUtil.getIpAddress(request);
        String data = "";
        try{

            if (requestModel == null){
                return "no";
            }
            if (requestModel.aoid == null){
                return "no";
            }
            if (requestModel.order_id == null){
                return "no";
            }
            if (requestModel.price == null){
                return "no";
            }
            if (requestModel.pay_price == null){
                return "no";
            }
            if (requestModel.sign == null){
                return "no";
            }
            log.info("---------------BufPay:aoid:" + requestModel.aoid + "order_id:" + requestModel.order_id);
            String total_amount = requestModel.pay_price;

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
            if (!StringUtils.isBlank(channelDataModel.getServiceCharge())){
                serviceCharge = channelDataModel.getServiceCharge();
            }else {
                serviceCharge = channelGewayModel.getServiceCharge();
            }
            serviceCharge = StringUtil.getMultiply(total_amount, serviceCharge);
            String actualMoney = StringUtil.getBigDecimalSubtractStr(total_amount, serviceCharge);
//            int deductRatio = channelGewayModel.getDeductRatio();
            int tradeStatus = 0;
            String resPrice = StringUtil.getBigDecimalSubtractStr(channelDataModel.getTotalAmount(), total_amount);
            if (resPrice.equals("0")){
                tradeStatus = 1;
            }else {
                tradeStatus = 2;
            }
            // 更新状态
            ReceivingAccountDataModel receivingAccountDataModel = new ReceivingAccountDataModel();
            receivingAccountDataModel.setMyTradeNo(requestModel.order_id);
            receivingAccountDataModel.setIsOk(2);
            ComponentUtil.receivingAccountDataService.update(receivingAccountDataModel);
            //组装上游数据
            DataCoreModel dataCoreModel = HodgepodgeMethod.assembleDataCoreBufPay(requestModel, channelDataModel, channelGewayModel, total_amount, serviceCharge, actualMoney, tradeStatus);
            int num = ComponentUtil.dataCoreService.add(dataCoreModel);
            if (num > 0){
                return "OK";
            }else {
                return "no";
            }
        }catch (Exception e){
            Map<String,String> map = ExceptionMethod.getException(e, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
            // #添加异常
            log.error(String.format("this DataCoreController.jh() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            e.printStackTrace();
            return JSON.toJSONString(map);
        }
    }



    /**
     * @Description: 美好的
     * @param response
     * @return com.gd.chain.common.utils.JsonResult<java.lang.Object>
     * @author yoko
     * @date 2019/11/25 22:58
     * local:http://localhost:8085/pay/data/fine
     * http://localhost:8085/pay/data/fine
     *
     * {"total_amount":"100.00","pay_amount":"80.00","out_trade_no":"2020080317100600001","trade_status":1,"trade_no":"test_order_no_sl_1","trade_time":"2020-08-05 20:10:35"}
     * {"total_amount":"100.00","pay_amount":"200.00","out_trade_no":"2020080317080600001","trade_status":1,"trade_no":"test_order_no_dl_1","trade_time":"2020-08-05 20:10:35"}
     * {"total_amount":"200.00","pay_amount":"200.00","out_trade_no":"2020080317040000001","trade_status":1,"trade_no":"test_order_no_yz_1","trade_time":"2020-08-05 20:10:35"}
     */
//    @RequestMapping(value = "/fine", method = {RequestMethod.GET})
//    @GetMapping(value = "fine")
    @RequestMapping(value = "/fine", method = {RequestMethod.POST})
    public String fine(HttpServletRequest request, HttpServletResponse response,@RequestBody RequestFine requestModel) throws Exception{
        String sgid = ComponentUtil.redisIdService.getNewId();
        String cgid = "";
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
            log.error(String.format("this DataCoreController.fine() is error , the cgid=%s and sgid=%s and all data=%s!", cgid, sgid, data));
            e.printStackTrace();
            return JSON.toJSONString(map);
        }
    }

}
