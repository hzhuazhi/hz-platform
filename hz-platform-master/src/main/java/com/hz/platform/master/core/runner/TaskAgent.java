package com.hz.platform.master.core.runner;

import com.hz.platform.master.core.common.utils.DateUtil;
import com.hz.platform.master.core.model.agent.AgentModel;
import com.hz.platform.master.core.model.agent.AgentProfitModel;
import com.hz.platform.master.core.model.statistics.StatisticsAgentModel;
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
 * @desc task:代理当天收益信息
 * @create 2022-01-06 15:09
 **/
@Component
@EnableScheduling
public class TaskAgent {


    private final static Logger log = LoggerFactory.getLogger(TaskAgent.class);

    @Value("${task.limit.num}")
    private int limitNum;


    /**
     * 10分钟
     */
    public long TEN_MIN = 10;


    /**
     * @Description: task：统计代理当日的资金以及收益情况
     * <p>每天23:59触发</p>
     * @author yoko
     * @date 2019/12/6 20:25
     */
    @Scheduled(cron = "0 59 23 * * ?")
    public void statisticsAgent() throws Exception{
        log.info("----------------------------------TaskAgent.statisticsAgent()----start");
        int curday = DateUtil.getDayNumber(new Date());
        String startCreateTime = DateUtil.getNowFormateDate() + " 00:00:01";
        String endCreateTime = DateUtil.getNowFormateDate() + " 23:59:59";
//        int curday = 20211231;

        // 获取代理数据
        List<AgentModel> synchroList = ComponentUtil.agentService.findByCondition(new AgentModel());
        for (AgentModel data : synchroList){
            try{
                // 获取代理相关的订单金额以及收益金额数据
                AgentProfitModel agentProfitQuery = TaskMethod.assembleAgentProfitQueryByAgent(data.getId(), curday);
                AgentProfitModel agentProfitModel = ComponentUtil.agentProfitService.totalDayMoney(agentProfitQuery);

                // 获取提现中的金额
                String withdrawIngMoney = "0.0000";
                WithdrawModel withdrawQuery_ing = TaskMethod.assembleWithdrawQuery(0,data.getId(), 1, 3, startCreateTime, endCreateTime);
                WithdrawModel withdrawModel_ing = ComponentUtil.withdrawService.getWithdrawMoney(withdrawQuery_ing);
                if (withdrawModel_ing != null){
                    if (!StringUtils.isBlank(withdrawModel_ing.getMoney())){
                        withdrawIngMoney = withdrawModel_ing.getMoney();
                    }
                }

                // 获取提现驳回的金额
                String withdrawFailMoney = "0.0000";
                WithdrawModel withdrawQuery_fail = TaskMethod.assembleWithdrawQuery(0,data.getId(), 2, 3, startCreateTime, endCreateTime);
                WithdrawModel withdrawModel_fail = ComponentUtil.withdrawService.getWithdrawMoney(withdrawQuery_fail);
                if (withdrawModel_fail != null){
                    if (!StringUtils.isBlank(withdrawModel_fail.getMoney())){
                        withdrawFailMoney = withdrawModel_fail.getMoney();
                    }
                }

                // 获取提现成功的金额
                String withdrawSucMoney = "0.0000";
                WithdrawModel withdrawQuery_suc = TaskMethod.assembleWithdrawQuery(0,data.getId(), 3, 3, startCreateTime, endCreateTime);
                WithdrawModel withdrawModel_suc = ComponentUtil.withdrawService.getWithdrawMoney(withdrawQuery_suc);
                if (withdrawModel_suc != null){
                    if (!StringUtils.isBlank(withdrawModel_suc.getMoney())){
                        withdrawSucMoney = withdrawModel_suc.getMoney();
                    }
                }


                // 添加代理隔日数据统计
                StatisticsAgentModel statisticsAgentModel = TaskMethod.assembleStatisticsAgentAdd(data, agentProfitModel, withdrawIngMoney, withdrawFailMoney, withdrawSucMoney, null, null);
                ComponentUtil.statisticsAgentService.add(statisticsAgentModel);

            }catch (Exception e){
                log.error(String.format("this TaskAgent.statisticsAgent() is error , the dataId=%s !", data.getId()));
                e.printStackTrace();
            }
        }

        log.info("----------------------------------TaskAgent.statisticsAgent()----end");
    }

}
