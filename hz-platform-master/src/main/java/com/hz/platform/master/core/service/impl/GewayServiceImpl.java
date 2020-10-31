package com.hz.platform.master.core.service.impl;

import com.hz.platform.master.core.common.dao.BaseDao;
import com.hz.platform.master.core.common.service.impl.BaseServiceImpl;
import com.hz.platform.master.core.mapper.GewayMapper;
import com.hz.platform.master.core.service.GewayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 通道的Service层的实现层
 * @Author yoko
 * @Date 2020/3/24 15:41
 * @Version 1.0
 */
@Service
public class GewayServiceImpl<T> extends BaseServiceImpl<T> implements GewayService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private GewayMapper gewayMapper;


    public BaseDao<T> getDao() {
        return gewayMapper;
    }
}
