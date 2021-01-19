package com.hz.platform.master.core.service;

import com.hz.platform.master.core.common.service.BaseService;
import com.hz.platform.master.core.model.geway.GewayChangeModel;

import java.util.List;

/**
 * @Description task:通道金额变更纪录的Service层
 * @Author yoko
 * @Date 2021/1/18 18:26
 * @Version 1.0
 */
public interface TaskGewayChangeService<T> extends BaseService<T> {

    /**
     * @Description: 查询未跑的通道金额变更纪录数据
     * @param obj
     * @return
     * @author yoko
     * @date 2020/6/3 13:53
     */
    public List<GewayChangeModel> getDataList(Object obj);

    /**
     * @Description: 更新通道金额变更纪录信息数据的状态
     * @param obj
     * @return
     * @author yoko
     * @date 2020/1/11 16:30
     */
    public int updateStatus(Object obj);
}
