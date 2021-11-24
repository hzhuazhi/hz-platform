package com.hz.platform.master.core.service;

import com.hz.platform.master.core.common.service.BaseService;
import com.hz.platform.master.core.model.channel.ChannelModel;
import com.hz.platform.master.core.model.channelbalancededuct.ChannelBalanceDeductModel;
import com.hz.platform.master.core.model.channelout.ChannelOutModel;

/**
 * @Description 渠道数据代付订单的Service层
 * @Author yoko
 * @Date 2020/10/31 15:35
 * @Version 1.0
 */
public interface ChannelOutService<T> extends BaseService<T> {

    /**
     * @Description: 处理生成代付订单
     * <p>
     *     1.扣减渠道余额。
     *     2.添加渠道扣减余额的流水。
     *     3.添加代付订单信息
     * </p>
     * @param channelModel - 渠道更新余额
     * @param channelBalanceDeductModel - 渠道扣减余额流水
     * @param channelOutModel - 代付订单信息
     * @return
     * @author yoko
     * @date 2020/6/12 22:49
     */
    public boolean handleChannelOut(ChannelModel channelModel, ChannelBalanceDeductModel channelBalanceDeductModel, ChannelOutModel channelOutModel) throws Exception;


    /**
     * @Description: 根据订单号更新代付订单的订单信息
     * @param model
     * @return
     * @author yoko
     * @date 2020/11/1 16:23
     */
    public int updateOrderStatusByOrderNo(ChannelOutModel model);



    /**
     * @Description: 处理订单余额以及流水
     * <p>
     *     1.扣减渠道余额。
     *     2.添加渠道扣减余额的流水。
     *     3.更新订单的请求状态。
     * </p>
     * @param channelModel - 渠道更新余额
     * @param channelBalanceDeductModel - 渠道扣减余额流水
     * @param channelOutModel - 要更新的代付订单的请求状态
     * @return
     * @author yoko
     * @date 2020/6/12 22:49
     */
    public boolean handleChannelOutMoney(ChannelModel channelModel, ChannelBalanceDeductModel channelBalanceDeductModel, ChannelOutModel channelOutModel) throws Exception;

}
