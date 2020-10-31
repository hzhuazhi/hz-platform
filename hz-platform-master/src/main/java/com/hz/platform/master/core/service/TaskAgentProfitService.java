package com.hz.platform.master.core.service;

import com.hz.platform.master.core.common.service.BaseService;
import com.hz.platform.master.core.model.agent.AgentProfitModel;

import java.util.List;

/**
 * @Description task:代理收益数据的Service层
 * @Author yoko
 * @Date 2020/9/17 22:14
 * @Version 1.0
 */
public interface TaskAgentProfitService<T> extends BaseService<T> {

    /**
     * @Description: 查询未跑的代理收益信息
     * @param obj
     * @return
     * @author yoko
     * @date 2020/6/3 13:53
     */
    public List<AgentProfitModel> getDataList(Object obj);

    /**
     * @Description: 更新代理收益信息数据的状态
     * @param obj
     * @return
     * @author yoko
     * @date 2020/1/11 16:30
     */
    public int updateStatus(Object obj);
}
