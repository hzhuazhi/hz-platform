package com.hz.pay.master.core.service.impl;

import com.hz.pay.master.core.common.dao.BaseDao;
import com.hz.pay.master.core.common.service.impl.BaseServiceImpl;
import com.hz.pay.master.core.mapper.AdvertMapper;
import com.hz.pay.master.core.model.advert.AdvertModel;
import com.hz.pay.master.core.service.AdvertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 广告的Service层的实现层
 * @Author yoko
 * @Date 2020/3/3 21:47
 * @Version 1.0
 */
@Service
public class AdvertServiceImpl<T> extends BaseServiceImpl<T> implements AdvertService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private AdvertMapper advertMapper;


    public BaseDao<T> getDao() {
        return advertMapper;
    }

    @Override
    public AdvertModel getRandomAdvert(AdvertModel model) {
        return advertMapper.getRandomAdvert(model);
    }
}
