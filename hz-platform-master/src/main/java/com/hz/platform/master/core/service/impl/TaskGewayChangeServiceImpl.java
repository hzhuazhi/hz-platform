package com.hz.platform.master.core.service.impl;

import com.hz.platform.master.core.common.dao.BaseDao;
import com.hz.platform.master.core.common.service.impl.BaseServiceImpl;
import com.hz.platform.master.core.mapper.TaskGewayChangeMapper;
import com.hz.platform.master.core.model.geway.GewayChangeModel;
import com.hz.platform.master.core.service.TaskGewayChangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description task:通道金额变更纪录的Service层的实现层
 * @Author yoko
 * @Date 2021/1/18 18:47
 * @Version 1.0
 */
@Service
public class TaskGewayChangeServiceImpl<T> extends BaseServiceImpl<T> implements TaskGewayChangeService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private TaskGewayChangeMapper taskGewayChangeMapper;

    public BaseDao<T> getDao() {
        return taskGewayChangeMapper;
    }

    @Override
    public List<GewayChangeModel> getDataList(Object obj) {
        return taskGewayChangeMapper.getDataList(obj);
    }

    @Override
    public int updateStatus(Object obj) {
        return taskGewayChangeMapper.updateStatus(obj);
    }
}
