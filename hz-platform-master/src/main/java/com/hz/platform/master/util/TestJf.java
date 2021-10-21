package com.hz.platform.master.util;

import com.alibaba.fastjson.JSON;
import com.hz.platform.master.core.common.utils.HttpUtil;
import com.hz.platform.master.core.common.utils.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName:
 * @Description: TODO
 * @Author: yoko
 * @Date: $
 * @Version: 1.0
 **/
public class TestJf {
    private static Logger log = LoggerFactory.getLogger(Test.class);


    public static void main(String [] args) throws Exception{
        String order_no = String.valueOf(System.currentTimeMillis());
        String public_key = "ADCMIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJx2ncJ/KQyCIqHWszq0/RypCbW8zYN5aChU8GC5sLaOCWcXkvTsFMZVgT43c2vTjBlSf59yl1JAvPxToMYmYkqcFnA1wbH1PsxSpYznW3xxdLL0RSwyd4Kc/G/GkRv2Myrsv14xNW16ktBMWNpvQ+b4rI7NFGm2gS9MN0/MAAFdAgMBAAECgYAyyFEF6q3REH/bFUtQ16N7ZOXw5ZmyfXov9Pmb+0j+x3UHz9/zvJg1k1UCc47zxVKoeV1QzCmPGdH898ur5JNr7wHDadQmfL9ealZ+jKAZs8NkdDDYCUUnG4NNQ5kCDJJp2DUjrN58WMsOMWmotlsnB2T07R4w0k09IMFiIBSRwQJBAOIO+dZvFvLMLZsFDS6ODkpdkNxCE09rc8qEQ/WdeZWrx26cg+tjo4597aiDeHxJdFwUXKqE4CJKYubJrLBc9jECQQCxL9wAkCMzYmsvrcZqPbn50zEqtg6xFjwvzBVAYcvhHnkcyKyu9O3Im+6b/QZiqpDQOb1w7qoB/4QHYRwm7fbtAkEA1wcNyVZkStHC87krX8RKfZxgxdKoUZxRSSrlBgYj+CWtD18b8NUXZ9wLK4bbHcY1Q70upBi+pn+BciwV8rCKkQJAWdSTy01IK2L4SKHneDF3OitSP/8xD6LgPLNRCuUKAkX6zRcIRWMzsoMrw7XSKO+kn4zGeJznYD/05fDb3vjCOQJAIacE22J+7pczP57cWiyuk6oYuyUFr3VokUHUKFEPr1n67xMRClaCtHVZ8y5WqGXoiyeqXecquN/dCcsnoCweSQ==";
        // 金服-拉单
        Map<String ,String> sendDataMap = new HashMap<>();
        sendDataMap.put("order_no", order_no);
        sendDataMap.put("amount", "50.00");
        sendDataMap.put("bankCrad", "bankCrad_test");
        sendDataMap.put("cardholderName", "cardholderName_test");
        sendDataMap.put("companyCode","SSRJ15683451314");
        sendDataMap.put("timestamp", String.valueOf(System.currentTimeMillis()));
        String sendData = SecurityUtil.map2str(sendDataMap);
        log.info("sendData:" + sendData);
        String mySign = SecurityUtil.sign(public_key, SecurityUtil.map2str(sendDataMap));
        log.info("--------buf--------mySign:" + mySign);
        sendDataMap.put("sign", mySign);
        String parameter = JSON.toJSONString(sendDataMap);
        log.info("请求数据_拉单:" + parameter);
        String resData = HttpUtil.doPostJson("http://testgame.huanyuxingkong.cn:8183/cpg/alipayForPrivate/aliPay_bankCard", parameter);
        log.info("----拉单----resData:" + resData);


        // 金服-查单
        Map<String ,String> sendDataMap_query = new HashMap<>();
//        sendDataMap_query.put("order_no", order_no);
//        sendDataMap_query.put("order_no", "1634717933885");
        sendDataMap_query.put("order_no", "1634717933885111");
        sendDataMap_query.put("companyCode","SSRJ15683451314");
        sendDataMap_query.put("timestamp", String.valueOf(System.currentTimeMillis()));
        String mySign_query = SecurityUtil.sign(public_key, SecurityUtil.map2str(sendDataMap_query));
        log.info("--------buf--------mySign_query:" + mySign_query);
        sendDataMap_query.put("sign", mySign_query);
        String parameter_query = JSON.toJSONString(sendDataMap_query);
        log.info("请求数据_查单:" + parameter_query);
        String resData_query = HttpUtil.doPostJson("http://testgame.huanyuxingkong.cn:8183/cpg/alipayForPrivate/queryOrder", parameter_query);
        log.info("----查单----resData_query:" + resData_query);



        // 金服-查余额
        Map<String ,String> sendDataMap_balance = new HashMap<>();
        sendDataMap_balance.put("companyCode","SSRJ15683451314");
        sendDataMap_balance.put("timestamp", String.valueOf(System.currentTimeMillis()));
        String mySign_balance = SecurityUtil.sign(public_key, SecurityUtil.map2str(sendDataMap_balance));
        log.info("--------buf--------mySign_balance:" + mySign_balance);
        sendDataMap_balance.put("sign", mySign_balance);
        String parameter_balance = JSON.toJSONString(sendDataMap_balance);
        log.info("请求数据_查余额:" + parameter_balance);
        String resData_balance = HttpUtil.doPostJson("http://testgame.huanyuxingkong.cn:8183/cpg/alipayForPrivate/queryBalance", parameter_balance);
        log.info("----查余额----resData_balance:" + resData_balance);


    }


}
