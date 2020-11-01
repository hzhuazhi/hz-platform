package com.hz.platform.master.core.mapper;

import com.hz.platform.master.core.common.dao.BaseDao;
import com.hz.platform.master.core.model.channelout.ChannelOutModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description task：渠道数据代付订单的Dao层
 * @Author yoko
 * @Date 2020/11/1 18:29
 * @Version 1.0
 */
@Mapper
public interface TaskChannelOutMapper<T> extends BaseDao<T> {

    /**
     * @Description: 查询未跑的渠道数据代付订单信息
     * @param obj
     * @return
     * @author yoko
     * @date 2020/6/3 13:53
     */
    public List<ChannelOutModel> getDataList(Object obj);

    /**
     * @Description: 更新渠道数据代付订单信息数据的状态
     * @param obj
     * @return
     * @author yoko
     * @date 2020/1/11 16:30
     */
    public int updateStatus(Object obj);
}
