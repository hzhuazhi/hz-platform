package com.hz.pay.master.core.service.impl;

import com.hz.pay.master.core.common.dao.BaseDao;
import com.hz.pay.master.core.common.service.impl.BaseServiceImpl;
import com.hz.pay.master.core.mapper.TaskMapper;
import com.hz.pay.master.core.model.datacore.DataCoreModel;
import com.hz.pay.master.core.model.task.TaskAlipayNotifyModel;
import com.hz.pay.master.core.model.withdraw.WithdrawModel;
import com.hz.pay.master.core.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description TODO
 * @Author yoko
 * @Date 2020/3/25 16:14
 * @Version 1.0
 */
@Service
public class TaskServiceImpl<T> extends BaseServiceImpl<T> implements TaskService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private TaskMapper taskMapper;


    public BaseDao<T> getDao() {
        return taskMapper;
    }

    @Override
    public List<DataCoreModel> getWorkTypeList(Object obj) {
        return taskMapper.getWorkTypeList(obj);
    }

    @Override
    public int updateWorkType(Object obj) {
        return taskMapper.updateWorkType(obj);
    }

    @Override
    public int updateChannelMoney(Object obj) {
        return taskMapper.updateChannelMoney(obj);
    }

    @Override
    public List<DataCoreModel> getNotifyList(Object obj) {
        return taskMapper.getNotifyList(obj);
    }

    @Override
    public int updateNotifyStatus(Object obj) {
        return taskMapper.updateNotifyStatus(obj);
    }

    @Override
    public List<WithdrawModel> getWithdrawList(Object obj) {
        return taskMapper.getWithdrawList(obj);
    }

    @Override
    public int updateWithdrawStatus(Object obj) {
        return taskMapper.updateWithdrawStatus(obj);
    }

    @Override
    public int updateChannelReduceMoney(Object obj) {
        return taskMapper.updateChannelReduceMoney(obj);
    }

    @Override
    public int updateAgentReduceMoney(Object obj) {
        return taskMapper.updateAgentReduceMoney(obj);
    }

    @Override
    public List<TaskAlipayNotifyModel> getTaskAlipayNotify(Object obj) {
        return taskMapper.getTaskAlipayNotify(obj);
    }

    @Override
    public int updateTaskAlipayNotifyStatus(Object obj) {
        return taskMapper.updateTaskAlipayNotifyStatus(obj);
    }
}
