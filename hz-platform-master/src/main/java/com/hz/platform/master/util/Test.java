package com.hz.platform.master.util;

import com.alibaba.fastjson.JSON;
import com.hz.platform.master.core.common.utils.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

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

//        Map<String ,Object> sendDataMap = new HashMap<>();
//        sendDataMap.put("merchant_code", "10040");
//        sendDataMap.put("order_id", String.valueOf(System.currentTimeMillis()));
//        sendDataMap.put("merchant_time", DateUtil.getNowPlusTime());
//        sendDataMap.put("paytype", "501");
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
//        String resData = HttpSendUtils.sendPostAppJson("http://ff3.nanjintm.com/pay/unifiedorder", parameter);
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




        // 高防支付
        Map<String ,Object> sendDataMap = new HashMap<>();
        sendDataMap.put("channel", "20101");
        sendDataMap.put("trade_type", "600002");
        sendDataMap.put("total_amount", "50.00");
        sendDataMap.put("out_trade_no", String.valueOf(System.currentTimeMillis()));
        sendDataMap.put("notify_url", "http://www.baidu.com");
        sendDataMap.put("interface_ver", "4.0");
        sendDataMap.put("return_url", "http://www.qidian.com");
        sendDataMap.put("noredirect", "1");

        String mySign = "channel=" + sendDataMap.get("channel") + "&" + "trade_type=" + sendDataMap.get("trade_type") + "&" + "total_amount=" + sendDataMap.get("total_amount")
                + "&" + "out_trade_no=" + sendDataMap.get("out_trade_no") + "&" + "notify_url=" + sendDataMap.get("notify_url") + "&" + "key=" + "1ffaf89a96af3f6b27d77e39a42cd867";
        mySign = MD5Util.encryption(mySign);

        sendDataMap.put("sign", mySign);
        String parameter = JSON.toJSONString(sendDataMap);

        String resData = HttpUtil.doPostJson("http://155.235.245.43:8092/platform/action/dataCore", parameter);
        log.info("--------resData:" + resData);
        if (!StringUtils.isBlank(resData) && resData.indexOf("code") <= -1) {
            byte[] decoded = Base64.getDecoder().decode(resData);
            String decodeStr = new String(decoded);
            String qrCodeUrl = decodeStr;
            resData = "ok";
            log.info("qrCodeUrl:" + qrCodeUrl);
        }








    }
}
