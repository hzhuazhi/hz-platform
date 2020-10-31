package com.hz.pay.master.core.service;

import com.hz.pay.master.core.common.service.BaseService;
import com.hz.pay.master.core.model.agent.AgentModel;

/**
 * @Description 代理的Service层
 * @Author yoko
 * @Date 2020/5/5 8:57
 * @Version 1.0
 */
public interface AgentService<T> extends BaseService<T> {

    /**
     * @Description: 更新金额
     * @param model
     * @return
     * @author yoko
     * @date 2020/9/17 22:28
     */
    public int updateMoney(AgentModel model);
}
