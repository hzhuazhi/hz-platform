package com.hz.pay.master.core.service.impl;

import com.hz.pay.master.core.common.dao.BaseDao;
import com.hz.pay.master.core.common.service.impl.BaseServiceImpl;
import com.hz.pay.master.core.mapper.ReceivingAccountMapper;
import com.hz.pay.master.core.service.ReceivingAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 收款账号的Service层的实现层
 * @Author yoko
 * @Date 2020/4/20 16:53
 * @Version 1.0
 */
@Service
public class ReceivingAccountServiceImpl<T> extends BaseServiceImpl<T> implements ReceivingAccountService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private ReceivingAccountMapper receivingAccountMapper;


    public BaseDao<T> getDao() {
        return receivingAccountMapper;
    }
}
