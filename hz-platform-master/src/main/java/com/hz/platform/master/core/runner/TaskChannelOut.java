package com.hz.platform.master.core.runner;

import com.alibaba.fastjson.JSON;
import com.hz.platform.master.core.common.utils.HttpSendUtils;
import com.hz.platform.master.core.common.utils.HttpUtil;
import com.hz.platform.master.core.common.utils.MD5Util;
import com.hz.platform.master.core.common.utils.constant.CacheKey;
import com.hz.platform.master.core.common.utils.constant.CachedKeyUtils;
import com.hz.platform.master.core.model.channel.ChannelModel;
import com.hz.platform.master.core.model.channelout.ChannelOutModel;
import com.hz.platform.master.core.model.task.base.StatusModel;
import com.hz.platform.master.util.ComponentUtil;
import com.hz.platform.master.util.TaskMethod;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description task：渠道数据代付订单
 * @Author yoko
 * @Date 2020/11/1 18:29
 * @Version 1.0
 */
@Component
@EnableScheduling
public class TaskChannelOut {

    private final static Logger log = LoggerFactory.getLogger(TaskChannelOut.class);

    @Value("${task.limit.num}")
    private int limitNum;



    /**
     * 10分钟
     */
    public long TEN_MIN = 10;


    /**
     * @Description: 执行代付成功订单的数据同步
     * <p>
     *     每2秒执行运行一次
     *     1.查询出已成功的代付订单数据数据。
     *     2.根据同步地址进行数据同步。
     * </p>
     * @author yoko
     * @date 2019/12/6 20:25
     */
    @Scheduled(fixedDelay = 2000) // 每2秒执行
    public void orderNotify() throws Exception {
//        log.info("----------------------------------TaskChannelOut.orderNotify()----start");
        // 获取未跑的数据
        StatusModel statusQuery = TaskMethod.assembleTaskStatusQuery(limitNum, 0, 0, 1, 0, 1, 0);
        List<ChannelOutModel> synchroList = ComponentUtil.taskChannelOutService.getDataList(statusQuery);
        for (ChannelOutModel data : synchroList) {
            StatusModel statusModel = null;
            try {
                // 锁住这个数据流水
                String lockKey = CachedKeyUtils.getCacheKey(CacheKey.LOCK_CHANNEL_OUT_SEND, data.getId());
                boolean flagLock = ComponentUtil.redisIdService.lock(lockKey);
                if (flagLock) {

                    // 查询渠道信息
                    ChannelModel channelModel = (ChannelModel) ComponentUtil.channelService.findById(data.getChannelId());


                    // 判断渠道是否需要数据同步
                    if (channelModel.getIsSynchro() == 2){
                        // 无需同步

                        // 更新任务状态：更新成功的
                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 0, 3, 0, null);
                    }else{
                        // 需要同步

                        String notify_suc = "";
                        if (!StringUtils.isBlank(channelModel.getLowerSuc())){
                            notify_suc = channelModel.getLowerSuc();
                        }else {
                            notify_suc = "OK";
                        }

                        int tradeStatus = 2;// 交易状态：1表示成功，2表示失败
                        if (data.getOrderStatus() == 4){
                            tradeStatus = 1;
                        }

                        String picture_ads = "";// 转账凭证地址
                        if (!StringUtils.isBlank(data.getPictureAds())){
                            picture_ads = data.getPictureAds();
//                            picture_ads = URLEncoder.encode(picture_ads,"UTF-8");
                        }
                        String fail_info = "";// 失败缘由
                        if (!StringUtils.isBlank(data.getFailInfo())){
                            fail_info = data.getFailInfo();
                            fail_info = URLEncoder.encode(fail_info,"UTF-8");
                        }

                        // 执行发送数据
                        String sign = "total_amount=" + data.getTotalAmount() + "&" + "out_trade_no=" + data.getOutTradeNo() + "&" + "trade_status=" + tradeStatus
                                + "&" + "key=" + channelModel.getSecretKey();
                        sign = MD5Util.encryption(sign);
                        String sendUrl = data.getNotifyUrl();// 需要发送给下游的接口
                        String resp = "";// 返回的数据
                        if (channelModel.getSendDataType() == 1){
                            // get形式
                            String sendData = "?total_amount=" + data.getTotalAmount() + "&" + "actual_money=" + data.getActualMoney() + "&" + "out_trade_no=" + data.getOutTradeNo() + "&" + "trade_status=" + tradeStatus
                                    + "&" + "trade_no=" + data.getMyTradeNo() + "&" + "extra_return_param=" + data.getExtraReturnParam() + "&" + "sign=" + sign
                                    + "&" + "trade_time=" + System.currentTimeMillis() + "&" + "fail_info=" + fail_info + "&" + "picture_ads=" + picture_ads;
//                        String resp = HttpSendUtils.sendGet(sendUrl + URLEncoder.encode(sendData,"UTF-8"), null, null);
                            log.info("---order-out--get--sendUrl + sendData :" + sendUrl + sendData);
                            resp = HttpSendUtils.sendGet(sendUrl + sendData, null, null);
                        }else{
                            Map<String, Object> sendData = new HashMap<>();
                            sendData.put("total_amount", data.getTotalAmount());
                            sendData.put("actual_money", data.getActualMoney());
                            sendData.put("out_trade_no", data.getOutTradeNo());
                            sendData.put("trade_status", tradeStatus);
                            sendData.put("trade_no", data.getMyTradeNo());
                            sendData.put("extra_return_param", data.getExtraReturnParam());
                            sendData.put("sign", sign);
                            sendData.put("trade_time", System.currentTimeMillis());
                            sendData.put("fail_info", fail_info);
                            sendData.put("picture_ads", picture_ads);

                            if (channelModel.getSendDataType() == 2){
                                // post/form
                                log.info("---order-out--post/form--sendUrl + sendData :" + sendUrl + JSON.toJSONString(sendData));
                                resp = HttpSendUtils.doPostForm(sendUrl, sendData);
                            }else if (channelModel.getSendDataType() == 3){
                                // post/json
                                String parameter = JSON.toJSONString(sendData);
                                log.info("---order-out--post/json--sendUrl + sendData :" + sendUrl + parameter);
                                resp = HttpUtil.doPostJson(sendUrl, parameter);
                            }
                        }
                        log.info("-----order-out-----out_trade_no:" + data.getOutTradeNo() + ",resp:" + resp);
                        if (!StringUtils.isBlank(resp)){
//                            if (resp.equals(notify_suc)){
                            if (notify_suc.contains(resp)){
                                // 成功
                                // 组装更改运行状态的数据：更新成成功
                                // 更新任务状态：更新成功的
                                statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 0, 3, 0, null);
                            }else {
                                // 组装更改运行状态的数据：更新成失败
                                // 更新任务状态：更新失败的
                                statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 0, 2, 0, null);
                            }
                        }else{
                            // 组装更改运行状态的数据：更新成失败
                            // 更新任务状态：更新失败的
                            statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 0, 2, 0, null);
                        }


                    }

                    // 更新状态
                    ComponentUtil.taskChannelOutService.updateStatus(statusModel);

                    // 解锁
                    ComponentUtil.redisIdService.delLock(lockKey);
                }

//                log.info("----------------------------------TaskChannelOut.orderNotify()----end");
            } catch (Exception e) {
                log.error(String.format("this TaskChannelOut.orderNotify() is error , the dataId=%s !", data));
                e.printStackTrace();
                // 更新此次task的状态：更新成失败
                statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 0, 2, 0, "异常失败try!");
                ComponentUtil.taskChannelOutService.updateStatus(statusModel);
            }
        }
    }
}
