package com.hz.platform.master.core.service;

import com.hz.platform.master.core.common.service.BaseService;
import com.hz.platform.master.core.model.withdraw.WithdrawModel;


/**
 * @Description task：提现记录的Service层
 * @Author yoko
 * @Date 2020/11/18 18:42
 * @Version 1.0
 */
public interface WithdrawService<T> extends BaseService<T> {

    /**
     * @Description:根据条件获取提现信息
     * @param model
     * @return:
     * @author: yoko
     * @date: 2021/12/31 16:42
     * @version 1.0.0
     */
    public WithdrawModel getWithdrawMoney(WithdrawModel model);
}
