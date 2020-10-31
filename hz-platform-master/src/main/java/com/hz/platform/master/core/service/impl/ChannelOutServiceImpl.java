package com.hz.platform.master.core.service.impl;

import com.hz.platform.master.core.common.dao.BaseDao;
import com.hz.platform.master.core.common.exception.ServiceException;
import com.hz.platform.master.core.common.service.impl.BaseServiceImpl;
import com.hz.platform.master.core.mapper.ChannelBalanceDeductMapper;
import com.hz.platform.master.core.mapper.ChannelMapper;
import com.hz.platform.master.core.mapper.ChannelOutMapper;
import com.hz.platform.master.core.model.channel.ChannelModel;
import com.hz.platform.master.core.model.channelbalancededuct.ChannelBalanceDeductModel;
import com.hz.platform.master.core.model.channelout.ChannelOutModel;
import com.hz.platform.master.core.service.ChannelOutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private ChannelMapper channelMapper;

    @Autowired
    private ChannelBalanceDeductMapper channelBalanceDeductMapper;


    public BaseDao<T> getDao() {
        return channelOutMapper;
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public boolean handleChannelOut(ChannelModel channelModel, ChannelBalanceDeductModel channelBalanceDeductModel, ChannelOutModel channelOutModel) throws Exception {
        int num1 = channelMapper.updateBalance(channelModel);
        int num2 = channelBalanceDeductMapper.add(channelBalanceDeductModel);
        int num3 = 0;
        int num4 = channelOutMapper.add(channelOutModel);

        if (num1 > 0 && num2 > 0 && num3 > 0){
            return true;
        }else{
            throw new ServiceException("handleChannelOut", "三个执行更新SQL其中有一个或者多个响应行为0");
        }
    }
}
