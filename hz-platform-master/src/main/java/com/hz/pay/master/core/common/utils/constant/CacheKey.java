package com.hz.pay.master.core.common.utils.constant;

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

}
