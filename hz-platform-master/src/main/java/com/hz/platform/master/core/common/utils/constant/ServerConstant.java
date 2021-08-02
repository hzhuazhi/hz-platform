package com.hz.platform.master.core.common.utils.constant;

/**
 * @author df
 * @Description:服务端常量
 * @create 2018-07-24 16:05
 **/
public class ServerConstant {

    /**
     * 公共，公用的常量
     */
    public final class PUBLIC_CONSTANT{
        /**
         *值是否等于0的判断条件
         */
        public static final int SIZE_VALUE_ZERO = 0;

        /**
         * 值是否等于1的判断条件
         */
        public static final int SIZE_VALUE_ONE = 1;

        /**
         * 值是否等于2的判断条件
         */
        public static final int SIZE_VALUE_TWO = 2;

        /**
         * 值是否等于3的判断条件
         */
        public static final int SIZE_VALUE_THREE = 3;

        /**
         * 字符串值等于1
         */
        public static final String STR_VALUE_ONE = "1";

        /**
         * token计算标识
         */
        public static final String TAG_HZ = "HZ";

        /**
         * 是否需要邮件提醒：1不需要邮件提醒，2需要邮件提醒
         * 这里2表示需要邮件提醒
         */
        public static final int MAIL_REMIND_YES = 2;

        /**
         * 钱包地址：前缀标签
         */
        public static final String CURRENCY_TAG = "gd";

        /**
         * 锁redis的key的前缀标签
         */
        public static final String REDIS_LOCK_TAG = "lock";


        /**
         * 运行计算状态：：0初始化，1锁定，2计算失败，3计算成功，4扣量
         * 这里3表示成功
         */
        public static final int RUN_STATUS_THREE = 3;

        /**
         * 跑数据被执行了5次为上限
         */
        public static final int RUN_NUM_FIVE = 5;


    }






    /**
     * @Description:支付类型说明的枚举
     * @author yoko
     * @date 2019/12/10 9:54
     */
    public enum PayTypeEnum{
        WN_ZFBSM("1001", "ALIPAY_NATIVE", "支付宝扫码", ""),
        WN_ZFBH5("1002", "ALIPAY_H5", "支付宝H5", "0.032"),
        WN_WXSM("1003", "WEIXIN_NATIVE", "微信扫码", ""),
        WN_WXGZH("1004", "WEIXIN_JSAPI", "微信公众号", ""),
        WN_WXH5("1005", "WEIXIN_H5", "微信H5", "0.035"),
        WN_QQQBSM("1006", "QQPAY_NATIVE", "QQ钱包扫码", ""),
        WN_QQQBGZH("1007", "QQPAY_JSAPI", "QQ钱包公众号", ""),
        WN_QQQBH5("1008", "QQPAY_H5", "QQ钱包H5", ""),
        WN_JDSM("1009", "JD_NATIVE", "京东扫码", ""),
        WN_JDH5("1010", "JD_H5", "京东H5", ""),
        WN_PFYH("1011", "SPDB", "浦发银行", ""),
        WN_HXYH("1012", "HXB", "华夏银行", ""),
        WN_PAYH("1013", "SPABANK", "平安银行", ""),
        WN_ZXYH("1014", "ECITIC", "中信银行", ""),
        WN_XYYH("1015", "CIB", "兴业银行", ""),
        WN_GDYH("1016", "CEBB", "光大银行", ""),
        WN_MSYH("1017", "CMBC", "民生银行", ""),
        WN_ZSYH("1018", "CMB", "招商银行", ""),
        WN_ZGYH("1019", "BOC", "中国银行", ""),
        WN_JTYH("1020", "BCOM", "交通银行", ""),
        WN_JSYH("1021", "CCB", "建设银行", ""),
        WN_GSYH("1022", "ICBC", "工商银行", ""),
        WN_NYYH("1023", "ABC", "农业银行", ""),
        WN_YLKJ("1024", "GATEWAY_QUICK", "银联快捷", ""),
        WN_YLSM("1025", "GATEWAY_NATIVE", "银联扫码", ""),

        API_WXGZH("2001", "901", "微信公众号", ""),
        API_WXSMZF("2002", "902", "微信扫码支付", ""),
        API_ZFBSMZF("2003", "903", "支付宝扫码支付", ""),
        API_ZFBSJ("2004", "904", "支付宝手机", ""),
        API_QQSJZF("2005", "905", "QQ手机支付", ""),
        API_WYZF("2007", "907", "网银支付", ""),
        API_QQSMZF("2008", "908", "QQ扫码支付", ""),
        API_BDQB("2009", "909", "百度钱包", ""),
        API_JDZF("2010", "910", "京东支付", ""),
        API_PDDZFBH5("2032", "932", "PDD支付宝H5", "0.023"),
        API_PDDWXH5("2033", "933", "PDD微信H5", "0.023"),
        API_ZFBSM("20963", "963", "支付宝扫码", ""),




        ;


        /**
         * 我方类型code
         */
        private String myCode;

        /**
         * 对方类型code/通道的code
         */
        private String otherCode;

        /**
         * 描述
         */
        private String desc;

        /**
         * 手续费
         */
        private String serviceCharge;

        private PayTypeEnum(String myCode,String otherCode, String desc, String serviceCharge){
            this.myCode = myCode;
            this.otherCode = otherCode;
            this.desc = desc;
            this.serviceCharge = serviceCharge;
        }

        public String getMyCode() {
            return myCode;
        }

        public void setMyCode(String myCode) {
            this.myCode = myCode;
        }

        public String getOtherCode() {
            return otherCode;
        }

        public void setOtherCode(String otherCode) {
            this.otherCode = otherCode;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getServiceCharge() {
            return serviceCharge;
        }

        public void setServiceCharge(String serviceCharge) {
            this.serviceCharge = serviceCharge;
        }
    }



    /**
     * 策略的枚举
     * 策略类型：1充值金额列表
     */
    public enum StrategyEnum{
        QR_CODE_SWITCH(1,""),
        OUT_QR_CODE_SWITCH(2,""),
        W_PAY(3,""),







        ;
        private int stgType;
        private String stgKey;

        private StrategyEnum(int stgType, String stgKey) {
            this.stgType = stgType;
            this.stgKey = stgKey;
        }

        public int getStgType() {
            return stgType;
        }

        public void setStgType(int stgType) {
            this.stgType = stgType;
        }

        public String getStgKey() {
            return stgKey;
        }

        public void setStgKey(String stgKey) {
            this.stgKey = stgKey;
        }
    }


    /**
     * 收益枚举
     * 收益类型以及收益来由
     */
    public enum ProfitEnum{
        ADVERT(1, "广告收益"),
        RED_PACKET(2, "瓜分红包"),
        FRIEND(3, "好友广告佣金提成"),
        ;
        private int profitType;
        private String origin;

        private ProfitEnum(int profitType, String origin) {
            this.profitType = profitType;
            this.origin = origin;
        }

        public int getProfitType() {
            return profitType;
        }

        public void setProfitType(int profitType) {
            this.profitType = profitType;
        }

        public String getOrigin() {
            return origin;
        }

        public void setOrigin(String origin) {
            this.origin = origin;
        }
    }


    /**
     * 商品码枚举
     * 具体的商品码以及商品码对照什么商品
     */
    public enum ProductCodeEnum{
        VIP("ADVERT_001", "VIP会员"),
        ADVERT("ADVERT_002", "广告购买"),
        ;

        private String code;// 商品码
        private String desc;// 商品描述

        private ProductCodeEnum(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }






}
