package com.hz.platform.master.core.service.impl;

import com.hz.platform.master.core.common.dao.BaseDao;
import com.hz.platform.master.core.common.service.impl.BaseServiceImpl;
import com.hz.platform.master.core.mapper.ReceivingAccountDataMapper;
import com.hz.platform.master.core.model.receivingaccountdata.ReceivingAccountDataModel;
import com.hz.platform.master.core.service.ReceivingAccountDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 收款账号的数据纪录的Service层的实现层
 * @Author yoko
 * @Date 2020/4/20 16:53
 * @Version 1.0
 */
@Service
public class ReceivingAccountDataServiceImpl<T> extends BaseServiceImpl<T> implements ReceivingAccountDataService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private ReceivingAccountDataMapper receivingAccountDataMapper;


    public BaseDao<T> getDao() {
        return receivingAccountDataMapper;
    }

    @Override
    public String getTotalMoney(ReceivingAccountDataModel model) {
        return receivingAccountDataMapper.getTotalMoney(model);
    }

    @Override
    public int getTotalNum(ReceivingAccountDataModel model) {
        return receivingAccountDataMapper.getTotalNum(model);
    }
}
