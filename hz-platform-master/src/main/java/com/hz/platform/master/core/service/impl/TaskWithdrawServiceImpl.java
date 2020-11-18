package com.hz.platform.master.core.service.impl;

import com.hz.platform.master.core.common.dao.BaseDao;
import com.hz.platform.master.core.common.service.impl.BaseServiceImpl;
import com.hz.platform.master.core.mapper.TaskWithdrawMapper;
import com.hz.platform.master.core.model.withdraw.WithdrawModel;
import com.hz.platform.master.core.service.TaskWithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description task：提现记录的Service层的实现层
 * @Author yoko
 * @Date 2020/11/18 18:43
 * @Version 1.0
 */
@Service
public class TaskWithdrawServiceImpl<T> extends BaseServiceImpl<T> implements TaskWithdrawService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private TaskWithdrawMapper taskWithdrawMapper;

    public BaseDao<T> getDao() {
        return taskWithdrawMapper;
    }

    @Override
    public List<WithdrawModel> getDataList(Object obj) {
        return taskWithdrawMapper.getDataList(obj);
    }

    @Override
    public int updateStatus(Object obj) {
        return taskWithdrawMapper.updateStatus(obj);
    }
}
