package com.hz.platform.master.core.service.impl;

import com.hz.platform.master.core.common.dao.BaseDao;
import com.hz.platform.master.core.common.service.impl.BaseServiceImpl;
import com.hz.platform.master.core.mapper.GewayProfitMapper;
import com.hz.platform.master.core.service.GewayProfitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 通道收益数据的Service层的实现层
 * @Author yoko
 * @Date 2021/1/20 16:23
 * @Version 1.0
 */
@Service
public class GewayProfitServiceImpl<T> extends BaseServiceImpl<T> implements GewayProfitService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private GewayProfitMapper gewayProfitMapper;


    public BaseDao<T> getDao() {
        return gewayProfitMapper;
    }
}
