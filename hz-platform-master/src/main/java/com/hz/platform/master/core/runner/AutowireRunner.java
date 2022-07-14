package com.hz.platform.master.core.runner;

import com.hz.platform.master.core.common.redis.RedisIdService;
import com.hz.platform.master.core.common.redis.RedisService;
import com.hz.platform.master.core.common.utils.constant.LoadConstant;
import com.hz.platform.master.core.model.agent.AgentModel;
import com.hz.platform.master.core.model.channel.ChannelModel;
import com.hz.platform.master.core.service.*;
import com.hz.platform.master.util.ComponentUtil;
import com.hz.platform.master.core.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;


@Component
@Order(0)
public class AutowireRunner implements ApplicationRunner {
    private final static Logger log = LoggerFactory.getLogger(AutowireRunner.class);

    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    @Value("${sp.send.url}")
    private String spSendUrl;

    Thread runThread = null;

    @Autowired
    private RedisIdService redisIdService;
    @Autowired
    private RedisService redisService;

    @Resource
    private LoadConstant loadConstant;

    @Autowired
    private RegionService regionService;

    @Autowired
    private ChannelService channelService;

    @Autowired
    private AdvertService advertService;

    @Autowired
    private GewayService gewayService;

    @Autowired
    private ChannelGewayService channelGewayService;

    @Autowired
    private ChannelDataService channelDataService;

    @Autowired
    private DataCoreService dataCoreService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private GewaytradetypeService gewaytradetypeService;

    @Autowired
    private ZfbAppService zfbAppService;

    @Autowired
    private AlipayService alipayService;

    @Autowired
    private BufpayService bufpayService;

    @Autowired
    private ReceivingAccountService receivingAccountService;

    @Autowired
    private ReceivingAccountDataService receivingAccountDataService;

    @Autowired
    private AgentService agentService;

    @Autowired
    private AgentChannelGewayService agentChannelGewayService;

    @Autowired
    private AgentProfitService agentProfitService;

    @Autowired
    private TaskAgentProfitService taskAgentProfitService;

    @Autowired
    private ChannelOutService channelOutService;

    @Autowired
    private ChannelBalanceDeductService channelBalanceDeductService;

    @Autowired
    private DataCoreOutService dataCoreOutService;

    @Autowired
    private TaskDataCoreOutService taskDataCoreOutService;

    @Autowired
    private TaskChannelBalanceDeductService taskChannelBalanceDeductService;

    @Autowired
    private TaskChannelOutService taskChannelOutService;

    @Autowired
    private ChannelChangeService channelChangeService;

    @Autowired
    private TaskChannelChangeService taskChannelChangeService;

    @Autowired
    private TaskWithdrawService taskWithdrawService;

    @Autowired
    private TaskGewayChangeService taskGewayChangeService;

    @Autowired
    private TaskDataCoreService taskDataCoreService;

    @Autowired
    private GewayProfitService gewayProfitService;

    @Autowired
    private ChannelProfitService channelProfitService;

    @Autowired
    private TaskGewayProfitService taskGewayProfitService;

    @Autowired
    private TaskChannelProfitService taskChannelProfitService;

    @Autowired
    private StrategyService strategyService;

    @Autowired
    private PrepareRechargeService prepareRechargeService;

    @Autowired
    private TaskPrepareRechargeService taskPrepareRechargeService;

    @Autowired
    private StatisticsAgentService statisticsAgentService;

    @Autowired
    private StatisticsOutChannelService statisticsOutChannelService;

    @Autowired
    private WithdrawService withdrawService;

    @Autowired
    private ZbWhitelistService zbWhitelistService;

    @Autowired
    private TaskZbWhitelistService taskZbWhitelistService;







    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("AutowireRunner ...");
        ComponentUtil.redisIdService = redisIdService;
        ComponentUtil.redisService = redisService;
        ComponentUtil.loadConstant = loadConstant;
        ComponentUtil.regionService = regionService;
        ComponentUtil.channelService = channelService;
        ComponentUtil.advertService = advertService;
        ComponentUtil.gewayService = gewayService;
        ComponentUtil.channelGewayService = channelGewayService;
        ComponentUtil.channelDataService = channelDataService;
        ComponentUtil.dataCoreService = dataCoreService;
        ComponentUtil.taskService = taskService;
        ComponentUtil.gewaytradetypeService = gewaytradetypeService;
        ComponentUtil.zfbAppService = zfbAppService;
        ComponentUtil.alipayService = alipayService;
        ComponentUtil.bufpayService = bufpayService;
        ComponentUtil.receivingAccountService = receivingAccountService;
        ComponentUtil.receivingAccountDataService = receivingAccountDataService;
        ComponentUtil.agentService = agentService;
        ComponentUtil.agentChannelGewayService = agentChannelGewayService;
        ComponentUtil.agentProfitService = agentProfitService;
        ComponentUtil.taskAgentProfitService = taskAgentProfitService;
        ComponentUtil.channelOutService = channelOutService;
        ComponentUtil.channelBalanceDeductService = channelBalanceDeductService;
        ComponentUtil.dataCoreOutService = dataCoreOutService;
        ComponentUtil.taskDataCoreOutService = taskDataCoreOutService;
        ComponentUtil.taskChannelBalanceDeductService = taskChannelBalanceDeductService;
        ComponentUtil.taskChannelOutService = taskChannelOutService;
        ComponentUtil.channelChangeService = channelChangeService;
        ComponentUtil.taskChannelChangeService = taskChannelChangeService;
        ComponentUtil.taskWithdrawService = taskWithdrawService;
        ComponentUtil.taskGewayChangeService = taskGewayChangeService;
        ComponentUtil.taskDataCoreService = taskDataCoreService;
        ComponentUtil.gewayProfitService = gewayProfitService;
        ComponentUtil.channelProfitService = channelProfitService;
        ComponentUtil.taskGewayProfitService = taskGewayProfitService;
        ComponentUtil.taskChannelProfitService = taskChannelProfitService;
        ComponentUtil.strategyService = strategyService;
        ComponentUtil.prepareRechargeService = prepareRechargeService;
        ComponentUtil.taskPrepareRechargeService = taskPrepareRechargeService;
        ComponentUtil.statisticsAgentService = statisticsAgentService;
        ComponentUtil.statisticsOutChannelService = statisticsOutChannelService;
        ComponentUtil.withdrawService = withdrawService;

        ComponentUtil.zbWhitelistService = zbWhitelistService;
        ComponentUtil.taskZbWhitelistService = taskZbWhitelistService;

        runThread = new RunThread();
        runThread.start();






    }

    /**
     * @author df
     * @Description: TODO(模拟请求)
     * <p>1.随机获取当日金额的任务</p>
     * <p>2.获取代码信息</p>
     * @create 20:21 2019/1/29
     **/
    class RunThread extends Thread{
        @Override
        public void run() {
            log.info("启动啦............");


            while (1==1) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("渠道-start");
                List<ChannelModel> channelList = ComponentUtil.channelService.findByCondition(new ChannelModel());
                for (ChannelModel channelModel : channelList){
                    log.info("id:" + channelModel.getId() + ", channelName:" + channelModel.getChannelName() + ", totalMoney:" + channelModel.getTotalMoney() + ", balance:" + channelModel.getBalance() + ", lockMoney:" + channelModel.getLockMoney());
                }
                log.info("渠道-end");

                log.info("代理-start");
                List<AgentModel> agentList = ComponentUtil.agentService.findByCondition(new AgentModel());
                for (AgentModel agentModel : agentList){
                    log.info("id:" + agentModel.getId() + ", agentName:" + agentModel.getAgentName() + ", totalMoney:" + agentModel.getTotalMoney() + ", balance:" + agentModel.getBalance());
                }
                log.info("代理-end");
            }

        }
    }




}
