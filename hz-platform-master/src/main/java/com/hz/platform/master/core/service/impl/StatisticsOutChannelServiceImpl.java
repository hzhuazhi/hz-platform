package com.hz.platform.master.core.service.impl;

import com.hz.platform.master.core.common.dao.BaseDao;
import com.hz.platform.master.core.common.service.impl.BaseServiceImpl;
import com.hz.platform.master.core.mapper.StatisticsOutChannelMapper;
import com.hz.platform.master.core.service.StatisticsOutChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 统计代付渠道的每日记录的Service层的实现层
 * @Author yoko
 * @Date 2020/3/2 17:30
 * @Version 1.0
 */
@Service
public class StatisticsOutChannelServiceImpl<T> extends BaseServiceImpl<T> implements StatisticsOutChannelService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private StatisticsOutChannelMapper statisticsOutChannelMapper;


    public BaseDao<T> getDao() {
        return statisticsOutChannelMapper;
    }

}
