package com.hz.platform.master.core.service;

import com.hz.platform.master.core.common.service.BaseService;
import com.hz.platform.master.core.model.datacore.DataCoreModel;

import java.util.List;

/**
 * @Description task:通道回调的成功数据的Service层
 * @Author yoko
 * @Date 2021/1/19 18:41
 * @Version 1.0
 */
public interface TaskDataCoreService<T> extends BaseService<T> {

    /**
     * @Description: 查询未跑的通道回调数据
     * @param obj
     * @return
     * @author yoko
     * @date 2020/6/3 13:53
     */
    public List<DataCoreModel> getDataList(Object obj);

    /**
     * @Description: 更新通道回调数据的状态
     * @param obj
     * @return
     * @author yoko
     * @date 2020/1/11 16:30
     */
    public int updateStatus(Object obj);
}
