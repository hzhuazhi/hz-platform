package com.hz.platform.master.core.service.impl;

import com.hz.platform.master.core.common.dao.BaseDao;
import com.hz.platform.master.core.common.service.impl.BaseServiceImpl;
import com.hz.platform.master.core.mapper.AlipayMapper;
import com.hz.platform.master.core.model.alipay.AlipayNotifyModel;
import com.hz.platform.master.core.service.AlipayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 阿里支付的Service层的实现层
 * @Author yoko
 * @Date 2019/12/26 13:54
 * @Version 1.0
 */
@Service
public class AlipayServiceImpl<T> extends BaseServiceImpl<T> implements AlipayService<T> {

    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private AlipayMapper alipayMapper;


    public BaseDao<T> getDao() {
        return alipayMapper;
    }

    @Override
    public int addAlipayNotify(AlipayNotifyModel model) {
        return alipayMapper.addAlipayNotify(model);
    }
}
