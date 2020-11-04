package com.hz.platform.master.core.common.utils;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * md5加密util
 *
 * @Title: MD5Util
 * @Description:
 * @Author: yoko from HangZhouZhengqu， E-mail: 283899571@qq.com
 * @Date: Created in 2019/2/15 20:17
 * @Version 1.0
 * @Company HangZhouZhengqu Co., Ltd.
 */
public class MD5Util {

    /**
     * 默认的密码字符串组合，用来将字节转换成 16 进制表示的字符,apache校验下载的文件的正确性用的就是默认的这个组合
     */
    protected static char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    protected static MessageDigest messagedigest = null;

    static {
        try {
            messagedigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException nsaex) {
            System.err.println(MD5Util.class.getName() + "初始化失败，MessageDigest不支持MD5Util。");
            nsaex.printStackTrace();
        }
    }

    /**
     * 生成字符串的md5校验值
     *
     * @param s
     * @return
     */
    public static String getMD5String(String s) {
        return getMD5String(s.getBytes());
    }

    /**
     * 判断字符串的md5校验码是否与一个已知的md5码相匹配
     *
     * @param password  要校验的字符串
     * @param md5PwdStr 已知的md5校验码
     * @return
     */
    public static boolean checkPassword(String password, String md5PwdStr) {
        String s = getMD5String(password);
        return s.equals(md5PwdStr);
    }

    /**
     * 生成文件的md5校验值
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static String getFileMD5String(File file) throws IOException {
        InputStream fis;
        fis = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        int numRead = 0;
        while ((numRead = fis.read(buffer)) > 0) {
            messagedigest.update(buffer, 0, numRead);
        }
        fis.close();
        return bufferToHex(messagedigest.digest());
    }

    /**
     *
     * JDK1.4中不支持以MappedByteBuffer类型为参数update方法，并且网上有讨论要慎用MappedByteBuffer，
     * 原因是当使用 FileChannel.map 方法时，MappedByteBuffer 已经在系统内占用了一个句柄， 而使用
     * FileChannel.close 方法是无法释放这个句柄的，且FileChannel有没有提供类似 unmap 的方法，
     * 因此会出现无法删除文件的情况。
     * <p>
     * 不推荐使用
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static String getFileMD5String_old(File file) throws IOException {
        FileInputStream in = new FileInputStream(file);
        FileChannel ch = in.getChannel();
        MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
        messagedigest.update(byteBuffer);
        in.close();
        return bufferToHex(messagedigest.digest());
    }

    public static String getMD5String(byte[] bytes) {
        messagedigest.update(bytes);
        return bufferToHex(messagedigest.digest());
    }

    private static String bufferToHex(byte bytes[]) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte bytes[], int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }

    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = hexDigits[(bt & 0xf0) >> 4];// 取字节中高 4 位的数字转换, >>>
        // 为逻辑右移，将符号位一起右移,此处未发现两种符号有何不同
        char c1 = hexDigits[bt & 0xf];// 取字节中低 4 位的数字转换
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }



    /**
     * 加密
     *
     * @param plain 明文
     * @return 32位小写密文
     * @author qianyikai from HangZhouZhengqu， E-mail: 469640411@qq.com
     * @date Created in 2019/2/15 20:18
     */
    public static String encryption(String plain) {
        String re_md5 = new String();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plain.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }

            re_md5 = buf.toString();

        } catch (NoSuchAlgorithmException e) {
//            logger.error("MD5Util加密失败：", e);
        }
        return re_md5;
    }

    public static  void  main(String[] args){
//            System.out.println(MD5Utils.("crp941918"));

        // http://pay.woniubank.com:8000/pay/gateway?app_id=2003040133&trade_type=WEIXIN_H5&total_amount=500&out_trade_no=13702160672&notify_url=http://www.baidu.com/notify.php&return_url=http://www.baidu.com/return.php&extra_return_param=test&interface_version=V2.0&sign=d9b0dd3c535618a1b9060606f6a3046a
        // app_id=2003040133&notify_url=http://www.baidu.com/notify.php&out_trade_no=13702160671&total_amount=199&trade_type=WEIXIN_H5
        String str = "app_id=2003040133&notify_url=http://www.baidu.com/notify.php&out_trade_no=13702160672&total_amount=500&trade_type=WEIXIN_H50688fa7bcb789ae764a801c11a2c21df";
//        String str = "app_id=1803110116&notify_url=http://www.baidu.com/notify.php&out_trade_no=201803130890&total_amount=100&trade_type=WEIXIN_NATIVEaef5ef05374ad5043f9cee3a1789fe91";
        String sign = "";
        String sb = encryption(str);
        System.out.println(sb);

        // 校验sign签名
        String channel = "10101";
//        String trade_type = "1005";
        String trade_type = "100002";
        String total_amount = "3000.00";
        String out_trade_no = "out_trade_no_1";
//        String out_trade_no = "20200615102453535754";
        String notify_url = "http://www.baidu.com/sb";
//        String notify_url = "http://ds.di1h.cn/Pay_Gaofang_notifyurl.html";
        String secretKey = "d27b23d1e5094611096c519bfffe233f";
        String checkSign = "channel=" + channel + "&" + "trade_type=" + trade_type + "&" + "total_amount=" + total_amount
                + "&" + "out_trade_no=" + out_trade_no + "&" + "notify_url=" + notify_url + "&" + "key=" + secretKey ;
        checkSign = MD5Util.encryption(checkSign);
        System.out.println("checkSign:" + checkSign);

        String sb3 = "channel=2003270117&trade_type=2001&total_amount=100&out_trade_no=202003130890&notify_url=http://www.baidu.com/notify.php&key=0688fa7bcb789ae764a801c11a2c21se";
        sb3 = MD5Util.encryption(sb3);
        System.out.println("sb3:" + sb3);

        String m_out_trade_no = "2020032421550000001";
        String m_total_amount = "50000";
        String trade_status = "SUCCESS";
        String m_secretKey = "0688fa7bcb789ae764a801c11a2c21df";
        String mySign = "out_trade_no=" + m_out_trade_no + "&" + "total_amount=" + m_total_amount + "&" + "trade_status=" + trade_status + m_secretKey;
        mySign = MD5Util.encryption(mySign);
        System.out.println("mySign:" + mySign);

        String testSign = MD5Util.encryption("test_namewechat1order_id_2order_uid_1notify_url_1return_url_1feedback_url_191c04753bb844e5ab6780f36bab4b967");
        System.out.println("testSign:" + testSign);

        String xunyin = MD5Util.encryption("parter=8142&type=98&value=100&orderid=orderid_1&callbackurl=http%3A%2F%2Fwww.baidu.comdf319a1b237c41db86c510954e3a51ae");
        System.out.println("xunyin:" + xunyin);

        String tb_total_amount = "50";
        String tb_out_trade_no = "CZ20090123161569855";
        int tb_trade_status = 1;
        String tb_key = "cc753d1959e43c87fb8ec036d6eefd54";
        String tb_sign = "total_amount=" + tb_total_amount + "&" + "out_trade_no=" + tb_out_trade_no + "&" + "trade_status=" + tb_trade_status
                + "&" + "key=" + tb_key;
        tb_sign = MD5Util.encryption(tb_sign);
        System.out.println("tb_sign:" + tb_sign);



        // 校验代付签名
//        String df_channel = "channel_9";
//        String df_trade_type = "200001";
//        String df_total_amount = "500.00";
//        String df_out_trade_no = "df_out_trade_no_1";
//        String df_bank_name = "中国银行";
//        String df_bank_card = "银行卡卡号";
//        String df_account_name = "开户人";
//        String df_key = "94d87e39f14dcc86f0ca5c40fbd458da";

        String df_channel = "10101";
        String df_trade_type = "200001";
        String df_total_amount = "3000.00";
        String df_out_trade_no = "df_out_trade_no_1";
        String df_bank_name = "建设银行";
        String df_bank_card = "8888888888";
        String df_account_name = "张三";
        String df_key = "d27b23d1e5094611096c519bfffe233f";
        String dfCheckSign = "channel=" + df_channel + "&" + "trade_type=" + df_trade_type + "&" + "total_amount=" + df_total_amount
                + "&" + "out_trade_no=" + df_out_trade_no + "&" + "bank_name=" + df_bank_name + "&"
                + "&" + "bank_card=" + df_bank_card + "&" + "account_name=" + df_account_name
                + "key=" + df_key;
        dfCheckSign = MD5Util.encryption(dfCheckSign);
        System.out.println("dfCheckSign:" + dfCheckSign);
    }

//    public static void main(String[] args) throws IOException {
//        long begin = System.currentTimeMillis();
//
//        File file = new File("D:\\app-debug.apk");
//        if (!file.exists()) {
//            System.out.println("不存在");
//        }
//        String md5 = getFileMD5String(file);
//
////      String md5 = getMD5String("a");
//
//        long end = System.currentTimeMillis();
//        System.out.println("md5:" + md5 + " time:" + ((end - begin) / 1000) + "s");
//        System.out.println(file.getPath());
//    }


}
