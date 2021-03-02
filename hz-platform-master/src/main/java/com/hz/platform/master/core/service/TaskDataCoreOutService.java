package com.hz.platform.master.core.service;

import com.hz.platform.master.core.common.service.BaseService;
import com.hz.platform.master.core.model.channelbalancededuct.ChannelBalanceDeductModel;
import com.hz.platform.master.core.model.channelout.ChannelOutModel;
import com.hz.platform.master.core.model.datacoreout.DataCoreOutModel;
import com.hz.platform.master.core.model.geway.GewayProfitModel;

import java.util.List;

/**
 * @Description task：代付订单数据回传的Service层
 * @Author yoko
 * @Date 2020/11/1 14:17
 * @Version 1.0
 */
public interface TaskDataCoreOutService<T> extends BaseService<T> {

    /**
     * @Description: 查询未跑的代付回调信息
     * @param obj
     * @return
     * @author yoko
     * @date 2020/6/3 13:53
     */
    public List<DataCoreOutModel> getDataList(Object obj);

    /**
     * @Description: 更新代付回调信息数据的状态
     * @param obj
     * @return
     * @author yoko
     * @date 2020/1/11 16:30
     */
    public int updateStatus(Object obj);


    /**
     * @Description: 处理代付订单回调结果的逻辑
     * <p>
     *     1.修改渠道扣款流水的流水订单状态。
     *     2.修改代付订单的订单状态
     * </p>
     * @param channelBalanceDeductModel - 渠道扣款流水
     * @param channelOutModel - 代付订单信息
     * @param gewayProfitModel - 通道收益信息
     * @return
     * @author yoko
     * @date 2020/9/23 21:40
     */
    public boolean handleDataCoreOut(ChannelBalanceDeductModel channelBalanceDeductModel, ChannelOutModel channelOutModel, GewayProfitModel gewayProfitModel) throws Exception;

}
