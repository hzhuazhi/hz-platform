package com.hz.platform.master.core.mapper;

import com.hz.platform.master.core.common.dao.BaseDao;
import com.hz.platform.master.core.model.agent.AgentProfitModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description task:代理收益数据的Dao层
 * @Author yoko
 * @Date 2020/9/17 22:12
 * @Version 1.0
 */
@Mapper
public interface TaskAgentProfitMapper<T> extends BaseDao<T> {

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
