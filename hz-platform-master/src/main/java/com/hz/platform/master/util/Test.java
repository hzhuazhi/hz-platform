package com.hz.platform.master.util;

import com.alibaba.fastjson.JSON;
import com.hz.platform.master.core.common.utils.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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


        String str = StringUtil.getBigDecimalSubtractStr("100.00", "100");
        log.info("flag:" + str);
    }
}
