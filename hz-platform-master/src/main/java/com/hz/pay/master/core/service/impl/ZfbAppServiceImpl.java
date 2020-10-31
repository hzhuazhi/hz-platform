package com.hz.pay.master.core.service.impl;

import com.hz.pay.master.core.common.dao.BaseDao;
import com.hz.pay.master.core.common.service.impl.BaseServiceImpl;
import com.hz.pay.master.core.mapper.ZfbAppMapper;
import com.hz.pay.master.core.service.ZfbAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 支付宝账号的Service层的实现层
 * @Author yoko
 * @Date 2020/4/9 17:03
 * @Version 1.0
 */
@Service
public class ZfbAppServiceImpl<T> extends BaseServiceImpl<T> implements ZfbAppService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private ZfbAppMapper zfbAppMapper;


    public BaseDao<T> getDao() {
        return zfbAppMapper;
    }
}
