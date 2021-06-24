package com.hz.platform.master.core.common.utils;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;


import com.alibaba.fastjson.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;

/**
 * 微派支付接口调用帮助类(Java)
 * @version 2.0
 * @author Jack
 * */
public class WPayHelper {
    public static void main(String[] args) throws Exception{
        //WPayBankcardDPayExample();
        //WPayWeixinDPayExample();
//        WPayQueryBalanceDPayExample();
        //WPayQueryOrderStateDPayExample();
        String api_url = "http://show.wiipay.cn/1/jspay/api/pay.do";
        String api_key = "x40xmixvaxzrgngk4ub1eo1nmfjsx67z";
        String callback_url = "http://www.qidian.com";
        String notify_url = "httP://www.baidu.com";
        String out_trade_no = "CS" + String.valueOf(System.currentTimeMillis());
        String total_fee = "500";
        String res = getPayUrl(api_url, api_key, callback_url, notify_url, out_trade_no, total_fee);
        System.out.println("res:" + res);






//        java.util.Map<String,String> paramMap = new java.util.HashMap<String,String>();
//        paramMap.put("app_id", "3cce6640e1c76faf14287297910e8a98");//微派平台分配的WIIPAY_APP_ID,必填
//        paramMap.put("body", "付款");//商品名称
//        paramMap.put("callback_url", "http://www.baidu.com");//支付成功或失败后，页面跳转地址
//        paramMap.put("notify_url", "http://www.baidu.com");//异步通知地址，非必填
//        paramMap.put("channel_id", "appleStore");//渠道信息
//        paramMap.put("format", "json");//返回数据格式，请填写json
//        paramMap.put("out_trade_no", "test_"+String.valueOf(System.currentTimeMillis()));//商户单号信息
//        paramMap.put("pay_type", "alipay");//支付方式，必填
//        paramMap.put("total_fee", "0.10");//金额，单位元
//        paramMap.put("version", "4.0");//接口版本号,必填
//        paramMap.put("currency_code", "RMB");//币种编号，默认填写RMB即可，必填项
//
//        String pretxt = WPayHelper.createLinkString(WPayHelper.paraFilter(paramMap));//按字母序,值为空的不参与签名
//        System.out.println("pretxt:"+pretxt);
//        String sign = sign(pretxt, api_key, "utf-8").toUpperCase();//签名，大写
//        paramMap.put("sign", sign);
//        String resString = "";
//        try{
//            resString = WPayHelper.simplePost(api_url, paramMap);
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//        System.out.println("resString:"+resString);
//        if(StringUtils.isNotEmpty(resString)){
//            JSONObject jo = JSONObject.parseObject(resString);
//            //{"message":"打款调用成功","code":10000}
//            String ret_state = jo.getString("ret_state");
//            String ret_msg = jo.getString("ret_msg");
//            String pay_url = jo.getString("pay_url");
//            if("success".equals(ret_state) && StringUtils.isNotEmpty(pay_url)){
//                //TODO 支付  pay_url
//            }else{
//                System.out.println("获取支付通道失败:"+ret_state+","+ret_msg);
//            }
//        }



    }




    public static String getPayUrl(String api_url, String api_key, String callback_url, String notify_url, String out_trade_no, String total_fee){
        String payUrl = null;
        Map<String,String> paramMap = new HashMap<String,String>();
        paramMap.put("app_id", "3cce6640e1c76faf14287297910e8a98");//微派平台分配的WIIPAY_APP_ID,必填
        paramMap.put("body", "钻石" + total_fee);//商品名称
        paramMap.put("callback_url", callback_url);//支付成功或失败后，页面跳转地址
        paramMap.put("notify_url", notify_url);//异步通知地址，非必填
//        paramMap.put("channel_id", "appleStore");//渠道信息
        paramMap.put("format", "json");//返回数据格式，请填写json
        paramMap.put("out_trade_no", out_trade_no);//商户单号信息
        paramMap.put("pay_type", "alipay");//支付方式，必填
        paramMap.put("total_fee", total_fee);//金额，单位元
        paramMap.put("version", "4.0");//接口版本号,必填
        paramMap.put("currency_code", "RMB");//币种编号，默认填写RMB即可，必填项
        String pretxt = WPayHelper.createLinkString(WPayHelper.paraFilter(paramMap));//按字母序,值为空的不参与签名
        System.out.println("pretxt:"+pretxt);
        String sign = sign(pretxt, api_key, "utf-8").toUpperCase();//签名，大写
        paramMap.put("sign", sign);
        String resString = "";
        try{
            resString = WPayHelper.simplePost(api_url, paramMap);
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("resString:"+resString);
        if(StringUtils.isNotEmpty(resString)){
            JSONObject jo = JSONObject.parseObject(resString);
            //{"message":"打款调用成功","code":10000}
            String ret_state = jo.getString("ret_state");
            String ret_msg = jo.getString("ret_msg");
            String pay_url = jo.getString("pay_url");
            if("success".equals(ret_state) && StringUtils.isNotEmpty(pay_url)){
                //TODO 支付  pay_url
                payUrl = pay_url;
            }else{
                System.out.println("获取支付通道失败:"+ret_state+","+ret_msg);
            }
        }
        return payUrl;
    }





    /**
     * 微派企业打款(代付到银行卡)接口调用示例
     *
     * */
    public static void WPayBankcardDPayExample(){
        String api_url = "http://dapi.wiipay.cn/transfer/transfer.do";
        String api_key = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";//秘钥(key)

        java.util.Map<String,String> paramMap = new java.util.HashMap<String,String>();
        paramMap.put("version", "2.0");//接口版本号,必填
        paramMap.put("appid", "demobf1e4c8b41b640c8834cb0cc45db");//微派平台分配的WIIPAY_APP_ID,必填
        paramMap.put("transfer_type", "bankcard");//打款方式,必填，bankcard,weixin,alipay
        paramMap.put("out_trade_no", String.valueOf(System.currentTimeMillis()));//商户自己平台的订单号
        paramMap.put("amount", "1.00");//打款金额(单位:元),必填
        paramMap.put("bankcard_no", "6217002000029000000");//银行卡号,必填
        paramMap.put("bank_name", "建设银行");//银行名称,必填
        paramMap.put("currency_code", "RMB");// 币种编号默认RMB(人民币)必填
        paramMap.put("bank_branch", "苏州工业园区金鸡湖支行");//分行名称,暂可为空
        paramMap.put("cardholder_name", "张三");//持卡人姓名
        paramMap.put("cardholder_mobile", "");//持卡人手机号(可为空),非必填

        paramMap.put("notify_cardholder", "n");//到账短信通知{n,y},非必填

        String pretxt = WPayHelper.createLinkString(WPayHelper.paraFilter(paramMap));//按字母序,值为空的不参与签名
        System.out.println("pretxt:"+pretxt);
        String sign = sign(pretxt, api_key, "utf-8").toUpperCase();//签名，大写
        paramMap.put("sign", sign);

        String resString = "";
        try{
            resString = WPayHelper.simplePost(api_url, paramMap);
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("resString:"+resString);
        if(StringUtils.isNotEmpty(resString)){
            JSONObject jo = JSONObject.parseObject(resString);
            //{"message":"打款调用成功","code":10000}
            int code = jo.getIntValue("code");
            String message = jo.getString("message");
            if(code == 10000){
                System.out.println("调用成功");
            }else{
                System.out.println("调用失败:"+code+","+message);
            }
        }
    }

    /**
     * 微派企业打款(代付到微信零钱账户)接口调用示例
     *
     * */
    public static void WPayWeixinDPayExample(){
        String api_url = "http://dapi.wiipay.cn/transfer/transfer.do";
        String api_key = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";//秘钥(key)

        java.util.Map<String,String> paramMap = new java.util.HashMap<String,String>();
        paramMap.put("version", "2.0");//接口版本号,必填
        paramMap.put("appid", "demobf1e4c8b41b640c8834cb0cc45db");//微派平台分配的WIIPAY_APP_ID,必填
        paramMap.put("transfer_type", "weixin");//打款方式,必填，bankcard,weixin,alipay
        paramMap.put("out_trade_no", String.valueOf(System.currentTimeMillis()));//商户自己平台的订单号,必填
        paramMap.put("amount", "1.00");//打款金额(单位:元),必填
        paramMap.put("currency_code", "RMB");// 币种编号默认RMB(人民币)必填
         /*
         paramMap.put("bankcard_no", "");
         paramMap.put("bank_name", "");
         paramMap.put("bank_branch", "");
         */
        paramMap.put("cardholder_name", "张三");//持卡人姓名或者用户的唯一标识(如用户在商户公众号平台的openid),必填
        paramMap.put("cardholder_mobile", "");//持卡人手机号,非必填
        paramMap.put("notify_cardholder", "n");//到账短信通知{n,y},非必填

        String pretxt = WPayHelper.createLinkString(WPayHelper.paraFilter(paramMap));//按字母序,值为空的不参与签名
        System.out.println("pretxt:"+pretxt);
        String sign = sign(pretxt, api_key, "utf-8").toUpperCase();//签名，大写
        paramMap.put("sign", sign);

        String resString = "";
        try{
            resString = WPayHelper.simplePost(api_url, paramMap);
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("resString:"+resString);
        if(StringUtils.isNotEmpty(resString)){
            JSONObject jo = JSONObject.parseObject(resString);
            //{"message":"打款调用成功","code":10000}
            int code = jo.getIntValue("code");
            String message = jo.getString("message");
            String withdraw_url = jo.getString("withdraw_url");//提现地址,需要在用户在微信浏览器中访问
            if(code == 10000){
                System.out.println("调用成功");
                if (!StringUtils.isBlank(withdraw_url)){
                    //String nextPayUrl = withdraw_url + "?cardholder_name=xxx";
                }
            }else{
                System.out.println("调用失败:"+code+","+message);
            }
        }
    }

    /**
     * 微派企业打款-交易状态查询接口调用示例
     *
     * */
    public static void WPayQueryOrderStateDPayExample(){
        String api_url = "http://dapi.wiipay.cn/transfer/orderState.do";
        String api_key = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";//秘钥(key)

        java.util.Map<String,String> paramMap = new java.util.HashMap<String,String>();
        paramMap.put("version", "2.0");//接口版本号,必填
        paramMap.put("currency_code", "RMB");// 币种编号默认RMB(人民币)必填
        paramMap.put("appid", "demobf1e4c8b41b640c8834cb0cc45db");//微派平台分配的WIIPAY_APP_ID,必填
        paramMap.put("out_trade_no", "1501676616255");//商户自己平台的订单号,必填
        String pretxt = WPayHelper.createLinkString(WPayHelper.paraFilter(paramMap));//按字母序,值为空的不参与签名
        System.out.println("pretxt:"+pretxt);
        String sign = sign(pretxt, api_key, "utf-8").toUpperCase();//签名，大写
        paramMap.put("sign", sign);

        String resString = "";
        try{
            resString = WPayHelper.simplePost(api_url, paramMap);
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("resString:"+resString);
        if(StringUtils.isNotEmpty(resString)){
            JSONObject jo = JSONObject.parseObject(resString);
            //{"code":10000,"state":"success","message":""}
            int code = jo.getIntValue("code");
            String message = jo.getString("message");
            String state = jo.getString("state");//成功success,失败fail,处理中processing
            if(code == 10000){
                System.out.println("交易状态:"+state);
            }else{
                System.out.println("调用失败:"+code+","+message);
            }
        }
    }

    /**
     * 微派企业打款-账户余额查询接口调用示例
     *
     * */
    public static void WPayQueryBalanceDPayExample(){
        String api_url = "http://dapi.wiipay.cn/transfer/balance.do";
        String api_key = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";//秘钥(key)

        java.util.Map<String,String> paramMap = new java.util.HashMap<String,String>();
        paramMap.put("version", "2.0");//接口版本号,必填
        paramMap.put("appid", "demobf1e4c8b41b640c8834cb0cc45db");//微派平台分配的WIIPAY_APP_ID,必填

        String pretxt = WPayHelper.createLinkString(WPayHelper.paraFilter(paramMap));//按字母序,值为空的不参与签名
        System.out.println("pretxt:"+pretxt);
        String sign = sign(pretxt, api_key, "utf-8").toUpperCase();//签名，大写
        paramMap.put("sign", sign);

        String resString = "";
        try{
            resString = WPayHelper.simplePost(api_url, paramMap);
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("resString:"+resString);
        if(StringUtils.isNotEmpty(resString)){
            JSONObject jo = JSONObject.parseObject(resString);
            //{"code":10000,"balance":35.93,"message":""}
            int code = jo.getIntValue("code");
            String message = jo.getString("message");
            BigDecimal balance = jo.getBigDecimal("balance");//账户余额(单位:元)
            if(code == 10000){
                System.out.println("余额:"+balance);
            }else{
                System.out.println("调用失败:"+code+","+message);
            }
        }
    }

    /**
     * HTTP POST application/x-www-form-urlencoded
     * */
    public static String simplePost(String url,Map<String,String> postParamMap) throws Exception {
        String result = null;
        PostMethod post = null;
        try {
            post = new PostMethod(url);
            if(null != postParamMap && !postParamMap.isEmpty()){
                Set<String> keySet = postParamMap.keySet();
                for(String key:keySet){
                    String v = null==postParamMap.get(key)?"":postParamMap.get(key);
                    post.addParameter(key, v);
                }
            }
            HttpClient client = new HttpClient();
            client.getParams().setParameter("http.protocol.content-charset", "UTF-8");
            client.getHttpConnectionManager().getParams().setConnectionTimeout(120000);
            client.getHttpConnectionManager().getParams().setSoTimeout(120000);

            int resCode = client.executeMethod(post);
            if(resCode == 200){
                BufferedReader br = new BufferedReader(new         InputStreamReader(post.getResponseBodyAsStream(),"UTF-8"));
                StringBuffer resBuffer = new StringBuffer();
                String resTemp = "";
                while((resTemp = br.readLine()) != null){
                    resBuffer.append(resTemp);
                }
                result = resBuffer.toString();
            }else{
                System.out.println("resCode:"+resCode);
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            if(null != post){
                post.releaseConnection();
            }
        }
        return result;
    }

    /**
     * 签名字符串
     * @param text 需要签名的字符串
     * @param key 密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
    public static String sign(String text, String key, String input_charset) {
        text = text + key;
        return DigestUtils.md5Hex(getContentBytes(text, input_charset));
    }

    /**
     * @param content
     * @param charset
     * @return
     * @throws UnsupportedEncodingException
     */
    private static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }

    /**
     * 除去数组中的空值和签名参数
     * @param sArray 签名参数组
     * @return 去掉空值与签名参数后的新签名参数组
     */
    public static Map<String, String> paraFilter(Map<String, String> sArray) {
        Map<String, String> result = new HashMap<String, String>();
        if (sArray == null || sArray.size() <= 0) {
            return result;
        }
        for (String key : sArray.keySet()) {
            String value = sArray.get(key);
            if (value == null || "".equals(value) || key.equalsIgnoreCase("sign")
            ) {
                continue;
            }
            result.put(key, value);
        }
        return result;
    }

    public static String createLinkString(Map<String, String> params) {
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        String prestr = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }
        return prestr;
    }

}


