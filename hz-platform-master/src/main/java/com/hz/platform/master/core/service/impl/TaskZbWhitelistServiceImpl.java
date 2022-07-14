package com.hz.platform.master.core.service.impl;

import com.hz.platform.master.core.common.dao.BaseDao;
import com.hz.platform.master.core.common.service.impl.BaseServiceImpl;
import com.hz.platform.master.core.mapper.TaskZbWhitelistMapper;
import com.hz.platform.master.core.model.zhongbang.ZbWhitelistModel;
import com.hz.platform.master.core.service.TaskZbWhitelistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description task:众邦白名单的Service层的实现层
 * @Author yoko
 * @Date 2020/9/14 22:15
 * @Version 1.0
 */
@Service
public class TaskZbWhitelistServiceImpl<T> extends BaseServiceImpl<T> implements TaskZbWhitelistService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private TaskZbWhitelistMapper taskZbWhitelistMapper;




    public BaseDao<T> getDao() {
        return taskZbWhitelistMapper;
    }

    @Override
    public List<ZbWhitelistModel> getDataList(Object obj) {
        return taskZbWhitelistMapper.getDataList(obj);
    }

    @Override
    public int updateStatus(Object obj) {
        return taskZbWhitelistMapper.updateStatus(obj);
    }

    @Override
    public List<ZbWhitelistModel> getWhitelistNotifyList(Object obj) {
        return taskZbWhitelistMapper.getWhitelistNotifyList(obj);
    }


}
