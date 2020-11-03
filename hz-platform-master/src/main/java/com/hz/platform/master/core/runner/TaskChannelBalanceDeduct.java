package com.hz.platform.master.core.runner;

import com.hz.platform.master.core.common.utils.constant.CacheKey;
import com.hz.platform.master.core.common.utils.constant.CachedKeyUtils;
import com.hz.platform.master.core.model.channel.ChannelModel;
import com.hz.platform.master.core.model.channelbalancededuct.ChannelBalanceDeductModel;
import com.hz.platform.master.core.model.task.base.StatusModel;
import com.hz.platform.master.util.ComponentUtil;
import com.hz.platform.master.util.HodgepodgeMethod;
import com.hz.platform.master.util.TaskMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description task：渠道扣减余额流水
 * @Author yoko
 * @Date 2020/11/1 17:11
 * @Version 1.0
 */
@Component
@EnableScheduling
public class TaskChannelBalanceDeduct {

    private final static Logger log = LoggerFactory.getLogger(TaskChannelBalanceDeduct.class);

    @Value("${task.limit.num}")
    private int limitNum;



    /**
     * 10分钟
     */
    public long TEN_MIN = 10;


    /**
     * @Description: 检测渠道扣款流水超时后的业务逻辑
     * <p>
     *     每5秒执行运行一次
     *     1.查询未跑的订单状态是初始化状态，并且delay_time小于等于当前系统时间的数据。
     *     2.更新把扣款流水的金额返还给渠道，也就是更新渠道的余额
     * </p>
     * @author yoko
     * @date 2019/12/6 20:25
     */
//    @Scheduled(fixedDelay = 1000) // 每1秒执行
    @Scheduled(fixedDelay = 5000) // 每5秒执行
    public void channelBalanceDeductByDelayTime() throws Exception{
//        log.info("----------------------------------TaskChannelBalanceDeduct.channelBalanceDeductByDelayTime()----start");
        // 获取未跑的数据
        StatusModel statusQuery = TaskMethod.assembleTaskStatusQuery(limitNum, 1, 0, 0, 1, 0, 1);
        List<ChannelBalanceDeductModel> synchroList = ComponentUtil.taskChannelBalanceDeductService.getDataList(statusQuery);
        for (ChannelBalanceDeductModel data : synchroList){
            StatusModel statusModel = null;
            try{
                // 锁住这个数据流水
                String lockKey = CachedKeyUtils.getCacheKey(CacheKey.LOCK_CHANNEL_BALANCE_DEDUCT_BY_DELAY_TIME, data.getId());
                boolean flagLock = ComponentUtil.redisIdService.lock(lockKey);
                if (flagLock){
                    // 锁住要执行的渠道ID
                    String lockKey_channel = CachedKeyUtils.getCacheKey(CacheKey.LOCK_TASK_WORK_TYPE_CHANNEL, data.getChannelId());
                    boolean flagLock_channel = ComponentUtil.redisIdService.lock(lockKey_channel);
                    if (flagLock_channel){
                        // 组装渠道余额加扣款流水的金额
                        ChannelModel updateBalance = HodgepodgeMethod.assembleChannelBalanceAdd(data.getChannelId(), data.getMoney());

                        // 正式处理逻辑-返还渠道扣款金额
                        int num = ComponentUtil.channelService.updateBalance(updateBalance);
                        if (num > 0){
                            statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 3, 0,  0,0,null);
                        }else {
                            statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0,  0,0,null);
                        }
                        // 解锁
                        ComponentUtil.redisIdService.delLock(lockKey_channel);

                    }

                    // 更新状态
                    ComponentUtil.taskChannelBalanceDeductService.updateStatus(statusModel);

                    // 解锁
                    ComponentUtil.redisIdService.delLock(lockKey);
                }

//                log.info("----------------------------------TaskChannelBalanceDeduct.channelBalanceDeductByDelayTime()----end");
            }catch (Exception e){
                log.error(String.format("this TaskChannelBalanceDeduct.channelBalanceDeductByDelayTime() is error , the dataId=%s !", data));
                e.printStackTrace();
                // 更新此次task的状态：更新成失败
                statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0, 0,0,"异常失败try!");
                ComponentUtil.taskChannelBalanceDeductService.updateStatus(statusModel);
            }
        }
    }



    /**
     * @Description: 检测渠道扣款流水订单状态不是初始化状态的业务逻辑
     * <p>
     *     每5秒执行运行一次
     *     1.查询未跑的订单状态不是初始化状态的渠道流水数据。
     *     2.如果订单状态是成功的，则直接更新task任务状态，更新成成功状态。
     *     3.如果订单状态是失败的，则需要把金额返还给渠道
     * </p>
     * @author yoko
     * @date 2019/12/6 20:25
     */
//    @Scheduled(fixedDelay = 1000) // 每1秒执行
    @Scheduled(fixedDelay = 5000) // 每5秒执行
    public void channelBalanceDeductByOrderStatus() throws Exception{
//        log.info("----------------------------------TaskChannelBalanceDeduct.channelBalanceDeductByOrderStatus()----start");
        // 获取未跑的数据
        StatusModel statusQuery = TaskMethod.assembleTaskStatusQuery(limitNum, 1, 0, 1, 0, 0, 0);
        List<ChannelBalanceDeductModel> synchroList = ComponentUtil.taskChannelBalanceDeductService.getDataList(statusQuery);
        for (ChannelBalanceDeductModel data : synchroList){
            StatusModel statusModel = null;
            try{
                // 锁住这个数据流水
                String lockKey = CachedKeyUtils.getCacheKey(CacheKey.LOCK_CHANNEL_BALANCE_DEDUCT_BY_ORDER_STATUS, data.getId());
                boolean flagLock = ComponentUtil.redisIdService.lock(lockKey);
                if (flagLock){

                    if (data.getOrderStatus() == 4){
                        // 订单状态是成功的

                        // 任务更新成功状态
                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 3, 0,  0,0,null);
                    }else if (data.getOrderStatus() == 2){
                        // 订单状态是失败的

                        // 锁住要执行的渠道ID
                        String lockKey_channel = CachedKeyUtils.getCacheKey(CacheKey.LOCK_TASK_WORK_TYPE_CHANNEL, data.getChannelId());
                        boolean flagLock_channel = ComponentUtil.redisIdService.lock(lockKey_channel);
                        if (flagLock_channel){
                            // 组装渠道余额加扣款流水的金额
                            ChannelModel updateBalance = HodgepodgeMethod.assembleChannelBalanceAdd(data.getChannelId(), data.getMoney());

                            // 正式处理逻辑-返还渠道扣款金额
                            int num = ComponentUtil.channelService.updateBalance(updateBalance);
                            if (num > 0){
                                statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 3, 0,  0,0,null);
                            }else {
                                statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0,  0,0,null);
                            }
                            log.info("");
                            // 解锁
                            ComponentUtil.redisIdService.delLock(lockKey_channel);
                        }
                    }

                    // 更新状态
                    ComponentUtil.taskChannelBalanceDeductService.updateStatus(statusModel);

                    // 解锁
                    ComponentUtil.redisIdService.delLock(lockKey);
                }

//                log.info("----------------------------------TaskChannelBalanceDeduct.channelBalanceDeductByOrderStatus()----end");
            }catch (Exception e){
                log.error(String.format("this TaskChannelBalanceDeduct.channelBalanceDeductByOrderStatus() is error , the dataId=%s !", data));
                e.printStackTrace();
                // 更新此次task的状态：更新成失败
                statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0, 0,0,"异常失败try!");
                ComponentUtil.taskChannelBalanceDeductService.updateStatus(statusModel);
            }
        }
    }



    /**
     * @Description: 检测渠道扣款流水运行状态属于初始化的总金额
     * <p>
     *     每3秒执行运行一次
     *     1.查询所有渠道信息。
     *     2.求和渠道扣款流水中run_status=0的金额
     *     3.更新渠道的锁定金额
     * </p>
     * @author yoko
     * @date 2019/12/6 20:25
     */
//    @Scheduled(fixedDelay = 1000) // 每1秒执行
    @Scheduled(fixedDelay = 3000) // 每3秒执行
    public void sumMoneyChannelBalanceDeduct() throws Exception{
//        log.info("----------------------------------TaskChannelBalanceDeduct.sumMoneyChannelBalanceDeduct()----start");
        // 获取所有渠道的数据
        List<ChannelModel> synchroList = ComponentUtil.channelService.findByCondition(new ChannelModel());
        for (ChannelModel data : synchroList){
            try{
                ChannelBalanceDeductModel channelBalanceDeductModel = HodgepodgeMethod.assembleChannelBalanceDeduct(0, data.getId(), null, 2, null,
                        0,null,null,0);
                String lockMoney = ComponentUtil.channelBalanceDeductService.sumMoney(channelBalanceDeductModel);
                // 正式更新渠道的锁定金额
                ChannelModel channelUpdate = HodgepodgeMethod.assembleLockMoneyUpdate(data.getId(), lockMoney);
                ComponentUtil.channelService.updateLockMoney(channelUpdate);

//                log.info("----------------------------------TaskChannelBalanceDeduct.sumMoneyChannelBalanceDeduct()----end");
            }catch (Exception e){
                log.error(String.format("this TaskChannelBalanceDeduct.sumMoneyChannelBalanceDeduct() is error , the dataId=%s !", data));
                e.printStackTrace();
            }
        }
    }

}
