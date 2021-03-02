package com.hz.platform.master.core.runner;

import com.hz.platform.master.core.common.utils.constant.CacheKey;
import com.hz.platform.master.core.common.utils.constant.CachedKeyUtils;
import com.hz.platform.master.core.model.channelbalancededuct.ChannelBalanceDeductModel;
import com.hz.platform.master.core.model.channelout.ChannelOutModel;
import com.hz.platform.master.core.model.datacoreout.DataCoreOutModel;
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
 * @Description task：代付订单数据回传
 * @Author yoko
 * @Date 2020/11/1 14:38
 * @Version 1.0
 */
@Component
@EnableScheduling
public class TaskDataCoreOut {


    private final static Logger log = LoggerFactory.getLogger(TaskDataCoreOut.class);

    @Value("${task.limit.num}")
    private int limitNum;



    /**
     * 10分钟
     */
    public long TEN_MIN = 10;


    /**
     * @Description: 检测代付订单上游回传结果，进行运算
     * <p>
     *     每5秒执行运行一次
     *     1.查询未跑的上游代付订单回传数据。
     *     2.更新渠道代付扣款流水的订单状态。
     *     3.更新渠道代付订单的订单状态。
     * </p>
     * @author yoko
     * @date 2019/12/6 20:25
     */
//    @Scheduled(fixedDelay = 1000) // 每1秒执行
    @Scheduled(fixedDelay = 5000) // 每5秒执行
    public void tradeStatus() throws Exception{
//        log.info("----------------------------------TaskDataCoreOut.tradeStatus()----start");
        // 获取未跑的数据
        StatusModel statusQuery = TaskMethod.assembleTaskStatusQuery(limitNum, 1, 0, 0, 0, 0, 0);
        List<DataCoreOutModel> synchroList = ComponentUtil.taskDataCoreOutService.getDataList(statusQuery);
        for (DataCoreOutModel data : synchroList){
            StatusModel statusModel = null;
            try{
                // 锁住这个数据流水
                String lockKey = CachedKeyUtils.getCacheKey(CacheKey.LOCK_DATA_CORE_OUT, data.getId());
                boolean flagLock = ComponentUtil.redisIdService.lock(lockKey);
                if (flagLock){
                    int orderStatus = 0;
                    if (data.getTradeStatus() == 1){
                        orderStatus = 4;
                    }else if (data.getTradeStatus() == 2){
                        orderStatus = 2;
                    }

                    // 组装通道收益
                    GewayProfitModel gewayProfitModel = null;
                    GewayModel gewayQuery = new GewayModel();
                    gewayQuery.setId(data.getGewayId());
                    GewayModel gewayModel = (GewayModel)ComponentUtil.gewayService.findByObject(gewayQuery);
                    if (gewayModel != null && gewayModel.getId() != null && gewayModel.getId() > 0){
                        gewayProfitModel = HodgepodgeMethod.assembleGewayProfitByOutOrder(data, 2, gewayModel.getGewayType());
                    }



                    // 组装更新渠道订单扣款流水的数据
                    ChannelBalanceDeductModel updateChannelBalanceDeduct = HodgepodgeMethod.assembleChannelBalanceDeduct(0,0, data.getMyTradeNo(),
                            0, null, orderStatus,null, null, 0);

                    ChannelOutModel updateChannelOut = HodgepodgeMethod.assembleChannelOutUpdate(data.getMyTradeNo(), orderStatus, data.getPictureAds(), data.getFailInfo());
                    boolean flag  = ComponentUtil.taskDataCoreOutService.handleDataCoreOut(updateChannelBalanceDeduct, updateChannelOut, gewayProfitModel);
//                    段峰
                    if (flag){
                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 3, 0,  0,0,null);
                    }else {
                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0,  0,0,null);
                    }

                    // 更新状态
                    ComponentUtil.taskDataCoreOutService.updateStatus(statusModel);

                    // 解锁
                    ComponentUtil.redisIdService.delLock(lockKey);
                }

//                log.info("----------------------------------TaskDataCoreOut.tradeStatus()----end");
            }catch (Exception e){
                log.error(String.format("this TaskDataCoreOut.tradeStatus() is error , the dataId=%s !", data));
                e.printStackTrace();
                // 更新此次task的状态：更新成失败
                statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0, 0,0,"异常失败try!");
                ComponentUtil.taskDataCoreOutService.updateStatus(statusModel);
            }
        }
    }

}
