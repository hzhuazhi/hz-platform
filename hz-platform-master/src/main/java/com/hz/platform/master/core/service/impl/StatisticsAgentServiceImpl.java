package com.hz.platform.master.core.service.impl;

import com.hz.platform.master.core.common.dao.BaseDao;
import com.hz.platform.master.core.common.service.impl.BaseServiceImpl;
import com.hz.platform.master.core.mapper.StatisticsAgentMapper;
import com.hz.platform.master.core.service.StatisticsAgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 统计代理的每日记录的Service层的实现层
 * @Author yoko
 * @Date 2020/3/2 17:30
 * @Version 1.0
 */
@Service
public class StatisticsAgentServiceImpl<T> extends BaseServiceImpl<T> implements StatisticsAgentService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private StatisticsAgentMapper statisticsAgentMapper;


    public BaseDao<T> getDao() {
        return statisticsAgentMapper;
    }

}
