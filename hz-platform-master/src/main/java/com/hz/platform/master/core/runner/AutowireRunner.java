package com.hz.platform.master.core.runner;

import com.hz.platform.master.core.common.redis.RedisIdService;
import com.hz.platform.master.core.common.redis.RedisService;
import com.hz.platform.master.core.common.utils.constant.LoadConstant;
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
//            while (true){
//                String lockKey = CachedKeyUtils.getCacheKey(CacheKey.LOCK_TASK_WORK_TYPE_CHANNEL, 1);
//                boolean flagLock = ComponentUtil.redisIdService.lock(lockKey);
//                log.info("flagLock：" + flagLock);
//                if (flagLock){
//                    break;
//                }
//            }
        }
    }




}
