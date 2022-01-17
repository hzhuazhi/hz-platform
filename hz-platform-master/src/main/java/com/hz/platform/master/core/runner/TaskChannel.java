package com.hz.platform.master.core.runner;

import com.hz.platform.master.core.common.utils.DateUtil;
import com.hz.platform.master.core.model.channel.ChannelModel;
import com.hz.platform.master.core.model.channelchange.ChannelChangeModel;
import com.hz.platform.master.core.model.channelout.ChannelOutModel;
import com.hz.platform.master.core.model.statistics.StatisticsOutChannelModel;
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

import java.util.Date;
import java.util.List;

/**
 * @author yoko
 * @desc task:渠道当天跑量信息
 * @create 2021-12-31 15:17
 **/
@Component
@EnableScheduling
public class TaskChannel {

    private final static Logger log = LoggerFactory.getLogger(TaskChannel.class);

    @Value("${task.limit.num}")
    private int limitNum;


    /**
     * 10分钟
     */
    public long TEN_MIN = 10;


    /**
     * @Description: task：统计代付渠道当日的资金以及跑量情况
     * <p>每天23:59触发</p>
     * @author yoko
     * @date 2019/12/6 20:25
     */
    @Scheduled(cron = "0 59 23 * * ?")
    public void statisticsOutChannel() throws Exception{
        log.info("----------------------------------TaskChannel.statisticsOutChannel()----start");
        int curday = DateUtil.getDayNumber(new Date());
        String startCreateTime = DateUtil.getNowFormateDate() + " 00:00:01";
        String endCreateTime = DateUtil.getNowFormateDate() + " 23:59:59";


        // 获取渠道数据
        ChannelModel channelQuery = TaskMethod.assembleChannelQuery(0, null, null, 3, 2);
        List<ChannelModel> synchroList = ComponentUtil.channelService.findByCondition(channelQuery);
        for (ChannelModel data : synchroList){
            try{
                // 获取代付的跑量金额数据
                ChannelOutModel channelOutQuery = TaskMethod.assembleOutOrderQueryByDayMoney(data.getId(), 4, curday);
                ChannelOutModel channelOutModel = ComponentUtil.channelOutService.totalDayMoney(channelOutQuery);

                // 获取提现中的金额
                String withdrawIngMoney = "0.0000";
                WithdrawModel withdrawQuery_ing = TaskMethod.assembleWithdrawQuery(0,data.getId(), 1, 2, startCreateTime, endCreateTime);
                WithdrawModel withdrawModel_ing = ComponentUtil.withdrawService.getWithdrawMoney(withdrawQuery_ing);
                if (withdrawModel_ing != null){
                    if (!StringUtils.isBlank(withdrawModel_ing.getMoney())){
                        withdrawIngMoney = withdrawModel_ing.getMoney();
                    }
                }

                // 获取提现驳回的金额
                String withdrawFailMoney = "0.0000";
                WithdrawModel withdrawQuery_fail = TaskMethod.assembleWithdrawQuery(0,data.getId(), 2, 2, startCreateTime, endCreateTime);
                WithdrawModel withdrawModel_fail = ComponentUtil.withdrawService.getWithdrawMoney(withdrawQuery_fail);
                if (withdrawModel_fail != null){
                    if (!StringUtils.isBlank(withdrawModel_fail.getMoney())){
                        withdrawFailMoney = withdrawModel_fail.getMoney();
                    }
                }

                // 获取提现成功的金额
                String withdrawSucMoney = "0.0000";
                WithdrawModel withdrawQuery_suc = TaskMethod.assembleWithdrawQuery(0,data.getId(), 3, 2, startCreateTime, endCreateTime);
                WithdrawModel withdrawModel_suc = ComponentUtil.withdrawService.getWithdrawMoney(withdrawQuery_suc);
                if (withdrawModel_suc != null){
                    if (!StringUtils.isBlank(withdrawModel_suc.getMoney())){
                        withdrawSucMoney = withdrawModel_suc.getMoney();
                    }
                }

                // 加金额
                String addMoney = "0.0000";
                ChannelChangeModel channelChangeQuery_add = TaskMethod.assembleChannelChangeQuery(0, data.getId(), 2, 0, curday);
                ChannelChangeModel channelChangeModel_add = ComponentUtil.channelChangeService.getDayMoney(channelChangeQuery_add);
                if (channelChangeModel_add != null){
                    if (!StringUtils.isBlank(channelChangeModel_add.getMoney())){
                        addMoney = channelChangeModel_add.getMoney();
                    }
                }

                // 减金额
                String reduceMoney = "0.0000";
                ChannelChangeModel channelChangeQuery_reduce = TaskMethod.assembleChannelChangeQuery(0, data.getId(), 1, 0, curday);
                ChannelChangeModel channelChangeModel_reduce = ComponentUtil.channelChangeService.getDayMoney(channelChangeQuery_reduce);
                if (channelChangeModel_reduce != null){
                    if (!StringUtils.isBlank(channelChangeModel_reduce.getMoney())){
                        reduceMoney = channelChangeModel_reduce.getMoney();
                    }
                }

                // 添加渠道隔日数据统计
                StatisticsOutChannelModel statisticsOutChannelModel = TaskMethod.assembleStatisticsOutChannelAdd(data, channelOutModel, withdrawIngMoney, withdrawFailMoney, withdrawSucMoney, addMoney, reduceMoney);
                ComponentUtil.statisticsOutChannelService.add(statisticsOutChannelModel);

            }catch (Exception e){
                log.error(String.format("this TaskChannel.statisticsOutChannel() is error , the dataId=%s !", data.getId()));
                e.printStackTrace();
            }
        }

        log.info("----------------------------------TaskChannel.statisticsOutChannel()----end");
    }


}
