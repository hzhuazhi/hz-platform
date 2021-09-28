package com.hz.platform.master.util;

import com.alibaba.fastjson.JSONObject;
import com.hz.platform.master.core.common.utils.HttpSendUtils;
import com.hz.platform.master.core.common.utils.yibao.SHA256WithRSAUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * @ClassName:
 * @Description: TODO
 * @Author: yoko
 * @Date: $
 * @Version: 1.0
 **/
public class TestYb {
    private static Logger log = LoggerFactory.getLogger(TestYb.class);

    /***请求返回编码KEY***/
    private static final String RETURN_CODE = "code";

    /**商户私钥KEY，用于支付签名（RSA2加密）*/
    private static final String SIGN_PRIVATE_KEY = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQDH9tn2xrgsUi56erI9yAo61PpLptq9NZnYiC1yhPuLITplbmKCwgFWJ1ULQRjZOLVTM2SGTm/2CcI7gVoqmTqmSmiaFWTsQUiE9yYkygbeFza7ludLcDnlCkYmyUNRdG9Pw+C24w/6gPAtyFMMTtu71jSjHG8kTIgw+Q2UpdvGiaHgGbFvzjM63wOQccrMj51nR+9JjjCLi+RLDr6EoYJNKtjNGANr0q+HSQp63m9mAf9mwJHINcxGwZui8GrRQw29lOI2THPKIKI6r35t7d8KqnEGCB2lXgLeAtfSZL/EwJ/vIbl8q8k2/7W9FzVgy0vfD9/dwJ7f6YnH2DIxgiKtAgMBAAECggEAcgseOnkRVu2z5lSNzWCGPRF0gqrxu9GTIYRg7xxa3fmgdXLkKhD9dCZQgW+p/VEzUPVRkquvfRIJvahDRWJIqchsKdyjx35gHr1Z+sdh7U4uAATLeyHRUwhuA/brNsyCI2rigUu0tDjPQj0q6C0dct5y/Mfdt0USs2OHVEzXsD2RiFptmlS7HMCYIUaHYq6DE0ZwO2jzTeWDppkbb6FA9LLxcD+nHkC1Ljf7gFANF4fOA04vcaq3GZfItjuPVAiA5+Xj4O/bN2ZIvMDHbRJTkTXnXd3mGJckrlp8+UdcKUB4kGJS3Xu2bixhj/ZZKzyB/a/1RbuxYch9qYPnjJ7bfQKBgQD3IpYeuZGCfpp+w8pfFqtxOieRq/KDTm8xZRMR+0ca5xAt68mwUOJGpV3sSSGuEnOcXfpTqoN8CdNJYGoDe6XJhQVS7OnhNt7UX7CK6PlF0qHr/qEdpj5G0rszdLTILHEbAfSLMxA6HBbvrArwXjx7a60J27TL0lhgIkJD8mPiJwKBgQDPIxm9mI64KdXRKUpz9JbFrjOfOXq6PUqa1GqvOSNjPeMCsQaa1/uxsHPwKMSdfp4Nz01NaT+LS8RXRffgUD0o7mZX2dlH2XUMY0UAwL5ETnfCYM8gqeDGhROpaJUBh2ORYtMlszXgnE3qqL48jBx3aSIeaYstKCuBO8UBGT8dCwKBgCwxBBUUa7+g8fFvA1YDkL6bfo3e6Guwb9OuVj2l6C4YWV1Mx3D+1ad6VJQ2843UANZaO7QQTpi0jRKW6f4bcybY4X3VnXDHRgtF3STJyiFBK/EuRBMwMfyqHA10dgtBXyxMKjVl+SXa9gL/iak3wKr1MykLH4Y+48y085KA2nmLAoGALAltVoaRiQuz3nTl1EMirsI7TVWeBuwy6TnRuJDwOhAVLCRQoCjc5Mj7zQoNyuH5Xjw9VgDTNp0UD+3bJgBJEGOmtHSnIgRUQx7zs5ZGnSUFhvin8GzNUcdHB9QpIZsUCvu0oiY4hRkV45aeYybyglyCmW6c3SvWG/KGDpa8eUMCgYApnNHBYESxJ7oqQRREQCv6SL0YxUkyXDr6FjtOtstzmpsAk89QFN6JEOQtajSTwlvao+AzbdYKOsxipmaWK6FaLw7ZTlSOqxYly9FYk//QrMxnabnBtaBIqJoPBNQWLH/jhKlyoI3MXgaolJQBBgWUL+tdnJr4CQrtFk4H4AEfHw==";

//    private static final String SIGN_PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJfqoZ9qIY6Y9dwTPXAI9FKpIQU9iumS82LZGMlxcuiffjT9uJtu1Z2c1hlnphO8lQH2b53+zZlejArdFTjzFNaLDmH3nuk4rsmQSH61lXcfkMLrWCFD+HETw4V9Y+Im7b0xhV/WK3sfXKoRy+qAEWxaW/f1Y1HNiB0t8Gv5bDFnAgMBAAECgYB5X8v7uubOsGUWjQS0I2NZO6mXL0FyNqdgXYWRRKQDOZKzMdexz866G5lvOMGW2T/QDp3FBNQtaj3HXDXdh1EojUFYGWN5gkoL9EW3+KUtGFuWEdJ21+GB3/hvWjKijoIcvVBfyLFCWQvScaFxcEu9QIorRvg0gtrLvfGO5sTwuQJBAOehsiLGrfQm7OiIOMMaxezEq5iDCrf/KQQ5S0d3mjinR/fB55g7Ylbl2wFhvSiB25FGA2YDlPhM4YLsHG50tlMCQQCn5gxQy8yHqmSIVcgjX/HrDj0wG2zAFJfoYF/zoFlZnOgI/S45mdtuxadNr5o9MBzAErH1pv6Sa3slG1qdeQ4dAkEA2++4yTSFb9KWGKVf4EHHtW140bN7tgHd3Fjald2G3kxYFXRUh0O7uG9AaCt6BMBoz6P/DhpQlQPLDRlMVcUUaQJBAKCU9Sh9kzQaMyb2LIIw7LctGjadtEiOKbAWJQ9lSK/d00ttoz1yOx5aKqaxkip9jrHh75UM0CSIafJWlDdSpb0CQApfWd09RvHXnDqZe1/sT8qve9fnj7vV49vi4MjbTceQDDSTRhZwLKpkxSsjYT3l5TWbcAnKxn3cXrUsP8vDBj4=";

    /**平台公钥KEY,用于回调验签（RSA2解密）*/
    private static final String SIGN_PUBLIC_KEY = "平台公钥";

    private static class PayContants{
        /**支付回调失败**/
        private static final String FAIL = "fail";

        /**下单请求成功返回code**/
        private static final String SUCCESS_CODE = "1";

        /**支付回调成功**/
        private static final String SUCCESS = "success";

        /**支付商编码 东川支付**/
        private static final String CODE = "XDCZF";

        /**class name**/
        private static final String CLASS_NAME = "XDCZFPayServiceImpl";
    }




    /**
     * @return
     */
    public static void sealRequest()throws Exception {
        Map<String ,String> sendDataMap = new HashMap<>();
        //商户号
        sendDataMap.put("merId","20210920210013");
        //商户订单号
        sendDataMap.put("orderId",String.valueOf(System.currentTimeMillis()));
        sendDataMap.put("orderAmt", "50.00");//支付金额,请求的价格(单位：元) 可以0.01元
        sendDataMap.put("channel", "zfbwap");//通道列表请查看商户后台，或联系商务
        //商品名称，utf-8编码
        sendDataMap.put("desc","TOP-UP");
        sendDataMap.put("ip", "192.168.0.1");//支付用户IP地址,用户支付时设备的IP地址
        sendDataMap.put("notifyUrl", "http://www.qidian.com");//异步通知地址,异步接收支付结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数。
        sendDataMap.put("returnUrl", "http://www.baidu.com");//同步通知地址,支付成功后跳转到的地址，不参与签名。
        sendDataMap.put("nonceStr", String.valueOf(System.currentTimeMillis()));//随机字符串
        String sign = generatorSign(sendDataMap,1);

        sendDataMap.put("sign",sign);
        log.info("--------sendDataMap:" + JSONObject.toJSONString(sendDataMap));
        String resData = HttpSendUtils.doPostForm("http://api.yeepaycdn.org/pay", sendDataMap);
        log.info("--------resData:" + resData);

    }

    /**
     * @param data
     * @return
     * @throws Exception
     * @Description 生成支付签名串
     */
    public static String generatorSign(Map<String, String> data, Integer type) throws Exception {
        log.info(">>>>>>>>>>聚合支付生成支付签名串开始==================START========================入参data:{}",data);
        Map<String, String> sortMap = new TreeMap<>(data);
        StringBuffer sb = new StringBuffer();
        Iterator<String> iterator = sortMap.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String val = sortMap.get(key);
            if (StringUtils.isBlank(val) || key.equalsIgnoreCase("sign")) {
                continue;
            }
            sb.append(key).append("=").append(val).append("&");
        }
        sb.append("key=").append("VSWqIjxmXDJifuowZkYyNQLgCdtlRrsA");
        //生成待签名串
        String singStr = sb.toString();
        log.info(">>>>>>>>>>聚合支付生成待MD5签名串singStr:{}" ,singStr);
        //生成加密串
//        String sign = MD5Utils.md5toUpCase_32Bit(sb.toString());
        String sign = DigestUtils.md5Hex(sb.toString()).toUpperCase();
        log.info(">>>>>>>>>>聚合支付生成MD5签名串sign:{}" , sign);
        if(type==1){//支付签名
//            String rsaSign = SHA256WithRSAUtils.buildRSASignByPrivateKey(sign,SIGN_PRIVATE_KEY);
//            String rsaSign = SHA256WithRSAUtils.buildRSASignByPrivateKey(sign,SIGN_PRIVATE_KEY).replaceAll("/n","").trim();
            String rsaSign = SHA256WithRSAUtils.buildRSASignByPrivateKey(sign,SIGN_PRIVATE_KEY);

            rsaSign= URLEncoder.encode(rsaSign).replaceAll("%2B","");
            log.info(">>>>>>>>>>聚合支付生成RSA2签名串rsaSign:{}" , rsaSign);
            return rsaSign;
        }
        return sign;
    }


    public static void main(String [] args) throws Exception{
        sealRequest();
    }


}
