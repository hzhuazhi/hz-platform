package com.hz.platform.master.core.runner;

import com.hz.platform.master.core.common.utils.constant.CacheKey;
import com.hz.platform.master.core.common.utils.constant.CachedKeyUtils;
import com.hz.platform.master.core.model.agent.AgentModel;
import com.hz.platform.master.core.model.channel.ChannelModel;
import com.hz.platform.master.core.model.channel.ChannelProfitModel;
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
 * @Description task:渠道收益数据
 * @Author yoko
 * @Date 2021/1/21 15:16
 * @Version 1.0
 */
@Component
@EnableScheduling
public class TaskChannelProfit {

    private final static Logger log = LoggerFactory.getLogger(TaskChannelProfit.class);

    @Value("${task.limit.num}")
    private int limitNum;



    /**
     * 10分钟
     */
    public long TEN_MIN = 10;


    /**
     * @Description: 处理渠道收益
     * @author yoko
     * @date 2019/12/6 20:25
     */
    @Scheduled(fixedDelay = 5000) // 每5秒执行
    public void handleProfit() throws Exception{
//        log.info("----------------------------------TaskChannelProfit.handleProfit()----start");
        // 获取订单补单数据
        StatusModel statusQuery = TaskMethod.assembleStatusQuery(limitNum);
        List<ChannelProfitModel> synchroList = ComponentUtil.taskChannelProfitService.getDataList(statusQuery);
        for (ChannelProfitModel data : synchroList){
            try{

                // 锁住这个数据流水
                String lockKey = CachedKeyUtils.getCacheKey(CacheKey.LOCK_CHANNEL_PROFIT, data.getId());
                boolean flagLock = ComponentUtil.redisIdService.lock(lockKey);
                if (flagLock){
                    StatusModel statusModel = null;

                    String lockKey_channel = CachedKeyUtils.getCacheKey(CacheKey.LOCK_TASK_WORK_TYPE_CHANNEL, data.getChannelId());
                    boolean flagLock_channel = ComponentUtil.redisIdService.lock(lockKey_channel);
                    if (flagLock_channel){
                        // 更新渠道的金额
                        ChannelModel channelUpdate = HodgepodgeMethod.assembleUpdateChannelMoney(data.getChannelId(), data.getMoney());
                        int num = ComponentUtil.channelService.updateChannelMoney(channelUpdate);
                        if (num > 0){
                            statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 3, 0,  0,0,null);
                        }else{
                            statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0,  0,0,null);
                        }

                        // 更新状态
                        ComponentUtil.taskChannelProfitService.updateStatus(statusModel);
                        // 解锁
                        ComponentUtil.redisIdService.delLock(lockKey);
                    }


                }

//                log.info("----------------------------------TaskChannelProfit.handleProfit()----end");
            }catch (Exception e){
                log.error(String.format("this TaskChannelProfit.handleProfit() is error , the dataId=%s !", data.getId()));
                e.printStackTrace();
                // 更新此次task的状态：更新成失败：因为必填项没数据
                StatusModel statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0, 0, 0,0,"异常失败try!");
                ComponentUtil.taskChannelProfitService.updateStatus(statusModel);
            }
        }
    }
}
