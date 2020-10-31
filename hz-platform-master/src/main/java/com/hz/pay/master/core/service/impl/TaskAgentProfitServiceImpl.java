package com.hz.pay.master.core.service.impl;

import com.hz.pay.master.core.common.dao.BaseDao;
import com.hz.pay.master.core.common.service.impl.BaseServiceImpl;
import com.hz.pay.master.core.mapper.TaskAgentProfitMapper;
import com.hz.pay.master.core.model.agent.AgentProfitModel;
import com.hz.pay.master.core.service.TaskAgentProfitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description task:代理收益数据的Service层的实现层
 * @Author yoko
 * @Date 2020/9/17 22:15
 * @Version 1.0
 */
@Service
public class TaskAgentProfitServiceImpl<T> extends BaseServiceImpl<T> implements TaskAgentProfitService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private TaskAgentProfitMapper taskAgentProfitMapper;


    public BaseDao<T> getDao() {
        return taskAgentProfitMapper;
    }

    @Override
    public List<AgentProfitModel> getDataList(Object obj) {
        return taskAgentProfitMapper.getDataList(obj);
    }

    @Override
    public int updateStatus(Object obj) {
        return taskAgentProfitMapper.updateStatus(obj);
    }
}
