package com.hz.pay.master.core.mapper;

import com.hz.pay.master.core.common.dao.BaseDao;
import com.hz.pay.master.core.model.agent.AgentModel;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 代理的Dao层
 * @Author yoko
 * @Date 2020/5/5 8:59
 * @Version 1.0
 */
@Mapper
public interface AgentMapper<T> extends BaseDao<T> {

    /**
     * @Description: 更新金额
     * @param model
     * @return
     * @author yoko
     * @date 2020/9/17 22:28
    */
    public int updateMoney(AgentModel model);
}
