package com.hz.platform.master.core.runner;

import com.alibaba.fastjson.JSON;
import com.hz.platform.master.core.common.utils.HttpSendUtils;
import com.hz.platform.master.core.common.utils.constant.CacheKey;
import com.hz.platform.master.core.common.utils.constant.CachedKeyUtils;
import com.hz.platform.master.core.model.task.base.StatusModel;
import com.hz.platform.master.core.model.zhongbang.ZbWhitelistModel;
import com.hz.platform.master.util.ComponentUtil;
import com.hz.platform.master.util.TaskMethod;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yoko
 * @desc task:众邦白名单
 * @create 2022-07-12 14:08
 **/
@Component
@EnableScheduling
public class TaskZbWhitelist {

    private final static Logger log = LoggerFactory.getLogger(TaskZbWhitelist.class);

    @Value("${task.limit.num}")
    private int limitNum;



    /**
     * 10分钟
     */
    public long TEN_MIN = 10;






    /**
     * @Description: task：执行白名单结果数据同步
     * <p>
     *     每1每秒运行一次
     *     1.查询出失败，成功的白名单数据。
     *     2.根据同步地址进行数据同步。
     * </p>
     * @author yoko
     * @date 2019/12/6 20:25
     */
    @Scheduled(fixedDelay = 1000) // 每10秒执行
    public void whitelistNotify() throws Exception{
//        log.info("----------------------------------TaskZbWhitelist.whitelistNotify()----start");

        // 获取已成功的订单数据，并且为同步给下游的数据
        StatusModel statusQuery = TaskMethod.assembleTaskStatusQuery(limitNum, 0, 0, 0, 0, 0,1,0,null);
        List<ZbWhitelistModel> synchroList = ComponentUtil.taskZbWhitelistService.getWhitelistNotifyList(statusQuery);
        for (ZbWhitelistModel data : synchroList){
            try{
                // 锁住这个数据流水
                String lockKey = CachedKeyUtils.getCacheKey(CacheKey.LOCK_ZHONG_BANG_WHITELIST_NOTIFY, data.getId());
                boolean flagLock = ComponentUtil.redisIdService.lock(lockKey);
                if (flagLock){
                    StatusModel statusModel = null;
                    // 进行数据同步

                    Map<String, Object> sendMap = new HashMap<>();
                    sendMap.put("user_serial_no", data.getUserSerialNo());
                    if (data.getOrderStatus() == 3){
                        // 成功的
                        sendMap.put("add_status", 1);
                    }else if (data.getOrderStatus() == 2){
                        // 失败
                        sendMap.put("add_status", 2);
                    }
                    if (!StringUtils.isBlank(data.getResCode())){
                        sendMap.put("res_code", data.getResCode());
                    }else {
                        sendMap.put("res_code", "");
                    }
                    if (!StringUtils.isBlank(data.getResExplain())){
                        sendMap.put("res_explain", data.getResExplain());
                    }else {
                        sendMap.put("res_explain", "");
                    }

                    String sendUrl = "";
                    if (!StringUtils.isBlank(data.getNotifyUrl())){
                        sendUrl = data.getNotifyUrl();
                    }
                    String resp = HttpSendUtils.sendPostAppJson(sendUrl , JSON.toJSONString(sendMap));
                    if (resp.indexOf("OK") > -1){
                        // 成功
                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 0, 0, 3,0,null);
                    }else {
                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 0, 0, 2,0,null);
                    }


                    // 更新状态
                    ComponentUtil.taskZbWhitelistService.updateStatus(statusModel);
                    // 解锁
                    ComponentUtil.redisIdService.delLock(lockKey);
                }

//                log.info("----------------------------------TaskZbWhitelist.whitelistNotify()----end");
            }catch (Exception e){
                log.error(String.format("this TaskZbWhitelist.whitelistNotify() is error , the dataId=%s !", data.getId()));
                e.printStackTrace();
                // 更新此次task的状态：更新成失败：因为ERROR
                StatusModel statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 0, 0, 2,0,null);
                ComponentUtil.taskZbWhitelistService.updateStatus(statusModel);
            }
        }
    }

}
