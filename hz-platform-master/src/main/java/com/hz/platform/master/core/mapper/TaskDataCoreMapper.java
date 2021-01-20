package com.hz.platform.master.core.mapper;

import com.hz.platform.master.core.common.dao.BaseDao;
import com.hz.platform.master.core.model.datacore.DataCoreModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description task:通道回调的成功数据的Dao层
 * @Author yoko
 * @Date 2021/1/19 18:37
 * @Version 1.0
 */
@Mapper
public interface TaskDataCoreMapper<T> extends BaseDao<T> {

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
