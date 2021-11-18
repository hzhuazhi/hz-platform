package com.hz.platform.master.core.service;

import com.hz.platform.master.core.common.service.BaseService;
import com.hz.platform.master.core.model.preparerecharge.PrepareRechargeModel;

import java.util.List;

/**
 * @Description task：代付订单数据回传的Service层
 * @Author yoko
 * @Date 2020/11/1 14:17
 * @Version 1.0
 */
public interface TaskPrepareRechargeService<T> extends BaseService<T> {

    /**
     * @Description: 查询未跑的代付预备充值信息
     * @param obj
     * @return
     * @author yoko
     * @date 2020/6/3 13:53
     */
    public List<PrepareRechargeModel> getDataList(Object obj);

    /**
     * @Description: 更新代付预备充值信息数据的状态
     * @param obj
     * @return
     * @author yoko
     * @date 2020/1/11 16:30
     */
    public int updateStatus(Object obj);

}
