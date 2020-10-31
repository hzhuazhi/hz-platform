package com.hz.pay.master.core.service;

import com.hz.pay.master.core.common.service.BaseService;
import com.hz.pay.master.core.model.receivingaccountdata.ReceivingAccountDataModel;

/**
 * @Description 收款账号的数据纪录的Service层
 * @Author yoko
 * @Date 2020/4/20 16:52
 * @Version 1.0
 */
public interface ReceivingAccountDataService<T> extends BaseService<T> {

    /**
     * @Description: 查询每日，每月，每年账户收款的总金额
     * @param model - 收款账号的数据
     * @return int
     * @author yoko
     * @date 2019/12/27 15:56
     */
    public String getTotalMoney(ReceivingAccountDataModel model);


    /**
     * @Description: 查询每日，每月，每年账户收款的总次数
     * @param model - 收款账号的数据
     * @return int
     * @author yoko
     * @date 2019/12/27 15:56
     */
    public int getTotalNum(ReceivingAccountDataModel model);
}
