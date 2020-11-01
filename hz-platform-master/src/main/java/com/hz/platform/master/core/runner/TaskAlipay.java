package com.hz.platform.master.core.runner;

import com.hz.platform.master.core.common.utils.StringUtil;
import com.hz.platform.master.core.common.utils.constant.CacheKey;
import com.hz.platform.master.core.common.utils.constant.CachedKeyUtils;
import com.hz.platform.master.core.common.utils.constant.ServerConstant;
import com.hz.platform.master.core.model.channel.ChannelModel;
import com.hz.platform.master.core.model.channeldata.ChannelDataModel;
import com.hz.platform.master.core.model.channelgeway.ChannelGewayModel;
import com.hz.platform.master.core.model.datacore.DataCoreModel;
import com.hz.platform.master.core.model.geway.GewayModel;
import com.hz.platform.master.core.model.task.TaskAlipayNotifyModel;
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
 * @Description 阿里支付宝的task类
 * @Author yoko
 * @Date 2020/1/9 19:28
 * @Version 1.0
 */
@Component
@EnableScheduling
public class TaskAlipay {
    private final static Logger log = LoggerFactory.getLogger(TaskAlipay.class);


//    @Value("${task.limit.num}")
//    private int limitNum;
//
//
//
//    /**
//     * @Description: 阿里云支付：用户支付
//     * @author yoko
//     * @date 2019/12/27 21:30
//     */
////    @Scheduled(cron = "1 * * * * ?")
//    @Scheduled(fixedDelay = 1000) // 每秒执行
//    public void taskAlipay(){
//        // 查询要跑的数据
//        StatusModel statusQuery = TaskMethod.assembleTaskAlipayNotifyStatusQuery(limitNum);
//        List<TaskAlipayNotifyModel> dataList = ComponentUtil.taskService.getTaskAlipayNotify(statusQuery);
//        if (dataList != null && dataList.size() > ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO){
//            for (TaskAlipayNotifyModel dataModel : dataList){
//                //锁住这个订单交易流水
//                String lockKey = CachedKeyUtils.getCacheKey(CacheKey.LOCK_TASK_ALIPAY_NOTIFY, dataModel.getId());
//                boolean flagLock = ComponentUtil.redisIdService.lock(lockKey);
//                if (flagLock){
//                    try {
//                        int num = 0;
//                        // start
//                        // 查询此数据属于哪个订单
//                        ChannelDataModel channelDataModel = new ChannelDataModel();
//                        channelDataModel.setMyTradeNo(dataModel.getOutTradeNo());
//                        channelDataModel = (ChannelDataModel) ComponentUtil.channelDataService.findByObject(channelDataModel);
//                        if (channelDataModel == null){
//                            // 更新此次task的状态：更新成失败
//                            StatusModel statusModel = TaskMethod.assembleUpdateStatusModel(dataModel.getId(), ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
//                            ComponentUtil.taskService.updateTaskAlipayNotifyStatus(statusModel);
//                            return;
//                        }
//
//                        // 查询渠道信息
//                        ChannelModel channelModel = (ChannelModel) ComponentUtil.channelService.findById(channelDataModel.getChannelId());
//                        if (channelModel == null){
//                            // 更新此次task的状态：更新成失败
//                            StatusModel statusModel = TaskMethod.assembleUpdateStatusModel(dataModel.getId(), ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
//                            ComponentUtil.taskService.updateTaskAlipayNotifyStatus(statusModel);
//                            return;
//                        }
//                        // 查询通道信息
//                        GewayModel gewayModel = (GewayModel) ComponentUtil.gewayService.findById(channelDataModel.getGewayId());
//                        if (gewayModel == null){
//                            // 更新此次task的状态：更新成失败
//                            StatusModel statusModel = TaskMethod.assembleUpdateStatusModel(dataModel.getId(), ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
//                            ComponentUtil.taskService.updateTaskAlipayNotifyStatus(statusModel);
//                            return;
//                        }
//
//
//                        // 根据渠道号主键ID以及通道ID查询关联关系
//                        ChannelGewayModel channelGewayModel = new ChannelGewayModel();
//                        channelGewayModel.setChannelId(channelDataModel.getChannelId());
//                        channelGewayModel.setGewayId(channelDataModel.getGewayId());
//                        channelGewayModel = (ChannelGewayModel) ComponentUtil.channelGewayService.findByObject(channelGewayModel);
//
//                        String serviceCharge = "";
//                        if (!StringUtils.isBlank(channelDataModel.getServiceCharge())){
//                            serviceCharge = channelDataModel.getServiceCharge();
//                        }else {
//                            serviceCharge = channelGewayModel.getServiceCharge();
//                        }
//                        serviceCharge = StringUtil.getMultiply(channelDataModel.getTotalAmount(), serviceCharge);
//                        String actualMoney = StringUtil.getBigDecimalSubtractStr(channelDataModel.getTotalAmount(), serviceCharge);
//                        int tradeStatus = 2;
//                        // 组装上游数据
//                        DataCoreModel dataCoreModel = HodgepodgeMethod.assembleDataCoreAlipay(dataModel, channelDataModel, channelGewayModel, channelDataModel.getTotalAmount(), serviceCharge, actualMoney, tradeStatus);
//                        num = ComponentUtil.dataCoreService.add(dataCoreModel);
//
//                        // end
//
//                        if (num != 0){
//                            // 组装更改运行状态的数据：更新成成功
//                            StatusModel statusModel = TaskMethod.assembleUpdateStatusModel(dataModel.getId(), ServerConstant.PUBLIC_CONSTANT.RUN_STATUS_THREE);
//                            ComponentUtil.taskService.updateTaskAlipayNotifyStatus(statusModel);
//                        }
//                        // 更新此次task的状态：更新成失败
//                        StatusModel statusModel = TaskMethod.assembleUpdateStatusModel(dataModel.getId(), ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
//                        ComponentUtil.taskService.updateTaskAlipayNotifyStatus(statusModel);
//                    }catch (Exception e){
//                        log.error(String.format("this TaskAlipay.taskAlipay() is error , the dataId=%s !", dataModel.getId()));
//                        e.printStackTrace();
//                        // 更新此次task的状态：更新成失败
//                        StatusModel statusModel = TaskMethod.assembleUpdateStatusModel(dataModel.getId(), ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
//                        ComponentUtil.taskService.updateTaskAlipayNotifyStatus(statusModel);
//                    }finally {
//                        // 解锁
//                        ComponentUtil.redisIdService.delLock(lockKey);
//                    }
//                }
//
//            }
//        }
//    }
//
//    public static void main(String [] args) throws Exception{
//        TaskAlipay t = new TaskAlipay();
//    }
}
