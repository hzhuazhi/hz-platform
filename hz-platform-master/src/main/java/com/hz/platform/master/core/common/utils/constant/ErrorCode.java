package com.hz.platform.master.core.common.utils.constant;

/**
 * @author df
 * @Description:异常状态码
 * @create 2018-07-27 11:13
 **/
public class ErrorCode {

    /**
     * 常量异常
     */
    public final class ERROR_CONSTANT {
        /**
         * 没有被捕捉到的异常
         * 默认系统异常状态码=255
         */
        public static final String DEFAULT_EXCEPTION_ERROR_CODE = "255";

        /**
         * 没有被捕捉到的异常
         * 默认系统异常错误信息=SYS_ERROR
         */
        public static final String DEFAULT_EXCEPTION_ERROR_MESSAGE = "ERROR";

        /**
         * 被捕捉到的异常，并且捕捉的异常错误码为空，则默认异常状态码
         * 默认捕捉的异常状态码=256
         */
        public static final String DEFAULT_SERVICE_EXCEPTION_ERROR_CODE = "256";

        /**
         * 被捕捉到的异常，但是错误信息为空，则默认异常信息提醒
         * 默认捕捉的异常信息提醒=错误
         */
        public static final String DEFAULT_SERVICE_EXCEPTION_ERROR_MESSAGE = "错误";

    }


    /**
     * 异常-枚举类
     */
    public enum ENUM_ERROR {






        /***********************************************
         * C打头表示用户的错误
         **********************************************/
        C00000("C00000", "请登录!", "服务端token失效!"),
        C00001("C00001", "错误,请重试!", "用户登录/注册时,请求的所有数据为空!"),
        C00002("C00002", "错误,请重试!", "用户登录/注册时,请求微信的openid数据为空!"),
        C00003("C00003", "错误,请重试!", "用户登录/注册时,请求微信的unionid数据为空!"),
        C00004("C00004", "错误,请重试!", "用户登录/注册时,赋值之后的did数据为零!"),
        C00005("C00005", "错误,请重试!", "获取好友列表时,请求的所有数据为空!"),
        C00006("C00006", "错误,请重试!", "获取好友列表时,token值为空!"),
        C00007("C00007", "错误,请重试!", "获取用户基本信息时,请求的所有数据为空!"),
        C00008("C00008", "错误,请重试!", "获取用户基本信息时,token值为空!"),
        C00009("C00009", "错误,请重试!", "获取用户今日信息时,策略的默认浏览次数值为空!"),
        C00010("C00010", "错误,请重试!", "根据用户DID获取用户的账号信息为空!"),
        C00011("C00011", "错误,请重试!", "根据用户DID获取用户信息为空!"),
        C00012("C00012", "错误,请重试!", "绑定更换支付宝账号时,请求的所有数据为空!"),
        C00013("C00013", "错误,请重试!", "绑定更换支付宝账号时,token值为空!"),
        C00014("C00014", "错误,请重试!", "绑定更换支付宝账号时,交易账号(支付宝账号)值为空!"),
        C00015("C00015", "错误,请重试!", "绑定更换支付宝账号时,交易账号类型值为空!"),
        C00016("C00016", "错误,请重试!", "绑定更换支付宝账号时,真实姓名值为空!"),
        C00017("C00017", "错误,请重试!", "绑定更换支付宝账号时,根据DID查询用户账号数据值为空!"),
        C00018("C00018", "错误,请重试!", "绑定更换支付宝账号时,支付宝账号已绑定有值!"),
        C00019("C00019", "此支付宝账号已绑定其它账号!", "绑定更换支付宝账号时,此支付宝账号已绑定其它账号!"),
        C00020("C00020", "错误,请重试!", "用户登录/注册时,请求的sign数据为空!"),
        C00021("C00021", "错误,请重试!", "用户登录/注册时,请求的sign错误!"),// 需要替换提示
        C00022("C00022", "错误,请重试!", "校验用户是否为新用户时,请求的所有数据为空!"),
        C00023("C00023", "错误,请重试!", "校验用户是否为新用户时,请求微信的openid数据为空!"),
        C00024("C00024", "错误,请重试!", "校验用户是否为新用户时,请求微信的unionid数据为空!"),
        C00025("C00025", "错误,请重试!", "校验用户是否为新用户时,请求的sign数据为空!"),
        C00026("C00026", "错误,请重试!", "校验用户是否为新用户时,请求的sign错误!"),// 需要替换提示



        /***********************************************
         * A打头表示阿里的错误
         **********************************************/
        A00001("A00001", "错误,请重试!", "阿里支付:生成订单码时,所有数据都为空!"),
        A00002("A00002", "错误,请重试!", "阿里支付:生成订单码时,token值为空!"),
        A00003("A00003", "错误,请重试!", "阿里支付:生成订单码时,商品码不是特定的商品码!"),
        A00004("A00004", "错误,请重试!", "阿里支付:生成订单码时,购买广告次数超过规定上限次数!"),
        A00005("A00005", "错误,请重试!", "阿里支付:生成订单码时,商品码数据为空!"),
        A00006("A00006", "错误,请重试!", "阿里支付:生成订单码时,购买广告次数为空!"),
        A00007("A00007", "错误,请重试!", "阿里支付:生成订单码时,购买广告的开关属于关闭状态!"),


        /***********************************************
         * N打头表示公告的错误
         **********************************************/
        N00001("N00001", "错误,请重试!", "获取公告详情时,请求的所有数据为空!"),
        N00002("N00002", "错误,请重试!", "获取公告详情时,请求的公告ID数据为空!"),

        /***********************************************
         * Q打头表示常见问题的错误
         **********************************************/
        Q00001("Q00001", "错误,请重试!", "获取常见问题详情时,请求的所有数据为空!"),
        Q00002("Q00002", "错误,请重试!", "获取常见问题详情时,请求的常见问题ID数据为空!"),


        /***********************************************
         * T打头表示广告的错误
         **********************************************/
        T00001("T00001", "错误,请重试!", "浏览广告时,请求的所有数据为空!"),
        T00002("T00002", "错误,请重试!", "浏览广告时,token值为空!"),
        T00003("T00003", "广告浏览次数已用完!", "广告浏览次数已用完!"),
        T00004("T00004", "错误,请重试!", "广告浏览时,数据库中广告数据为空!"),
        T00005("T00005", "错误,请重试!", "广告浏览时,策略的收益范围数值为空!"),
        T00006("T00006", "错误,请重试!", "广告浏览时,用户请求过于频繁数值为空!"),
        T00007("T00007", "您的会员已过期,只有会员才能浏览广告!", "广告浏览时,用户会员到期!"),
        T00008("T00008", "您的会员已过期,只有在有效期的会员才能观看超级视频!", "观看超级视频时,用户会员到期!"),

        /***********************************************
         * S打头表示策略的错误
         **********************************************/
        S00001("S00001", "错误,请重试!", "获取专属客服时,请求的所有数据为空!"),
        S00002("S00002", "错误,请重试!", "获取专属客服时,数据库数据为空!"),
        S00003("S00003", "错误,请重试!", "获取广告购买次数时,请求的所有数据为空!"),
        S00004("S00004", "错误,请重试!", "获取购买广告的价格每次时,数据库数据为空!"),
        S00005("S00005", "错误,请重试!", "获取购买广告的最大次数时,数据库数据为空!"),
        S00006("S00006", "错误,请重试!", "获取分享数据时,请求的所有数据为空!"),
        S00007("S00007", "错误,请重试!", "获取推广分享默认地址时,数据库数据为空!"),
        S00008("S00008", "错误,请重试!", "获取可提现金额列表时,请求的所有数据为空!"),
        S00009("S00009", "错误,请重试!", "获取可提现金额列表时,数据库数据策略可提现金额数据为空!"),
        S00010("S00010", "错误,请重试!", "获取策略充值会员的价格,购买广告的价格每次时,数据库数据为空!"),
        S00011("S00011", "错误,请重试!", "获取会员VIP的价格,单次购买广告的价格时,请求的所有数据为空!"),
        S00012("S00012", "错误,请重试!", "获取充值会员的价格时,数据库数据为空!"),
        S00013("S00013", "错误,请重试!", "获取购买广告的价格每次时,数据库数据为空!"),
        S00014("S00014", "错误,请重试!", "获取是否可以购买广告次数的开关时,请求的所有数据为空!"),
        S00015("S00015", "错误,请重试!", "获取是否可以购买广告次数的开关时,数据库数据为空!"),


        /***********************************************
         * R打头表示纪录的错误
         **********************************************/
        R00001("R00001", "错误,请重试!", "正式提现时,请求的所有数据为空!"),
        R00002("R00002", "错误,请重试!", "正式提现时,token值为空!"),
        R00003("R00003", "错误,请重试!", "正式提现时,提现金额值为空!"),
        R00004("R00004", "错误,请重试!", "正式提现时,提现金额大于余额为空!"),
        R00005("R00005", "错误,请重试!", "正式提现时,用户账户的余额为空!"),
        R00006("R00006", "请绑定支付宝!", "正式提现时,用户的提现支付宝账号未绑定!"),
        R00007("R00007", "错误,请重试!", "正式提现时,提现的金额列表与部署的金额大小不匹配!"),
        R00008("R00008", "错误,请重试!", "获取提现记录集合时,请求的所有数据为空!"),
        R00009("R00009", "错误,请重试!", "获取提现记录集合时,token值为空!"),
        R00010("R00010", "错误,请重试!", "获取用户收益记录集合时,请求的所有数据为空!"),
        R00011("R00011", "错误,请重试!", "获取用户收益记录集合时,token值为空!"),
        R00012("R00012", "错误,请重试!", "获取用户观看记录集合时,请求的所有数据为空!"),
        R00013("R00013", "错误,请重试!", "获取用户观看记录集合时,token值为空!"),
        R00014("R00014", "错误,请重试!", "添加用户观看记录时,请求的所有数据为空!"),
        R00015("R00015", "错误,请重试!", "添加用户观看记录时,token值为空!"),
        R00016("R00016", "错误,请重试!", "添加用户观看记录时,视频资源地址数据为空!"),

        /***********************************************
         * V打头表示视频类的错误
         **********************************************/
        V00001("V00001", "错误,请重试!", "获取视频解析数据时,请求的所有数据为空!"),
        V00002("V00002", "错误,请重试!", "获取视频解析数据时,token值为空!"),



        ;

        /**
         * 错误码
         */
        private String eCode;
        /**
         * 给客户端看的错误信息
         */
        private String eDesc;
        /**
         * 插入数据库的错误信息
         */
        private String dbDesc;




        private ENUM_ERROR(String eCode, String eDesc,String dbDesc) {
            this.eCode = eCode;
            this.eDesc = eDesc;
            this.dbDesc  = dbDesc;
        }

        public String geteCode() {
            return eCode;
        }

        public void seteCode(String eCode) {
            this.eCode = eCode;
        }

        public String geteDesc() {
            return eDesc;
        }

        public void seteDesc(String eDesc) {
            this.eDesc = eDesc;
        }

        public String getDbDesc() {
            return dbDesc;
        }

        public void setDbDesc(String dbDesc) {
            this.dbDesc = dbDesc;
        }
    }
}
