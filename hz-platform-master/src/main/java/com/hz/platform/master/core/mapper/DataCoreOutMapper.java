package com.hz.platform.master.core.mapper;

import com.hz.platform.master.core.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 代付订单数据回传:接口上游的数据的Dao层
 * @Author yoko
 * @Date 2020/10/31 21:38
 * @Version 1.0
 */
@Mapper
public interface DataCoreOutMapper<T> extends BaseDao<T> {
}
