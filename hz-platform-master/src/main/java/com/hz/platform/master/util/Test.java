package com.hz.platform.master.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hz.platform.master.core.common.huichaopay.RSAutil;
import com.hz.platform.master.core.common.utils.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @ClassName: Test
 * @Description: TODO
 * @Author: yoko
 * @Date: $
 * @Version: 1.0
 **/
public class Test {
    private static Logger log = LoggerFactory.getLogger(Test.class);

    public static void main(String [] args) throws Exception{

//        // 狮子支付
//        Map<String ,Object> sendDataMap = new HashMap<>();
//        sendDataMap.put("merchant_code", "10040");
//        sendDataMap.put("order_id", String.valueOf(System.currentTimeMillis()));
//        sendDataMap.put("merchant_time", DateUtil.getNowPlusTime());
//        sendDataMap.put("paytype", "202");
//        sendDataMap.put("subject", "钻石");
//        sendDataMap.put("body", "打赏");
//        sendDataMap.put("amount", "50.00");
//        sendDataMap.put("returnUrl", "http://www.baidu.com");
//        sendDataMap.put("notify_url", "http://www.qidian.com");
//
//
//        String mySign = "merchant_code=" + sendDataMap.get("merchant_code") + "&" + "order_id=" + sendDataMap.get("order_id") + "&" + "merchant_time=" + sendDataMap.get("merchant_time") + "&" + "paytype=" + sendDataMap.get("paytype")
//                + "&" + "subject=" + sendDataMap.get("subject") + "&" + "body=" + sendDataMap.get("body") + "&" + "amount=" + sendDataMap.get("amount") + "&" + "returnUrl=" + sendDataMap.get("returnUrl") + "&" + "notify_url=" + sendDataMap.get("notify_url")
//                + "65d8d9072f6039dac2407c265d71a281";
//        log.info("--------buf--------data:" + mySign);
////        mySign = MD5Util.encryption(mySign).toLowerCase();
//        mySign = ASCIISort.getSign(sendDataMap, "65d8d9072f6039dac2407c265d71a281", 1);
//        log.info("--------buf--------mySign:" + mySign);
//        sendDataMap.put("sign", mySign);
//        String parameter = JSON.toJSONString(sendDataMap);
//
//        String resData = HttpSendUtils.sendPostAppJson("http://shizi.nanjintm.com/pay/unifiedorder", parameter);
//        System.out.println(resData);

//        // 木星支付
//        Map<String ,Object> sendDataMap = new HashMap<>();
//        sendDataMap.put("pay_memberid", "10095");
//        sendDataMap.put("pay_orderid", String.valueOf(System.currentTimeMillis()));
//        sendDataMap.put("pay_amount", "66700");
//        sendDataMap.put("pay_callbackurl", "http://www.qidian.com");
//        sendDataMap.put("pay_turnyurl", "http://www.baidu.com");
//        sendDataMap.put("pay_productname", "zs");
//        sendDataMap.put("pay_tradetype", "9001");
//
//
//
//        String mySign = ASCIISort.getKeySign(sendDataMap, "c3e8fd2d5d3388f5bc0de50735a1bc89", 2);
//        log.info("--------buf--------mySign:" + mySign);
//        sendDataMap.put("signature", mySign);
//        String parameter = JSON.toJSONString(sendDataMap);
//
//        String resData = HttpSendUtils.doPostForm("http://kang.uhgincherkgjinga3.top/pay_index.html", sendDataMap);
//        log.info("--------resData:" + resData);
//
//        Map<String, Object> resMap = new HashMap<>();
//        if (!StringUtils.isBlank(resData)) {
//            resMap = JSON.parseObject(resData, Map.class);
//            if (Integer.parseInt(resMap.get("code").toString()) == 11) {
//                String qrCodeUrl = (String) resMap.get("codeUrl");
//                log.info("qrCodeUrl:" + qrCodeUrl);
//            }
//        }


//        // 木星支付
//        Map<String ,Object> sendDataMap = new HashMap<>();
//        sendDataMap.put("pay_memberid", "210736387");
//        sendDataMap.put("pay_orderid", String.valueOf(System.currentTimeMillis()));
//        sendDataMap.put("pay_applydate", DateUtil.getNowPlusTime());
//        sendDataMap.put("pay_bankcode", "938");
//        sendDataMap.put("pay_notifyurl", "http://www.baidu.com");
//        sendDataMap.put("pay_callbackurl", "http://www.qidian.com");
//        sendDataMap.put("pay_amount", "200.00");
//
//
//
//        String mySign = ASCIISort.getKeySign(sendDataMap, "i43mmr6r68eabgh1wbtnpwxh6map8tsk", 2);
//        log.info("--------buf--------mySign:" + mySign);
//        sendDataMap.put("pay_type", "json");
//        sendDataMap.put("pay_md5sign", mySign);
//        String parameter = JSON.toJSONString(sendDataMap);
//
//        String resData = HttpSendUtils.doPostForm("https://www.dxyu.cn/Pay_Index.html", sendDataMap);
//        log.info("--------resData:" + resData);
//        Map<String, Object> resMap = new HashMap<>();
//        if (!StringUtils.isBlank(resData)) {
//            resMap = JSON.parseObject(resData, Map.class);
//            if (resMap.get("data") != null) {
//                if (!StringUtils.isBlank(resMap.get("data").toString())){
//                    String qrCodeUrl = (String) resMap.get("data");
//                    resData = "ok";
//                }
//
//            }
//        }


//        String str = StringUtil.getBigDecimalSubtractStr("100.00", "100");
//        log.info("flag:" + str);




//        // 金木星支付
//        Map<String ,Object> sendDataMap = new HashMap<>();
//        sendDataMap.put("mch_id", "40");
//        sendDataMap.put("pass_code", "111");
//        sendDataMap.put("subject", "钻石");
//        sendDataMap.put("out_trade_no", String.valueOf(System.currentTimeMillis()));
//        sendDataMap.put("money", "200.00");
//        sendDataMap.put("client_ip", "192.168.0.1");
//        sendDataMap.put("notify_url", "http://www.baidu.com");
//        sendDataMap.put("return_url", "http://www.qidian.com");
//        sendDataMap.put("timestamp", DateUtil.getNowPlusTime());
//
//
//
//
//        String mySign = ASCIISort.getKeySign(sendDataMap, "9cbafd157fa24ce2a6a1f30b04342e65", 2);
//        log.info("--------buf--------mySign:" + mySign);
//        sendDataMap.put("sign", mySign);
//        String parameter = JSON.toJSONString(sendDataMap);
//
//        String resData = HttpSendUtils.sentPost("http://www.jinmuxing.vip/api/mch/unifiedorder", parameter);
//        log.info("--------resData:" + resData);
//        Map<String, Object> resMap = new HashMap<>();
//        if (!StringUtils.isBlank(resData)) {
//            resMap = JSON.parseObject(resData, Map.class);
//            if (Integer.parseInt(resMap.get("code").toString()) == 0){
//                if (resMap.get("data") != null) {
//                    if (!StringUtils.isBlank(resMap.get("data").toString())){
//                        Map<String, Object> resDataMap = new HashMap<>();
//                        resDataMap = JSON.parseObject(resMap.get("data").toString(), Map.class);
//                        String qrCodeUrl = (String) resDataMap.get("pay_url");
//                        resData = "ok";
//                        log.info("qrCodeUrl:" + qrCodeUrl);
//                    }
//
//                }
//            }
//        }




//        // 高防支付
//        Map<String ,Object> sendDataMap = new HashMap<>();
//        sendDataMap.put("channel", "20101");
//        sendDataMap.put("trade_type", "600002");
//        sendDataMap.put("total_amount", "50.00");
//        sendDataMap.put("out_trade_no", String.valueOf(System.currentTimeMillis()));
//        sendDataMap.put("notify_url", "http://www.baidu.com");
//        sendDataMap.put("interface_ver", "4.0");
//        sendDataMap.put("return_url", "http://www.qidian.com");
//        sendDataMap.put("noredirect", "1");
//
//        String mySign = "channel=" + sendDataMap.get("channel") + "&" + "trade_type=" + sendDataMap.get("trade_type") + "&" + "total_amount=" + sendDataMap.get("total_amount")
//                + "&" + "out_trade_no=" + sendDataMap.get("out_trade_no") + "&" + "notify_url=" + sendDataMap.get("notify_url") + "&" + "key=" + "1ffaf89a96af3f6b27d77e39a42cd867";
//        mySign = MD5Util.encryption(mySign);
//
//        sendDataMap.put("sign", mySign);
//        String parameter = JSON.toJSONString(sendDataMap);
//
//        String resData = HttpUtil.doPostJson("http://155.235.245.43:8092/platform/action/dataCore", parameter);
//        log.info("--------resData:" + resData);
//        if (!StringUtils.isBlank(resData) && resData.indexOf("code") <= -1) {
//            byte[] decoded = Base64.getDecoder().decode(resData);
//            String decodeStr = new String(decoded);
//            String qrCodeUrl = decodeStr;
//            resData = "ok";
//            log.info("qrCodeUrl:" + qrCodeUrl);
//        }




//        // 小肥龙支付
//        Map<String ,Object> sendDataMap = new HashMap<>();
//        sendDataMap.put("mchId", "20000102");
//        sendDataMap.put("appId", "8df9fea55dd5459babe85c97b31ff591");
//        sendDataMap.put("productId", "8028");// 8029
//        sendDataMap.put("mchOrderNo", String.valueOf(System.currentTimeMillis()));
//        sendDataMap.put("amount", "5000");
//        sendDataMap.put("currency", "cny");
//        sendDataMap.put("clientIp", "192.168.0.1");
//        sendDataMap.put("device", "ios10.3.1");
//        sendDataMap.put("notifyUrl", "http://www.qidian.com");
//        sendDataMap.put("returnUrl", "http://www.baidu.com");
//        sendDataMap.put("subject", "打赏");
//        sendDataMap.put("body", "钻石");
//        sendDataMap.put("reqTime", DateUtil.getNowPlusTimeMill());
//        sendDataMap.put("version", "1.0");
//
//
//
//
//        String mySign = ASCIISort.getKeySign(sendDataMap, "7cJmBXSiYisQyeRTXTMXpTMFpfOrQ8tpNY3OYmPOExoZPJ9qivNTpHrX1XsnMfxBb6scb4kpTfopq3NLcdTNf9YPUOiuowP5BVB8mzLxyBnUlOBIUdAanYvMobvLn69g", 2);
//        log.info("--------xfl--------mySign:" + mySign);
//        sendDataMap.put("sign", mySign);
//        String parameter = JSON.toJSONString(sendDataMap);
//
//        String resData = HttpSendUtils.doPostForm("https://api.xflvpay.com/api/pay/create_order", sendDataMap);
//        log.info("--------resData:" + resData);
//
//        Map<String, Object> resMap = new HashMap<>();
//        if (!StringUtils.isBlank(resData)) {
//            resMap = JSON.parseObject(resData, Map.class);
//            if (resMap.get("retCode").equals("0")) {
//                String qrCodeUrl = (String) resMap.get("payJumpUrl");
//                log.info("qrCodeUrl:" + qrCodeUrl);
//            }
//        }




//        // 思维付支付
//        Map<String ,Object> sendDataMap = new HashMap<>();
//        sendDataMap.put("memberid", "M20210803143126091673");
//        sendDataMap.put("backurl", "http://www.qidian.com");
//        sendDataMap.put("webbackurl", "http://www.baidu.com");
//        sendDataMap.put("clienturl", "http://www.google.com");
//        sendDataMap.put("type", "202");// 303，202
//        sendDataMap.put("body", "钻石");
//        sendDataMap.put("amount", "5000");
//        sendDataMap.put("ordernum", String.valueOf(System.currentTimeMillis()));
//        sendDataMap.put("openid", "openid");
//        sendDataMap.put("appid", "appid");
//        sendDataMap.put("key", "bZzgQWm0AuOFFoen9jSDew==");
//        sendDataMap.put("code", "code");
//
//
//
//
//        String mySign = ASCIISort.getSignNoKey(sendDataMap,  1);
//        mySign = StringUtil.mergeCodeBase64(mySign);
//        log.info("--------swf--------mySign:" + mySign);
//        sendDataMap.put("sign", mySign);
//
//        sendDataMap.remove("key");
//        String parameter = JSON.toJSONString(sendDataMap);
//
//
//        String resData = HttpSendUtils.doPostForm("https://pay.sweetgo.top/payserver/Unifiedorder", sendDataMap);
//        log.info("--------resData:" + resData);
//        resData = "{\"clienturl\":\"http://www.google.com\",\"imgurl\":\"https://pay.sweetgo.top/content/Code_Images/PayCodeQr/1627989664253.png\",\"msg\":\"\\u4EA4\\u6613\\u6210\\u529F\",\"orderid\":\"14431552\",\"state\":\"1\",\"url\":\"https://pay.sweetgo.top/payserver/XtbankAlipayH5?data=mtE8vHTaxl9WWY8UNKBHSnXOXlEd57ZkZd61JQ3H4CdE1Cepew3Dhtxsjdyahk7D16VzNcrX5SuIZohojEpI/FVU4VQSEr/C35+D9Kk36Zk0FPE170TKaLHKvGUAXRRaIU0LCSDIxRiLVNEz5TZmAo0yh9yMECr+PgjI2UXWsErh/SW+pFSH3CRjO6C8PcCtMT2P6cAzRf46kxvP1LwIpZgXCFdK4YRjt5MbLeovxPUI411ChPpPCw==\"}";
//        Map<String, Object> resMap = new HashMap<>();
//        if (!StringUtils.isBlank(resData)) {
//            resMap = JSON.parseObject(resData, Map.class);
//            boolean containsKey = resMap.containsKey("state");
//            if (containsKey){
//                if (resMap.get("state").equals("1")) {
//                    String qrCodeUrl = (String) resMap.get("url");
//                    log.info("qrCodeUrl:" + qrCodeUrl);
//                }
//            }
//            log.info("containsKey:"+containsKey);
//
//        }




//        // 花呗通支付
//        Map<String ,Object> sendDataMap = new HashMap<>();
//        sendDataMap.put("p1_merchantno", "MER20210817174926578869");
//        sendDataMap.put("p2_amount", "50.00");
//        sendDataMap.put("p3_orderno", String.valueOf(System.currentTimeMillis()));
//        sendDataMap.put("p4_paytype", "WechatScan");
////        sendDataMap.put("p4_paytype", "WechatH5_808");
//        sendDataMap.put("p5_reqtime", DateUtil.getNowLongTime());
//        sendDataMap.put("p6_goodsname", "钻石");
//        sendDataMap.put("p8_returnurl", "http://www.baidu.com");
//        sendDataMap.put("p9_callbackurl", "http://www.qidian.com");
//
//
//
//        String mySign = ASCIISort.getKeySign(sendDataMap, "a5e17415978f431fbd19fe42411ef4be", 2);
//        log.info("--------buf--------mySign:" + mySign);
//        sendDataMap.put("sign", mySign);
//        String parameter = JSON.toJSONString(sendDataMap);
//
//        String resData = HttpSendUtils.doPostForm("https://api.huabeitong.net/pay", sendDataMap);
//        log.info("--------resData:" + resData);
//
//        Map<String, Object> resMap = new HashMap<>();
//        if (!StringUtils.isBlank(resData)) {
//            resMap = JSON.parseObject(resData, Map.class);
//            if (resMap.get("rspcode").equals("A0")) {
//                String qrCodeUrl = (String) resMap.get("data");
//                log.info("qrCodeUrl:" + qrCodeUrl);
//            }
//        }

//        // 易宝支付
//        Map<String ,Object> sendDataMap = new HashMap<>();
//        sendDataMap.put("merId", "20210920210013");
//        sendDataMap.put("orderId", String.valueOf(System.currentTimeMillis()));
//        sendDataMap.put("orderAmt", "50.00");
//        sendDataMap.put("channel", "zfbwap");//通道列表请查看商户后台，或联系商务
//        //商品名称，utf-8编码
//        sendDataMap.put("desc","TOP-UP");
//        sendDataMap.put("ip", "192.168.0.1");//支付用户IP地址,用户支付时设备的IP地址
//        sendDataMap.put("notifyUrl", "http://www.qidian.com");//异步通知地址,异步接收支付结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数。
//        sendDataMap.put("returnUrl", "http://www.baidu.com");//同步通知地址,支付成功后跳转到的地址，不参与签名。
//        sendDataMap.put("nonceStr", String.valueOf(System.currentTimeMillis()));//随机字符串
//
//
//
//        String mySign = ASCIISort.getKeySign(sendDataMap, "VSWqIjxmXDJifuowZkYyNQLgCdtlRrsA", 2);
//        log.info("--------buf--------mySign:" + mySign);
//        sendDataMap.put("sign", mySign);
//        String parameter = JSON.toJSONString(sendDataMap);
//
//        String resData = HttpSendUtils.doPostForm("http://api.yeepaycdn.org/pay", sendDataMap);
//        log.info("--------resData:" + resData);
//        // {"code":1,"msg":"请求成功!","time":"1631608831","data":{"payurl":"https:\/\/openapi.alipay.com\/gateway.do?alipay_sdk=alipay-sdk-php-20161101&app_id=2021002141614154&biz_content=%7B%22out_trade_no%22%3A%221631608831352926837%22%2C%22total_amount%22%3A%2250.00%22%2C%22subject%22%3A%22S%E5%BD%A2%E5%B1%95%E6%9D%BF%E7%B3%BB%E5%88%97-%E5%BC%A7%E5%BD%A2S+%E5%BF%AB%E5%B9%95%E7%A7%80S%E5%BD%A2%E8%9B%87%E5%BD%A2%E5%B1%95%E6%9E%B6+%E8%BD%BB%E4%BE%BF%E7%94%BB%E9%9D%A2%E5%BF%AB%E5%B1%95%E7%A4%BA%E6%9E%B6+%E5%95%86%E5%9C%BA%E4%BF%83%E9%94%80%E5%B1%95%E5%85%B7%22%2C%22product_code%22%3A%22FAST_INSTANT_TRADE_PAY%22%7D&charset=UTF-8&format=json&method=alipay.trade.wap.pay&notify_url=http%3A%2F%2Fapi.yeepaycdn.org%2FPay%2Fnotify%2Fcode%2FAlipay%2Forderno%2F1631608831352926837&return_url=http%3A%2F%2Fapi.yeepaycdn.org%2FPay%2Fbackurl%2Fcode%2FAlipay%2Forderno%2F1631608831352926837&sign=decuRtyil5QddjoKUNhXmbIAj0lyKu0bWp%2B%2B%2Be6vEFEAF29Yt4WSZeVHNO2ZUz9JMsXA51SjGJ%2BVBDPqifLTLXD6NCYpU1ICWraKBoU9qJieXnC6ao6q3clxpFOmJTLixJPpcyn4%2Bl9%2BjXDGheOwb8lW0K%2F2I4KmwH5GwUvsMJkAREw%2BkNQi87NZp1fjrGgsM4c%2BqbdiuBoUSK9o5j52BxS7O0bjRvZtEFKhe8bL9lXZ4jrjkJLIPOdnDsjyW8v813Y2hE2Ea5hL9isGOrq7LuCmzE4NxpWwVy7uBjugK6YnYf3I8OvdaDImxiRidvduGPJQhDTjqhFN5%2B5dRkUlpQ%3D%3D&sign_type=RSA2&timestamp=2021-09-14+16%3A40%3A31&version=1.0","orderno":"1631608834702","sysorderno":"1631608831352926837"}}
//
//        Map<String, Object> resMap = new HashMap<>();
//        resMap = JSON.parseObject(resData, Map.class);
//        boolean containsKey = resMap.containsKey("code");
//        if (containsKey) {
//            if (Integer.parseInt(resMap.get("code").toString()) == 1) {
//                if (resMap.get("data") != null) {
//                    if (!StringUtils.isBlank(resMap.get("data").toString())){
//                        Map<String, Object> resDataMap = new HashMap<>();
//                        resDataMap = JSON.parseObject(resMap.get("data").toString(), Map.class);
//                        String qrCodeUrl = (String) resDataMap.get("payurl");
//                        resData = "ok";
//                        log.info("qrCodeUrl:" + qrCodeUrl);
//                    }
//                }
//            }
//        }




//        // 芒果支付
//        Map<String ,Object> sendDataMap = new HashMap<>();
//        sendDataMap.put("pay_memberid", "10296");
//        sendDataMap.put("pay_orderid", String.valueOf(System.currentTimeMillis()));
//        sendDataMap.put("pay_applydate", DateUtil.getNowPlusTime()); // 提交时间- 时间格式：2016-12-26 18:18:18
//        sendDataMap.put("pay_bankcode", "1008");// 银行编码
//        sendDataMap.put("pay_notifyurl", "http://www.qidian.com");// 服务端通知 服务端返回地址.（POST返回数据）
//        sendDataMap.put("pay_callbackurl", "http://www.baidu.com");// 页面跳转通知
//        sendDataMap.put("pay_amount", "50.00"); // 订单金额
//
//
//
//        String mySign = ASCIISort.getKeySign(sendDataMap, "j2qjykyt2lz5hpkcmfek8eams4f99bi4", 2);
//        log.info("--------buf--------mySign:" + mySign);
//        sendDataMap.put("pay_md5sign", mySign);
//        sendDataMap.put("content_type", "json");
//
//        String parameter = JSON.toJSONString(sendDataMap);
//
//        String resData = HttpSendUtils.doPostForm("http://api.qdxzdz.com/Pay_Index.html", sendDataMap);
////        String resData = HttpUtil.doPostJson("http://api.qdxzdz.com/Pay_Index.html", parameter);
//
//        log.info("--------resData:" + resData);
//        // {"code":1,"msg":"请求成功!","time":"1631608831","data":{"payurl":"https:\/\/openapi.alipay.com\/gateway.do?alipay_sdk=alipay-sdk-php-20161101&app_id=2021002141614154&biz_content=%7B%22out_trade_no%22%3A%221631608831352926837%22%2C%22total_amount%22%3A%2250.00%22%2C%22subject%22%3A%22S%E5%BD%A2%E5%B1%95%E6%9D%BF%E7%B3%BB%E5%88%97-%E5%BC%A7%E5%BD%A2S+%E5%BF%AB%E5%B9%95%E7%A7%80S%E5%BD%A2%E8%9B%87%E5%BD%A2%E5%B1%95%E6%9E%B6+%E8%BD%BB%E4%BE%BF%E7%94%BB%E9%9D%A2%E5%BF%AB%E5%B1%95%E7%A4%BA%E6%9E%B6+%E5%95%86%E5%9C%BA%E4%BF%83%E9%94%80%E5%B1%95%E5%85%B7%22%2C%22product_code%22%3A%22FAST_INSTANT_TRADE_PAY%22%7D&charset=UTF-8&format=json&method=alipay.trade.wap.pay&notify_url=http%3A%2F%2Fapi.yeepaycdn.org%2FPay%2Fnotify%2Fcode%2FAlipay%2Forderno%2F1631608831352926837&return_url=http%3A%2F%2Fapi.yeepaycdn.org%2FPay%2Fbackurl%2Fcode%2FAlipay%2Forderno%2F1631608831352926837&sign=decuRtyil5QddjoKUNhXmbIAj0lyKu0bWp%2B%2B%2Be6vEFEAF29Yt4WSZeVHNO2ZUz9JMsXA51SjGJ%2BVBDPqifLTLXD6NCYpU1ICWraKBoU9qJieXnC6ao6q3clxpFOmJTLixJPpcyn4%2Bl9%2BjXDGheOwb8lW0K%2F2I4KmwH5GwUvsMJkAREw%2BkNQi87NZp1fjrGgsM4c%2BqbdiuBoUSK9o5j52BxS7O0bjRvZtEFKhe8bL9lXZ4jrjkJLIPOdnDsjyW8v813Y2hE2Ea5hL9isGOrq7LuCmzE4NxpWwVy7uBjugK6YnYf3I8OvdaDImxiRidvduGPJQhDTjqhFN5%2B5dRkUlpQ%3D%3D&sign_type=RSA2&timestamp=2021-09-14+16%3A40%3A31&version=1.0","orderno":"1631608834702","sysorderno":"1631608831352926837"}}
//
//        Map<String, Object> resMap = new HashMap<>();
//        resMap = JSON.parseObject(resData, Map.class);
//        boolean containsKey = resMap.containsKey("code");
//        if (containsKey) {
//            if (Integer.parseInt(resMap.get("code").toString()) == 200) {
//                if (!StringUtils.isBlank(resMap.get("payurl").toString())){
//                    String qrCodeUrl = (String) resMap.get("payurl");
//                    resData = "ok";
//                    log.info("qrCodeUrl:" + qrCodeUrl);
//                }
//            }
//        }



//        // 东风支付
//        Map<String ,Object> sendDataMap = new HashMap<>();
//        sendDataMap.put("mchId", "5555");// 商户号
//        sendDataMap.put("amount", "50.00"); // 支付金额/元
//        sendDataMap.put("bankCode", "5");// 微信扫码=2, 微信H5=3,支付宝=4,支付宝H5=5 网关代付=7 sdk支付宝=13
//        String a = String.valueOf(new Random().nextInt(9));
//        String b = String.valueOf(new Random().nextInt(9));
//        String c = String.valueOf(new Random().nextInt(9));
//        String d = String.valueOf(new Random().nextInt(9));
//        String e = String.valueOf(new Random().nextInt(9));
//        String f = String.valueOf(new Random().nextInt(9));
//        String g = String.valueOf(new Random().nextInt(9));
//        String temStr = "5" + g + "."+ f + a + ".2"+ b + c + ".2" + d + e;
//        sendDataMap.put("clientIp", temStr); // 主要防止 cc 恶意请求，以及匹配收款商户，不传则无法支付（不能传局域网 IP 不能是国外 IP，不能是服务器 IP）
//        sendDataMap.put("goodsName", "钻石");// 异步通知平台更新数据
//        sendDataMap.put("notiryUrl", "http://www.qidian.com");// 异步通知平台更新数据
//        sendDataMap.put("orderNo", String.valueOf(System.currentTimeMillis()));// 商户生成的订单编号
//        sendDataMap.put("payTime", DateUtil.getNowLongTime()); // 提交时间，格式：yyyyMMddHHmmss
//        sendDataMap.put("returnUrl", "http://www.baidu.com");// 同步通知 Url
//
//
//
//        String mySign = ASCIISort.getSign(sendDataMap,"f005635f08985e9f86141532919fb007", 2);
//        log.info("--------buf--------mySign:" + mySign);
//        sendDataMap.put("sign", mySign);
//
//        String parameter = JSON.toJSONString(sendDataMap);
//
//        String resData = HttpSendUtils.doPostForm("http://103.195.50.217/gatewayjson", sendDataMap);
//
//        log.info("--------resData:" + resData);
//        // {"RState":"1","RMsg":null,"Data":"http://www.payalibaba.com/pay/89313141120327680"}
//
//        Map<String, Object> resMap = new HashMap<>();
//        resMap = JSON.parseObject(resData, Map.class);
//        boolean containsKey = resMap.containsKey("RState");
//        if (containsKey) {
//            if (resMap.get("RState").equals("1")) {
//                if (!StringUtils.isBlank(resMap.get("Data").toString())){
//                    String qrCodeUrl = (String) resMap.get("Data");
//                    resData = "ok";
//                    log.info("qrCodeUrl:" + qrCodeUrl);
//                }
//            }
//        }



//        // 汇潮支付
//        Map<String ,String> sendDataMap = new HashMap<>();
//        sendDataMap.put("MerNo", "51655");// 10296
//        sendDataMap.put("BillNo", String.valueOf(System.currentTimeMillis()));// 订单号
//        sendDataMap.put("Amount", "50.00"); // 订单金额
//        sendDataMap.put("ReturnURL", "http://www.baidu.com");// 页面跳转同步通知页面:支付完成后web页面跳转显示支付结果
//        sendDataMap.put("AdviceURL", "http://www.qidian.com");// 服务器异步通知路径:汇潮服务器主动异步通知商户网站指定的路径
//        sendDataMap.put("OrderTime", DateUtil.getNowLongTime()); // 提交时间- 时间格式：2016-12-26 18:18:18
//        sendDataMap.put("payType", "B2CDebit");// 支付方式:可传入参数：B2CCredit：B2C信用卡；B2CDebit:B2C借记卡；noCard：银联快捷支付UnionScanPay_H5： 云闪付H5借记卡支付方式；UnionScanCreditPay_H5：贷记卡manualPay：B2B转账B2B：企业网银支付Installment：分期支付
//
//        String key = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIO00l3ZGn8HF9wRnhutpXv707TBJFWhFMsWSFOkVTUl9nBS5v8sVsVwMFYgmE2RrRrYAnLtSgH2da+IZCPHwaI3BpsUN2HvAv7bZhngCczEikR7I9YHxtKjR8QIWQW9aDfQlcPmMNvNSCcPlScxSqmx5ZspPhSNUSCUiNS+56O3AgMBAAECgYA8FBTN+IXMoiixG1w7Fffh2ZrV3jC72tHIXi658MFpkBqdXEPA7LHcOHPkJdQzthr3nsdnM3TJ9mnym03KwIlDziYvrkf8hwSNSE5Ov9bAz38IlqgSHvBfYjnmOZ5GlvmF2kYXq5yUGpb08iUFpux4FwsAX2ePbW1EUGzbeqofQQJBAPVmJtalVWnfc8QwO1AqiAzLSXDNemeVLZ2h5taFQrbVjJulYbfwGeXro9TWSF66bfV5yRUyvFT0yyE9fqFMytcCQQCJZVoB0ZCILZpGTbWK4k2G+rV9609YjJEonAVcVMZYmZBPqmEEwvk2c5g+MmZR9WrQDCgIhMhDNodtrxDByLIhAkBrclG5E+UlGSCgGxotTKILMAs059MbfXbemR/wd6KxWSlakPwLRIaiZB10uGoxh+FTZQKFVJSfghtar4k9aNk7AkBEA3Hs2IqNftWR28H0gFYUbWMOdD+Q+/SXf7R/ok+VaF8xsIvaSZIITGyezWAtTimT15CxttlTvFiKCVFoPCtBAkActYlolXcT6rf2ZRNbjKf7XZDhJZxfQHYiXztYDPjjB5FSDtIyjIaNHSbCbrk1qWWkSDDka+iXN4VAR7R16rEg";
//        String strContent = "MerNo=" + sendDataMap.get("MerNo") + "&BillNo=" + sendDataMap.get("BillNo") + "&Amount=" + sendDataMap.get("Amount") + "&OrderTime=" + sendDataMap.get("OrderTime") +
//                "&ReturnURL=" + sendDataMap.get("ReturnURL") + "&AdviceURL=" + sendDataMap.get("AdviceURL");
//        log.info("strContent:" + strContent);
//        String mySign = SecurityUtil.sign( key, strContent);
//        log.info("--------buf--------mySign:" + mySign);
//        sendDataMap.put("SignInfo", mySign);
//
//        String resData = HttpSendUtils.doPostFormByHeader("https://gwapi.yemadai.com/pay/sslpayment", sendDataMap);
//        log.info("--------resData:" + resData);


    }
}
