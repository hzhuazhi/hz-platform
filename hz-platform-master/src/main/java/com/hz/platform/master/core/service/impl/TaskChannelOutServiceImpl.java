package com.hz.platform.master.core.service.impl;

import com.hz.platform.master.core.common.dao.BaseDao;
import com.hz.platform.master.core.common.service.impl.BaseServiceImpl;
import com.hz.platform.master.core.mapper.TaskChannelOutMapper;
import com.hz.platform.master.core.model.channelout.ChannelOutModel;
import com.hz.platform.master.core.service.TaskChannelOutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description task：渠道数据代付订单的Service层的实现层
 * @Author yoko
 * @Date 2020/11/1 18:32
 * @Version 1.0
 */
@Service
public class TaskChannelOutServiceImpl<T> extends BaseServiceImpl<T> implements TaskChannelOutService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private TaskChannelOutMapper taskChannelOutMapper;

    public BaseDao<T> getDao() {
        return taskChannelOutMapper;
    }

    @Override
    public List<ChannelOutModel> getDataList(Object obj) {
        return taskChannelOutMapper.getDataList(obj);
    }

    @Override
    public int updateStatus(Object obj) {
        return taskChannelOutMapper.updateStatus(obj);
    }
}
