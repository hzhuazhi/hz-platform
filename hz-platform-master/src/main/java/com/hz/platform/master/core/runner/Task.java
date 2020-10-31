package com.hz.platform.master.core.runner;

import com.hz.platform.master.core.common.utils.BeanUtils;
import com.hz.platform.master.core.common.utils.HttpSendUtils;
import com.hz.platform.master.core.common.utils.MD5Util;
import com.hz.platform.master.core.common.utils.StringUtil;
import com.hz.platform.master.core.common.utils.constant.CacheKey;
import com.hz.platform.master.core.common.utils.constant.CachedKeyUtils;
import com.hz.platform.master.core.common.utils.constant.ServerConstant;
import com.hz.platform.master.core.model.agent.AgentChannelGewayModel;
import com.hz.platform.master.core.model.agent.AgentModel;
import com.hz.platform.master.core.model.agent.AgentProfitModel;
import com.hz.platform.master.core.model.channel.ChannelModel;
import com.hz.platform.master.core.model.datacore.DataCoreModel;
import com.hz.platform.master.core.model.task.base.StatusModel;
import com.hz.platform.master.core.model.withdraw.WithdrawModel;
import com.hz.platform.master.util.ComponentUtil;
import com.hz.platform.master.util.TaskMethod;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

/**
 * @Description 大杂烩的task类
 * @Author yoko
 * @Date 2020/3/11 20:31
 * @Version 1.0
 */
@Component
@EnableScheduling
public class Task {
    private final static Logger log = LoggerFactory.getLogger(Task.class);


    @Value("${task.limit.num}")
    private int limitNum;

    /**
     * @Description: 扣量
     * <p>
     *     1.查询出所有work_type=0的数据。
     *     2.进行扣量计算：如果是扣量的则修改成扣量的状态；如果不是扣量的，则往渠道的收益添加金额数据
     * </p>
     * @author yoko
     * @date 2020/3/11 20:34
    */
//    @Scheduled(cron = "0 0 1 * * ?")
//    @Scheduled(cron = "1 * * * * ?")
    @Scheduled(fixedDelay = 1000) // 每秒执行
    public void deductRatioData(){
        StatusModel statusQuery = TaskMethod.assembleDeductRatioStatusQuery(limitNum, 0);
        List<DataCoreModel> synchroList = ComponentUtil.taskService.getWorkTypeList(statusQuery);
        for (DataCoreModel data : synchroList){
            if (data != null){
                try{
                    // 锁住这个task的ID
                    String lockKey = CachedKeyUtils.getCacheKey(CacheKey.LOCK_TASK_WORK_TYPE, data.getId());
                    boolean flagLock = ComponentUtil.redisIdService.lock(lockKey);
                    // 锁住要执行的渠道ID
                    String ch_lockKey = CachedKeyUtils.getCacheKey(CacheKey.LOCK_TASK_WORK_TYPE_CHANNEL, data.getChannelId());
                    boolean ch_flagLock = ComponentUtil.redisIdService.lock(ch_lockKey);
                    if (flagLock && ch_flagLock){
                        int workType = 0;
                        int runStatus = 0;
                        // 扣量计算
                        boolean deductFlag = false;//是否要进行扣量：false不扣量，true扣量
                        if (data.getDeductRatio() != null && data.getDeductRatio() > 0){
                            //进行扣量计算
                            int random = new Random().nextInt(100);
                            if (random < data.getDeductRatio()){
                                deductFlag = true;
                            }
                        }
                        // 查询渠道信息
                        ChannelModel channelModel = (ChannelModel)ComponentUtil.channelService.findById(data.getChannelId());
                        if (channelModel.getIsSynchro() == 1){
                            //是否需要数据同步:1需要同步，2不需要同步
                            workType = 1;
                        }else{
                            workType = 2;
                        }
                        // 判断是否扣量
                        if (deductFlag){
                            runStatus = 4;
                        }
                        StatusModel upWorkType = new StatusModel();
                        upWorkType.setId(data.getId());
                        upWorkType.setWorkType(workType);
                        upWorkType.setRunStatus(runStatus);
                        ComponentUtil.taskService.updateWorkType(upWorkType);


                        // 判断是否是多人分配利益
                        if (data.getProfitType() == 2){
                            AgentChannelGewayModel agentChannelGewayModel = new AgentChannelGewayModel();
                            agentChannelGewayModel.setChannelGewayId(data.getChannelGewayId());
                            List<AgentChannelGewayModel> agentChannelGewayList = ComponentUtil.agentChannelGewayService.findByCondition(agentChannelGewayModel);
                            if (agentChannelGewayList != null && agentChannelGewayList.size() > 0){
                                for (AgentChannelGewayModel ag_data : agentChannelGewayList){
//                                    String profit = StringUtil.getMultiply(data.getServiceCharge(), ag_data.getServiceCharge());
                                    String profit = StringUtil.getMultiply(data.getPayAmount(), ag_data.getServiceCharge());
                                    AgentProfitModel agentProfitModel = BeanUtils.copy(data, AgentProfitModel.class);
                                    agentProfitModel.setId(null);
                                    agentProfitModel.setAgentId(ag_data.getAgentId());
                                    agentProfitModel.setProfitRatio(ag_data.getServiceCharge());
                                    agentProfitModel.setProfit(profit);
                                    agentProfitModel.setRunNum(null);
                                    agentProfitModel.setRunStatus(null);
                                    ComponentUtil.agentProfitService.add(agentProfitModel);
                                }
                            }
                        }


                        if (!deductFlag){
                            // 更新渠道的金额
                            String money = "";
                            if (data.getMoneyFitType() == 4){
                                // 金额一致
                                money = data.getActualMoney();
                            }else if (data.getMoneyFitType() == 1){
                                // 老版本数据
                                money = data.getActualMoney();
                            }else{
                                // 金额多了，或者少了，使用实际支付金额
                                money = data.getPayActualMoney();
                            }

                            StatusModel chMoney = new StatusModel();
                            chMoney.setId(data.getChannelId());
                            chMoney.setMoney(money);
                            ComponentUtil.taskService.updateChannelMoney(chMoney);
                        }
                    }
                    // 解锁
                    ComponentUtil.redisIdService.delLock(lockKey);
                    ComponentUtil.redisIdService.delLock(ch_lockKey);
                }catch (Exception e){
                    log.error(String.format("this Task.deductRatioData() is error , the id=%s !", data.getId()));
                    e.printStackTrace();
                }
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
        StatusModel statusQuery = TaskMethod.assembleNotifyStatusQuery(limitNum, 1);
        List<DataCoreModel> dataList = ComponentUtil.taskService.getNotifyList(statusQuery);
        if (dataList != null && dataList.size() > 0){
            for (DataCoreModel dataModel : dataList){
                //锁住这个task
                String lockKey = CachedKeyUtils.getCacheKey(CacheKey.LOCK_TASK_NOTIFY, dataModel.getId());
                boolean flagLock = ComponentUtil.redisIdService.lock(lockKey);
                if (flagLock){
                    try {
                        // 查询渠道信息
                        ChannelModel channelModel = (ChannelModel) ComponentUtil.channelService.findById(dataModel.getChannelId());
                        String notify_suc = "";
                        if (!StringUtils.isBlank(channelModel.getLowerSuc())){
                            notify_suc = channelModel.getLowerSuc();
                        }else {
                            notify_suc = "OK";
                        }
                        String pay_amount = "";
                        if (!StringUtils.isBlank(dataModel.getPayAmount())){
                            pay_amount = dataModel.getPayAmount();
                        }
                        // 执行发送数据
                        String sign = "total_amount=" + dataModel.getTotalAmount() + "&" + "out_trade_no=" + dataModel.getOutTradeNo() + "&" + "trade_status=" + dataModel.getTradeStatus()
                                + "&" + "key=" + channelModel.getSecretKey();
                        sign = MD5Util.encryption(sign);
                        String sendUrl = dataModel.getNotifyUrl();// 需要发送给下游的接口
                        String sendData = "?total_amount=" + dataModel.getTotalAmount() + "&" + "pay_amount=" + dataModel.getPayAmount() + "&" + "out_trade_no=" + dataModel.getOutTradeNo() + "&" + "trade_status=" + dataModel.getTradeStatus()
                                + "&" + "trade_no=" + dataModel.getMyTradeNo() + "&" + "extra_return_param=" + dataModel.getXyExtraReturnParam() + "&" + "sign=" + sign
                                + "&" + "trade_time=" + System.currentTimeMillis();
//                        String resp = HttpSendUtils.sendGet(sendUrl + URLEncoder.encode(sendData,"UTF-8"), null, null);
                        String resp = HttpSendUtils.sendGet(sendUrl + sendData, null, null);
                        if (resp.equals(notify_suc)){
                            // 成功
                            // 组装更改运行状态的数据：更新成成功
                            StatusModel statusModel = TaskMethod.assembleUpdateStatusModel(dataModel.getId(), ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_THREE);
                            ComponentUtil.taskService.updateNotifyStatus(statusModel);
                        }else {
                            // 组装更改运行状态的数据：更新成失败
                            StatusModel statusModel = TaskMethod.assembleUpdateStatusModel(dataModel.getId(), ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
                            ComponentUtil.taskService.updateNotifyStatus(statusModel);
                        }


                    }catch (Exception e){
                        // 组装更改运行状态的数据：更新成失败
                        StatusModel statusModel = TaskMethod.assembleUpdateStatusModel(dataModel.getId(), ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
                        ComponentUtil.taskService.updateNotifyStatus(statusModel);
                        log.error(String.format("this Task.notifyData() is error , the dataId=%s !", dataModel.getId()));
                        e.printStackTrace();
                    }finally {
                        // 解锁
                        ComponentUtil.redisIdService.delLock(lockKey);
                    }
                }

            }
        }
    }



    /**
     * @Description: 提现
     * <p>
     *     1.用户提现的数据从余额中扣除
     * </p>
     * @author yoko
     * @date 2020/3/11 20:34
     */
//    @Scheduled(cron = "0 0 1 * * ?")
//    @Scheduled(cron = "1 * * * * ?")
    @Scheduled(fixedDelay = 1000) // 每秒执行
    public void withdrawData(){
        StatusModel statusQuery = TaskMethod.assembleWithdrawStatusQuery(limitNum);
        List<WithdrawModel> synchroList = ComponentUtil.taskService.getWithdrawList(statusQuery);
        for (WithdrawModel data : synchroList){
            if (data != null){
                try{
                    // 锁住这个task的ID
                    String lockKey = CachedKeyUtils.getCacheKey(CacheKey.LOCK_TASK_WITHDRAW, data.getId());
                    boolean flagLock = ComponentUtil.redisIdService.lock(lockKey);
                    // 锁住要执行的渠道ID或者是代理ID
                    String str_lockKey = "";
                    if (data.getRoleId() == 2){
                        // 渠道
                        str_lockKey = CachedKeyUtils.getCacheKey(CacheKey.LOCK_TASK_WORK_TYPE_CHANNEL, data.getLinkId());
                    }else if (data.getRoleId() == 3){
                        // 代理
                        str_lockKey = CachedKeyUtils.getCacheKey(CacheKey.LOCK_TASK_WORK_TYPE_AGENT, data.getLinkId());
                    }
                    boolean str_flagLock = ComponentUtil.redisIdService.lock(str_lockKey);
                    if (flagLock && str_flagLock){
                        // 渠道正式提现
                        if (data.getRoleId() == 2){
                            ChannelModel channelModel = (ChannelModel)ComponentUtil.channelService.findById(data.getLinkId());
                            boolean flag_money = StringUtil.getBigDecimalSubtract(channelModel.getBalance(), data.getMoney());
                            if (flag_money){
                                // 余额足够进行提现
                                // 更新渠道的金额
                                String totalMoney = StringUtil.getBigDecimalAdd(data.getMoney(), data.getServiceCharge());
                                StatusModel chMoney = new StatusModel();
                                chMoney.setId(data.getLinkId());
                                chMoney.setMoney(totalMoney);
                                ComponentUtil.taskService.updateChannelReduceMoney(chMoney);
                                // 组装更改运行状态的数据：更新成成功
                                StatusModel statusModel = TaskMethod.assembleUpdateStatusModel(data.getId(), ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_THREE);
                                ComponentUtil.taskService.updateWithdrawStatus(statusModel);
                            }else{
                                // 余额不足提现
                                // 组装更改运行状态的数据：更新成失败
                                StatusModel statusModel = TaskMethod.assembleUpdateStatusModel(data.getId(), ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
                                statusModel.setMoney("检测：余额不足提现!");
                                ComponentUtil.taskService.updateWithdrawStatus(statusModel);
                            }
                        }else if(data.getRoleId() == 3){
                            // 代理正式提现
                            AgentModel agentModel = (AgentModel)ComponentUtil.agentService.findById(data.getLinkId());
                            boolean flag_money = StringUtil.getBigDecimalSubtract(agentModel.getBalance(), data.getMoney());
                            if (flag_money){
                                // 余额足够进行提现
                                // 更新代理的金额
                                String totalMoney = StringUtil.getBigDecimalAdd(data.getMoney(), data.getServiceCharge());
                                StatusModel agMoney = new StatusModel();
                                agMoney.setId(data.getLinkId());
                                agMoney.setMoney(totalMoney);
                                ComponentUtil.taskService.updateAgentReduceMoney(agMoney);
                                // 组装更改运行状态的数据：更新成成功
                                StatusModel statusModel = TaskMethod.assembleUpdateStatusModel(data.getId(), ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_THREE);
                                ComponentUtil.taskService.updateWithdrawStatus(statusModel);
                            }else{
                                // 余额不足提现
                                // 组装更改运行状态的数据：更新成失败
                                StatusModel statusModel = TaskMethod.assembleUpdateStatusModel(data.getId(), ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
                                statusModel.setMoney("检测：余额不足提现!");
                                ComponentUtil.taskService.updateWithdrawStatus(statusModel);
                            }
                        }

                    }
                    // 解锁
                    ComponentUtil.redisIdService.delLock(lockKey);
                    ComponentUtil.redisIdService.delLock(str_lockKey);
                }catch (Exception e){
                    // 组装更改运行状态的数据：更新成失败
                    StatusModel statusModel = TaskMethod.assembleUpdateStatusModel(data.getId(), ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
                    ComponentUtil.taskService.updateWithdrawStatus(statusModel);
                    log.error(String.format("this Task.deductRatioData() is error , the id=%s !", data.getId()));
                    e.printStackTrace();
                }
            }
        }
    }


}
