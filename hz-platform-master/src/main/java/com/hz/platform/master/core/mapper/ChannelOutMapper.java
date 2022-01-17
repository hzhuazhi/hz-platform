package com.hz.platform.master.core.mapper;

import com.hz.platform.master.core.common.dao.BaseDao;
import com.hz.platform.master.core.model.channelout.ChannelOutModel;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 渠道数据代付订单的Dao层
 * @Author yoko
 * @Date 2020/10/31 15:32
 * @Version 1.0
 */
@Mapper
public interface ChannelOutMapper<T> extends BaseDao<T> {
    
    /**
     * @Description: 根据订单号更新代付订单的订单信息
     * @param model
     * @return 
     * @author yoko
     * @date 2020/11/1 16:23 
    */
    public int updateOrderStatusByOrderNo(ChannelOutModel model);

    /**
     * @Description:根据条件获取当天跑量金额，实际金额
     * @param model
     * @author: yoko
     * @date: 2021/12/31 16:12
     * @version 1.0.0
     */
    public ChannelOutModel totalDayMoney(ChannelOutModel model);
}
