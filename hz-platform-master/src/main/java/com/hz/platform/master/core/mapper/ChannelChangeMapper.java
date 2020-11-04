package com.hz.platform.master.core.mapper;

import com.hz.platform.master.core.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 渠道金额变更纪录的Dao层
 * @Author yoko
 * @Date 2020/11/4 10:47
 * @Version 1.0
 */
@Mapper
public interface ChannelChangeMapper<T> extends BaseDao<T> {
}
