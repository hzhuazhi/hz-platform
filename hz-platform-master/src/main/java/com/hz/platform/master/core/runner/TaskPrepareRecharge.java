package com.hz.platform.master.core.runner;

import com.hz.platform.master.core.common.utils.constant.CacheKey;
import com.hz.platform.master.core.common.utils.constant.CachedKeyUtils;
import com.hz.platform.master.core.model.channelchange.ChannelChangeModel;
import com.hz.platform.master.core.model.preparerecharge.PrepareRechargeModel;
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
 * @author yoko
 * @desc task:代付预备充值表:预备给渠道进行充值
 * @create 2021-11-18 14:02
 **/
@Component
@EnableScheduling
public class TaskPrepareRecharge {

    private final static Logger log = LoggerFactory.getLogger(TaskPrepareRecharge.class);

    @Value("${task.limit.num}")
    private int limitNum;



    /**
     * 10分钟
     */
    public long TEN_MIN = 10;



    /**
     * @Description: 处理代付预备充值信息
     * <p>
     *     1.查询出未跑的代付预备充值信息。
     *     2.把预备充值信息进行组装，组装成渠道金额变更数据，然后把数据添加到渠道金额变更表中。
     *     3.更新未跑的代付预备充值信息的运行状态。
     * </p>
     * @author yoko
     * @date 2019/12/6 20:25
     */
    @Scheduled(fixedDelay = 5000) // 每5秒执行
    public void handlePrepareRecharge() throws Exception{
//        log.info("----------------------------------TaskPrepareRecharge.handlePrepareRecharge()----start");
        // 获取未跑的代付预备充值信息
        StatusModel statusQuery = TaskMethod.assembleTaskStatusQuery(limitNum, 1, 3, 0, 0, 0, 0);
        List<PrepareRechargeModel> synchroList = ComponentUtil.taskPrepareRechargeService.getDataList(statusQuery);
        for (PrepareRechargeModel data : synchroList){
            StatusModel statusModel = null;
            try{

                // 锁住这个数据流水
                String lockKey = CachedKeyUtils.getCacheKey(CacheKey.LOCK_PREPARE_RECHARGE, data.getId());
                boolean flagLock = ComponentUtil.redisIdService.lock(lockKey);
                if (flagLock){
                    // 组装渠道金额变更的数据
                    ChannelChangeModel channelChangeAdd = HodgepodgeMethod.assembleChannelChangeAdd(data);
                    if (channelChangeAdd != null){
                        // 正式处理逻辑
                        int num = ComponentUtil.channelChangeService.add(channelChangeAdd);
                        if (num > 0){
                            statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 3, 0,  0,0,null);
                        }else {
                            statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0,  0,0,null);
                        }
                    }else{
                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0,  0,0,null);
                    }

                    // 更新状态
                    ComponentUtil.taskPrepareRechargeService.updateStatus(statusModel);
                    // 解锁
                    ComponentUtil.redisIdService.delLock(lockKey);

                }

//                log.info("----------------------------------TaskPrepareRecharge.handlePrepareRecharge()----end");
            }catch (Exception e){
                log.error(String.format("this TaskGewTaskPrepareRechargeayProfit.handlePrepareRecharge() is error , the dataId=%s !", data.getId()));
                e.printStackTrace();
                // 更新此次task的状态：更新成失败：因为必填项没数据
                statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0, 0, 0,0,"异常失败try!");
                ComponentUtil.taskPrepareRechargeService.updateStatus(statusModel);
            }
        }
    }

}
