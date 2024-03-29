package com.hz.platform.master.util;


import com.hz.platform.master.core.common.redis.RedisIdService;
import com.hz.platform.master.core.common.redis.RedisService;
import com.hz.platform.master.core.common.utils.constant.LoadConstant;
import com.hz.platform.master.core.service.*;

/**
 * 工具类
 */
public class ComponentUtil {
    public static RedisIdService redisIdService;
    public static RedisService redisService;
    public static LoadConstant loadConstant;
    public static RegionService regionService;
    public static ChannelService channelService;
    public static AdvertService advertService;
    public static GewayService gewayService;
    public static ChannelGewayService channelGewayService;
    public static ChannelDataService channelDataService;
    public static DataCoreService dataCoreService;
    public static TaskService taskService;
    public static GewaytradetypeService gewaytradetypeService;
    public static ZfbAppService zfbAppService;
    public static AlipayService alipayService;
    public static BufpayService bufpayService;
    public static ReceivingAccountService receivingAccountService;
    public static ReceivingAccountDataService receivingAccountDataService;
    public static AgentService agentService;
    public static AgentChannelGewayService agentChannelGewayService;
    public static AgentProfitService agentProfitService;
    public static TaskAgentProfitService taskAgentProfitService;
    public static ChannelOutService channelOutService;
    public static ChannelBalanceDeductService channelBalanceDeductService;
    public static DataCoreOutService dataCoreOutService;
    public static TaskDataCoreOutService taskDataCoreOutService;
    public static TaskChannelBalanceDeductService taskChannelBalanceDeductService;
    public static TaskChannelOutService taskChannelOutService;
    public static ChannelChangeService channelChangeService;
    public static TaskChannelChangeService taskChannelChangeService;
    public static TaskWithdrawService taskWithdrawService;
    public static TaskGewayChangeService taskGewayChangeService;
    public static TaskDataCoreService taskDataCoreService;
    public static GewayProfitService gewayProfitService;
    public static ChannelProfitService channelProfitService;
    public static TaskGewayProfitService taskGewayProfitService;
    public static TaskChannelProfitService taskChannelProfitService;
    public static StrategyService strategyService;
    public static PrepareRechargeService prepareRechargeService;
    public static TaskPrepareRechargeService taskPrepareRechargeService;
    public static StatisticsAgentService statisticsAgentService;
    public static StatisticsOutChannelService statisticsOutChannelService;
    public static WithdrawService withdrawService;

    public static ZbWhitelistService zbWhitelistService;
    public static TaskZbWhitelistService taskZbWhitelistService;

}
