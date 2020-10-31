package com.hz.pay.master.core.service.impl;

import com.hz.pay.master.core.common.dao.BaseDao;
import com.hz.pay.master.core.common.service.impl.BaseServiceImpl;
import com.hz.pay.master.core.mapper.DataCoreMapper;
import com.hz.pay.master.core.service.DataCoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 上游数据的Service层的实现层
 * @Author yoko
 * @Date 2020/3/25 11:48
 * @Version 1.0
 */
@Service
public class DataCoreServiceImpl<T> extends BaseServiceImpl<T> implements DataCoreService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private DataCoreMapper dataCoreMapper;


    public BaseDao<T> getDao() {
        return dataCoreMapper;
    }
}
