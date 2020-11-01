package com.hz.platform.master.core.service;

import com.hz.platform.master.core.common.service.BaseService;
import com.hz.platform.master.core.model.channelbalancededuct.ChannelBalanceDeductModel;

/**
 * @Description 渠道扣减余额流水的Service层
 * @Author yoko
 * @Date 2020/10/31 16:25
 * @Version 1.0
 */
public interface ChannelBalanceDeductService<T> extends BaseService<T> {

    /**
     * @Description: 根据订单号更新渠道扣款流水的订单状态
     * @param model - 基本信息
     * @return
     * @author yoko
     * @date 2020/11/1 15:38
     */
    public int updateOrderStatusByOrderNo(ChannelBalanceDeductModel model);
}
