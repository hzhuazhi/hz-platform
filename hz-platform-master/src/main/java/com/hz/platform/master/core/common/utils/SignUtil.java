package com.hz.platform.master.core.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description TODO
 * @Author yoko
 * @Date 2019/11/6 17:02
 * @Version 1.0
 */
public class SignUtil {
    private static Logger logger    = LoggerFactory.getLogger(SignUtil.class);



    /**
     * @Description: 签名加密
     * @param objects-多类型
     * @return java.lang.String
     * @author yoko
     * @date 2019/11/6 17:12
     */
    public static String getSgin(Object... objects) {
        StringBuffer key = new StringBuffer();
        for (Object object : objects) {
            if (object != null) {
                key.append(object.toString());
            }
        }
        logger.info("key:" + key.toString());
        String str = MD5Util.getMD5String(key.toString());
        logger.info("str:" + str);
        return str;
    }

    public static void main(String [] args){
        String openid = "openid_1";
        String unionid = "unionid_1";
        long ctime = 201911071802959L;
//        long ctime = 1584351658468L;
        String secretKeySign = "POIPLMLKJ";
        String sign = SignUtil.getSgin(openid, unionid, ctime, secretKeySign);
        System.out.println("sign:" + sign);
//        String sign = SignUtil.getSgin(requestModel.openid, requestModel.unionid, requestModel.channel, requestModel.spreadCode, requestModel.ctime, secretKeySign);
    }

}
