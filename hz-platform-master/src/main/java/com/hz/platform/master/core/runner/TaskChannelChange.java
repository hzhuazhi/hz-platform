package com.hz.platform.master.core.runner;

import com.hz.platform.master.core.common.utils.constant.CacheKey;
import com.hz.platform.master.core.common.utils.constant.CachedKeyUtils;
import com.hz.platform.master.core.model.channel.ChannelModel;
import com.hz.platform.master.core.model.channelbalancededuct.ChannelBalanceDeductModel;
import com.hz.platform.master.core.model.channelchange.ChannelChangeModel;
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
 * @Description task：渠道金额变更纪录
 * @Author yoko
 * @Date 2020/11/4 11:37
 * @Version 1.0
 */
@Component
@EnableScheduling
public class TaskChannelChange {

    private final static Logger log = LoggerFactory.getLogger(TaskChannelChange.class);

    @Value("${task.limit.num}")
    private int limitNum;



    /**
     * 10分钟
     */
    public long TEN_MIN = 10;


    /**
     * @Description: 变更渠道余额的逻辑
     * <p>
     *     每10秒执行运行一次
     *     1.查询未跑的渠道金额变更纪录数据。
     *     2.更新判断是核减金额，还是加金额，然后更新渠道的余额。
     * </p>
     * @author yoko
     * @date 2019/12/6 20:25
     */
//    @Scheduled(fixedDelay = 1000) // 每1秒执行
    @Scheduled(fixedDelay = 10000) // 每10秒执行
    public void changeMoney() throws Exception{
//        log.info("----------------------------------TaskChannelChange.changeMoney()----start");
        // 获取未跑的数据
        StatusModel statusQuery = TaskMethod.assembleTaskStatusQuery(limitNum, 1, 0, 0, 0, 0, 1);
        List<ChannelChangeModel> synchroList = ComponentUtil.taskChannelChangeService.getDataList(statusQuery);
        for (ChannelChangeModel data : synchroList){
            StatusModel statusModel = null;
            try{
                // 锁住这个数据流水
                String lockKey = CachedKeyUtils.getCacheKey(CacheKey.LOCK_CHANNEL_CHANGE, data.getId());
                boolean flagLock = ComponentUtil.redisIdService.lock(lockKey);
                if (flagLock){
                    // 锁住要执行的渠道ID
                    String lockKey_channel = CachedKeyUtils.getCacheKey(CacheKey.LOCK_TASK_WORK_TYPE_CHANNEL, data.getChannelId());
                    boolean flagLock_channel = ComponentUtil.redisIdService.lock(lockKey_channel);
                    if (flagLock_channel){
                        if (data.getChangeType() == 1){
                            // 核减金额
                            // 组装渠道余额核减金额
                            ChannelModel updateBalance = HodgepodgeMethod.assembleChannelBalanceSubtract(data.getChannelId(), data.getMoney());
                            // 正式处理逻辑
                            int num = ComponentUtil.channelService.updateBalance(updateBalance);
                            if (num > 0){
                                statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 3, 0,  0,0,null);
                            }else {
                                statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0,  0,0,null);
                            }

                        }else if (data.getChangeType() == 2){
                            // 加金额
                            // 组装渠道余额加金额
                            ChannelModel updateBalance = HodgepodgeMethod.assembleChannelBalanceAdd(data.getChannelId(), data.getMoney());
                            // 正式处理逻辑
                            int num = ComponentUtil.channelService.updateBalance(updateBalance);
                            if (num > 0){
                                statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 3, 0,  0,0,null);
                            }else {
                                statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0,  0,0,null);
                            }
                        }
                        // 解锁
                        ComponentUtil.redisIdService.delLock(lockKey_channel);

                    }

                    // 更新状态
                    ComponentUtil.taskChannelChangeService.updateStatus(statusModel);

                    // 解锁
                    ComponentUtil.redisIdService.delLock(lockKey);
                }

//                log.info("----------------------------------TaskChannelChange.changeMoney()----end");
            }catch (Exception e){
                log.error(String.format("this TaskChannelChange.changeMoney() is error , the dataId=%s !", data));
                e.printStackTrace();
                // 更新此次task的状态：更新成失败
                statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0, 0,0,"异常失败try!");
                ComponentUtil.taskChannelChangeService.updateStatus(statusModel);
            }
        }
    }

}
