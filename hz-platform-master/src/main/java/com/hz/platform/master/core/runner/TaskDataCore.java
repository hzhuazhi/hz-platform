package com.hz.platform.master.core.runner;

import com.hz.platform.master.core.common.utils.BeanUtils;
import com.hz.platform.master.core.common.utils.StringUtil;
import com.hz.platform.master.core.common.utils.constant.CacheKey;
import com.hz.platform.master.core.common.utils.constant.CachedKeyUtils;
import com.hz.platform.master.core.model.agent.AgentChannelGewayModel;
import com.hz.platform.master.core.model.agent.AgentProfitModel;
import com.hz.platform.master.core.model.channel.ChannelModel;
import com.hz.platform.master.core.model.datacore.DataCoreModel;
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
 * @Description task:通道回调的成功数据
 * @Author yoko
 * @Date 2021/1/19 18:35
 * @Version 1.0
 */
@Component
@EnableScheduling
public class TaskDataCore {

    private final static Logger log = LoggerFactory.getLogger(TaskDataCore.class);

    @Value("${task.limit.num}")
    private int limitNum;



    /**
     * 10分钟
     */
    public long TEN_MIN = 10;


    /**
     * @Description: task：利益者的收益数据的金额累加
     * <p>
     *     每30每秒运行一次
     *     1.查询未跑的利益者的收益流水信息
     *     2.给利益者收益金额进行累加
     * </p>
     * @author yoko
     * @date 2019/12/6 20:25
     */
//    @Scheduled(cron = "1 * * * * ?")
    @Scheduled(fixedDelay = 30000) // 每30秒执行
    public void handle() throws Exception{
//        log.info("----------------------------------TaskDataCore.handle()----start");

        // 获取未跑的卡商扣款流水信息，并且订单状态不是初始化的
        StatusModel statusQuery = TaskMethod.assembleTaskStatusQuery(limitNum, 1, 0, 0, 0, 0,0,0,null);
        List<DataCoreModel> synchroList = ComponentUtil.taskDataCoreService.getDataList(statusQuery);
        for (DataCoreModel data : synchroList){
            try{
                // 锁住这个数据流水
                String lockKey = CachedKeyUtils.getCacheKey(CacheKey.LOCK_TASK_WORK_TYPE, data.getId());
                boolean flagLock = ComponentUtil.redisIdService.lock(lockKey);
                if (flagLock){
                    StatusModel statusModel = null;
                    boolean flag = false;
                    int workType = 0;
                    // 查询渠道信息
                    ChannelModel channelModel = (ChannelModel)ComponentUtil.channelService.findById(data.getChannelId());
                    if (channelModel.getIsSynchro() == 1){
                        //是否需要数据同步:1需要同步，2不需要同步
                        workType = 1;
                    }else{
                        workType = 2;
                    }

                    // 判断是否是多人分配利益
                    List<AgentProfitModel> agentProfitList = null;
                    if (data.getProfitType() == 2){
                        AgentChannelGewayModel agentChannelGewayModel = new AgentChannelGewayModel();
                        agentChannelGewayModel.setChannelGewayId(data.getChannelGewayId());
                        List<AgentChannelGewayModel> agentChannelGewayList = ComponentUtil.agentChannelGewayService.findByCondition(agentChannelGewayModel);
                        if (agentChannelGewayList != null && agentChannelGewayList.size() > 0){
                            agentProfitList = HodgepodgeMethod.assembleAgentProfitList(agentChannelGewayList, data);
                        }
                    }

                    // 组装通道的

                    if (flagLock_interest){
                        int num = ComponentUtil.interestService.updateAddOrSubtractMoney(interestUpdate);
                        if (num > 0){
                            flag = true;
                        }
                        // 解锁
                        ComponentUtil.redisIdService.delLock(lockKey_interest);
                    }
                    if (flag){
                        // 成功
                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 3, 0, 0, 0,0,null);
                    }else {
                        // 失败
                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0, 0, 0,0,null);
                    }


                    // 更新状态
                    ComponentUtil.taskDataCoreService.updateStatus(statusModel);
                    // 解锁
                    ComponentUtil.redisIdService.delLock(lockKey);
                }

//                log.info("----------------------------------TaskDataCore.handle()----end");
            }catch (Exception e){
                log.error(String.format("this TaskDataCore.handle() is error , the dataId=%s !", data.getId()));
                e.printStackTrace();
                // 更新此次task的状态：更新成失败：因为ERROR
                StatusModel statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0, 0, 0,0,"异常失败try!");
                ComponentUtil.taskDataCoreService.updateStatus(statusModel);
            }
        }
    }
    
}
