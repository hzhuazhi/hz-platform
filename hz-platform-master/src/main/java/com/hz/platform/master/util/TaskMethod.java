package com.hz.platform.master.util;

import com.hz.platform.master.core.common.utils.DateUtil;
import com.hz.platform.master.core.common.utils.constant.ServerConstant;
import com.hz.platform.master.core.model.agent.AgentModel;
import com.hz.platform.master.core.model.agent.AgentProfitModel;
import com.hz.platform.master.core.model.channel.ChannelModel;
import com.hz.platform.master.core.model.channelchange.ChannelChangeModel;
import com.hz.platform.master.core.model.channelout.ChannelOutModel;
import com.hz.platform.master.core.model.statistics.StatisticsAgentModel;
import com.hz.platform.master.core.model.statistics.StatisticsOutChannelModel;
import com.hz.platform.master.core.model.task.base.StatusModel;
import com.hz.platform.master.core.model.withdraw.WithdrawModel;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;


/**
 * @Description 定时任务的公共类
 * @Author yoko
 * @Date 2020/1/11 16:20
 * @Version 1.0
 */
public class TaskMethod {
    private static Logger log = LoggerFactory.getLogger(TaskMethod.class);




    /**
     * @Description: 组装查询定时任务的查询条件
     * @param limitNum - 多少条数据
     * @param runType - 运行类型
     * @param workType - 运算类型
     * @param greaterThan - 大于
     * @param lessThan - 小于
     * @param sendType - 发送类型
     * @param orderStatus - 订单状态
     * @return
     * @author yoko
     * @date 2020/1/11 16:23
     */
    public static StatusModel assembleTaskStatusQuery(int limitNum, int runType, int workType, int greaterThan, int lessThan, int sendType, int orderStatus){
        StatusModel resBean = new StatusModel();
        if (runType > 0){
            resBean.setRunStatus(ServerConstant.PUBLIC_CONSTANT.RUN_STATUS_THREE);
            resBean.setRunNum(ServerConstant.PUBLIC_CONSTANT.RUN_NUM_FIVE);
        }
        if (workType > 0){
            resBean.setWorkType(workType);
        }
        if (greaterThan > 0){
            resBean.setGreaterThan(greaterThan);
        }
        if (lessThan > 0){
            resBean.setLessThan(lessThan);
        }
        if (sendType > 0){
            resBean.setSendStatus(ServerConstant.PUBLIC_CONSTANT.RUN_STATUS_THREE);
            resBean.setSendNum(ServerConstant.PUBLIC_CONSTANT.RUN_NUM_FIVE);
        }
        if (orderStatus > 0){
            resBean.setOrderStatus(orderStatus);
        }
        resBean.setLimitNum(limitNum);
        return resBean;
    }



    /**
     * @Description: 组装更改运行状态的数据
     * @param id - 主键ID
     * @param runStatus - 运行计算状态：：0初始化，1锁定，2计算失败，3计算成功
     * @param workType - 补充数据的类型：1初始化，2补充数据失败，3补充数据成功
     * @param sendStatus - 发送状态：0初始化，1锁定，2计算失败，3计算成功
     * @param orderStatus - 订单状态
     * @param info - 解析说明
     * @return StatusModel
     * @author yoko
     * @date 2019/12/10 10:42
     */
    public static StatusModel assembleTaskUpdateStatus(long id, int runStatus, int workType, int sendStatus,int orderStatus, String info){
        StatusModel resBean = new StatusModel();
        resBean.setId(id);
        if (runStatus > 0){
            resBean.setRunStatus(runStatus);
            if (runStatus == ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO){
                // 表示失败：失败则需要运行次数加一
                resBean.setRunNum(ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ONE);
            }
        }
        if (workType > 0){
            resBean.setWorkType(workType);
        }
        if (sendStatus > 0){
            resBean.setSendStatus(sendStatus);
            if (sendStatus == ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO){
                // 表示失败：失败则需要运行次数加一
                resBean.setSendNum(ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ONE);
            }
        }
        if (orderStatus > 0){
            resBean.setOrderStatus(orderStatus);
        }
        if (!StringUtils.isBlank(info)){
            resBean.setInfo(info);
        }
        return resBean;
    }


    /**
     * @Description: 组装查询提现的查询条件
     * @param limitNum - 多少条数据
     * @return
     * @author yoko
     * @date 2020/1/11 16:23
     */
    public static StatusModel assembleStatusQuery(int limitNum){
        StatusModel resBean = new StatusModel();
        resBean.setLimitNum(limitNum);
        resBean.setRunNum(ServerConstant.PUBLIC_CONSTANT.RUN_NUM_FIVE);
        resBean.setRunStatus(ServerConstant.PUBLIC_CONSTANT.RUN_STATUS_THREE);
        return resBean;
    }

    /**
     * @Description: 组装查询定数据填充的查询条件
     * @param limitNum - 多少条数据
     * @param workType - 值以work形式填充计算,以后数据多起来则这些字段值填充由worker来跑数据：0初始化，1填充完毕，2无需下发数据
     * @return
     * @author yoko
     * @date 2020/1/11 16:23
    */
    public static StatusModel assembleDeductRatioStatusQuery(int limitNum, int workType){
        StatusModel resBean = new StatusModel();
        resBean.setWorkType(workType);
        resBean.setLimitNum(limitNum);
        return resBean;
    }


    /**
     * @Description: 组装查询定数据同步给渠道数据的查询条件
     * @param limitNum - 多少条数据
     * @param workType - 值以work形式填充计算,以后数据多起来则这些字段值填充由worker来跑数据：0初始化，1填充完毕，2无需下发数据
     * @return
     * @author yoko
     * @date 2020/1/11 16:23
     */
    public static StatusModel assembleNotifyStatusQuery(int limitNum, int workType){
        StatusModel resBean = new StatusModel();
        resBean.setWorkType(workType);
        resBean.setLimitNum(limitNum);
        resBean.setRunStatus(ServerConstant.PUBLIC_CONSTANT.RUN_STATUS_THREE);
        resBean.setRunNum(ServerConstant.PUBLIC_CONSTANT.RUN_NUM_FIVE);
        return resBean;
    }


    /**
     * @Description: 组装更改运行状态的数据
     * @param id - 主键ID
     * @param runStatus - 运行计算状态：：0初始化，1锁定，2计算失败，3计算成功
     * @return StatusModel
     * @author yoko
     * @date 2019/12/10 10:42
     */
    public static StatusModel assembleUpdateStatusModel(long id, int runStatus){
        StatusModel resBean = new StatusModel();
        resBean.setId(id);
        resBean.setRunStatus(runStatus);
        if (runStatus == ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO){
            // 表示失败：失败则需要运行次数加一
            resBean.setRunNum(ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ONE);
        }
        return resBean;
    }




    /**
     * @Description: 组装查询提现的查询条件
     * @param limitNum - 多少条数据
     * @return
     * @author yoko
     * @date 2020/1/11 16:23
     */
    public static StatusModel assembleWithdrawStatusQuery(int limitNum){
        StatusModel resBean = new StatusModel();
        resBean.setLimitNum(limitNum);
        resBean.setRunStatus(ServerConstant.PUBLIC_CONSTANT.RUN_STATUS_THREE);
        resBean.setRunNum(ServerConstant.PUBLIC_CONSTANT.RUN_NUM_FIVE);
        return resBean;
    }


    /**
     * @Description: 组装查询支付宝订单同步的数据的查询条件
     * @param limitNum
     * @return StatusModel
     * @author yoko
     * @date 2019/12/6 22:48
     */
    public static StatusModel assembleTaskAlipayNotifyStatusQuery(int limitNum){
        StatusModel resBean = new StatusModel();
        resBean.setRunStatus(ServerConstant.PUBLIC_CONSTANT.RUN_STATUS_THREE);
        resBean.setRunNum(ServerConstant.PUBLIC_CONSTANT.RUN_NUM_FIVE);
        resBean.setLimitNum(limitNum);
        return resBean;
    }



    /**
     * @Description: 组装查询定时任务的查询条件
     * @param limitNum - 多少条数据
     * @param runType - 运行类型
     * @param workType - 运算类型
     * @param dataType - 数据类型
     * @param greaterThan - 大于
     * @param lessThan - 小于
     * @param sendType - 发送类型
     * @param orderStatus - 订单状态
     * @param invalidTime - 失效时间
     * @return
     * @author yoko
     * @date 2020/1/11 16:23
     */
    public static StatusModel assembleTaskStatusQuery(int limitNum, int runType, int workType, int dataType, int greaterThan, int lessThan, int sendType, int orderStatus, String invalidTime){
        StatusModel resBean = new StatusModel();
        if (runType > 0){
            resBean.setRunStatus(ServerConstant.PUBLIC_CONSTANT.RUN_STATUS_THREE);
            resBean.setRunNum(ServerConstant.PUBLIC_CONSTANT.RUN_NUM_FIVE);
        }
        if (workType > 0){
            resBean.setWorkType(workType);
        }
        if (dataType > 0){
            resBean.setDataType(dataType);
        }
        if (greaterThan > 0){
            resBean.setGreaterThan(greaterThan);
        }
        if (lessThan > 0){
            resBean.setLessThan(lessThan);
        }
        if (sendType > 0){
            resBean.setSendStatus(ServerConstant.PUBLIC_CONSTANT.RUN_STATUS_THREE);
            resBean.setSendNum(ServerConstant.PUBLIC_CONSTANT.RUN_NUM_FIVE);
        }
        if (orderStatus > 0){
            resBean.setOrderStatus(orderStatus);
        }
        if (!StringUtils.isBlank(invalidTime)){
            resBean.setInvalidTime(invalidTime);
        }
        resBean.setLimitNum(limitNum);
        return resBean;
    }



    /**
     * @Description: 组装更改运行状态的数据
     * @param id - 主键ID
     * @param runStatus - 运行计算状态：：0初始化，1锁定，2计算失败，3计算成功
     * @param workType - 补充数据的类型：1初始化，2补充数据失败，3补充数据成功
     * @param dataType - 数据类型
     * @param sendStatus - 发送状态：0初始化，1锁定，2计算失败，3计算成功
     * @param orderStatus - 订单状态
     * @param info - 解析说明
     * @return StatusModel
     * @author yoko
     * @date 2019/12/10 10:42
     */
    public static StatusModel assembleTaskUpdateStatus(long id, int runStatus, int workType, int dataType,int sendStatus,int orderStatus, String info){
        StatusModel resBean = new StatusModel();
        resBean.setId(id);
        if (runStatus > 0){
            resBean.setRunStatus(runStatus);
            if (runStatus == ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO){
                // 表示失败：失败则需要运行次数加一
                resBean.setRunNum(ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ONE);
            }
        }
        if (workType > 0){
            resBean.setWorkType(workType);
        }
        if (dataType > 0){
            resBean.setDataType(dataType);
        }
        if (sendStatus > 0){
            resBean.setSendStatus(sendStatus);
            if (sendStatus == ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO){
                // 表示失败：失败则需要运行次数加一
                resBean.setSendNum(ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ONE);
            }
        }
        if (orderStatus > 0){
            resBean.setOrderStatus(orderStatus);
        }
        if (!StringUtils.isBlank(info)){
            resBean.setInfo(info);
        }
        return resBean;
    }



    /**
     * @Description: 组装查询代理收益的查询条件
     * @param agentId - 代理主键ID
     * @param curday - 日期
     * @return: com.pear.task.master.core.model.agent.AgentProfitModel
     * @author: yoko
     * @date: 2022/1/6 15:37
     * @version 1.0.0
     */
    public static AgentProfitModel assembleAgentProfitQueryByAgent(long agentId, int curday){
        AgentProfitModel resBean = new AgentProfitModel();
        resBean.setAgentId(agentId);
        resBean.setCurday(curday);
        return resBean;
    }


    /**
     * @Description: 组装查询提现信息
     * @param id - 主键ID
     * @param linkId - 渠道/代理的主键ID
     * @param withdrawStatus - 提现状态:1提现中，2驳回/提现失败，3提现成功
     * @param roleId - 角色：2渠道，3代理
     * @param startCreateTime - 提现开始时间
     * @param endCreateTime - 提现结束时间
     * @return: com.hz.platform.master.core.model.withdraw.WithdrawModel
     * @author: yoko
     * @date: 2022/1/17 15:12
     * @version 1.0.0
     */
    public static WithdrawModel assembleWithdrawQuery(long id, long linkId, int withdrawStatus, long roleId, String startCreateTime, String endCreateTime){
        WithdrawModel resBean = new WithdrawModel();
        if (id > 0){
            resBean.setId(id);
        }
        if (linkId > 0){
            resBean.setLinkId(linkId);
        }
        if (withdrawStatus > 0){
            resBean.setWithdrawStatus(withdrawStatus);
        }
        if (roleId > 0){
            resBean.setRoleId(roleId);
        }
        if (!StringUtils.isBlank(startCreateTime)){
            resBean.setStartCreateTime(startCreateTime);
        }
        if (!StringUtils.isBlank(endCreateTime)){
            resBean.setEndCreateTime(endCreateTime);
        }
        return resBean;
    }



    /**
     * @Description: 组装添加统计-代理隔日详细数据
     * @param agentModel - 代理信息
     * @param agentProfitModel - 订单信息以及收益信息
     * @param withdrawIngMoney - 提现中的金额
     * @param withdrawFailMoney - 提现驳回的金额
     * @param withdrawSucMoney - 提现成功的金额
     * @param addMoney - 加金额
     * @param reduceMoney - 减金额
     * @return: com.pear.task.master.core.model.statistics.StatisticsAgentModel
     * @author: yoko
     * @date: 2021/12/31 18:27
     * @version 1.0.0
     */
    public static StatisticsAgentModel assembleStatisticsAgentAdd(AgentModel agentModel, AgentProfitModel agentProfitModel,
                                                                  String withdrawIngMoney, String withdrawFailMoney, String withdrawSucMoney,
                                                                  String addMoney, String reduceMoney){
        StatisticsAgentModel resBean = new StatisticsAgentModel();
        if (agentModel != null){
            if (agentModel.getId() != null && agentModel.getId() > 0){
                resBean.setAgentId(agentModel.getId());
            }else {
                return null;
            }
            if (!StringUtils.isBlank(agentModel.getTotalMoney())){
                resBean.setTotalMoney(agentModel.getTotalMoney());
            }else{
                resBean.setTotalMoney("0.0000");
            }
            if (!StringUtils.isBlank(agentModel.getBalance())){
                resBean.setBalance(agentModel.getBalance());
            }else{
                resBean.setBalance("0.0000");
            }
            resBean.setLockMoney("0.0000");
        } else {
            return null;
        }

        if (agentProfitModel != null){
            if (!StringUtils.isBlank(agentProfitModel.getTotalAmount())){
                resBean.setTotalAmount(agentProfitModel.getTotalAmount());
            }else {
                resBean.setTotalAmount("0.0000");
            }
            if (!StringUtils.isBlank(agentProfitModel.getProfit())){
                resBean.setProfit(agentProfitModel.getProfit());
            }else {
                resBean.setProfit("0.0000");
            }
        }

        if (!StringUtils.isBlank(withdrawIngMoney)){
            resBean.setWithdrawIngMoney(withdrawIngMoney);
        }else {
            resBean.setWithdrawIngMoney("0.0000");
        }
        if (!StringUtils.isBlank(withdrawFailMoney)){
            resBean.setWithdrawFailMoney(withdrawFailMoney);
        }else {
            resBean.setWithdrawFailMoney("0.0000");
        }
        if (!StringUtils.isBlank(withdrawSucMoney)){
            resBean.setWithdrawSucMoney(withdrawSucMoney);
        }else {
            resBean.setWithdrawSucMoney("0.0000");
        }
        if (!StringUtils.isBlank(addMoney)){
            resBean.setAddMoney(addMoney);
        }else {
            resBean.setAddMoney("0.0000");
        }
        if (!StringUtils.isBlank(reduceMoney)){
            resBean.setReduceMoney(reduceMoney);
        }else {
            resBean.setReduceMoney("0.0000");
        }

        resBean.setCurday(DateUtil.getDayNumber(new Date()));
        resBean.setCurhour(DateUtil.getHour(new Date()));
        resBean.setCurminute(DateUtil.getCurminute(new Date()));
        return resBean;
    }


    /**
     * @Description: 组装查询渠道的查询方法
     * @param id - 主键ID
     * @param channelName - 渠道名称
     * @param channel - 商家号
     * @param channelType - 渠道类型：1代收，2代付，3其它
     * @param isEnable - 是否启用：0初始化属于暂停状态，1表示暂停使用，2正常状态
     * @return com.hz.pear.master.core.model.channel.ChannelModel
     * @Author: yoko
     * @Date 2021/8/6 19:32
     */
    public static ChannelModel assembleChannelQuery(long id, String channelName, String channel, int channelType, int isEnable){
        ChannelModel resBean = new ChannelModel();
        if (id > 0){
            resBean.setId(id);
        }
        if (!StringUtils.isBlank(channelName)){
            resBean.setChannelName(channelName);
        }
        if (!StringUtils.isBlank(channel)){
            resBean.setChannel(channel);
        }
        if (channelType > 0){
            resBean.setChannelType(channelType);
        }
        if (isEnable > 0){
            resBean.setIsEnable(isEnable);
        }
        return resBean;
    }


    /**
     * @Description: 组装查询代付订单的查询条件
     * @param channelId - 渠道ID
     * @param orderStatus - 订单状态
     * @param curday - 创建日期：存的日期格式20160530
     * @author: yoko
     * @date: 2021/12/31 16:38
     * @version 1.0.0
     */
    public static ChannelOutModel assembleOutOrderQueryByDayMoney(long channelId, int orderStatus, int curday){
        ChannelOutModel resBean = new ChannelOutModel();
        resBean.setChannelId(channelId);
        resBean.setOrderStatus(orderStatus);
        resBean.setCurday(curday);
        return resBean;
    }


    /**
     * @Description: 组装查询渠道金额变更的查询方法
     * @param id - 主键ID
     * @param channelId - 渠道ID
     * @param changeType - 变更金额类型：0初始化，1核减金额，2加金额
     * @param isShow - 数据是否展现给用户看：0初始化，1展现，2不展现
     * @param curday - 创建日期：存的日期格式20160530
     * @return: com.pear.task.master.core.model.channel.ChannelChangeModel
     * @author: yoko
     * @date: 2021/12/31 17:46
     * @version 1.0.0
     */
    public static ChannelChangeModel assembleChannelChangeQuery(long id, long channelId, int changeType, int isShow, int curday){
        ChannelChangeModel resBean = new ChannelChangeModel();
        if (id > 0){
            resBean.setId(id);
        }
        if (channelId > 0){
            resBean.setChannelId(channelId);
        }
        if (changeType > 0){
            resBean.setChangeType(changeType);
        }
        if (isShow > 0){
            resBean.setIsShow(isShow);
        }
        if (curday > 0){
            resBean.setCurday(curday);
        }
        return resBean;
    }



    /**
     * @Description: 组装添加统计-渠道隔日详细数据
     * @param channelModel - 渠道信息
     * @param channelOutModel - 代付跑量信息
     * @param withdrawIngMoney - 提现中的金额
     * @param withdrawFailMoney - 提现驳回的金额
     * @param withdrawSucMoney - 提现成功的金额
     * @param addMoney - 加金额
     * @param reduceMoney - 减金额
     * @return: com.pear.task.master.core.model.statistics.StatisticsOutChannelModel
     * @author: yoko
     * @date: 2021/12/31 18:27
     * @version 1.0.0
     */
    public static StatisticsOutChannelModel assembleStatisticsOutChannelAdd(ChannelModel channelModel, ChannelOutModel channelOutModel,
                                                                            String withdrawIngMoney, String withdrawFailMoney, String withdrawSucMoney,
                                                                            String addMoney, String reduceMoney){
        StatisticsOutChannelModel resBean = new StatisticsOutChannelModel();
        if (channelModel != null){
            if (channelModel.getId() != null && channelModel.getId() > 0){
                resBean.setChannelId(channelModel.getId());
            }else {
                return null;
            }
            if (!StringUtils.isBlank(channelModel.getTotalMoney())){
                resBean.setTotalMoney(channelModel.getTotalMoney());
            }else{
                resBean.setTotalMoney("0.0000");
            }
            if (!StringUtils.isBlank(channelModel.getBalance())){
                resBean.setBalance(channelModel.getBalance());
            }else{
                resBean.setBalance("0.0000");
            }
            if (!StringUtils.isBlank(channelModel.getLockMoney())){
                resBean.setLockMoney(channelModel.getLockMoney());
            }else{
                resBean.setLockMoney("0.0000");
            }
        } else {
            return null;
        }

        if (channelOutModel != null){
            if (!StringUtils.isBlank(channelOutModel.getTotalAmount())){
                resBean.setTotalAmount(channelOutModel.getTotalAmount());
            }else {
                resBean.setTotalAmount("0.0000");
            }
            if (!StringUtils.isBlank(channelOutModel.getActualMoney())){
                resBean.setActualMoney(channelOutModel.getActualMoney());
            }else {
                resBean.setActualMoney("0.0000");
            }
        }

        if (!StringUtils.isBlank(withdrawIngMoney)){
            resBean.setWithdrawIngMoney(withdrawIngMoney);
        }else {
            resBean.setWithdrawIngMoney("0.0000");
        }
        if (!StringUtils.isBlank(withdrawFailMoney)){
            resBean.setWithdrawFailMoney(withdrawFailMoney);
        }else {
            resBean.setWithdrawFailMoney("0.0000");
        }
        if (!StringUtils.isBlank(withdrawSucMoney)){
            resBean.setWithdrawSucMoney(withdrawSucMoney);
        }else {
            resBean.setWithdrawSucMoney("0.0000");
        }
        if (!StringUtils.isBlank(addMoney)){
            resBean.setAddMoney(addMoney);
        }else {
            resBean.setAddMoney("0.0000");
        }
        if (!StringUtils.isBlank(reduceMoney)){
            resBean.setReduceMoney(reduceMoney);
        }else {
            resBean.setReduceMoney("0.0000");
        }

        resBean.setCurday(DateUtil.getDayNumber(new Date()));
        resBean.setCurhour(DateUtil.getHour(new Date()));
        resBean.setCurminute(DateUtil.getCurminute(new Date()));
        return resBean;
    }


}
