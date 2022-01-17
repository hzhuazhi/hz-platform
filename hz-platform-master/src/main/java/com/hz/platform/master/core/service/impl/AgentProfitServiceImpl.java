package com.hz.platform.master.core.service.impl;

import com.hz.platform.master.core.common.dao.BaseDao;
import com.hz.platform.master.core.common.service.impl.BaseServiceImpl;
import com.hz.platform.master.core.mapper.AgentProfitMapper;
import com.hz.platform.master.core.model.agent.AgentProfitModel;
import com.hz.platform.master.core.service.AgentProfitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description 代理收益数据的Service层
 * @Author yoko
 * @Date 2020/9/17 21:21
 * @Version 1.0
 */
@Service
public class AgentProfitServiceImpl<T> extends BaseServiceImpl<T> implements AgentProfitService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private AgentProfitMapper agentProfitMapper;


    public BaseDao<T> getDao() {
        return agentProfitMapper;
    }

    @Override
    public int addBatchAgentProfit(List<AgentProfitModel> list) {
        return agentProfitMapper.addBatchAgentProfit(list);
    }

    @Override
    public AgentProfitModel totalDayMoney(AgentProfitModel model) {
        return agentProfitMapper.totalDayMoney(model);
    }
}
