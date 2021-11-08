package com.hz.platform.master.util;

import com.hz.platform.master.core.common.utils.DateUtil;
import com.hz.platform.master.core.common.utils.HttpSendUtils;
import com.hz.platform.master.core.common.utils.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yoko
 * @desc
 * @create 2021-11-04 14:39
 **/
public class TestHc {
    private static Logger log = LoggerFactory.getLogger(Test.class);

    public static void main(String [] args) throws Exception{

        // 汇潮支付
        Map<String ,String> sendDataMap = new HashMap<>();
        sendDataMap.put("MerNo", "1151655111");// 10296
        sendDataMap.put("BillNo", String.valueOf(System.currentTimeMillis()));// 订单号
        sendDataMap.put("Amount", "50.00"); // 订单金额
        sendDataMap.put("ReturnURL", "http://www.baidu.com");// 页面跳转同步通知页面:支付完成后web页面跳转显示支付结果
        sendDataMap.put("AdviceURL", "http://www.qidian.com");// 服务器异步通知路径:汇潮服务器主动异步通知商户网站指定的路径
        sendDataMap.put("OrderTime", DateUtil.getNowLongTime()); // 提交时间- 时间格式：2016-12-26 18:18:18
        sendDataMap.put("payType", "B2CDebit");// 支付方式:可传入参数：B2CCredit：B2C信用卡；B2CDebit:B2C借记卡；noCard：银联快捷支付UnionScanPay_H5： 云闪付H5借记卡支付方式；UnionScanCreditPay_H5：贷记卡manualPay：B2B转账B2B：企业网银支付Installment：分期支付

        String key = "MIICdQIBADANB111gkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIO00l3ZGn8HF9wRnhutpXv707TBJFWhFMsWSFOkVTUl9nBS5v8sVsVwMFYgmE2RrRrYAnLtSgH2da+IZCPHwaI3BpsUN2HvAv7bZhngCczEikR7I9YHxtKjR8QIWQW9aDfQlcPmMNvNSCcPlScxSqmx5ZspPhSNUSCUiNS+56O3AgMBAAECgYA8FBTN+IXMoiixG1w7Fffh2ZrV3jC72tHIXi658MFpkBqdXEPA7LHcOHPkJdQzthr3nsdnM3TJ9mnym03KwIlDziYvrkf8hwSNSE5Ov9bAz38IlqgSHvBfYjnmOZ5GlvmF2kYXq5yUGpb08iUFpux4FwsAX2ePbW1EUGzbeqofQQJBAPVmJtalVWnfc8QwO1AqiAzLSXDNemeVLZ2h5taFQrbVjJulYbfwGeXro9TWSF66bfV5yRUyvFT0yyE9fqFMytcCQQCJZVoB0ZCILZpGTbWK4k2G+rV9609YjJEonAVcVMZYmZBPqmEEwvk2c5g+MmZR9WrQDCgIhMhDNodtrxDByLIhAkBrclG5E+UlGSCgGxotTKILMAs059MbfXbemR/wd6KxWSlakPwLRIaiZB10uGoxh+FTZQKFVJSfghtar4k9aNk7AkBEA3Hs2IqNftWR28H0gFYUbWMOdD+Q+/SXf7R/ok+VaF8xsIvaSZIITGyezWAtTimT15CxttlTvFiKCVFoPCtBAkActYlolXcT6rf2ZRNbjKf7XZDhJZxfQHYiXztYDPjjB5FSDtIyjIaNHSbCbrk1qWWkSDDka+iXN4VAR7R16rEg";
        String strContent = "MerNo=" + sendDataMap.get("MerNo") + "&BillNo=" + sendDataMap.get("BillNo") + "&Amount=" + sendDataMap.get("Amount") + "&OrderTime=" + sendDataMap.get("OrderTime") +
                "&ReturnURL=" + sendDataMap.get("ReturnURL") + "&AdviceURL=" + sendDataMap.get("AdviceURL");
        log.info("strContent:" + strContent);
        String mySign = SecurityUtil.sign( key, strContent);
        log.info("--------------mySign:" + mySign);
        sendDataMap.put("SignInfo", mySign);

        String resData = HttpSendUtils.doPostFormByHeader("https://gwapi.yemadai.com/pay/sslpayment", sendDataMap);
        log.info("--------resData:" + resData);

    }
}
