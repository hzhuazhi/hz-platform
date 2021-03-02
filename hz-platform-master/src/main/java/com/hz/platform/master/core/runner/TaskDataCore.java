package com.hz.platform.master.core.runner;

import com.hz.platform.master.core.common.utils.BeanUtils;
import com.hz.platform.master.core.common.utils.HttpSendUtils;
import com.hz.platform.master.core.common.utils.MD5Util;
import com.hz.platform.master.core.common.utils.StringUtil;
import com.hz.platform.master.core.common.utils.constant.CacheKey;
import com.hz.platform.master.core.common.utils.constant.CachedKeyUtils;
import com.hz.platform.master.core.common.utils.constant.ServerConstant;
import com.hz.platform.master.core.model.agent.AgentChannelGewayModel;
import com.hz.platform.master.core.model.agent.AgentProfitModel;
import com.hz.platform.master.core.model.channel.ChannelModel;
import com.hz.platform.master.core.model.channel.ChannelProfitModel;
import com.hz.platform.master.core.model.datacore.DataCoreModel;
import com.hz.platform.master.core.model.geway.GewayModel;
import com.hz.platform.master.core.model.geway.GewayProfitModel;
import com.hz.platform.master.core.model.task.base.StatusModel;
import com.hz.platform.master.util.ComponentUtil;
import com.hz.platform.master.util.HodgepodgeMethod;
import com.hz.platform.master.util.TaskMethod;
import org.apache.commons.lang.StringUtils;
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
     * @Description: task：通道回调成功订单的逻辑处理
     * <p>
     *     每3每秒运行一次
     *     1.查询未跑的成功订单信息
     *     2.计算添加利益者的利润数据
     *     3.针对预付款通道进行数据添加
     *     4.添加渠道金额收益数据
     * </p>
     * @author yoko
     * @date 2019/12/6 20:25
     */
//    @Scheduled(cron = "1 * * * * ?")
    @Scheduled(fixedDelay = 3000) // 每3秒执行
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

                    // 组装通道的收益
                    GewayProfitModel gewayProfitModel = null;
                    GewayModel gewayQuery = new GewayModel();
                    gewayQuery.setId(data.getGewayId());
                    GewayModel gewayModel = (GewayModel)ComponentUtil.gewayService.findByObject(gewayQuery);
                    if (gewayModel != null && gewayModel.getId() != null && gewayModel.getId() > 0){
                        if (gewayModel.getGewayType() == 2){
                            // 表示预付通道
                            gewayProfitModel = HodgepodgeMethod.assembleGewayProfit(data, 1, 2);
                        }
                    }

                    // 组装渠道的收益
                    ChannelProfitModel channelProfitModel = HodgepodgeMethod.assembleChannelProfit(data);

                    // 正式处理成功订单逻辑
                    flag = ComponentUtil.taskDataCoreService.handleSuccessOrder(agentProfitList, gewayProfitModel, channelProfitModel);

                    if (flag){
                        // 成功
                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 3, workType, 0, 0,0,null);
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



    /**
     * @Description: 同步数据给下游
     * <p>
     *     同步字段
     * </p>
     * @author yoko
     * @date 2019/12/27 21:30
     */
//    @Scheduled(cron = "1 * * * * ?")
    @Scheduled(fixedDelay = 1000) // 每秒执行
    public void notifyData(){
        // 查询要跑的数据
        StatusModel statusQuery = TaskMethod.assembleTaskStatusQuery(limitNum, 0, 1, 0, 2, 0,1,0,null);
        List<DataCoreModel> dataList = ComponentUtil.taskDataCoreService.getDataList(statusQuery);
        if (dataList != null && dataList.size() > 0){
            for (DataCoreModel data : dataList){
                //锁住这个task
                String lockKey = CachedKeyUtils.getCacheKey(CacheKey.LOCK_TASK_NOTIFY, data.getId());
                boolean flagLock = ComponentUtil.redisIdService.lock(lockKey);
                if (flagLock){
                    try {
                        StatusModel statusModel = null;
                        boolean flag = false;
                        // 查询渠道信息
                        ChannelModel channelModel = (ChannelModel) ComponentUtil.channelService.findById(data.getChannelId());
                        String notify_suc = "";
                        if (!StringUtils.isBlank(channelModel.getLowerSuc())){
                            notify_suc = channelModel.getLowerSuc();
                        }else {
                            notify_suc = "OK";
                        }
                        String pay_amount = "";
                        if (!StringUtils.isBlank(data.getPayAmount())){
                            pay_amount = data.getPayAmount();
                        }
                        // 执行发送数据
                        String sign = "total_amount=" + data.getTotalAmount() + "&" + "out_trade_no=" + data.getOutTradeNo() + "&" + "trade_status=" + data.getTradeStatus()
                                + "&" + "key=" + channelModel.getSecretKey();
                        sign = MD5Util.encryption(sign);
                        String sendUrl = data.getNotifyUrl();// 需要发送给下游的接口
                        String sendData = "?total_amount=" + data.getTotalAmount() + "&" + "pay_amount=" + data.getPayAmount() + "&" + "out_trade_no=" + data.getOutTradeNo() + "&" + "trade_status=" + data.getTradeStatus()
                                + "&" + "trade_no=" + data.getMyTradeNo() + "&" + "extra_return_param=" + data.getXyExtraReturnParam() + "&" + "sign=" + sign
                                + "&" + "trade_time=" + System.currentTimeMillis();
//                        String resp = HttpSendUtils.sendGet(sendUrl + URLEncoder.encode(sendData,"UTF-8"), null, null);
                        log.info("sendUrl:" + sendUrl + sendData);
                        String resp = HttpSendUtils.sendGet(sendUrl + sendData, null, null);
//                        flag = resp.equals(notify_suc);
                        if (resp.equals(notify_suc)){
                            // 成功
                            statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 0, 0, 3,0,null);
                        }else {
                            // 失败
                            statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 0, 0, 2,0,null);
                        }

                        // 更新状态
                        ComponentUtil.taskDataCoreService.updateStatus(statusModel);


                    }catch (Exception e){
                        // 组装更改运行状态的数据：更新成失败
                        StatusModel statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 0, 0, 2,0,"异常失败try!");
                        ComponentUtil.taskDataCoreService.updateStatus(statusModel);
                        log.error(String.format("this TaskDataCore.notifyData() is error , the dataId=%s !", data.getId()));
                        e.printStackTrace();
                    }finally {
                        // 解锁
                        ComponentUtil.redisIdService.delLock(lockKey);
                    }
                }

            }
        }
    }

    
}
