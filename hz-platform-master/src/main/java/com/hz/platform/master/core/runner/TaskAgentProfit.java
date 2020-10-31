package com.hz.platform.master.core.runner;

import com.hz.platform.master.core.common.utils.constant.CacheKey;
import com.hz.platform.master.core.common.utils.constant.CachedKeyUtils;
import com.hz.platform.master.core.model.agent.AgentModel;
import com.hz.platform.master.core.model.agent.AgentProfitModel;
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
 * @Description task:代理收益数据
 * @Author yoko
 * @Date 2020/9/17 22:12
 * @Version 1.0
 */
@Component
@EnableScheduling
public class TaskAgentProfit {

    private final static Logger log = LoggerFactory.getLogger(TaskAgentProfit.class);

    @Value("${task.limit.num}")
    private int limitNum;



    /**
     * 10分钟
     */
    public long TEN_MIN = 10;


    /**
     * @Description: 处理代理收益
     * @author yoko
     * @date 2019/12/6 20:25
     */
    @Scheduled(fixedDelay = 3000) // 每3秒执行
    public void handleProfit() throws Exception{
//        log.info("----------------------------------TaskAgentProfit.handleProfit()----start");
        // 获取订单补单数据
        StatusModel statusQuery = TaskMethod.assembleStatusQuery(limitNum);
        List<AgentProfitModel> synchroList = ComponentUtil.taskAgentProfitService.getDataList(statusQuery);
        for (AgentProfitModel data : synchroList){
            try{

                // 锁住这个数据流水
                String lockKey = CachedKeyUtils.getCacheKey(CacheKey.LOCK_AGENT_PROFIT, data.getId());
                boolean flagLock = ComponentUtil.redisIdService.lock(lockKey);
                if (flagLock){
                    StatusModel statusModel = null;

                    String lockKey_agent = CachedKeyUtils.getCacheKey(CacheKey.LOCK_TASK_WORK_TYPE_AGENT, data.getAgentId());
                    boolean flagLock_agent = ComponentUtil.redisIdService.lock(lockKey_agent);
                    if (flagLock_agent){
                        // 更新代理的金额
                        AgentModel agentUpdate = HodgepodgeMethod.assembleAgentUpdate(data.getAgentId(), data.getProfit());
                        int num = ComponentUtil.agentService.updateMoney(agentUpdate);
                        if (num > 0){
                            statusModel = TaskMethod.assembleUpdateStatusModel(data.getId(), 3);
                        }else{
                            statusModel = TaskMethod.assembleUpdateStatusModel(data.getId(), 2);
                        }

                        // 更新状态
                        ComponentUtil.taskAgentProfitService.updateStatus(statusModel);
                        // 解锁
                        ComponentUtil.redisIdService.delLock(lockKey);
                    }


                }

//                log.info("----------------------------------TaskAgentProfit.handleProfit()----end");
            }catch (Exception e){
                log.error(String.format("this TaskAgentProfit.handleProfit() is error , the dataId=%s !", data.getId()));
                e.printStackTrace();
                // 更新此次task的状态：更新成失败：因为必填项没数据
                StatusModel statusModel = TaskMethod.assembleUpdateStatusModel(data.getId(), 2);
                ComponentUtil.taskAgentProfitService.updateStatus(statusModel);
            }
        }
    }
}
