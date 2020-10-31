package com.hz.pay.master.core.mapper;

import com.hz.pay.master.core.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 代理与渠道的关联关系的Dao层
 * @Author yoko
 * @Date 2020/9/17 21:18
 * @Version 1.0
 */
@Mapper
public interface AgentChannelGewayMapper<T> extends BaseDao<T> {
}
