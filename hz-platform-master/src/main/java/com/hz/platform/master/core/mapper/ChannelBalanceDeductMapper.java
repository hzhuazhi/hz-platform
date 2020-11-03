package com.hz.platform.master.core.mapper;

import com.hz.platform.master.core.common.dao.BaseDao;
import com.hz.platform.master.core.model.channelbalancededuct.ChannelBalanceDeductModel;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 渠道扣减余额流水的Dao层
 * @Author yoko
 * @Date 2020/10/31 16:22
 * @Version 1.0
 */
@Mapper
public interface ChannelBalanceDeductMapper<T> extends BaseDao<T> {

    /**
     * @Description: 根据订单号更新渠道扣款流水的订单状态
     * @param model - 基本信息
     * @return
     * @author yoko
     * @date 2020/11/1 15:38
    */
    public int updateOrderStatusByOrderNo(ChannelBalanceDeductModel model);

    /**
     * @Description: 获取渠道的扣款流水总和
     * @param model
     * @return
     * @author yoko
     * @date 2020/11/3 11:22
    */
    public String sumMoney(ChannelBalanceDeductModel model);


}
