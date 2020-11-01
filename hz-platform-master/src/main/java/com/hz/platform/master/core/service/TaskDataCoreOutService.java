package com.hz.platform.master.core.service;

import com.hz.platform.master.core.common.service.BaseService;
import com.hz.platform.master.core.model.datacoreout.DataCoreOutModel;

import java.util.List;

/**
 * @Description task：代付订单数据回传的Service层
 * @Author yoko
 * @Date 2020/11/1 14:17
 * @Version 1.0
 */
public interface TaskDataCoreOutService<T> extends BaseService<T> {

    /**
     * @Description: 查询未跑的代付回调信息
     * @param obj
     * @return
     * @author yoko
     * @date 2020/6/3 13:53
     */
    public List<DataCoreOutModel> getDataList(Object obj);

    /**
     * @Description: 更新代付回调信息数据的状态
     * @param obj
     * @return
     * @author yoko
     * @date 2020/1/11 16:30
     */
    public int updateStatus(Object obj);
}
