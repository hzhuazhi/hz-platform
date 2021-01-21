package com.hz.platform.master.core.service;

import com.hz.platform.master.core.common.service.BaseService;
import com.hz.platform.master.core.model.channel.ChannelProfitModel;

import java.util.List;

/**
 * @Description task:渠道收益数据的Service层
 * @Author yoko
 * @Date 2021/1/21 14:34
 * @Version 1.0
 */
public interface TaskChannelProfitService<T> extends BaseService<T> {

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
