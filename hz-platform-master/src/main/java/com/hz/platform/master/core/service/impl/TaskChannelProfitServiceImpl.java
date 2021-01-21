package com.hz.platform.master.core.service.impl;

import com.hz.platform.master.core.common.dao.BaseDao;
import com.hz.platform.master.core.common.service.impl.BaseServiceImpl;
import com.hz.platform.master.core.mapper.TaskChannelProfitMapper;
import com.hz.platform.master.core.model.channel.ChannelProfitModel;
import com.hz.platform.master.core.service.TaskChannelProfitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description task:渠道收益数据的Service层的实现层
 * @Author yoko
 * @Date 2021/1/21 14:36
 * @Version 1.0
 */
@Service
public class TaskChannelProfitServiceImpl<T> extends BaseServiceImpl<T> implements TaskChannelProfitService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private TaskChannelProfitMapper taskChannelProfitMapper;

    public BaseDao<T> getDao() {
        return taskChannelProfitMapper;
    }

    @Override
    public List<ChannelProfitModel> getDataList(Object obj) {
        return taskChannelProfitMapper.getDataList(obj);
    }

    @Override
    public int updateStatus(Object obj) {
        return taskChannelProfitMapper.updateStatus(obj);
    }
}
