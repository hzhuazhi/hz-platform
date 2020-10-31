package com.hz.pay.master.core.mapper;

import com.hz.pay.master.core.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 渠道与通道的关联关系的Dao层
 * @Author yoko
 * @Date 2020/3/24 15:59
 * @Version 1.0
 */
@Mapper
public interface ChannelGewayMapper<T> extends BaseDao<T> {
}
