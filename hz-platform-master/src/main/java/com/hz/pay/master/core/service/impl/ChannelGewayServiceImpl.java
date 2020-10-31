package com.hz.pay.master.core.service.impl;

import com.hz.pay.master.core.common.dao.BaseDao;
import com.hz.pay.master.core.common.service.impl.BaseServiceImpl;
import com.hz.pay.master.core.mapper.ChannelGewayMapper;
import com.hz.pay.master.core.service.ChannelGewayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 渠道与通道的关联关系的Service层的实现层
 * @Author yoko
 * @Date 2020/3/24 16:01
 * @Version 1.0
 */
@Service
public class ChannelGewayServiceImpl<T> extends BaseServiceImpl<T> implements ChannelGewayService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private ChannelGewayMapper channelGewayMapper;


    public BaseDao<T> getDao() {
        return channelGewayMapper;
    }
}
