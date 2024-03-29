package com.hz.platform.master.core.common.utils.constant;

/**
 * @author df
 * @Description:redis的key
 * @create 2019-05-22 15:43
 **/
public interface CacheKey {

    /**
     * task跑workType数据填充时：锁住这条任务
     */
    String LOCK_TASK_WORK_TYPE = "-1";
    /**
     * task跑workType数据填充时：锁住这条任务归属的渠道
     * <p>因为要给渠道进行金额的累加</p>
     */
    String LOCK_TASK_WORK_TYPE_CHANNEL = "-2";

    /**
     * task跑数据同步给渠道时：锁住这条任务
     */
    String LOCK_TASK_NOTIFY = "-3";

    /**
     * task跑提现数据时：锁住这条任务
     */
    String LOCK_TASK_WITHDRAW = "-4";

    /**
     * task跑阿里支付宝订单同步数据时：锁住这条任务
     */
    String LOCK_TASK_ALIPAY_NOTIFY = "-5";

    /**
     * task跑workType数据填充时：锁住这条任务归属的代理
     * <p>因为要给代理进行金额的累加</p>
     */
    String LOCK_TASK_WORK_TYPE_AGENT = "-6";

    /**
     * task跑代理收益，锁住这条任务流水
     */
    String LOCK_AGENT_PROFIT = "-7";

    /**
     * LOCK-代付订单数据回传
     * <p>
     *     task任务锁住
     * </p>
     */
    String LOCK_DATA_CORE_OUT = "-8";

    /**
     * LOCK-渠道扣款流水超时的任务
     * <p>
     *     task任务锁住
     * </p>
     */
    String LOCK_CHANNEL_BALANCE_DEDUCT_BY_DELAY_TIME = "-9";

    /**
     * LOCK-渠道扣款流水订单状态不是初始化的任务
     * <p>
     *     task任务锁住
     * </p>
     */
    String LOCK_CHANNEL_BALANCE_DEDUCT_BY_ORDER_STATUS = "-10";

    /**
     * LOCK-代付订单数据同步给渠道
     * <p>
     *     task任务锁住
     * </p>
     */
    String LOCK_CHANNEL_OUT_SEND = "-11";

    /**
     * LOCK-渠道变更金额的的任务
     * <p>
     *     task任务锁住
     * </p>
     */
    String LOCK_CHANNEL_CHANGE = "-12";

    /**
     * LOCK-提现数据同步到蛋糕的任务
     * <p>
     *     task任务锁住
     * </p>
     */
    String LOCK_WITHDRAW_SYNCHRO = "-13";

    /**
     * LOCK-通道变更金额的的任务
     * <p>
     *     task任务锁住
     * </p>
     */
    String LOCK_GEWAY_CHANGE = "-14";

    /**
     * LOCK-通道变更金额
     * <p>
     *     task任务锁住
     *     锁余额、锁总额都用
     * </p>
     */
    String LOCK_GEWAY_MONEY = "-15";


    /**
     * task跑通道收益，锁住这条任务流水
     */
    String LOCK_GEWAY_PROFIT = "-16";

    /**
     * task跑渠道收益，锁住这条任务流水
     */
    String LOCK_CHANNEL_PROFIT = "-17";

    /**
     * 策略数据
     */
    String STRATEGY = "-18";

    /**
     * 查询代付订单网管是否过于频繁
     */
    String OUT_TRADE_NO_BY_OUT_ORDER = "-19";

    /**
     * task跑代付预备充值，锁住这条任务流水
     */
    String LOCK_PREPARE_RECHARGE = "-20";

    /**
     * 查询渠道余额时是否过于频繁
     */
    String CHANNEL_INFO = "-21";


    /**
     * 锁住渠道
     * <p>在操作代付订单的时候锁住渠道</p>
     */
    String CHANNEL_ORDER_OUT = "-22";

    /**
     * LOCK-众邦白名单结果同步
     */
    String LOCK_ZHONG_BANG_WHITELIST_NOTIFY = "-23";

}
