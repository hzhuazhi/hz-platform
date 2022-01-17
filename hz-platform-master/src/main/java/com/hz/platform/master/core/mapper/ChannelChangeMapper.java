package com.hz.platform.master.core.mapper;

import com.hz.platform.master.core.common.dao.BaseDao;
import com.hz.platform.master.core.model.channelchange.ChannelChangeModel;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 渠道金额变更纪录的Dao层
 * @Author yoko
 * @Date 2020/11/4 10:47
 * @Version 1.0
 */
@Mapper
public interface ChannelChangeMapper<T> extends BaseDao<T> {

    /**
     * @Description:根据条件获取渠道加减金额
     * @param model
     * @author: yoko
     * @date: 2021/12/31 17:41
     * @version 1.0.0
     */
    public ChannelChangeModel getDayMoney(ChannelChangeModel model);
}
