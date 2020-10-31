package com.hz.pay.master.core.mapper;

import com.hz.pay.master.core.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 上游数据的Dao层
 * @Author yoko
 * @Date 2020/3/25 11:46
 * @Version 1.0
 */
@Mapper
public interface DataCoreMapper<T> extends BaseDao<T> {
}
