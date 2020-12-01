package com.hz.platform.master.core.runner;

import com.hz.platform.master.core.common.utils.constant.CacheKey;
import com.hz.platform.master.core.common.utils.constant.CachedKeyUtils;
import com.hz.platform.master.core.model.channel.ChannelModel;
import com.hz.platform.master.core.model.channelwithdraw.ChannelWithdrawModel;
import com.hz.platform.master.core.model.task.base.StatusModel;
import com.hz.platform.master.core.model.withdraw.WithdrawModel;
import com.hz.platform.master.util.ComponentUtil;
import com.hz.platform.master.util.HodgepodgeMethod;
import com.hz.platform.master.util.TaskMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description task：提现记录
 * @Author yoko
 * @Date 2020/11/18 19:47
 * @Version 1.0
 */
@Component
@EnableScheduling
public class TaskWithdraw {


    private final static Logger log = LoggerFactory.getLogger(TaskWithdraw.class);

    @Value("${task.limit.num}")
    private int limitNum;



    /**
     * 10分钟
     */
    public long TEN_MIN = 10;


    /**
     * @Description: 提现往蛋糕平台进行数据的录入
     * <p>
     *     每10秒执行运行一次
     *     1.查询未跑的提现记录，把数据同步到蛋糕平台
     * </p>
     * @author yoko
     * @date 2019/12/6 20:25
     */
//    @Scheduled(fixedDelay = 1000) // 每1秒执行
    @Scheduled(fixedDelay = 10000) // 每10秒执行
    public void synchroWithdraw() throws Exception{
//        log.info("----------------------------------TaskWithdraw.synchroWithdraw()----start");
        // 获取未跑的数据
        StatusModel statusQuery = TaskMethod.assembleTaskStatusQuery(limitNum, 0, 3, 0, 0, 1, 0);
        List<WithdrawModel> synchroList = ComponentUtil.taskWithdrawService.getDataList(statusQuery);
        for (WithdrawModel data : synchroList){
            StatusModel statusModel = null;
            try{
                // 锁住这个数据流水
                String lockKey = CachedKeyUtils.getCacheKey(CacheKey.LOCK_WITHDRAW_SYNCHRO, data.getId());
                boolean flagLock = ComponentUtil.redisIdService.lock(lockKey);
                if (flagLock){
                    int channelType = 0;
                    String secretKey = "";// 渠道秘钥
                    if (data.getRoleId() == 2){
                        // 表示是渠道提现
                        ChannelModel channelQuery = HodgepodgeMethod.assembleChannelQuery(data.getLinkId(), null, null,0, null, 0,0,0,0);
                        ChannelModel channelModel = (ChannelModel)ComponentUtil.channelService.findByObject(channelQuery);
                        if (channelModel != null && channelModel.getId() != null){
                            channelType = channelModel.getChannelType();
                            secretKey = channelModel.getSecretKey();
                        }
                    }
                    // 数据添加同步到蛋糕平台
                    ChannelWithdrawModel channelWithdrawModel = HodgepodgeMethod.assembleChannelWithdraw(data, channelType, secretKey);
                    int num = ComponentUtil.taskWithdrawService.addChannelWithdraw(channelWithdrawModel);
                    if (num > 0){
                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 0,  3,0,null);
                    }else {
                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 0,  2,0,null);
                    }

                    // 更新状态
                    ComponentUtil.taskWithdrawService.updateStatus(statusModel);

                    // 解锁
                    ComponentUtil.redisIdService.delLock(lockKey);
                }

//                log.info("----------------------------------TaskWithdraw.synchroWithdraw()----end");
            }catch (Exception e){
                log.error(String.format("this TaskWithdraw.synchroWithdraw() is error , the dataId=%s !", data));
                e.printStackTrace();
                // 更新此次task的状态：更新成失败
                statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 0, 2,0,"异常失败try!");
                ComponentUtil.taskWithdrawService.updateStatus(statusModel);
            }
        }
    }

}
