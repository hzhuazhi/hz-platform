package com.hz.platform.master.core.service.impl;

import com.hz.platform.master.core.common.dao.BaseDao;
import com.hz.platform.master.core.common.service.impl.BaseServiceImpl;
import com.hz.platform.master.core.mapper.TaskDataCoreMapper;
import com.hz.platform.master.core.model.datacore.DataCoreModel;
import com.hz.platform.master.core.service.TaskDataCoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description task:通道回调的成功数据的Service层的实现层
 * @Author yoko
 * @Date 2021/1/19 18:42
 * @Version 1.0
 */
@Service
public class TaskDataCoreServiceImpl<T> extends BaseServiceImpl<T> implements TaskDataCoreService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private TaskDataCoreMapper taskDataCoreMapper;



    public BaseDao<T> getDao() {
        return taskDataCoreMapper;
    }

    @Override
    public List<DataCoreModel> getDataList(Object obj) {
        return taskDataCoreMapper.getDataList(obj);
    }

    @Override
    public int updateStatus(Object obj) {
        return taskDataCoreMapper.updateStatus(obj);
    }
}
