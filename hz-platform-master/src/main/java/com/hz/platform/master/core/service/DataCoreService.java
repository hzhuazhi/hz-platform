package com.hz.platform.master.core.service;

import com.hz.platform.master.core.common.service.BaseService;
import com.hz.platform.master.core.model.datacore.DataCoreModel;

/**
 * @Description 上游数据的Service层
 * @Author yoko
 * @Date 2020/3/25 11:47
 * @Version 1.0
 */
public interface DataCoreService<T> extends BaseService<T> {

    /**
     * @Description: 根据条件查询代收成功金额
     * @param model
     * @return
     * @Author: yoko
     * @Date 2021/7/26 13:51
     */
    public String getSumMoney(DataCoreModel model);
}
