package com.hz.pay.master.core.service.impl;

import com.hz.pay.master.core.common.dao.BaseDao;
import com.hz.pay.master.core.common.service.impl.BaseServiceImpl;
import com.hz.pay.master.core.mapper.AgentMapper;
import com.hz.pay.master.core.model.agent.AgentModel;
import com.hz.pay.master.core.service.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 代理的Service层的实现层
 * @Author yoko
 * @Date 2020/5/5 9:05
 * @Version 1.0
 */
@Service
public class AgentServiceImpl<T> extends BaseServiceImpl<T> implements AgentService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private AgentMapper agentMapper;


    public BaseDao<T> getDao() {
        return agentMapper;
    }

    @Override
    public int updateMoney(AgentModel model) {
        return agentMapper.updateMoney(model);
    }
}
