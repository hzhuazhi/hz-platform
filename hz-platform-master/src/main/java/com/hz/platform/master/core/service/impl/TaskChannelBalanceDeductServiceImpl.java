package com.hz.platform.master.core.service.impl;

import com.hz.platform.master.core.common.dao.BaseDao;
import com.hz.platform.master.core.common.service.impl.BaseServiceImpl;
import com.hz.platform.master.core.mapper.TaskChannelBalanceDeductMapper;
import com.hz.platform.master.core.model.channelbalancededuct.ChannelBalanceDeductModel;
import com.hz.platform.master.core.service.TaskChannelBalanceDeductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description task：渠道扣减余额流水的Service层的实现层
 * @Author yoko
 * @Date 2020/11/1 17:16
 * @Version 1.0
 */
@Service
public class TaskChannelBalanceDeductServiceImpl<T> extends BaseServiceImpl<T> implements TaskChannelBalanceDeductService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private TaskChannelBalanceDeductMapper taskChannelBalanceDeductMapper;

    public BaseDao<T> getDao() {
        return taskChannelBalanceDeductMapper;
    }

    @Override
    public List<ChannelBalanceDeductModel> getDataList(Object obj) {
        return taskChannelBalanceDeductMapper.getDataList(obj);
    }

    @Override
    public int updateStatus(Object obj) {
        return taskChannelBalanceDeductMapper.updateStatus(obj);
    }
}
