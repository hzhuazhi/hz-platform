package com.hz.platform.master.core.service;


import com.hz.platform.master.core.common.service.BaseService;
import com.hz.platform.master.core.model.zhongbang.ZbWhitelistModel;

/**
 * @Description 众邦白名单的Service层
 * @Author yoko
 * @Date 2020/9/8 20:53
 * @Version 1.0
 */
public interface ZbWhitelistService<T> extends BaseService<T> {

    /**
    * @Description: 更新众邦白名单添加后返回的结果信息
    * @param model
    * @author: yoko
    * @date: 2022/7/14 14:10
    * @version 1.0.0
    */
    public int updateResultInfo(ZbWhitelistModel model);

}
