package com.hz.platform.master.core.service.impl;

import com.hz.platform.master.core.common.dao.BaseDao;
import com.hz.platform.master.core.common.service.impl.BaseServiceImpl;
import com.hz.platform.master.core.mapper.DataCoreOutMapper;
import com.hz.platform.master.core.service.DataCoreOutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 代付订单数据回传:接口上游的数据的Service层的实现层
 * @Author yoko
 * @Date 2020/10/31 21:39
 * @Version 1.0
 */
@Service
public class DataCoreOutServiceImpl<T> extends BaseServiceImpl<T> implements DataCoreOutService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private DataCoreOutMapper dataCoreOutMapper;


    public BaseDao<T> getDao() {
        return dataCoreOutMapper;
    }
}
