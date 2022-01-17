package com.hz.platform.master.core.mapper;

import com.hz.platform.master.core.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author yoko
 * @desc 统计代付渠道的每日记录的Dao层
 * @create 2021-12-30 17:33
 **/
@Mapper
public interface StatisticsOutChannelMapper<T> extends BaseDao<T> {
}
