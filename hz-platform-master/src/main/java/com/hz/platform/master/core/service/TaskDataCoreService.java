package com.hz.platform.master.core.service;

import com.hz.platform.master.core.common.service.BaseService;
import com.hz.platform.master.core.model.agent.AgentProfitModel;
import com.hz.platform.master.core.model.channel.ChannelProfitModel;
import com.hz.platform.master.core.model.datacore.DataCoreModel;
import com.hz.platform.master.core.model.geway.GewayProfitModel;

import java.util.List;

/**
 * @Description task:通道回调的成功数据的Service层
 * @Author yoko
 * @Date 2021/1/19 18:41
 * @Version 1.0
 */
public interface TaskDataCoreService<T> extends BaseService<T> {

    /**
     * @Description: 查询未跑的通道回调数据
     * @param obj
     * @return
     * @author yoko
     * @date 2020/6/3 13:53
     */
    public List<DataCoreModel> getDataList(Object obj);

    /**
     * @Description: 更新通道回调数据的状态
     * @param obj
     * @return
     * @author yoko
     * @date 2020/1/11 16:30
     */
    public int updateStatus(Object obj);


    /**
     * @Description: 处理成功代收订单的逻辑
     * <p>
     *     1.添加代理收益明细收款信息。
     *     2.添加通道收益明细信息。
     *     3.添加渠道收益明细信息。
     * </p>
     * @param agentProfitList - 代理收益明细信息
     * @param gewayProfitModel - 通道收益明细信息
     * @param channelProfitModel - 渠道收益明细信息
     * @return
     * @author yoko
     * @date 2020/11/10 19:28
     */
    public boolean handleSuccessOrder(List<AgentProfitModel> agentProfitList, GewayProfitModel gewayProfitModel, ChannelProfitModel channelProfitModel) throws Exception;

}
