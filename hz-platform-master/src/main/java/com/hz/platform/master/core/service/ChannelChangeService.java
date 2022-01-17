package com.hz.platform.master.core.service;

import com.hz.platform.master.core.common.service.BaseService;
import com.hz.platform.master.core.model.channelchange.ChannelChangeModel;

/**
 * @Description 渠道金额变更纪录的Service层
 * @Author yoko
 * @Date 2020/11/4 10:49
 * @Version 1.0
 */
public interface ChannelChangeService<T> extends BaseService<T> {
    /**
     * @Description:根据条件获取渠道加减金额
     * @param model
     * @author: yoko
     * @date: 2021/12/31 17:41
     * @version 1.0.0
     */
    public ChannelChangeModel getDayMoney(ChannelChangeModel model);
}
