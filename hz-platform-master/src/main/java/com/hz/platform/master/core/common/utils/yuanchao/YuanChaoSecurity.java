package com.hz.platform.master.core.common.utils.yuanchao;


import com.hz.platform.master.core.common.utils.HttpSendUtils;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

/**
 * @author yoko
 * @desc 元朝加密
 * @create 2022-04-20 18:14
 **/
public class YuanChaoSecurity {

    // 加密类型
    private static final String ENCRYPT_TYPE = "RSA";
    // 签名类型
    private static final String SING_TYPE = "SHA256WITHRSA";
    // 编码格式
    private static final String UTF_8 = "UTF-8";
    // 下单网关
    private static final String GATEWAY = "https://www.yuanchaozf.com/pay/order/cashier.do";

    public static void main(String[] args) throws Exception {
//        下单数据组装，加密数据
        Map<String, String> repMap = new HashMap<>();
        repMap.put("memberid","220506543");
        repMap.put("appid","2022042004043827239");
        repMap.put("bankcode","104");
        repMap.put("orderid",String.valueOf(System.currentTimeMillis()));
        repMap.put("applydate","2021-03-25 02:41:05");
        repMap.put("amount","200.00");
        repMap.put("notify_url","http://pay13.xxxxx.com/pay/sync_ba");
        repMap.put("return_url","http://pay13.xxxxx.com/pay/href");

        String signContent = getSignContent(repMap);
        // 平台公钥 从后台拷贝
        String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA0gB9bp3A0N3KNQoCay/dwTitH25oO6+fuUWK/4b1yecx+eXlpZYN3oiaI+s0Df8Cdhl0VpJc466+r1aH+pUwX+duN4kNUDT0oR0/CorysmYIuOooSi5I5tYRTqt+JYlUxlD/irVZVVkWWo5w+Gb29W28vhwHGZBcB+wDRswFygr6BAYNIU13Jrf4yJ3hWosEPkh8tLoD+KKvVK4ruliJ/begQqJib9cSpkLXNS7XB9WrDNKFOiO20oN2XxyznebmU/u1n9dmQ6Z08RGf2gv7yK6d3tbAPJ/oGhoyyhjUp/7umh3SuPLZVVy6Xvmc53U/C9mWQhIFUWYoiUZOTDyMxQIDAQAB";
        // 商户私钥 从后台拷贝
        String privateKey = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQC6nzA+NnItNzgjTjwZiV19ku0SO9mKv1mu0h5PIBwsATfmc/a9zhQwj6WzztogM9XO97AJJtFCnnD9m2HigZ6sNK3GFpRfkdbHEqB/BFEpubwjTA6swC/buG9aJpkPJRua5ql56TgNzRsK1ytrxqzGKC/+F6jQmV4qIhNB35cEg58Eu6WZ3EjZhsOGZcmJNs+5uL0bc+IzjeitzQGfWGgDQu4ocJaoPRJLCUzLb9pD2/Hk8MYFsSC88xxCFwZRzALjyPkgMjQ51YX1ra4pUPkvuMLXYLR6vN1bRJuXVNfg9V66nzqG9UOzC77WCYnhOT6SalJsaxWSAM0v1aYtCoQ5AgMBAAECggEANiTVVFdrjN7GdMfP/AC0lSg5i969YNnfZS8lCAPi6Il59GyUm8DcbrJeeup6snkXgTKvahhPAisB0TJxlcvRg1jNSflnFsszKxKmafOQsLBoez0nzMr9l5vNArTMF27GsYxWgz48uxRU0jJXMK+1lNA4DFpE6jB093hnaec6413lnq012VvXMKkEMcjRaBTt+44NyvqRyDDxi640fy9ykcCHgnIN8WJvFbqGbULvp3YQKrs5bUsdGlnVbD2oltRQg3Y0kQWv2N3XEL7sSu7w5943T9/KtRhEm66C7hpcdGjPPIK9+Fme3eJ6YXJ+TynEV/c+MSXY3SyNk0tqeLjbuQKBgQDlDT0abF/2+Vd5rnEqNJI7VUPduuDLvOtRLba+tPpB1Ymlb/fsTqJXS11v4TWL1D5JO8/DNj6NRmuyqcpGEBDgavMNG1Kagcm5U4GrUC6I/Sg59WI5xZL2RWr6pCk9/16S2ieT8pnK07XGZ4hGI6u5gNRK9U3PEYTpJ/0Wp+L8hwKBgQDQlANKDyteSFZt+DN7bJOx+q9Jgvup/TWJ8ohFKJ/9gHvz/lPE/Sg6ccuipYtRqXXB2qgoeZhnY809FSzUqUg3gWOPc92D7VcCQ+YX4WleF2EuIsRO25N9UJBgqmUOkyHT9tgt7fNhgYd1rC8jkRFIZ5I68gm50Zj2Xs76Qk1pPwKBgC2pgvnmiwcIVCj83rCERHaxm9LzM2dKZTsMuvavy4geUs95T9p7Hp/6dUkdg3g5uqUM2HFZOgUd+dBnStzGzSaAC64cOU51+0oH3LzTQkOq/D75rThJK/AOjLIk0df7NdZeyS8UWRZ3ODCyaRdaexyDE09SMc3K900n3KuMkpK3AoGAGwHvYxmPvgeT2W14DRVEEpmpNNWXDK3SzufuqTXXcZW/dxqJckpteRPfEFJrNcnBi7Zqa8NC7rBHlQ823NqGlVlVsO+J0VlcGNj95D21zQ8t8DFkRem8sU9LQpkNuNNoX+/okhnrl8aT6bK/79UK7dsrXiKVC9COA+jG2DTZnTUCgYAafuViZNJpIrdm+fmKDu8qe6vSdqfSQnNvRBkh7zcIwxRq+CcS2gFOEJNby+oHfT2/DPE40w1jbr/6rFVEBjwstdSy6dVC76tL9J8L+28raDdJFTxfhiYFUJibFGbwgdVHe+7BHUtKJcp1v6jt/I7DzmmwAPF0ZypbNHSfiVwRGg==";
        // 4.签名
        String sign = sign(signContent, privateKey);
        repMap.put("sign_type","RSA2");
        repMap.put("attach","张三"); //付款人姓名，如果获取不到，这个字段去掉
        repMap.put("sign",sign);

        //此处 post表单方式提交

        String resData = HttpSendUtils.doPostForm("https://www.yuanchaozf.com/pay/order/cashier.do", repMap);
        System.out.println("resData:" + resData);


//        异步通知，返回数据格式
        Map<String,String> notifyMap = new HashMap<>();
        notifyMap.put("memberid","200404539"); // 商户ID
        notifyMap.put("appid","2020032311033643181"); // APPID
        notifyMap.put("bankcode","104"); // 支付编码
        notifyMap.put("amount","200.00"); // 交易金额
        notifyMap.put("amount_true","200.00"); // 实际金额
        notifyMap.put("orderid","kefuff-1585118465198"); // 商户订单号
        notifyMap.put("sys_orderid","adsfas-1585118465198"); // 支付流水号
        notifyMap.put("datetime","2020-03-25 02:41:05"); // 交易时间
        notifyMap.put("status","SUCCESS"); // 订单状态
        notifyMap.put("sign","GZXPU1JjR8vSaueQJYib6sIY7KzQCwjjfQptMBnVuTij9DGECFyte1ANwLmkHUtbqBHtkg7MVV9TE/4tcOHsdtFx5qmQhEAjnGSBZ25ZJn6tKPxQBtym38YGcvF0DzcHezl/QHmppZ40xMk/yEZU5RguBpgKzNPPn71YD6kQr8cgf9+HfkcmGCjjVn8YD4LPSrr47OI9H+fXFsnHUHdmHzvl3whOk4/kDR8Wiet0RWQaMNAt/zfpfJ9tgzqj6ctWudz/RpuU4oaN0wAASO8XxRmZB1nDJVDfib/aHHv79vOKyyL+9fQRns/hlcI9xV+Vo8qbModMSqG8QEu1XIkhPQ==");


        String notifySign = notifyMap.get("sign");
        notifyMap.remove("sign");
        String verifyStr = getSignContent(notifyMap);

        // 5.验签
        boolean verifySign = verifySign(notifySign, verifyStr, publicKey);
        System.out.println("验签结果：" + verifySign);

        if (verifySign){
            if(notifyMap.get("status") == "SUCCESS"){
                //返回success，平台才能确认通知成功
                System.out.println("success");
                //在此处做其他商户自身的校验及订单操作
            }

        }else {
            System.out.println("签名失败");

        }

    }

    /**
     * 生成密钥对
     *
     * @return 密钥对
     * @throws NoSuchAlgorithmException 密钥算法不可用
     */
    public static KeyPair createKeys() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ENCRYPT_TYPE);
        keyPairGenerator.initialize(1024); // 定义密钥的长度为：2048
        return keyPairGenerator.genKeyPair();
    }

    /**
     * 根据公钥字符串用Base64解码生成公钥对象
     *
     * @param publicKey Base64编码后的公钥字符串
     * @return 公钥对象
     * @throws Exception 异常
     */
    public static PublicKey decoderPublicKey(String publicKey) throws Exception {
        byte[] decode = Base64.getDecoder().decode(publicKey.getBytes());
        KeyFactory keyFactory = KeyFactory.getInstance(ENCRYPT_TYPE);
        return keyFactory.generatePublic(new X509EncodedKeySpec(decode));
    }

    public static PrivateKey decoderPrivateKey(String privateKey) throws Exception {
        byte[] decode = Base64.getDecoder().decode(privateKey.getBytes()); // 将私钥解码
        KeyFactory keyFactory = KeyFactory.getInstance(ENCRYPT_TYPE);
        return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(decode));

    }

    /**
     * 加密
     *
     * @param data      业务数据
     * @param publicKey Base64编码后的公钥
     * @return 经过Base64编码的加密后的数据
     * @throws Exception 异常
     */
    public static String encrypt(String data, String publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ENCRYPT_TYPE);
        cipher.init(Cipher.ENCRYPT_MODE, decoderPublicKey(publicKey));
        byte[] bytes = cipher.doFinal(data.getBytes(UTF_8));
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * 解密
     *
     * @param encryptData 经过Base64编码的加密后的数据
     * @param privateKey  Base64编码后的私钥
     * @return 业务数据
     * @throws Exception 异常
     */
    public static String decrypt(String encryptData, String privateKey) throws Exception {
        byte[] decode = Base64.getDecoder().decode(encryptData);// 将加密后的数据通过Base64解码
        Cipher cipher = Cipher.getInstance(ENCRYPT_TYPE);
        cipher.init(Cipher.DECRYPT_MODE, decoderPrivateKey(privateKey));
        return new String(cipher.doFinal(decode));
    }

    /**
     * @param sortedParams
     * @return
     */
    public static String getSignContent(Map<String, String> sortedParams) {
        StringBuilder content = new StringBuilder();
        List<String> keys = new ArrayList<String>(sortedParams.keySet());
        Collections.sort(keys);
        int index = 0;
        for (String key : keys) {
            String value = sortedParams.get(key);
            if (areNotEmpty(key, value)) {
                content.append(index == 0 ? "" : "&").append(key).append("=").append(value);
                index++;
            }
        }
        return content.toString();
    }

    /**
     * 检查指定的字符串列表是否不为空。
     */
    public static boolean areNotEmpty(String... values) {
        boolean result = true;
        if (values == null || values.length == 0) {
            result = false;
        } else {
            for (String value : values) {
                result &= !isEmpty(value);
            }
        }
        return result;
    }
    /**
     * 检查指定的字符串是否为空。
     * <ul>
     * <li>SysUtils.isEmpty(null) = true</li>
     * <li>SysUtils.isEmpty("") = true</li>
     * <li>SysUtils.isEmpty("   ") = true</li>
     * <li>SysUtils.isEmpty("abc") = false</li>
     * </ul>
     *
     * @param value 待检查的字符串
     * @return true/false
     */
    public static boolean isEmpty(String value) {
        int strLen;
        if (value == null || (strLen = value.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(value.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 签名
     *
     * @param data       业务数据
     * @param privateKey Base64编码后的私钥
     * @return 签名后的数据
     * @throws Exception 异常
     */
    public static String sign(String data, String privateKey) throws Exception {
        Signature signature = Signature.getInstance(SING_TYPE);
        signature.initSign(decoderPrivateKey(privateKey));
        signature.update(data.getBytes(UTF_8));
        return Base64.getEncoder().encodeToString(signature.sign());
    }

    /**
     * 验签
     *
     * @param sign      签名字符串
     * @param data      业务数据
     * @param publicKey 公钥
     * @return 验签结果
     * @throws Exception 异常
     */
    public static boolean verifySign(String sign, String data, String publicKey) throws Exception {
        Signature signature = Signature.getInstance(SING_TYPE);
        signature.initVerify(decoderPublicKey(publicKey));
        signature.update(data.getBytes(UTF_8));
        return signature.verify(Base64.getDecoder().decode(sign));
    }

}
