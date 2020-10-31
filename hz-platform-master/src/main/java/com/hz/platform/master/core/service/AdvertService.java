package com.hz.platform.master.core.service;


import com.hz.platform.master.core.common.service.BaseService;
import com.hz.platform.master.core.model.advert.AdvertModel;

/**
 * @Description 广告的Service层
 * @Author yoko
 * @Date 2020/3/3 21:46
 * @Version 1.0
 */
public interface AdvertService<T> extends BaseService<T> {

    /**
     * @Description: 随机获取一条广告
     * @param model
     * @return AdvertModel
     * @author yoko
     * @date 2020/3/3 21:49
     */
    public AdvertModel getRandomAdvert(AdvertModel model);
}
