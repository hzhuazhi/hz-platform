package com.hz.platform.master.core.service;

import com.hz.platform.master.core.common.service.BaseService;
import com.hz.platform.master.core.model.geway.GewayProfitModel;

import java.util.List;

/**
 * @Description task:通道收益数据的Service层
 * @Author yoko
 * @Date 2021/1/21 14:33
 * @Version 1.0
 */
public interface TaskGewayProfitService<T> extends BaseService<T> {

    /**
     * @Description: 查询未跑的通道收益数据信息
     * @param obj
     * @return
     * @author yoko
     * @date 2020/6/3 13:53
     */
    public List<GewayProfitModel> getDataList(Object obj);

    /**
     * @Description: 更新通道收益数据的状态
     * @param obj
     * @return
     * @author yoko
     * @date 2020/1/11 16:30
     */
    public int updateStatus(Object obj);
}
