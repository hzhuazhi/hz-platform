package com.hz.platform.master.core.runner;

import com.hz.platform.master.core.common.utils.constant.CacheKey;
import com.hz.platform.master.core.common.utils.constant.CachedKeyUtils;
import com.hz.platform.master.core.model.agent.AgentModel;
import com.hz.platform.master.core.model.geway.GewayModel;
import com.hz.platform.master.core.model.geway.GewayProfitModel;
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
 * @Description task:通道收益数据
 * @Author yoko
 * @Date 2021/1/21 15:15
 * @Version 1.0
 */
@Component
@EnableScheduling
public class TaskGewayProfit {

    private final static Logger log = LoggerFactory.getLogger(TaskGewayProfit.class);

    @Value("${task.limit.num}")
    private int limitNum;



    /**
     * 10分钟
     */
    public long TEN_MIN = 10;


    /**
     * @Description: 处理通道收益
     * <p>
     *     通道是预付款的通道，则它的余额需要核减。
     *     总额需要累加。
     * </p>
     * @author yoko
     * @date 2019/12/6 20:25
     */
    @Scheduled(fixedDelay = 5000) // 每5秒执行
    public void handleProfit() throws Exception{
//        log.info("----------------------------------TaskGewayProfit.handleProfit()----start");
        // 获取订单补单数据
        StatusModel statusQuery = TaskMethod.assembleStatusQuery(limitNum);
        List<GewayProfitModel> synchroList = ComponentUtil.taskGewayProfitService.getDataList(statusQuery);
        for (GewayProfitModel data : synchroList){
            try{

                // 锁住这个数据流水
                String lockKey = CachedKeyUtils.getCacheKey(CacheKey.LOCK_GEWAY_PROFIT, data.getId());
                boolean flagLock = ComponentUtil.redisIdService.lock(lockKey);
                if (flagLock){
                    StatusModel statusModel = null;
                    String lockKey_geway = CachedKeyUtils.getCacheKey(CacheKey.LOCK_GEWAY_MONEY, data.getGewayId());
                    boolean flagLock_geway = ComponentUtil.redisIdService.lock(lockKey_geway);
                    if (flagLock_geway){
                        // 组装通道余额核减金额
                        GewayModel updateBalance = HodgepodgeMethod.assembleGewayBalanceSubtract(data.getGewayId(), data.getMoney());
                        // 正式处理逻辑
                        int num = ComponentUtil.gewayService.updateMoney(updateBalance);
                        if (num > 0){
                            statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 3, 0,  0,0,null);
                        }else {
                            statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0,  0,0,null);
                        }

                        // 更新状态
                        ComponentUtil.taskGewayProfitService.updateStatus(statusModel);
                        // 解锁
                        ComponentUtil.redisIdService.delLock(lockKey);
                    }


                }

//                log.info("----------------------------------TaskGewayProfit.handleProfit()----end");
            }catch (Exception e){
                log.error(String.format("this TaskGewayProfit.handleProfit() is error , the dataId=%s !", data.getId()));
                e.printStackTrace();
                // 更新此次task的状态：更新成失败：因为必填项没数据
                StatusModel statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0, 0, 0,0,"异常失败try!");
                ComponentUtil.taskGewayProfitService.updateStatus(statusModel);
            }
        }
    }

}
