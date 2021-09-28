package com.hz.platform.master.util;

import com.alibaba.fastjson.JSON;
import com.hz.platform.master.core.common.utils.HttpUtil;
import com.hz.platform.master.core.common.utils.MD5Util;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName:
 * @Description: TODO
 * @Author: yoko
 * @Date: $
 * @Version: 1.0
 **/
public class GfTest {
    private static Logger log = LoggerFactory.getLogger(GfTest.class);


    public static void main(String [] args){
        // 高防支付
        Map<String ,Object> sendDataMap = new HashMap<>();
        sendDataMap.put("channel", "10101");
        sendDataMap.put("trade_type", "111");
        sendDataMap.put("total_amount", "50.00");
        sendDataMap.put("out_trade_no", String.valueOf(System.currentTimeMillis()));
        sendDataMap.put("notify_url", "http://www.baidu.com");
        sendDataMap.put("interface_ver", "4.0");
        sendDataMap.put("return_url", "http://www.qidian.com");
        sendDataMap.put("noredirect", "1");

        String mySign = "channel=" + sendDataMap.get("channel") + "&" + "trade_type=" + sendDataMap.get("trade_type") + "&" + "total_amount=" + sendDataMap.get("total_amount")
                + "&" + "out_trade_no=" + sendDataMap.get("out_trade_no") + "&" + "notify_url=" + sendDataMap.get("notify_url") + "&" + "key=" + "d27b23d1e5094611096c519bfffe233f";
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
