package com.hz.platform.master.core.service.impl;

import com.hz.platform.master.core.common.dao.BaseDao;
import com.hz.platform.master.core.common.service.impl.BaseServiceImpl;
import com.hz.platform.master.core.mapper.ChannelBalanceDeductMapper;
import com.hz.platform.master.core.service.ChannelBalanceDeductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 渠道扣减余额流水的Service层的实现层
 * @Author yoko
 * @Date 2020/10/31 16:26
 * @Version 1.0
 */
@Service
public class ChannelBalanceDeductServiceImpl<T> extends BaseServiceImpl<T> implements ChannelBalanceDeductService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private ChannelBalanceDeductMapper channelBalanceDeductMapper;


    public BaseDao<T> getDao() {
        return channelBalanceDeductMapper;
    }
}
