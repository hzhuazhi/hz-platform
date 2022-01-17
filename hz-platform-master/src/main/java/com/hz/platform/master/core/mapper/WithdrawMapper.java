package com.hz.platform.master.core.mapper;

import com.hz.platform.master.core.common.dao.BaseDao;
import com.hz.platform.master.core.model.withdraw.WithdrawModel;
import org.apache.ibatis.annotations.Mapper;


/**
 * @Description 提现记录的Dao层
 * @Author yoko
 * @Date 2020/11/18 18:40
 * @Version 1.0
 */
@Mapper
public interface WithdrawMapper<T> extends BaseDao<T> {

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
