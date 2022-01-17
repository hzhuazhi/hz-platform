package com.hz.platform.master.core.service.impl;

import com.hz.platform.master.core.common.dao.BaseDao;
import com.hz.platform.master.core.common.service.impl.BaseServiceImpl;
import com.hz.platform.master.core.mapper.WithdrawMapper;
import com.hz.platform.master.core.model.withdraw.WithdrawModel;
import com.hz.platform.master.core.service.WithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @Description task：提现记录的Service层的实现层
 * @Author yoko
 * @Date 2020/11/18 18:43
 * @Version 1.0
 */
@Service
public class WithdrawServiceImpl<T> extends BaseServiceImpl<T> implements WithdrawService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private WithdrawMapper withdrawMapper;

    public BaseDao<T> getDao() {
        return withdrawMapper;
    }

    @Override
    public WithdrawModel getWithdrawMoney(WithdrawModel model) {
        return withdrawMapper.getWithdrawMoney(model);
    }
}
