package com.hz.platform.master.core.service.impl;

import com.hz.platform.master.core.common.dao.BaseDao;
import com.hz.platform.master.core.common.service.impl.BaseServiceImpl;
import com.hz.platform.master.core.mapper.*;
import com.hz.platform.master.core.model.preparerecharge.PrepareRechargeModel;
import com.hz.platform.master.core.service.TaskPrepareRechargeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description task：代付预备充值表:预备给渠道进行充值的的Service层的实现层
 * @Author yoko
 * @Date 2020/11/1 14:19
 * @Version 1.0
 */
@Service
public class TaskPrepareRechargeServiceImpl<T> extends BaseServiceImpl<T> implements TaskPrepareRechargeService<T> {

    private final static Logger log = LoggerFactory.getLogger(TaskPrepareRechargeServiceImpl.class);
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private TaskPrepareRechargeMapper taskPrepareRechargeMapper;


    public BaseDao<T> getDao() {
        return taskPrepareRechargeMapper;
    }

    @Override
    public List<PrepareRechargeModel> getDataList(Object obj) {
        return taskPrepareRechargeMapper.getDataList(obj);
    }

    @Override
    public int updateStatus(Object obj) {
        return taskPrepareRechargeMapper.updateStatus(obj);
    }


}
