package com.hz.platform.master.core.service.impl;

import com.hz.platform.master.core.common.dao.BaseDao;
import com.hz.platform.master.core.common.service.impl.BaseServiceImpl;
import com.hz.platform.master.core.mapper.TaskDataCoreOutMapper;
import com.hz.platform.master.core.model.datacoreout.DataCoreOutModel;
import com.hz.platform.master.core.service.TaskDataCoreOutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description task：代付订单数据回传的Service层的实现层
 * @Author yoko
 * @Date 2020/11/1 14:19
 * @Version 1.0
 */
@Service
public class TaskDataCoreOutServiceImpl<T> extends BaseServiceImpl<T> implements TaskDataCoreOutService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private TaskDataCoreOutMapper taskDataCoreOutMapper;


    public BaseDao<T> getDao() {
        return taskDataCoreOutMapper;
    }

    @Override
    public List<DataCoreOutModel> getDataList(Object obj) {
        return taskDataCoreOutMapper.getDataList(obj);
    }

    @Override
    public int updateStatus(Object obj) {
        return taskDataCoreOutMapper.updateStatus(obj);
    }
}
