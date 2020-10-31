package com.hz.platform.master.core.service.impl;

import com.hz.platform.master.core.common.dao.BaseDao;
import com.hz.platform.master.core.common.service.impl.BaseServiceImpl;
import com.hz.platform.master.core.mapper.ChannelDataMapper;
import com.hz.platform.master.core.service.ChannelDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 渠道数据的Service层的实现层
 * @Author yoko
 * @Date 2020/3/24 21:17
 * @Version 1.0
 */
@Service
public class ChannelDataServiceImpl<T> extends BaseServiceImpl<T> implements ChannelDataService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private ChannelDataMapper channelDataMapper;


    public BaseDao<T> getDao() {
        return channelDataMapper;
    }
}
