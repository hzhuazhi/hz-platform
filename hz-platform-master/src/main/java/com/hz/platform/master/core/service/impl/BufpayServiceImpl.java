package com.hz.platform.master.core.service.impl;

import com.hz.platform.master.core.common.dao.BaseDao;
import com.hz.platform.master.core.common.service.impl.BaseServiceImpl;
import com.hz.platform.master.core.mapper.BufpayMapper;
import com.hz.platform.master.core.service.BufpayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description bufPay账号Service层的实现层
 * @Author yoko
 * @Date 2020/4/20 16:53
 * @Version 1.0
 */
@Service
public class BufpayServiceImpl<T> extends BaseServiceImpl<T> implements BufpayService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private BufpayMapper bufpayMapper;


    public BaseDao<T> getDao() {
        return bufpayMapper;
    }
}
