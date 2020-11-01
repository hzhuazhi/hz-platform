package com.hz.platform.master.core.service.impl;

import com.hz.platform.master.core.common.dao.BaseDao;
import com.hz.platform.master.core.common.exception.ServiceException;
import com.hz.platform.master.core.common.service.impl.BaseServiceImpl;
import com.hz.platform.master.core.mapper.ChannelBalanceDeductMapper;
import com.hz.platform.master.core.mapper.ChannelOutMapper;
import com.hz.platform.master.core.mapper.TaskDataCoreOutMapper;
import com.hz.platform.master.core.model.channelbalancededuct.ChannelBalanceDeductModel;
import com.hz.platform.master.core.model.channelout.ChannelOutModel;
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

    @Autowired
    private ChannelBalanceDeductMapper channelBalanceDeductMapper;

    @Autowired
    private ChannelOutMapper channelOutMapper;

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

    @Override
    public boolean handleDataCoreOut(ChannelBalanceDeductModel channelBalanceDeductModel, ChannelOutModel channelOutModel) throws Exception {
        int num1 = channelBalanceDeductMapper.updateOrderStatusByOrderNo(channelBalanceDeductModel);
        int num2 = channelOutMapper.updateOrderStatusByOrderNo(channelOutModel);

        if (num1 > 0 && num2 > 0){
            return true;
        }else{
            throw new ServiceException("handleDataCoreOut", "二个执行更新SQL其中有一个或者多个响应行为0");
        }
    }
}
