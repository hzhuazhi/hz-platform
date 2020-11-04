package com.hz.platform.master.core.service.impl;

import com.hz.platform.master.core.common.dao.BaseDao;
import com.hz.platform.master.core.common.service.impl.BaseServiceImpl;
import com.hz.platform.master.core.mapper.TaskChannelChangeMapper;
import com.hz.platform.master.core.model.channelchange.ChannelChangeModel;
import com.hz.platform.master.core.service.TaskChannelChangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description task：渠道金额变更纪录的Service层的实现层
 * @Author yoko
 * @Date 2020/11/4 11:31
 * @Version 1.0
 */
@Service
public class TaskChannelChangeServiceImpl<T> extends BaseServiceImpl<T> implements TaskChannelChangeService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private TaskChannelChangeMapper taskChannelChangeMapper;

    public BaseDao<T> getDao() {
        return taskChannelChangeMapper;
    }

    @Override
    public List<ChannelChangeModel> getDataList(Object obj) {
        return taskChannelChangeMapper.getDataList(obj);
    }

    @Override
    public int updateStatus(Object obj) {
        return taskChannelChangeMapper.updateStatus(obj);
    }
}
