package com.hz.pay.master.core.mapper;

import com.hz.pay.master.core.common.dao.BaseDao;
import com.hz.pay.master.core.model.alipay.AlipayNotifyModel;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 阿里支付的Dao层
 * @Author yoko
 * @Date 2019/12/26 13:51
 * @Version 1.0
 */
@Mapper
public interface AlipayMapper<T> extends BaseDao<T> {

    /**
     * @Description: 添加阿里支付宝订单结果数据
     * @param model - 订单回调的数据
     * @return int
     * @author yoko
     * @date 2019/12/27 15:56
     */
    public int addAlipayNotify(AlipayNotifyModel model);
}
