package com.hz.platform.master.core.mapper;

import com.hz.platform.master.core.common.dao.BaseDao;
import com.hz.platform.master.core.model.agent.AgentProfitModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description 代理收益数据的Dao层
 * @Author yoko
 * @Date 2020/9/17 21:18
 * @Version 1.0
 */
@Mapper
public interface AgentProfitMapper<T> extends BaseDao<T> {

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
