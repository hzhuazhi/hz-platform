package com.hz.platform.master.core.service.impl;

import com.hz.platform.master.core.common.dao.BaseDao;
import com.hz.platform.master.core.common.service.impl.BaseServiceImpl;
import com.hz.platform.master.core.mapper.GewaytradetypeMapper;
import com.hz.platform.master.core.service.GewaytradetypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 通道的支付类型的Service层的实现层
 * @Author yoko
 * @Date 2020/3/31 17:35
 * @Version 1.0
 */
@Service
public class GewaytradetypeServiceImpl<T> extends BaseServiceImpl<T> implements GewaytradetypeService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private GewaytradetypeMapper gewaytradetypeMapper;


    public BaseDao<T> getDao() {
        return gewaytradetypeMapper;
    }
}
