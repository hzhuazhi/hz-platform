package com.hz.pay.master.util;


import com.hz.pay.master.core.common.redis.RedisIdService;
import com.hz.pay.master.core.common.redis.RedisService;
import com.hz.pay.master.core.common.utils.constant.LoadConstant;
import com.hz.pay.master.core.service.*;

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


}
