package com.hz.platform.master.core.service;

import com.hz.platform.master.core.common.service.BaseService;
import com.hz.platform.master.core.model.agent.AgentProfitModel;

import java.util.List;

/**
 * @Description 代理收益数据的Service层
 * @Author yoko
 * @Date 2020/9/17 21:20
 * @Version 1.0
 */
public interface AgentProfitService<T> extends BaseService<T> {

    /**
     * @Description: 批量添加代理的收益信息
     * @param list - 代理的收益信息集合
     * @return
     * @author yoko
     * @date 2020/11/10 14:31
     */
    public int addBatchAgentProfit(List<AgentProfitModel> list);

    /**
     * @Description:根据条件获取当天代理相关订单金额，收益金额
     * @param model
     * @author: yoko
     * @date: 2021/12/31 16:12
     * @version 1.0.0
     */
    public AgentProfitModel totalDayMoney(AgentProfitModel model);

}
