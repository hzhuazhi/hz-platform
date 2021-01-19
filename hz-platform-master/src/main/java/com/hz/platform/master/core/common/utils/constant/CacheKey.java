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


}
