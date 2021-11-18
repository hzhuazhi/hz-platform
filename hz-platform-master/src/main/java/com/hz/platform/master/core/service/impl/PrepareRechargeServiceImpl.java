package com.hz.platform.master.core.service.impl;

import com.hz.platform.master.core.common.dao.BaseDao;
import com.hz.platform.master.core.common.service.impl.BaseServiceImpl;
import com.hz.platform.master.core.mapper.PrepareRechargeMapper;
import com.hz.platform.master.core.service.PrepareRechargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 代付预备充值表:预备给渠道进行充值的Service层的实现层
 * @Author yoko
 * @Date 2020/3/2 17:30
 * @Version 1.0
 */
@Service
public class PrepareRechargeServiceImpl<T> extends BaseServiceImpl<T> implements PrepareRechargeService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private PrepareRechargeMapper prepareRechargeMapper;


    public BaseDao<T> getDao() {
        return prepareRechargeMapper;
    }


}
