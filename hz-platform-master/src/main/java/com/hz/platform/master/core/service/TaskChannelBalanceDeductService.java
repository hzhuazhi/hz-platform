package com.hz.platform.master.core.service;

import com.hz.platform.master.core.common.service.BaseService;
import com.hz.platform.master.core.model.channelbalancededuct.ChannelBalanceDeductModel;

import java.util.List;

/**
 * @Description task：渠道扣减余额流水的Service层
 * @Author yoko
 * @Date 2020/11/1 17:14
 * @Version 1.0
 */
public interface TaskChannelBalanceDeductService<T> extends BaseService<T> {

    /**
     * @Description: 查询未跑的渠道扣款流水信息
     * @param obj
     * @return
     * @author yoko
     * @date 2020/6/3 13:53
     */
    public List<ChannelBalanceDeductModel> getDataList(Object obj);

    /**
     * @Description: 更新渠道扣款流水信息数据的状态
     * @param obj
     * @return
     * @author yoko
     * @date 2020/1/11 16:30
     */
    public int updateStatus(Object obj);


}
