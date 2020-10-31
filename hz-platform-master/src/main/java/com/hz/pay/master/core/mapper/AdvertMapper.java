package com.hz.pay.master.core.mapper;

import com.hz.pay.master.core.common.dao.BaseDao;
import com.hz.pay.master.core.model.advert.AdvertModel;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 广告的Dao层
 * @Author yoko
 * @Date 2020/3/3 21:45
 * @Version 1.0
 */
@Mapper
public interface AdvertMapper<T> extends BaseDao<T> {

    /**
     * @Description: 随机获取一条广告
     * @param model
     * @return AdvertModel
     * @author yoko
     * @date 2020/3/3 21:49
    */
    public AdvertModel getRandomAdvert(AdvertModel model);
}
