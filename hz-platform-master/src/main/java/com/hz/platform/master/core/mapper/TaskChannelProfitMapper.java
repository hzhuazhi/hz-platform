package com.hz.platform.master.core.mapper;

import com.hz.platform.master.core.common.dao.BaseDao;
import com.hz.platform.master.core.model.channel.ChannelProfitModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description task:渠道收益数据的Dao层
 * @Author yoko
 * @Date 2021/1/21 14:16
 * @Version 1.0
 */
@Mapper
public interface TaskChannelProfitMapper<T> extends BaseDao<T> {

    /**
     * @Description: 查询未跑的渠道收益数据信息
     * @param obj
     * @return
     * @author yoko
     * @date 2020/6/3 13:53
     */
    public List<ChannelProfitModel> getDataList(Object obj);

    /**
     * @Description: 更新渠道收益数据的状态
     * @param obj
     * @return
     * @author yoko
     * @date 2020/1/11 16:30
     */
    public int updateStatus(Object obj);
}
