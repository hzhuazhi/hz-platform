package com.hz.platform.master.core.service.impl;

import com.hz.platform.master.core.common.dao.BaseDao;
import com.hz.platform.master.core.common.service.impl.BaseServiceImpl;
import com.hz.platform.master.core.mapper.TaskGewayProfitMapper;
import com.hz.platform.master.core.model.geway.GewayProfitModel;
import com.hz.platform.master.core.service.TaskGewayProfitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description task:通道收益数据的Service层的实现层
 * @Author yoko
 * @Date 2021/1/21 14:36
 * @Version 1.0
 */
@Service
public class TaskGewayProfitServiceImpl<T> extends BaseServiceImpl<T> implements TaskGewayProfitService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private TaskGewayProfitMapper taskGewayProfitMapper;

    public BaseDao<T> getDao() {
        return taskGewayProfitMapper;
    }

    @Override
    public List<GewayProfitModel> getDataList(Object obj) {
        return taskGewayProfitMapper.getDataList(obj);
    }

    @Override
    public int updateStatus(Object obj) {
        return taskGewayProfitMapper.updateStatus(obj);
    }
}
