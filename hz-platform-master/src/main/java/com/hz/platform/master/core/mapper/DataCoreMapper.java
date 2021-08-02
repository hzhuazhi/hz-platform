package com.hz.platform.master.core.mapper;

import com.hz.platform.master.core.common.dao.BaseDao;
import com.hz.platform.master.core.model.datacore.DataCoreModel;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 上游数据的Dao层
 * @Author yoko
 * @Date 2020/3/25 11:46
 * @Version 1.0
 */
@Mapper
public interface DataCoreMapper<T> extends BaseDao<T> {

    /**
     * @Description: 根据条件查询代收成功金额
     * @param model
     * @return 
     * @Author: yoko
     * @Date 2021/7/26 13:51 
    */
    public String getSumMoney(DataCoreModel model);
}
