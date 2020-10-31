package com.hz.platform.master.core.service.impl;

import com.hz.platform.master.core.common.dao.BaseDao;
import com.hz.platform.master.core.common.service.impl.BaseServiceImpl;
import com.hz.platform.master.core.mapper.ChannelOutMapper;
import com.hz.platform.master.core.service.ChannelOutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 渠道数据代付订单的Service层的实现层
 * @Author yoko
 * @Date 2020/10/31 15:36
 * @Version 1.0
 */
@Service
public class ChannelOutServiceImpl<T> extends BaseServiceImpl<T> implements ChannelOutService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private ChannelOutMapper channelOutMapper;


    public BaseDao<T> getDao() {
        return channelOutMapper;
    }
}
